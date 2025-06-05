package dev.asper.app.service;

import dev.asper.app.component.PipelineModelRecoverException;
import dev.asper.app.component.InsufficientDataException;
import dev.asper.app.component.ModelTrainer;
import dev.asper.app.entity.Model;
import dev.asper.app.entity.base.BaseEntity;
import dev.asper.app.graphql.*;
import dev.asper.app.repository.ModelRepository;
import dev.asper.app.repository.spec.ModelSpecification;
import dev.asper.app.service.log.LogRecordCode;
import dev.asper.app.service.log.LogService;
import dev.asper.common.feature.Feature;
import dev.asper.common.feature.FeatureType;
import dev.asper.common.util.ExceptionHelper;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.spark.*;
import org.apache.commons.collections4.ListUtils;
import org.apache.spark.ml.PipelineModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class ModelServiceImpl implements ModelService {
    public static final int MAX_ALLOWED_DEPTH = 30;
    public static final int MIN_INSTANCES_PER_NODE = 1;
    public static final double MIN_INFO_GAIN = 0.0;
    public static final double MIN_WEIGHT_FRACTION_PER_NODE = 0.0;
    public static final int MIN_SAMPLES = 10;
    public static final String TMP_FOLDER = "Models";
    private final ModelRepository modelRepository;
    private final DatasetService datasetService;
    private final LogService logService;
    private final ModelTrainer modelTrainer;

    private final Map<String, PipelineInfo> cache = new HashMap<>();
    public static final DecisionTreeParamsInput DEFAULT_DECISION_TREE_PARAMS = DecisionTreeParamsInput.builder()
            .minInfoGain(MIN_INFO_GAIN)
            .minInstancesPerNode(MIN_INSTANCES_PER_NODE)
            .minWeightFractionPerNode(MIN_WEIGHT_FRACTION_PER_NODE)
            .maxDepth(MAX_ALLOWED_DEPTH)
            .build();

    @Override
    public Model findById(UUID uuid) {
        return modelRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Model not found. modelId=" + uuid));
    }

    @Autowired
    public ModelServiceImpl(ModelRepository modelRepository,
                            DatasetService datasetService,
                            LogService logService,
                            ModelTrainer modelTrainer) {
        this.modelRepository = modelRepository;
        this.datasetService = datasetService;
        this.logService = logService;
        this.modelTrainer = modelTrainer;
        try {
            Files.createDirectories(Path.of(TMP_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Model createModel(ModelInput modelInput) {
        Model model = buildModel(modelInput);
        Model save = modelRepository.save(model);
        logService.log(LogRecordCode.ENTITY_CREATED, Map.of("name", modelInput.getName()));
        return save;
    }

    @Override
    public List<Model> createModels(List<ModelInput> modelInputs) {
        List<Model> models = modelInputs.stream().map(this::buildModel).toList();
        return modelRepository.saveAll(models);
    }

    private Model buildModel(ModelInput modelInput) {
        FeatureSchema featureSchema = modelInput.getFeatureSchema();
        PokerSituation pokerSituation = modelInput.getPokerSituation();
        DecisionTreeParamsInput decisionTreeParamsInput = modelInput.getDecisionTreeParamsInput() == null ? DEFAULT_DECISION_TREE_PARAMS : modelInput.getDecisionTreeParamsInput();
        ExceptionHelper.throwIf(decisionTreeParamsInput.getMaxDepth() > MAX_ALLOWED_DEPTH, () -> new RuntimeException("Max depth must be less than " + MAX_ALLOWED_DEPTH + ". Passed value: " + decisionTreeParamsInput.getMaxDepth()));
        ExceptionHelper.throwIf(decisionTreeParamsInput.getMinInfoGain() < MIN_INFO_GAIN, () -> new RuntimeException("Min info gain must be positive. Passed value: " + decisionTreeParamsInput.getMinInfoGain()));
        ExceptionHelper.throwIf(decisionTreeParamsInput.getMinInstancesPerNode() < MIN_INSTANCES_PER_NODE, () -> new RuntimeException("Min instances per node must be greater than " + MIN_INSTANCES_PER_NODE + ". Passed value: " + decisionTreeParamsInput.getMinInstancesPerNode()));
        ExceptionHelper.throwIf(decisionTreeParamsInput.getMinWeightFractionPerNode() < MIN_WEIGHT_FRACTION_PER_NODE, () -> new RuntimeException("Min weight fraction per node must be positive. Passed value: " + decisionTreeParamsInput.getMinWeightFractionPerNode()));
        ModelType modelType = featureSchema.getModelType();
        return Model.builder()
                .modelType(modelType)
                .pokerSituation(pokerSituation)
                .spot(modelInput.getSpot().name())
                .featureSchema(featureSchema)
                .modelStatus(ModelStatus.NEW)
                .modelSize(0)
                .featureInfo(FeatureInfo.empty())
                .maxDepth(decisionTreeParamsInput.getMaxDepth())
                .depth(0)
                .statusInfo("New")
                .datasetQuery(modelInput.getDatasetQuery())
                .fallbackQuery(modelInput.getFallbackQuery())
                .minInfoGain(decisionTreeParamsInput.getMinInfoGain())
                .minWeightFractionPerNode(decisionTreeParamsInput.getMinWeightFractionPerNode())
                .minInstancesPerNode(decisionTreeParamsInput.getMinInstancesPerNode())
                .name(modelInput.getName())
                .description(modelInput.getDescription())
                .evalValueSeenData(0.)
                .evalValueUnknownData(0.)
                .body("".getBytes())
                .build();
    }

    @Override
    @Transactional
    public Model resetModel(Model model) {
        model.setStatusInfo("Model has been reset");
        model.setModelStatus(ModelStatus.NEW);
        model.setBody("".getBytes());
        model.setModelSize(0);
        model.setFeatureInfo(FeatureInfo.empty());
        model.setDepth(0);
        model.setEvalValueSeenData(0);
        model.setEvalValueUnknownData(0);
        return modelRepository.save(model);
    }

    @Override
    public List<String> trainAll() {
        logService.log(LogRecordCode.TRAINING_STARTED, Map.of("model_name", "model.getName()"));
        List<Model> models = new ArrayList<>(modelRepository.findByModelStatusIn(List.of(ModelStatus.NEW, ModelStatus.TRAINING_IN_PROGRESS)));
        new Thread(() -> ListUtils.partition(models, MIN_SAMPLES).parallelStream()
                .forEach(m -> m.forEach(this::train)))
                .start();
        return models.stream().map(BaseEntity::getName).toList();
    }

    @Override
    public PipelineInfo recoverPipelineModel(String modelName) throws PipelineModelRecoverException {
        PipelineInfo cachedPipelineInfo = cache.get(modelName);
        if (cachedPipelineInfo != null) {
            return cachedPipelineInfo;
        } else {
            try {
                Model model = findModelByName(modelName);
                PipelineModel pipelineModel = PipelineToBytesConverter.readFromFolder(TMP_FOLDER, modelName)
                        .orElseGet(() -> {
                            byte[] body = Objects.requireNonNull(modelRepository.getModelBody(model.getId()));
                            return PipelineToBytesConverter.fromBytes(TMP_FOLDER, modelName, body);
                        });
                PipelineInfo pipelineInfo = new PipelineInfo(model, pipelineModel);
                cache.put(modelName, pipelineInfo);
                return pipelineInfo;
            } catch (Exception e) {
                logService.log(LogRecordCode.PIPELINE_RECOVER_ERROR, Map.of("modelName", modelName, "exception", e.getMessage() != null ? e.getMessage() : e.getClass().getName()));
                throw new PipelineModelRecoverException(e);
            }
        }
    }

    @Override
    public List<String> findModelNames(String spot, String mask, ModelType modelType) {
        Specification<Model> spec = ModelSpecification.nameContains(mask)
                .and(ModelSpecification.modelTypeFilter(modelType))
                .and(ModelSpecification.spotLike(spot));
        return modelRepository.findAll(spec).stream().map(BaseEntity::getName).toList();
    }

    @Override
    public Model train(Model model) {
        ExceptionHelper.throwIf(!(model.getModelStatus() == ModelStatus.NEW || model.getModelStatus() == ModelStatus.FAILURE || model.getModelStatus() == ModelStatus.TRAINING_IN_PROGRESS),
                () -> new RuntimeException("Model must have " + ModelStatus.NEW + " or " + ModelStatus.FAILURE + " status for training started"));
        model.setModelStatus(ModelStatus.TRAINING_IN_PROGRESS);
        modelRepository.save(model);
        List<Feature<?, PokerGame>> features = model.getPokerSituation().getFeatures();
        ModelParams modelParams = new ModelParams(
                model.getModelType(),
                model.getFeatureSchema(),
                model.getFeatureSchema().getLabelFeature().name().name(),
                features.stream().filter(f -> f.featureType() == FeatureType.CATEGORICAL).map(Feature::name).map(Enum::name).toArray(String[]::new),
                features.stream().filter(f -> f.featureType() == FeatureType.QUANTITATIVE).map(Feature::name).map(Enum::name).toArray(String[]::new),
                model.getMaxDepth(),
                model.getMinInstancesPerNode(),
                model.getMinInfoGain(),
                model.getMinWeightFractionPerNode(),
                model.getDatasetQuery(),
                model.getFallbackQuery()
        );
        try {
            ModelInfo modelInfo = modelTrainer.buildAndTrain(modelParams, TrainMode.UNKNOWN_DATA_EVAL, MIN_SAMPLES);
            byte[] bytes = PipelineToBytesConverter.toBytes(TMP_FOLDER + File.separator + UUID.randomUUID(), modelInfo.pipelineModel());
            modelRepository.setModelBody(bytes, model.getId());
            model.setModelStatus(ModelStatus.TRAINED);
            model.setDepth(modelInfo.depth());
            model.setDatasetRowCount(modelInfo.datasetRowCount());
            model.setEvalValueSeenData(modelInfo.evalResult().seenDataValue());
            model.setEvalValueUnknownData(modelInfo.evalResult().unknownDataValue());
            model.setNumNodes(modelInfo.numNodes());
            model.setFeatureInfo(modelInfo.featureInfo());
            logService.log(LogRecordCode.MODEL_TRAINED, Map.of("model_name", model.getName(),
                    "depth", String.valueOf(modelInfo.depth()),
                    "datasetRowCount", String.valueOf(modelInfo.datasetRowCount()),
                    "numNodes", String.valueOf(modelInfo.numNodes()),
                    "seenDataValueEval", String.valueOf(modelInfo.evalResult().seenDataValue()),
                    "unknownDataValueEval", String.valueOf(modelInfo.evalResult().unknownDataValue())));
        } catch (InsufficientDataException e) {
            model.setModelStatus(ModelStatus.INSUFFICIENT_DATA_TRAIN);
            model.setStatusInfo(e.getMessage());
            logService.log(LogRecordCode.INSUFFICIENT_DATA, Map.of("model_name", model.getName()));
            return save(model);
        } catch (Exception e) {
            String message = Optional.ofNullable(e.getMessage()).orElse(e.getClass().getName());
            model.setModelStatus(ModelStatus.FAILURE);
            model.setStatusInfo(message);
            logService.log(LogRecordCode.GENERAL_FAILURE_TRAIN, Map.of("model_name", model.getName(), "msg", message));
            return save(model);
        }
        return modelRepository.save(model);
    }

    @Override
    @Transactional
    public Model save(Model entity) {
        return modelRepository.save(entity);
    }

    @Override
    public List<Model> findByModelStatus(ModelStatus modelStatus) {
        return modelRepository.findByModelStatus(modelStatus);
    }

    @Override
    public Set<Model> findByNameIn(Collection<String> names) {
        return modelRepository.findByNameIn(names);
    }

    @Override
    @Transactional
    public Model delete(Model model) {
        modelRepository.delete(model);
        return model;
    }

    @Override
    public ModelSheet getModelSheet(ModelFilter modelFilter, Pagination pagination) {
        Specification<Model> spec = ModelSpecification.defaultSpec();
        if (modelFilter != null) {
            spec = spec
                    .and(ModelSpecification.featureSchemaFilter(modelFilter.getFeatureSchemas()))
                    .and(ModelSpecification.modelStatusesFilter(modelFilter.getModelStatuses()))
                    .and(ModelSpecification.nameContains(modelFilter.getChars()))
                    .and(ModelSpecification.createdFilter(modelFilter.getCreatedInterval()));
        }
        if (pagination != null) {
            Pageable page = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), ModelSpecification.sort());
            Page<Model> modelPage = modelRepository.findAll(spec, page);
            return ModelSheet.builder()
                    .models(modelPage.getContent())
                    .sheetInfo(SheetInfo.builder()
                            .totalPages(modelPage.getTotalPages())
                            .totalElements((int) modelPage.getTotalElements())
                            .pageNumber(modelPage.getNumber())
                            .pageSize(modelPage.getSize())
                            .build())
                    .build();
        } else {
            List<Model> models = modelRepository.findAll(spec);
            return ModelSheet.builder()
                    .models(models)
                    .sheetInfo(SheetInfo.builder()
                            .totalPages(1)
                            .totalElements(models.size())
                            .pageNumber(0)
                            .pageSize(models.size())
                            .build())
                    .build();

        }
    }

    @Override
    public boolean exists(String name) {
        return modelRepository.existsByName(name);
    }

    @Override
    public Model findModelByName(String modelName) {
        return modelRepository.findByName(modelName)
                .orElseThrow(() -> new RuntimeException("Model not found. Name=" + modelName));
    }


    @Override
    public void delete(List<Model> models) {
        modelRepository.deleteAll(models);
    }

    @Override
    public List<Model> findAll() {
        return modelRepository.findAll();
    }

}

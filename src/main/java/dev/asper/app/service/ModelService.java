package dev.asper.app.service;


import dev.asper.app.component.PipelineModelRecoverException;
import dev.asper.app.entity.Model;
import dev.asper.app.graphql.*;
import dev.asper.spark.ModelStatus;
import dev.asper.spark.ModelType;
import dev.asper.spark.PipelineInfo;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ModelService {

    Model findById(UUID uuid);

    Model createModel(ModelInput modelInput);

    List<Model> createModels(List<ModelInput> modelInputs);

    Model resetModel(Model model);

    Model save(Model entity);

    List<Model> findByModelStatus(ModelStatus modelStatus);

    Set<Model> findByNameIn(Collection<String> names);

    Model train(Model model);

    Model delete(Model model);

    ModelSheet getModelSheet(ModelFilter modelFilter, Pagination pagination);

    boolean exists(String name);

    Model findModelByName(String modelName);

    void delete(List<Model> models);

    List<Model> findAll();

    List<String> trainAll();

    PipelineInfo recoverPipelineModel(String modelName) throws PipelineModelRecoverException;

    List<String> findModelNames(String spot, String mask, ModelType modelType);
}

package dev.asper.app.repository.spec;

import dev.asper.app.entity.Model;
import dev.asper.app.entity.PostflopChartBody;
import dev.asper.spark.FeatureSchema;
import dev.asper.spark.ModelStatus;
import dev.asper.spark.ModelType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public enum ModelSpecification {
    ;

    public static Specification<Model> emptySpec() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Model> defaultSpec() {
        return emptySpec();
    }

    public static Sort sort() {
        return Sort.by(Sort.Direction.DESC, "creationTimestamp");
    }

    public static Specification<Model> nameContains(String chars) {
        return (root, query, criteriaBuilder) ->
                chars == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("name"), "%" + chars + "%");
    }

    public static Specification<Model> createdFilter(TimestampInterval createdInterval) {
        return (root, query, criteriaBuilder) ->
                createdInterval == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.between(root.get("creationTimestamp"), createdInterval.getBegin(), createdInterval.getEnd());
    }

    public static Specification<Model> featureSchemaFilter(List<FeatureSchema> featureSchemas) {
        return (root, query, criteriaBuilder) ->
                featureSchemas == null ?
                        criteriaBuilder.conjunction() :
                        root.get("featureSchema").in(featureSchemas);
    }

    public static Specification<Model> modelTypeFilter(ModelType modelType) {
        return (root, query, criteriaBuilder) ->
                modelType == null ?
                        criteriaBuilder.conjunction() :
                        root.get("modelType").in(modelType);
    }

    public static Specification<Model> modelStatusesFilter(List<ModelStatus> modelStatuses) {
        return (root, query, criteriaBuilder) ->
                modelStatuses == null ?
                        criteriaBuilder.conjunction() :
                        root.get("modelStatus").in(modelStatuses);
    }

    public static Specification<Model> spotContains(String mask) {
        return (root, query, criteriaBuilder) ->
                mask == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("spot"), "%" + mask + "%");
    }

    public static Specification<Model> spotLike(String spot) {
        return (root, query, criteriaBuilder) ->
                spot == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("spot"), spot);
    }
}

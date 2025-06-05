package dev.asper.app.repository.spec;

import dev.asper.app.entity.Model;
import dev.asper.app.entity.PostflopChartBody;
import dev.asper.app.entity.PreflopChartBody;
import dev.asper.poker.ai.ChartType;
import dev.asper.spark.FeatureSchema;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

public enum PreflopChartSpecification {
    ;

    public static Specification<PreflopChartBody> emptySpec() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<PreflopChartBody> defaultSpec() {
        return emptySpec();
    }

    public static Sort sort() {
        return Sort.by(Sort.Direction.DESC, "creationTimestamp");
    }

    public static Specification<PreflopChartBody> spotContains(String mask) {
        return (root, query, criteriaBuilder) ->
                mask == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("spot"), "%" + mask + "%");
    }

    public static Specification<PreflopChartBody> spotLike(String spot) {
        return (root, query, criteriaBuilder) ->
                spot == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("spot"), spot);
    }

    public static Specification<PreflopChartBody> nameContains(String mask) {
        return (root, query, criteriaBuilder) ->
                mask == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("name"), "%" + mask + "%");
    }

    public static Specification<PreflopChartBody> nameIn(Collection<String> names) {
        return (root, query, criteriaBuilder) ->
                names == null ?
                        criteriaBuilder.conjunction() :
                        root.get("name").in(names);
    }

    public static Specification<PreflopChartBody> createdFilter(TimestampInterval createdInterval) {
        return (root, query, criteriaBuilder) ->
                createdInterval == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.between(root.get("creationTimestamp"), createdInterval.getBegin(), createdInterval.getEnd());
    }

    public static Specification<PreflopChartBody> chartTypeFilter(ChartType chartType) {
        return (root, query, criteriaBuilder) ->
                chartType == null ?
                        criteriaBuilder.conjunction() :
                        root.get("chartType").in(chartType);
    }
}

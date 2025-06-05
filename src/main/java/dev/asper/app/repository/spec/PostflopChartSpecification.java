package dev.asper.app.repository.spec;

import dev.asper.app.entity.PostflopChartBody;
import dev.asper.app.entity.PostflopChartBody;
import dev.asper.poker.ai.ChartType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public enum PostflopChartSpecification {
    ;

    public static Specification<PostflopChartBody> emptySpec() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<PostflopChartBody> defaultSpec() {
        return emptySpec();
    }

    public static Sort sort() {
        return Sort.by(Sort.Direction.DESC, "creationTimestamp");
    }

    public static Specification<PostflopChartBody> spotContains(String mask) {
        return (root, query, criteriaBuilder) ->
                mask == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("spot"), "%" + mask + "%");
    }

    public static Specification<PostflopChartBody> spotLike(String spot) {
        return (root, query, criteriaBuilder) ->
                spot == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("spot"), spot);
    }

    public static Specification<PostflopChartBody> nameContains(String mask) {
        return (root, query, criteriaBuilder) ->
                mask == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("name"), "%" + mask + "%");
    }

    public static Specification<PostflopChartBody> nameIn(Collection<String> names) {
        return (root, query, criteriaBuilder) ->
                names == null ?
                        criteriaBuilder.conjunction() :
                        root.get("name").in(names);
    }

    public static Specification<PostflopChartBody> createdFilter(TimestampInterval createdInterval) {
        return (root, query, criteriaBuilder) ->
                createdInterval == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.between(root.get("creationTimestamp"), createdInterval.getBegin(), createdInterval.getEnd());
    }

    public static Specification<PostflopChartBody> chartTypeFilter(ChartType chartType) {
        return (root, query, criteriaBuilder) ->
                chartType == null ?
                        criteriaBuilder.conjunction() :
                        root.get("chartType").in(chartType);
    }
}

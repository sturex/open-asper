package dev.asper.app.repository.spec;

import dev.asper.app.entity.SolutionInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public enum SolutionSpecification {
    ;

    public static Specification<SolutionInfo> emptySpec() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<SolutionInfo> defaultSpec() {
        return emptySpec();
    }

    public static Sort sort() {
        return Sort.by(Sort.Direction.DESC, "creationTimestamp");
    }

    public static Specification<SolutionInfo> nameContains(String chars) {
        return (root, query, criteriaBuilder) ->
                chars == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("name"), "%" + chars + "%");
    }

    public static Specification<SolutionInfo> nameIn(Collection<String> names) {
        return (root, query, criteriaBuilder) ->
                names == null ?
                        criteriaBuilder.conjunction() :
                        root.get("name").in(names);
    }

    public static Specification<SolutionInfo> createdFilter(TimestampInterval createdInterval) {
        return (root, query, criteriaBuilder) ->
                createdInterval == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.between(root.get("creationTimestamp"), createdInterval.getBegin(), createdInterval.getEnd());
    }
}

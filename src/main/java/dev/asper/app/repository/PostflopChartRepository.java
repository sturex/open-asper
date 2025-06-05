package dev.asper.app.repository;

import dev.asper.app.entity.PostflopChartBody;
import dev.asper.poker.ai.ChartType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public interface PostflopChartRepository extends JpaRepository<PostflopChartBody, UUID>, JpaSpecificationExecutor<PostflopChartBody> {
    Optional<PostflopChartBody> findByName(String name);

    Set<PostflopChartBody> findByIdIn(Collection<UUID> ids);

    List<PostflopChartBody> findByOrderByCreationTimestampDesc();

    Set<PostflopChartBody> findByNameIn(Collection<String> names);

    boolean existsByName(String name);

    @Query(value = "select body from mlpool.postflop_chart_body pcb where pcb.id = ?1", nativeQuery = true)
    byte[] getBody(UUID id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update mlpool.postflop_chart_body pcb set body = :body where pcb.id = :id", nativeQuery = true)
    @Transactional
    void setBody(@Param("body") byte[] body, @Param("id") UUID id);

    List<NameOnly> readAllBySpotAndChartType(String spot, ChartType chartType);
}
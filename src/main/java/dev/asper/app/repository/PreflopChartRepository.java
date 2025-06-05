package dev.asper.app.repository;

import dev.asper.app.entity.PreflopChartBody;
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
public interface PreflopChartRepository extends JpaRepository<PreflopChartBody, UUID>, JpaSpecificationExecutor<PreflopChartBody> {
    Optional<PreflopChartBody> findByName(String name);

    Set<PreflopChartBody> findByIdIn(Collection<UUID> ids);

    List<PreflopChartBody> findByOrderByCreationTimestampDesc();

    Set<PreflopChartBody> findByNameIn(Collection<String> names);

    boolean existsByName(String name);

    List<NameOnly> readAllBySpotAndChartType(String spot, ChartType chartType);

    @Query(value = "select body from mlpool.preflop_chart_body pcb where pcb.id = ?1", nativeQuery = true)
    byte[] getBody(UUID id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update mlpool.preflop_chart_body pcb set body = :body where pcb.id = :id", nativeQuery = true)
    @Transactional
    void setBody(@Param("body") byte[] body, @Param("id") UUID id);

}
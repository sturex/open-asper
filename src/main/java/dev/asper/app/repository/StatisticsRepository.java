package dev.asper.app.repository;

import dev.asper.app.entity.StatisticsBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public interface StatisticsRepository extends JpaRepository<StatisticsBody, UUID>, JpaSpecificationExecutor<StatisticsBody> {
    Optional<StatisticsBody> findByName(String name);

    Set<StatisticsBody> findByIdIn(Collection<UUID> ids);

    List<StatisticsBody> findByOrderByCreationTimestampDesc();

    Set<StatisticsBody> findByNameIn(Collection<String> names);

    boolean existsByName(String name);

    @Query(value = "select body from mlpool.statistics_body sb where sb.id = ?1", nativeQuery = true)
    byte[] getBody(UUID id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update mlpool.statistics_body sb set body = :body where sb.id = :id", nativeQuery = true)
    @Transactional
    void setBody(@Param("body") byte[] body, @Param("id") UUID id);

}
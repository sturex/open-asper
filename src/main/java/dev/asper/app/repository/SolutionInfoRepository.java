package dev.asper.app.repository;

import dev.asper.app.entity.SolutionInfo;
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
public interface SolutionInfoRepository extends JpaRepository<SolutionInfo, UUID>, JpaSpecificationExecutor<SolutionInfo> {

    List<NameOnly> findBy();
    Optional<SolutionInfo> findByName(String name);

    Set<SolutionInfo> findByNameIn(Collection<String> names);

    boolean existsByName(String name);

    @Query(value = "select body from mlpool.solution s where s.id = ?1", nativeQuery = true)
    byte[] getSolutionInfoBody(UUID id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update mlpool.solution s set body = :body where s.id = :id", nativeQuery = true)
    @Transactional
    void setSolutionInfoBody(@Param("body") byte[] body, @Param("id") UUID id);
}
package dev.asper.app.repository;

import dev.asper.app.entity.Model;
import dev.asper.spark.ModelStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public interface ModelRepository extends JpaRepository<Model, UUID>, JpaSpecificationExecutor<Model> {
    Optional<Model> findByName(String name);

    Set<Model> findByIdIn(Collection<UUID> ids);

    List<Model> findByOrderByCreationTimestampDesc();

    List<Model> findByModelStatus(ModelStatus modelStatus);
    List<Model> findByModelStatusIn(List<ModelStatus> modelStatuses);

    Set<Model> findByNameIn(Collection<String> names);

    boolean existsByName(String name);

    @Query(value = "select body from mlpool.model m where m.id = ?1", nativeQuery = true)
    byte[] getModelBody(UUID id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update mlpool.model m set body = :body where m.id = :id", nativeQuery = true)
    @Transactional
    void setModelBody(@Param("body") byte[] body, @Param("id") UUID id);

}
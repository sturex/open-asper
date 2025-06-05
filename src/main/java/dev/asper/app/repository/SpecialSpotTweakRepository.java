package dev.asper.app.repository;

import dev.asper.app.entity.SpecialSpotTweakBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public interface SpecialSpotTweakRepository extends JpaRepository<SpecialSpotTweakBody, UUID>, JpaSpecificationExecutor<SpecialSpotTweakBody> {
    Optional<SpecialSpotTweakBody> findByName(String name);

    Set<SpecialSpotTweakBody> findByIdIn(Collection<UUID> ids);

    List<SpecialSpotTweakBody> findByOrderByCreationTimestampDesc();

    Set<SpecialSpotTweakBody> findByNameIn(Collection<String> names);

    boolean existsByName(String name);

    @Query(value = "select body from mlpool.special_spot_tweak sb where sb.id = ?1", nativeQuery = true)
    byte[] getBody(UUID id);

    @Query(value = "select body from mlpool.special_spot_tweak sb where sb.name = ?1", nativeQuery = true)
    byte[] getBody(String name);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update mlpool.special_spot_tweak sb set body = :body where sb.id = :id", nativeQuery = true)
    @Transactional
    void setBody(@Param("body") byte[] body, @Param("id") UUID id);

    List<NameOnly> findAllBy();

}
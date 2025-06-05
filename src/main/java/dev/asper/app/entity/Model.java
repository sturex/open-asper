package dev.asper.app.entity;


import dev.asper.app.entity.base.BaseEntity;
import dev.asper.app.entity.custom.CustomStringArrayType;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.spark.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "model", schema = "mlpool")
public class Model extends BaseEntity {

    @Column(name = "model_type", nullable = false, length = Integer.MAX_VALUE)
    @Enumerated(EnumType.STRING)
    private ModelType modelType;

    @Column(name = "poker_situation", nullable = false, length = Integer.MAX_VALUE)
    @Enumerated(EnumType.STRING)
    private PokerSituation pokerSituation;

    @Column(name = "feature_schema", nullable = false, length = Integer.MAX_VALUE)
    @Enumerated(EnumType.STRING)
    private FeatureSchema featureSchema;

    @Column(name = "dataset_query", nullable = false, length = Integer.MAX_VALUE)
    private String datasetQuery;

    @Column(name = "fallback_query", nullable = false, length = Integer.MAX_VALUE)
    private String fallbackQuery;

    @Column(name = "spot", nullable = false, length = Integer.MAX_VALUE)
    private String spot;

    @Column(name = "status_info", nullable = false, length = Integer.MAX_VALUE)
    private String statusInfo;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "body")
    @Transient
    private byte[] body;

    @Column(name = "max_depth", nullable = false)
    private int maxDepth;

    @Column(name = "depth", nullable = false)
    private int depth;

    @Column(name = "min_instances_per_node", nullable = false)
    private int minInstancesPerNode;

    @Column(name = "min_info_gain", nullable = false)
    private double minInfoGain;

    @Column(name = "min_weight_fraction_per_node", nullable = false)
    private double minWeightFractionPerNode;

    @Column(name = "dataset_row_count", nullable = false)
    private int datasetRowCount;

    @Column(name = "model_size", nullable = false)
    private int modelSize;

    @Column(name = "num_nodes", nullable = false)
    private int numNodes;

    @JdbcTypeCode(SqlTypes.JSON)
    private FeatureInfo featureInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "model_status", nullable = false, length = Integer.MAX_VALUE)
    private ModelStatus modelStatus;

    @Column(name = "eval_value_seen_data", nullable = false)
    private double evalValueSeenData;

    @Column(name = "eval_value_unknown_data", nullable = false)
    private double evalValueUnknownData;
}
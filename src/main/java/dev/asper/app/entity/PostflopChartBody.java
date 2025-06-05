package dev.asper.app.entity;


import dev.asper.app.entity.base.BaseEntity;
import dev.asper.poker.ai.ChartType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "postflop_chart_body", schema = "mlpool")
public class PostflopChartBody extends BaseEntity {

    @Column(name = "spot", nullable = false, length = Integer.MAX_VALUE)
    private String spot;

    @Column(name = "chart_type", nullable = false, length = Integer.MAX_VALUE)
    @Enumerated(EnumType.STRING)
    private ChartType chartType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "body")
    @Transient
    private byte[] body;


}
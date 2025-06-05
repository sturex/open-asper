package dev.asper.app.entity;

import dev.asper.app.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "solution", schema = "mlpool")
public class SolutionInfo extends BaseEntity {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "body")
    @Transient
    private byte[] body;
}
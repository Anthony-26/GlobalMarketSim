package fr.globalmarket.adapter.out.db.entity;

import fr.globalmarket.domain.model.AssetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "assets")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AssetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    public AssetEntity() {}
}

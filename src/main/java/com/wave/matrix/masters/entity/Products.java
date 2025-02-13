package com.wave.matrix.masters.entity;

import com.wave.matrix.common.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "mt_products")
public class Products extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category categoryId;

    @Column(name = "product_name")
    @NotNull(message = "Product name is required")
    private String productName;

    @Column(name = "product_description", length = 2000)
    private String productDescription;

    @Column(name = "product_material", length = 2000)
    private String productMaterial;

    @Type(JsonBinaryType.class)
    @Column(name = "dimensions", columnDefinition = "jsonb")
    private List<Dimensions> dimensions;

    @Type(JsonBinaryType.class)
    @Column(name = "key_features", columnDefinition = "jsonb")
    private List<KeyFeatures> keyFeatures;

    @Type(JsonBinaryType.class)
    @Column(name = "product_colours", columnDefinition = "jsonb")
    private List<ProductColours> productColours;


    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_delete")
    private Boolean isDelete = false;
}

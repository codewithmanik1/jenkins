package com.wave.matrix.masters.entity;

import com.wave.matrix.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mt_category")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    @NotNull(message = "Category can not be null")
    private String categoryName;

    @Column(name = "category_thumbnail_path")
    @NotNull(message = "category thumbnail can not be null")
    private String categoryThumbNailPath;

    @Column(name = "is_active")
    private Boolean isActive= true;

    @Column(name = "is_delete")
    private Boolean isDelete = false;
}

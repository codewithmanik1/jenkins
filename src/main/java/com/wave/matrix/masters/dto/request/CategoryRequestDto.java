package com.wave.matrix.masters.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDto {

    private Long id;

    @Column(name = "category_name")
    @NotNull(message = "Category can not be null")
    private String categoryName;

    @Column(name = "category_thumbnail_path")
    @NotNull(message = "category thumbnail can not be null")
    private String categoryThumbNail;

    private Boolean isActive = true;

    private Long createdBy;

    private Long updatedBy;
}

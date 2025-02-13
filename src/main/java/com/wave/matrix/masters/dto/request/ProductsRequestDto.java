package com.wave.matrix.masters.dto.request;

import com.wave.matrix.masters.entity.Category;
import com.wave.matrix.masters.entity.Dimensions;
import com.wave.matrix.masters.entity.KeyFeatures;
import com.wave.matrix.masters.entity.ProductColours;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductsRequestDto {

    private Long id;

    private Category categoryId;

    @NotNull(message = "product name is not null")
    private String productName;

    private String productDescription;

    private String productMaterial;

    private List<Dimensions> dimensions;

    private List<KeyFeatures> keyFeatures;

   private List<ProductColours> productColours;

    private Boolean isActive = true;

    private Long createdBy;

    private Long updateBy;
}

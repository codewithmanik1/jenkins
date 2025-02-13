package com.wave.matrix.masters.repository;

import com.wave.matrix.masters.dto.response.ProductsCategoryResponseDto;
import com.wave.matrix.masters.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "select * from fn_get_product_category_list()", nativeQuery = true)
    List<ProductsCategoryResponseDto> getProductsCategory();

    @Query(value = "select * from fn_get_product_category_list_count()", nativeQuery = true)
    Long getProductsCategoryCount();
}

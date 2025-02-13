package com.wave.matrix.masters.repository;

import com.wave.matrix.masters.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface ProductRepository extends JpaRepository<Products, Long> {

    @Query(value = "select * from fn_get_products_by_category_id(?1, ?2, ?3)", nativeQuery = true)
    List<Map<String, Objects>> getProductsByCategoryId(Long categoryId, Integer page, Integer size);

    @Query(value = "select * from fn_get_products_by_category_id_count(?1)", nativeQuery = true)
    Long getProductsByCategoryIdCount(Long categoryId);
}

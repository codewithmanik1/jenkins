package com.wave.matrix.masters.service;

import com.wave.matrix.common.FilterDto;
import com.wave.matrix.masters.dto.request.CategoryRequestDto;
import com.wave.matrix.masters.dto.request.FAQsRequestDto;
import com.wave.matrix.masters.dto.request.ProductsRequestDto;
import org.springframework.http.ResponseEntity;

public interface MasterService {

    ResponseEntity<?> getImageAsFile(String imagePath);

    ResponseEntity<?> saveProducts(ProductsRequestDto productsRequestDto);

    ResponseEntity<?> saveCategory(CategoryRequestDto categoryRequestDto);

    ResponseEntity<?> getProductsCategory();

    ResponseEntity<?> getProductsByCategoryId(FilterDto filterDto);

    ResponseEntity<?> saveFAQs(FAQsRequestDto faQsRequestDto);

}

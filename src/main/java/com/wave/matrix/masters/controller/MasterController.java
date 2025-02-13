package com.wave.matrix.masters.controller;

import com.wave.matrix.common.FilterDto;
import com.wave.matrix.masters.dto.request.CategoryRequestDto;
import com.wave.matrix.masters.dto.request.FAQsRequestDto;
import com.wave.matrix.masters.dto.request.ProductsRequestDto;
import com.wave.matrix.masters.service.MasterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/masters")
public class MasterController {

    @Autowired
    private MasterService masterService;

    @PostMapping("/saveProducts")
    public ResponseEntity<?> saveProducts(@RequestBody ProductsRequestDto productsRequestDto) {
        return masterService.saveProducts(productsRequestDto);
    }

    @PostMapping("/saveCategory")
    public ResponseEntity<?> saveCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return masterService.saveCategory(categoryRequestDto);
    }

    @PostMapping("/saveFAQs")
    public ResponseEntity<?> saveFAQs(@RequestBody FAQsRequestDto faQsRequestDto){
        return masterService.saveFAQs(faQsRequestDto);
    }
}

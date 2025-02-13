package com.wave.matrix.publicapi.controller;

import com.wave.matrix.common.FilterDto;
import com.wave.matrix.masters.service.MasterService;
import com.wave.matrix.publicapi.dto.request.EnquiryDataRequestDto;
import com.wave.matrix.publicapi.service.PublicApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publicApi")
public class PublicApiController {

    @Autowired
    private PublicApiService publicApiService;

    @Autowired
    private MasterService masterService;

    @GetMapping("/getImages/{imagePath}")
    public ResponseEntity<?> getImages(@PathVariable String imagePath) {
        return masterService.getImageAsFile(imagePath);
    }

    @GetMapping("/getProductsCategory")
    public ResponseEntity<?> getProductsCategory(){
        return masterService.getProductsCategory();
    }

    @PostMapping("/getProductsByCategoryId")
    ResponseEntity<?> getProductsByCategoryId(@RequestBody FilterDto filterDto){
        return masterService.getProductsByCategoryId(filterDto);
    }

    @PostMapping("/saveEnquiryData")
    public ResponseEntity<?> saveEnquiryData(@RequestBody EnquiryDataRequestDto enquiryDataRequestDto){
        return publicApiService.saveEnquiryData(enquiryDataRequestDto);
    }

    @PostMapping("/getFAQs")
    public ResponseEntity<?> getFAQs(@RequestBody FilterDto filterDto){
        return publicApiService.getFAQs(filterDto);
    }
}

package com.wave.matrix.publicapi.service.impl;

import com.wave.matrix.common.ApiResponse;
import com.wave.matrix.common.FilterDto;
import com.wave.matrix.masters.repository.FAQsRepository;
import com.wave.matrix.publicapi.dto.request.EnquiryDataRequestDto;
import com.wave.matrix.publicapi.entity.EnquiryData;
import com.wave.matrix.publicapi.repository.EnquiryDataRepository;
import com.wave.matrix.publicapi.service.PublicApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PublicApiServiceImpl implements PublicApiService {

    @Autowired
    private EnquiryDataRepository enquiryDataRepository;

    @Autowired
    private FAQsRepository faQsRepository;

    @Override
    public ResponseEntity<?> saveEnquiryData(EnquiryDataRequestDto enquiryDataRequestDto) {
        var response = new ApiResponse<>();
        EnquiryData enquiryData = new EnquiryData();
        enquiryData.setName(enquiryDataRequestDto.getName());
        enquiryData.setMobileNumber(enquiryDataRequestDto.getMobileNumber());
        enquiryData.setEmailAddress(enquiryDataRequestDto.getEmailAddress());
        enquiryData.setDetails(enquiryDataRequestDto.getDetails());
        response.responseMethod(HttpStatus.OK.value(), "Information saved successfully", null, null);
        enquiryDataRepository.save(enquiryData);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getFAQs(FilterDto filterDto) {
        var response = new ApiResponse<>();

        List<Map<String, Objects>> data = faQsRepository.getFAQs(filterDto.getPage(), filterDto.getSize());
        if(!data.isEmpty()){
            Long getCount = faQsRepository.getFAQsCount();
            response.responseMethod(HttpStatus.OK.value(), "Data fetch successfully", data, getCount);
        }else{
            response.responseMethod(HttpStatus.NOT_FOUND.value(), "Data not found", data, null);
        }
        return ResponseEntity.ok(response);
    }
}

package com.wave.matrix.publicapi.service;

import com.wave.matrix.common.FilterDto;
import com.wave.matrix.publicapi.dto.request.EnquiryDataRequestDto;
import org.springframework.http.ResponseEntity;

public interface PublicApiService {

    ResponseEntity<?> saveEnquiryData(EnquiryDataRequestDto enquiryDataRequestDto);

    ResponseEntity<?> getFAQs(FilterDto filterDto);
}

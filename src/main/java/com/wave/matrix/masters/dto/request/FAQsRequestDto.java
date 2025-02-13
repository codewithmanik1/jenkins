package com.wave.matrix.masters.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FAQsRequestDto {

    private Long id;

    private String questions;

    private String answer;

    private Boolean isActive = true;

    private Long createdBy;

    private Long updatedBy;
}

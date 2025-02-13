package com.wave.matrix.masters.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({
        "Sr No.",
        "Id",
        "Category Name",
        "Category Thumbnail Path"
})
public interface ProductsCategoryResponseDto {

    @JsonProperty("Sr No.")
    Long getSrNo();

    @JsonProperty("Id")
    Long getId();

    @JsonProperty("Category Name")
    String getCategoryName();

    @JsonProperty("Category Thumbnail Path")
    String getCategoryThumbnailPath();
}

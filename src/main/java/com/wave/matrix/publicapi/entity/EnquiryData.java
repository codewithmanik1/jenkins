package com.wave.matrix.publicapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "enquiry_data")
public class EnquiryData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 80)
    private String name;

    @Column(name = "email address", length = 50)
    private String emailAddress;

    @Column(name = "mobile_number", length = 12)
    private String mobileNumber;

    @Column(name = "details", length = 500)
    private String details;

    @Column(name = "created_date")
    @NotNull(message = "created date cannot be null")
    @JsonIgnore
    private LocalDateTime createdDate = LocalDateTime.now();

}

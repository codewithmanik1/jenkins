package com.wave.matrix.publicapi.repository;

import com.wave.matrix.publicapi.entity.EnquiryData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnquiryDataRepository extends JpaRepository<EnquiryData, Long> {
}

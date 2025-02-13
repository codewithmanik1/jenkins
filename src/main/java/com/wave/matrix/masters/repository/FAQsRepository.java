package com.wave.matrix.masters.repository;

import com.wave.matrix.masters.entity.FAQs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface FAQsRepository extends JpaRepository<FAQs, Long> {

    @Query(value = "select * from fn_get_faqs_list(?1, ?2)", nativeQuery = true)
    List<Map<String, Objects>> getFAQs(Integer page, Integer size);

    @Query(value = "select * from fn_get_faqs_list_count()", nativeQuery = true)
    Long getFAQsCount();
}

package com.ssafy.triptogether.attraction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.triptogether.attraction.domain.Attraction;
import com.ssafy.triptogether.attraction.repository.query.AttractionRepositoryCustom;

public interface AttractionRepository extends JpaRepository<Attraction, Long>, AttractionRepositoryCustom {
}

package com.ssafy.triptogether.attraction.utils;

import com.ssafy.triptogether.attraction.domain.Attraction;
import com.ssafy.triptogether.attraction.domain.Region;
import com.ssafy.triptogether.attraction.repository.AttractionRepository;
import com.ssafy.triptogether.attraction.repository.RegionRepository;
import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.global.exception.response.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttractionUtils {
    public static Region findByRegionId(RegionRepository repository, Long regionId) {
        return repository.findById(regionId)
                .orElseThrow(
                        () -> new NotFoundException("FindByRegionId", ErrorCode.REGION_NOT_FOUND)
                );
    }

    public static Attraction findByAttractionId(AttractionRepository repository, Long attractionId) {
        return repository.findById(attractionId)
                .orElseThrow(
                        () -> new NotFoundException("FindByAttractionId", ErrorCode.ATTRACTION_NOT_FOUND)
                );
    }
}

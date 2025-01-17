package com.advertisement.service;
import com.advertisement.dto.SearchDTO;
import com.advertisement.dto.StatisticDTO;
import com.advertisement.model.Advertisement;

import java.util.List;

import com.advertisement.model.Advertisement;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdvertisementService {

    List<Advertisement> findAll();
    List<Advertisement> search(SearchDTO dto);
    List<Advertisement> findAll(Long agentID);
    ResponseEntity<?> add(Advertisement ad);
    int findAllCount(Long ownerId);
    Advertisement find(Long id);
    String getRentRequestsCarClass(Long id);

    List<StatisticDTO> getMostComment(Long id);

    List<StatisticDTO> getMostKm(Long id);

    List<StatisticDTO> getBestRate(Long id);

    String getIdCar(Long id);
}

package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.OrderAdoptionDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.OrderAdoption;
import com.petbackend.thbao.responses.OrderAdoptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IOrderAdoptionService {
    OrderAdoption createOrderAdoption(OrderAdoptionDTO orderAdoptionDTO) throws DataNotFoundException;
    Page<OrderAdoptionResponse> getAllOrderAdoption(PageRequest pageRequest);
    OrderAdoption getOrderAdoptionById(Long id) throws DataNotFoundException;
    List<OrderAdoption> getOrderAdoptionByUserId(Long userId) throws DataNotFoundException;
    List<OrderAdoption> getOrderAdoptionByPetAdoptionId(Long petAdoptionId) throws DataNotFoundException;

    OrderAdoption updateOrderAdoption(Long id, OrderAdoptionDTO orderAdoptionDTO) throws DataNotFoundException;
    void delete(long id) throws DataNotFoundException;
}

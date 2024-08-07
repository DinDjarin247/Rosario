package com.example.rosario.service;

import com.example.rosario.dto.DeliveryDto;
import com.example.rosario.entity.Delivery;
import com.example.rosario.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    public List<DeliveryDto> getAllDeliveries(){
        List<Delivery> deliveries = deliveryRepository.findAll();
        return deliveries.stream()
                .map(delivery -> new DeliveryDto(
                        delivery.getDeliveryId(),
                        delivery.getOrders().getOrdersId(),
                        delivery.getSeller().getSellerId(),
                        delivery.getCustomer().getCustomerId(),
                        delivery.getDeliveryCount(),
                        delivery.getDeliveryPrice(),
                        delivery.getDeliveryType(),
                        delivery.getDeliveryRqDt(),
                        delivery.getDeliveryCpDt(),
                        delivery.getDeliveryState(),
                        delivery.getCustomer().getCustomerNm(),
                        delivery.getOrders().getOrdersDescription()
                ))
                .collect(Collectors.toList());
    }

    // ID로 특정 배송 정보를 조회하는 메서드 (Delivery 반환)
    public Delivery getDeliveryById(Long deliveryId){
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        return optionalDelivery.orElseThrow(() -> new RuntimeException("Delivery not found"));
    }

    // sellerId로 delivery 정보 조회
    public List<DeliveryDto> getDeliveriesBySellerId(Long sellerId) {
        List<Delivery> deliveries = deliveryRepository.findBySeller_SellerId(sellerId);
        return deliveries.stream()
                .map(delivery -> new DeliveryDto(
                        delivery.getDeliveryId(),
                        delivery.getOrders().getOrdersId(),
                        delivery.getSeller().getSellerId(),
                        delivery.getCustomer().getCustomerId(),
                        delivery.getDeliveryCount(),
                        delivery.getDeliveryPrice(),
                        delivery.getDeliveryType(),
                        delivery.getDeliveryRqDt(),
                        delivery.getDeliveryCpDt(),
                        delivery.getDeliveryState(),
                        delivery.getCustomer().getCustomerNm(),
                        delivery.getOrders().getOrdersDescription()
                ))
                .collect(Collectors.toList());
    }


    //Other methods for CRUD operations
}

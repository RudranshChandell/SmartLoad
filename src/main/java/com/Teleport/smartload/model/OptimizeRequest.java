package com.Teleport.smartload.model;

import lombok.Data;

import java.util.List;

@Data
public class OptimizeRequest {
    private Truck truck;
    private List<Order> orders;
}

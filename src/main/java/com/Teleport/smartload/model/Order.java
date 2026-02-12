package com.Teleport.smartload.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Order {
    private String id;

    @JsonProperty("payout_cents")
    private long payoutCents;

    @JsonProperty("weight_lbs")
    private int weightLbs;

    @JsonProperty("volume_cuft")
    private int volumeCuft;

    private String origin;
    private String destination;

    @JsonProperty("pickup_date")
    private String pickupDate;

    @JsonProperty("delivery_date")
    private String deliveryDate;

    @JsonProperty("is_hazmat")
    private boolean isHazmat;
}
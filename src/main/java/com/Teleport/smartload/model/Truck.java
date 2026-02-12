package com.Teleport.smartload.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Truck {
    private String id;

    @JsonProperty("max_weight_lbs")
    private int maxWeightLbs;

    @JsonProperty("max_volume_cuft")
    private int maxVolumeCuft;
}
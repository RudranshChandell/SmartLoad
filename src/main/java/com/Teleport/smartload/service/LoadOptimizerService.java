package com.Teleport.smartload.service;

import com.Teleport.smartload.model.OptimizeRequest;
import com.Teleport.smartload.model.OptimizeResponse;

public interface LoadOptimizerService {
    OptimizeResponse optimize(OptimizeRequest request);
}
package com.Teleport.smartload.controller;

import com.Teleport.smartload.model.OptimizeRequest;
import com.Teleport.smartload.model.OptimizeResponse;
import com.Teleport.smartload.service.LoadOptimizerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/load-optimizer")
public class LoadOptimizerController {

    private final LoadOptimizerService service;

    public LoadOptimizerController(LoadOptimizerService service) {
        this.service = service;
    }

    @PostMapping("/optimize")
    public ResponseEntity<?> optimize(@RequestBody OptimizeRequest request) {
        // Basic request validation
        if (request == null || request.getTruck() == null || request.getOrders() == null) {
            return ResponseEntity.badRequest().body("Malformed request payload.");
        }

        // Limit payload size to ensure system stability
        if (request.getOrders().size() > 50) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("Order count exceeds processing limits.");
        }

        try {
            return ResponseEntity.ok(service.optimize(request));
        } catch (Exception e) {
            // Log exception here in real production
            return ResponseEntity.internalServerError().build();
        }
    }
}
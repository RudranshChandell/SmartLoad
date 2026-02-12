package com.Teleport.smartload.service;

import com.Teleport.smartload.model.OptimizeRequest;
import com.Teleport.smartload.model.OptimizeResponse;
import com.Teleport.smartload.model.Order;
import com.Teleport.smartload.model.Truck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoadOptimizerServiceImpl implements LoadOptimizerService {

    @Override
    public OptimizeResponse optimize(OptimizeRequest request) {
        Truck truck = request.getTruck();

        // Group by lane and hazmat to ensure compatibility and isolation
        Map<String, List<Order>> lanes = request.getOrders().stream()
                .collect(Collectors.groupingBy(o ->
                        o.getOrigin() + "::" + o.getDestination() + "::" + o.isHazmat()
                ));

        OptimizationResult globalBest = new OptimizationResult(0, 0, 0, new ArrayList<>());

        for (List<Order> group : lanes.values()) {
            Map<String, OptimizationResult> memo = new HashMap<>();
            OptimizationResult result = solve(0, truck.getMaxWeightLbs(), truck.getMaxVolumeCuft(), group, memo);

            if (result.getPayout() > globalBest.getPayout()) {
                globalBest = result;
            }
        }

        return buildResponse(truck, globalBest);
    }

    /**
     * Multi-dimensional Knapsack solver using Top-Down DP.
     * Complexity: O(N * Weight * Volume)
     */
    private OptimizationResult solve(int idx, int remWeight, int remVol,
                                     List<Order> orders,
                                     Map<String, OptimizationResult> memo) {

        if (idx >= orders.size()) {
            return new OptimizationResult(0, 0, 0, new ArrayList<>());
        }

        String stateKey = idx + "|" + remWeight + "|" + remVol;
        if (memo.containsKey(stateKey)) {
            return memo.get(stateKey);
        }

        Order current = orders.get(idx);

        // Branch 1: Exclude current order
        OptimizationResult best = solve(idx + 1, remWeight, remVol, orders, memo);

        // Branch 2: Include current order if constraints allow
        if (current.getWeightLbs() <= remWeight && current.getVolumeCuft() <= remVol) {
            OptimizationResult included = solve(
                    idx + 1,
                    remWeight - current.getWeightLbs(),
                    remVol - current.getVolumeCuft(),
                    orders,
                    memo
            );

            long totalPayout = current.getPayoutCents() + included.getPayout();
            if (totalPayout > best.getPayout()) {
                List<String> ids = new ArrayList<>();
                ids.add(current.getId());
                ids.addAll(included.getSelectedIds());

                best = new OptimizationResult(
                        totalPayout,
                        current.getWeightLbs() + included.getWeight(),
                        current.getVolumeCuft() + included.getVolume(),
                        ids
                );
            }
        }

        memo.put(stateKey, best);
        return best;
    }

    private OptimizeResponse buildResponse(Truck truck, OptimizationResult result) {
        double wUtil = (double) result.getWeight() / truck.getMaxWeightLbs() * 100;
        double vUtil = (double) result.getVolume() / truck.getMaxVolumeCuft() * 100;

        return OptimizeResponse.builder()
                .truckId(truck.getId())
                .selectedOrderId(result.getSelectedIds())
                .totalPayoutCents(result.getPayout())
                .totalWeightLbs(result.getWeight())
                .totalVolumeCuft(result.getVolume())
                .utilizationWeightPercent(Math.round(wUtil * 100.0) / 100.0)
                .utilizationVolumePercent(Math.round(vUtil * 100.0) / 100.0)
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class OptimizationResult {
        private long payout;
        private int weight;
        private int volume;
        private List<String> selectedIds;
    }
}
package org.afs.pakinglot.domain.strategies;

import org.afs.pakinglot.domain.common.StrategyConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ParkingStrategyConfig {

    @Bean
    public Map<String, ParkingStrategy> strategyMap(SequentiallyStrategy sequentiallyStrategy,
                                                    MaxAvailableStrategy maxAvailableStrategy,
                                                    AvailableRateStrategy availableRateStrategy) {
        Map<String, ParkingStrategy> strategyMap = new HashMap<>();
        strategyMap.put(StrategyConstant.STANDARD_PARKING_BOY_STRATEGY, sequentiallyStrategy);
        strategyMap.put(StrategyConstant.SMART_PARKING_BOY_STRATEGY, maxAvailableStrategy);
        strategyMap.put(StrategyConstant.SUPER_SMART_PARKING_BOY_STRATEGY, availableRateStrategy);
        return strategyMap;
    }

}

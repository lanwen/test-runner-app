package ru.lanwen.junit.config;

import org.apache.camel.processor.aggregate.MemoryAggregationRepository;
import org.apache.camel.spi.AggregationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;

/**
 * @author lanwen (Merkushev Kirill)
 */
@Configuration
public class Config {

    @Bean
    @Named("started")
    public AggregationRepository started() {
        return new MemoryAggregationRepository();
    }
}

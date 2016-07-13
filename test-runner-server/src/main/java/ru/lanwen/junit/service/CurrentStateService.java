package ru.lanwen.junit.service;

import org.apache.camel.spi.AggregationRepository;
import org.springframework.stereotype.Service;
import ru.lanwen.junit.entity.CurrentState;

import javax.inject.Inject;

/**
 * @author lanwen (Merkushev Kirill)
 */
@Service
public class CurrentStateService {

    @Inject
    private AggregationRepository started;

    @Inject
    private FinishedRepository finished;

    public CurrentState currentState() {
        return new CurrentState().withTestcases(finished.cases());
    }
}

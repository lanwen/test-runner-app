package ru.lanwen.junit.camel.beans;

import org.slf4j.LoggerFactory;
import ru.lanwen.junit.entity.Failure;
import ru.lanwen.junit.entity.Testcase;

import java.util.Optional;

/**
 * @author lanwen (Merkushev Kirill)
 */
public class UntilFinishedAggregation {

    public Testcase merge(Testcase started, Testcase finished) {
        LoggerFactory.getLogger(getClass()).info("{} {}", started, finished);
        return finished
                .withStart(started.getStart())
                .withFailure(truncatedFailure(finished.getFailure()));
    }

    private Failure truncatedFailure(Failure failure) {
        return Optional.ofNullable(failure)
                .map(toTruncate -> toTruncate.withStackTrace(
                        String.format("%.100s", toTruncate.getStackTrace())
                ))
                .orElse(null);
    }
}

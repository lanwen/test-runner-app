package ru.lanwen.junit.camel.beans;

import ru.lanwen.junit.entity.Testcase;

import java.util.Optional;

/**
 * @author lanwen (Merkushev Kirill)
 */
public class TestcaseFinishedPredicate {

    public boolean finished(Testcase testcase) {
        return Optional.ofNullable(testcase).map(Testcase::getStatus).isPresent();
    }
}

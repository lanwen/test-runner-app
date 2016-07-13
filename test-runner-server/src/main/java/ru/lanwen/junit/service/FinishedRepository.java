package ru.lanwen.junit.service;

import org.springframework.stereotype.Repository;
import ru.lanwen.junit.entity.Testcase;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lanwen (Merkushev Kirill)
 */
@Singleton
@Repository
public class FinishedRepository {

    public List<Testcase> cases = Collections.synchronizedList(new LinkedList<>());

    public Testcase store(Testcase testcase) {
        cases.add(testcase);
        return testcase;
    }

    public List<Testcase> cases() {
        return cases;
    }
}

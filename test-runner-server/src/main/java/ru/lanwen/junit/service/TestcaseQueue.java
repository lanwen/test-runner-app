package ru.lanwen.junit.service;

import ru.lanwen.junit.entity.Testcase;

/**
 * @author lanwen (Merkushev Kirill)
 */
public interface TestcaseQueue {
    
    void enqueue(Testcase testcase);
}

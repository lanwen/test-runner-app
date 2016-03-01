package ru.lanwen.junit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class UserService {

    @Inject
    private Environment env;

    @Value("${section}")
    private String val;

    public String get() {
        return val;
    }
}

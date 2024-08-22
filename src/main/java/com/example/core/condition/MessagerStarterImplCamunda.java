package com.example.core.condition;


import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class MessagerStarterImplCamunda implements MessageStarter{
    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void start(String xml) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("w",xml);
        runtimeService.startProcessInstanceByKey("Process_0et24m7",variables);
    }
}

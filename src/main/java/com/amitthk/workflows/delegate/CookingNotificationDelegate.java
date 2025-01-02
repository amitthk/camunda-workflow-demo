package com.amitthk.workflows.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("cookingNotificationService")
public class CookingNotificationDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Map<String, Object> variables = execution.getVariables();
        String queueNumber = (String) variables.get("queueNumber");

        // Dummy logic: Notify cooking stations
        if (queueNumber != null) {
            execution.setVariable("cookingNotified", true);
        } else {
            throw new RuntimeException("Cooking notification failed. No queue number assigned.");
        }
    }
}

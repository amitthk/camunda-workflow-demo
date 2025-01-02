package com.amitthk.workflows.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("notificationService")
public class CustomerNotificationDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Map<String, Object> variables = execution.getVariables();
        String queueNumber = (String) variables.get("queueNumber");

        // Dummy logic: Notify customer
        if (queueNumber != null) {
            execution.setVariable("customerNotified", true);
        } else {
            throw new RuntimeException("Notification failed. Queue number is missing.");
        }
    }
}

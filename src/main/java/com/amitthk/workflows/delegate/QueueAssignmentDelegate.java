package com.amitthk.workflows.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("queueAssignmentService")
public class QueueAssignmentDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Map<String, Object> variables = execution.getVariables();

        // Dummy logic: Assign a queue number if payment is successful
        boolean paymentSuccess = (boolean) variables.getOrDefault("paymentSuccess", false);
        if (paymentSuccess) {
            execution.setVariable("queueNumber", "Q123");
        } else {
            throw new RuntimeException("Cannot assign queue. Payment failed.");
        }
    }
}

package com.amitthk.workflows.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component("paymentGatewayService")
public class PaymentGatewayDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Map<String, Object> variables = execution.getVariables();
        String paymentType = (String) variables.get("paymentType");
        Double amount = (Double) variables.get("amount");

        // Dummy logic: Simulate payment processing
        boolean paymentSuccessful = amount != null && amount > 0 && !"fail".equalsIgnoreCase(paymentType);
        execution.setVariable("paymentSuccess", paymentSuccessful);
        execution.setVariable("transactionId", paymentSuccessful ? "TXN12345" : null);
    }
}

package com.amitthk.workflows.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import java.util.Map;
@Component("inventoryService")
public class InventoryCheckDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Map<String, Object> variables = execution.getVariables();
        String item = (String) variables.get("item");
        Integer quantity = (Integer) variables.get("quantity");

        // Dummy logic: Simulate inventory check
        boolean inventoryAvailable = quantity != null && quantity > 0 && !"unavailable".equalsIgnoreCase(item);
        execution.setVariable("inventoryAvailable", inventoryAvailable);
    }
}

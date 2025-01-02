package com.amitthk.workflows.service;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestaurantOrderService {
    private final TaskService taskService;

    public String findTaskId(String processInstanceId, String taskDefinitionKey) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(taskDefinitionKey)
                .active()
                .singleResult();

        return task != null ? task.getId() : null;
    }

    public void checkInventory(String taskId, Map<String, Object> data) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("drinks", data.get("drinks")); // List of drinks
        variables.put("foodItems", data.get("foodItems")); // List of food items
        variables.put("inventoryAvailable", data.get("inventoryAvailable")); // Boolean
        taskService.complete(taskId, variables);
    }

    public void updateOrder(String taskId, Map<String, Object> data) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("updatedDrinks", data.get("updatedDrinks")); // Updated drinks
        variables.put("updatedFoodItems", data.get("updatedFoodItems")); // Updated food items
        variables.put("orderUpdated", true); // Boolean for order update
        taskService.complete(taskId, variables);
    }

    public void proceedToPayment(String taskId, Map<String, Object> data) {
        Map<String, Object> variables = new HashMap<>();
            variables.put("orderSummary", data.get("orderSummary")); // Order summary
        taskService.complete(taskId, variables);
    }

    public void processPayment(String taskId, Map<String, Object> data) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("paymentType", data.get("paymentType")); // Card, cash, etc.
        variables.put("paymentSuccess", data.get("paymentSuccess")); // Boolean
        if (Boolean.TRUE.equals(data.get("paymentSuccess"))) {
            variables.put("transactionId", data.get("transactionId")); // Transaction ID
        }
        taskService.complete(taskId, variables);
    }

    public void retryPayment(String taskId, Map<String, Object> data) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("retryPaymentDetails", data.get("retryPaymentDetails")); // Payment details for retry
        taskService.complete(taskId, variables);
    }

    public void assignQueueNumber(String taskId, Map<String, Object> data) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("queueNumber", data.get("queueNumber")); // Queue number
        taskService.complete(taskId, variables);
    }

    public void notifyCookingStations(String taskId, Map<String, Object> data) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("cookingStationsNotified", true); // Boolean for notification
        taskService.complete(taskId, variables);
    }

    public void assembleOrder(String taskId, Map<String, Object> data) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("assembledOrderDetails", data.get("assembledOrderDetails")); // Details of assembled order
        taskService.complete(taskId, variables);
    }

    public void notifyCustomer(String taskId, Map<String, Object> data) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("notificationMessage", data.get("notificationMessage")); // Notification message
        taskService.complete(taskId, variables);
    }

    // Utility methods for sensitive data
    private String maskPII(String input) {
        if (input == null || input.length() < 4) return "****";
        return "****" + input.substring(input.length() - 4);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "****";
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}

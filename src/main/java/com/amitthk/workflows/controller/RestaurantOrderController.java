package com.amitthk.workflows.controller;

import com.amitthk.workflows.service.RestaurantOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/restaurant-order")
@RequiredArgsConstructor
public class RestaurantOrderController {
    private final RestaurantOrderService restaurantOrderService;

    @PostMapping("/{processInstanceId}/check-inventory")
    public ResponseEntity<?> checkInventory(
            @PathVariable String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        return handleWorkflowTask(processInstanceId, "Task_CheckInventory", variables,
                restaurantOrderService::checkInventory);
    }

    @PostMapping("/{processInstanceId}/update-order")
    public ResponseEntity<?> updateOrder(
            @PathVariable String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        return handleWorkflowTask(processInstanceId, "Task_UpdateOrder", variables,
                restaurantOrderService::updateOrder);
    }

    @PostMapping("/{processInstanceId}/proceed-to-payment")
    public ResponseEntity<?> proceedToPayment(
            @PathVariable String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        return handleWorkflowTask(processInstanceId, "Task_ProceedToPayment", variables,
                restaurantOrderService::proceedToPayment);
    }

    @PostMapping("/{processInstanceId}/process-payment")
    public ResponseEntity<?> processPayment(
            @PathVariable String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        return handleWorkflowTask(processInstanceId, "Task_ProcessPayment", variables,
                restaurantOrderService::processPayment);
    }

    @PostMapping("/{processInstanceId}/retry-payment")
    public ResponseEntity<?> retryPayment(
            @PathVariable String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        return handleWorkflowTask(processInstanceId, "Task_RetryPayment", variables,
                restaurantOrderService::retryPayment);
    }

    @PostMapping("/{processInstanceId}/assign-queue")
    public ResponseEntity<?> assignQueueNumber(
            @PathVariable String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        return handleWorkflowTask(processInstanceId, "Task_AssignQueue", variables,
                restaurantOrderService::assignQueueNumber);
    }

    @PostMapping("/{processInstanceId}/notify-cooking")
    public ResponseEntity<?> notifyCookingStations(
            @PathVariable String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        return handleWorkflowTask(processInstanceId, "Task_NotifyCookingStations", variables,
                restaurantOrderService::notifyCookingStations);
    }

    @PostMapping("/{processInstanceId}/assemble-order")
    public ResponseEntity<?> assembleOrder(
            @PathVariable String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        return handleWorkflowTask(processInstanceId, "Task_AssembleOrder", variables,
                restaurantOrderService::assembleOrder);
    }

    @PostMapping("/{processInstanceId}/notify-customer")
    public ResponseEntity<?> notifyCustomer(
            @PathVariable String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        return handleWorkflowTask(processInstanceId, "Task_NotifyCustomer", variables,
                restaurantOrderService::notifyCustomer);
    }

    private ResponseEntity<?> handleWorkflowTask(
            String processInstanceId,
            String taskDefinitionKey,
            Map<String, Object> variables,
            TaskHandler taskHandler) {
        try {
            String taskId = restaurantOrderService.findTaskId(processInstanceId, taskDefinitionKey);
            if (taskId == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", String.format("No active task '%s' found for process instance: %s",
                                        taskDefinitionKey, processInstanceId)
                        ));
            }

            taskHandler.handle(taskId, variables);
            return ResponseEntity.ok(Map.of(
                    "message", "Task completed successfully",
                    "taskDefinitionKey", taskDefinitionKey,
                    "processInstanceId", processInstanceId
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error processing task",
                            "message", e.getMessage(),
                            "processInstanceId", processInstanceId
                    ));
        }
    }

    @FunctionalInterface
    interface TaskHandler {
        void handle(String taskId, Map<String, Object> variables);
    }
}

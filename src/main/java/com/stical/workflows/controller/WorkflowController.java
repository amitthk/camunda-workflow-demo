package com.stical.workflows.controller;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    private ZeebeClient zeebeClient;

    /**
     * Handle student initiation step
     */
    @PostMapping("/student-initiate")
    public ResponseEntity<String> startStudentInitiation(@RequestBody Map<String, Object> variables) {
        return handleJob("Activity_1xdj3k3", variables, "Student initiation completed.");
    }

    /**
     * Handle student registration completion
     */
    @PostMapping("/student-complete")
    public ResponseEntity<String> completeStudentRegistration(@RequestBody Map<String, Object> variables) {
        return handleJob("Activity_0iylgp5", variables, "Student registration completed.");
    }

    /**
     * Handle parent registration step
     */
    @PostMapping("/parent-registration")
    public ResponseEntity<String> completeParentRegistration(@RequestBody Map<String, Object> variables) {
        return handleJob("Activity_095qm55", variables, "Parent registration completed.");
    }

    /**
     * Handle parent KYC step
     */
    @PostMapping("/parent-kyc")
    public ResponseEntity<String> completeParentKYC(@RequestBody Map<String, Object> variables) {
        return handleJob("Activity_0ntxlep", variables, "Parent KYC completed.");
    }

    /**
     * Handle payment details addition step
     */
    @PostMapping("/payment-details")
    public ResponseEntity<String> addPaymentDetails(@RequestBody Map<String, Object> variables) {
        return handleJob("Activity_11e2rb5", variables, "Payment details added.");
    }

    /**
     * General method to handle jobs for the given task type
     */
    private ResponseEntity<String> handleJob(String taskType, Map<String, Object> variables, String successMessage) {
        try {
            // Activate jobs of the given task type
            zeebeClient
                    .newActivateJobsCommand()
                    .jobType(taskType)
                    .maxJobsToActivate(1)
                    .timeout(Duration.ofDays(60000)) // Set the job timeout
                    .send()
                    .join()
                    .getJobs()
                    .stream()
                    .findFirst()
                    .ifPresentOrElse(job -> {
                        // Complete the job with variables
                        zeebeClient.newCompleteCommand(job.getKey())
                                .variables(variables)
                                .send()
                                .join();
                    }, () -> {
                        throw new IllegalStateException("No active jobs found for task type: " + taskType);
                    });

            return ResponseEntity.ok(successMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}

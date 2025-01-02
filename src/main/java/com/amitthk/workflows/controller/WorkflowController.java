package com.amitthk.workflows.controller;

import com.amitthk.workflows.dto.ProcessInstanceDTO;
import com.amitthk.workflows.dto.TaskDTO;
import com.amitthk.workflows.service.WorkflowManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workflow")
@RequiredArgsConstructor
public class WorkflowController {
    private final WorkflowManagementService workflowManagementService;

    @PostMapping("/start")
    public ResponseEntity<ProcessInstanceDTO> startWorkflow(@RequestBody Map<String, Object> initialVariables) {
        ProcessInstanceDTO instance = workflowManagementService.startWorkflowInstance(initialVariables);
        return ResponseEntity.ok(instance);
    }

    @GetMapping("/{processInstanceId}/status")
    public ResponseEntity<ProcessInstanceDTO> getWorkflowStatus(@PathVariable String processInstanceId) {
        ProcessInstanceDTO status = workflowManagementService.getWorkflowStatus(processInstanceId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/{processInstanceId}/tasks")
    public ResponseEntity<List<TaskDTO>> getActiveTasks(@PathVariable String processInstanceId) {
        List<TaskDTO> tasks = workflowManagementService.getActiveTasks(processInstanceId);
        return ResponseEntity.ok(tasks);
    }

}

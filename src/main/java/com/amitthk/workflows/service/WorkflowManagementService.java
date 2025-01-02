package com.amitthk.workflows.service;
import com.amitthk.workflows.dto.ProcessInstanceDTO;
import com.amitthk.workflows.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkflowManagementService {
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public static final String PROCESS_DEFINITION_KEY = "FastFoodOrderingProcess";

    public ProcessInstanceDTO startWorkflowInstance(Map<String, Object> initialVariables) {
        // Add tracking metadata
        Map<String, Object> processVariables = new HashMap<>(initialVariables);
        processVariables.put("startTime", System.currentTimeMillis());
        processVariables.put("initiator", initialVariables.get("userId"));
        processVariables.put("status", "INITIATED");

        // Start the process instance
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                PROCESS_DEFINITION_KEY,
                generateBusinessKey(),
                processVariables
        );

        return new ProcessInstanceDTO(processInstance.getId(),
                processInstance.getBusinessKey(),
                processInstance.getProcessDefinitionId(),
                "INITIATED",
                getCurrentTaskInfo(processInstance.getId())
        );
    }

    public ProcessInstanceDTO getWorkflowStatus(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance == null) {
            throw new IllegalStateException("Process instance not found: " + processInstanceId);
        }

        String status = (String) runtimeService.getVariable(processInstanceId, "status");
        return new ProcessInstanceDTO(
                processInstance.getId(),
                processInstance.getBusinessKey(),
                processInstance.getProcessDefinitionId(),
                status,
                getCurrentTaskInfo(processInstanceId)
        );
    }

    public List<TaskDTO> getActiveTasks(String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .list();

        return tasks.stream()
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getName(),
                        task.getTaskDefinitionKey(),
                        task.getCreateTime(),
                        task.getDueDate()))
                .toList();
    }

    private TaskDTO getCurrentTaskInfo(String processInstanceId) {
        Task currentTask = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .singleResult();

        return currentTask != null ? new TaskDTO(
                currentTask.getId(),
                currentTask.getName(),
                currentTask.getTaskDefinitionKey(),
                currentTask.getCreateTime(),
                currentTask.getDueDate()) : null;
    }

    private String generateBusinessKey() {
        return "REG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}



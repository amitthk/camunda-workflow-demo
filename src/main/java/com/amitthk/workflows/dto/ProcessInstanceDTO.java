package com.amitthk.workflows.dto;

import lombok.Value;

@Value
public class ProcessInstanceDTO {
    String processInstanceId;
    String businessKey;
    String processDefinitionId;
    String status;
    TaskDTO currentTask;
}

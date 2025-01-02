package com.amitthk.workflows.dto;

import lombok.Value;

import java.util.Date;

@Value
public class TaskDTO {
    String taskId;
    String taskName;
    String taskDefinitionKey;
    Date createTime;
    Date dueDate;
}

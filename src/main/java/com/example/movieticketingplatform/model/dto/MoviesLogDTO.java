package com.example.movieticketingplatform.model.dto;

public class MoviesLogDTO {
    private String title;
    private Long operationTargetId;
    private String operationType;
    private String operationTargetType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getOperationTargetId() {
        return operationTargetId;
    }

    public void setOperationTargetId(Long operationTargetId) {
        this.operationTargetId = operationTargetId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationTargetType() {
        return operationTargetType;
    }

    public void setOperationTargetType(String operationTargetType) {
        this.operationTargetType = operationTargetType;
    }
}

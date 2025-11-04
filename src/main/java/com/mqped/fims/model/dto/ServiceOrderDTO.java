package com.mqped.fims.model.dto;

import com.mqped.fims.model.entity.ServiceOrder;
import com.mqped.fims.model.enums.ServiceOrderStatus;
import com.mqped.fims.model.enums.ServiceOrderType;
import java.time.LocalDateTime;

public class ServiceOrderDTO {
    private Integer id;
    private Integer targetId;
    private ServiceOrderStatus status;
    private ServiceOrderType type;
    private LocalDateTime createdAt;
    private LocalDateTime executedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public ServiceOrderStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceOrderStatus status) {
        this.status = status;
    }

    public ServiceOrderType getType() {
        return type;
    }

    public void setType(ServiceOrderType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public static ServiceOrderDTO fromEntity(ServiceOrder serviceOrder) {
        ServiceOrderDTO dto = new ServiceOrderDTO();
        dto.setId(serviceOrder.getId());
        dto.setStatus(serviceOrder.getStatus());
        dto.setType(serviceOrder.getType());
        dto.setCreatedAt(serviceOrder.getCreatedAt());
        dto.setExecutedAt(serviceOrder.getExecutedAt());
        
        if (serviceOrder.getTarget() != null) {
            dto.setTargetId(serviceOrder.getTarget().getId());
        }
        
        return dto;
    }
}
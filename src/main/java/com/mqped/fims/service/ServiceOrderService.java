package com.mqped.fims.service;

import com.mqped.fims.exceptions.InvalidDataException;
import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.ServiceOrder;
import com.mqped.fims.model.enums.ServiceOrderStatus;
import com.mqped.fims.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ServiceOrderService implements CrudService<ServiceOrder, Integer> {

    private final ServiceOrderRepository repository;

    public ServiceOrderService(ServiceOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceOrder add(ServiceOrder order) {
        validate(order);
        return repository.save(order);
    }

    @Override
    public List<ServiceOrder> findAll() {
        return repository.findAll();
    }

    @Override
    public ServiceOrder findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder with id " + id + " not found"));
    }

    @Override
    public ServiceOrder update(Integer id, ServiceOrder order) {
        validate(order);

        ServiceOrder existing = findById(id); // throws if not found
        
        existing.setTarget(order.getTarget());
        existing.setType(order.getType());
        existing.setStatus(order.getStatus());
        existing.setExecutedAt(order.getExecutedAt());
        
        return repository.save(existing);
    }

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ServiceOrder with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    public List<ServiceOrder> findByStatus(ServiceOrderStatus status) {
        return repository.findByStatus(status);
    }

    public List<ServiceOrder> findByTargetId(Integer targetId) {
        return repository.findByTargetId(targetId);
    }

    public List<ServiceOrder> findByTargetDistanceFromBaseBetween(Double min, Double max) {
        return repository.findByTargetDistanceFromBaseBetween(min, max);
    }

    public List<ServiceOrder> findByTargetSignature(String signature) {
        return repository.findByTargetSignature(signature);
    }

    public List<ServiceOrder> findByTargetSignatureContaining(String partial) {
        return repository.findByTargetSignatureContaining(partial);
    }

    public List<ServiceOrder> findOlderThanDays(long days) {
        LocalDateTime cutoff = LocalDateTime.now().minus(days, ChronoUnit.DAYS);
        return repository.findOlderThan(cutoff);
    }

    public List<ServiceOrder> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findByCreatedAtBetween(start, end);
    }

    private void validate(ServiceOrder order) {
        if (order == null) {
            throw new InvalidDataException("ServiceOrder cannot be null");
        }
        if (order.getTarget() == null) {
            throw new InvalidDataException("Target is required");
        }
        if (order.getType() == null) {
            throw new InvalidDataException("ServiceOrder type is required");
        }
    }
}
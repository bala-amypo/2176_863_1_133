package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_suggestions")
public class TransferSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Store sourceStore;

    @ManyToOne(optional = false)
    private Store targetStore;

    @ManyToOne(optional = false)
    private Product product;

    @Column(nullable = false)
    private Integer suggestedQuantity;

    private String reason;

    @Enumerated(EnumType.STRING)
    private TransferStatus status = TransferStatus.PENDING;

    @Column(nullable = false)
    private Integer priority = 1;

    private LocalDateTime generatedAt;

    @PrePersist
    public void prePersist() {
        this.generatedAt = LocalDateTime.now();
        if (status == null) status = TransferStatus.PENDING;
        if (priority == null) priority = 1;
    }


    public Long getId() { return id; }

    public Store getSourceStore() { return sourceStore; }

    public Store getTargetStore() { return targetStore; }

    public Product getProduct() { return product; }

    public Integer getSuggestedQuantity() { return suggestedQuantity; }

    public String getReason() { return reason; }

    public TransferStatus getStatus() { return status; }

    public Integer getPriority() { return priority; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }

    public void setId(Long id) { this.id = id; }

    public void setSourceStore(Store sourceStore) { this.sourceStore = sourceStore; }

    public void setTargetStore(Store targetStore) { this.targetStore = targetStore; }

    public void setProduct(Product product) { this.product = product; }

    public void setSuggestedQuantity(Integer suggestedQuantity) {
        this.suggestedQuantity = suggestedQuantity;
    }

    public void setReason(String reason) { this.reason = reason; }

    public void setStatus(TransferStatus status) { this.status = status; }

    public void setPriority(Integer priority) { this.priority = priority; }
}

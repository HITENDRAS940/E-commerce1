package com.hitendra.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Order order;

    @NotBlank
    @Size(min=4)
    private String paymentMethod;

    private String pgGateway;

    private String pgPaymentId;

    private String pgStatus;

    private String pgResponseMessage;

    private String pgName;

    public Payment(String paymentMethod, String pgPaymentId, String pgStatus, String pgResponseMessage, String pgName) {
        this.paymentMethod = paymentMethod;
        this.pgStatus = pgStatus;
        this.pgPaymentId = pgPaymentId;
        this.pgResponseMessage = pgResponseMessage;
        this.pgName = pgName;
    }
}

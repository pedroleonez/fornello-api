package pedroleonez.fornello.api.entities;

import jakarta.persistence.*;
import lombok.*;
import pedroleonez.fornello.api.enums.PaymentMethod;
import pedroleonez.fornello.api.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    private BigDecimal amount;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private DeliveryData deliveryData;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @PrePersist
    private void configureCreatedDate() {
        this.createdDate = LocalDateTime.now();
    }
}

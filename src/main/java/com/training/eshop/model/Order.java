package com.training.eshop.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Order implements Serializable {

    private static final long serialVersionUID = 3906771677381811334L;

    @Id
    @SequenceGenerator(name = "ordersIdSeq", sequenceName = "order_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordersIdSeq")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Good> goods;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Attachment> attachments;

    public Order() {
        this.goods = new ArrayList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(totalPrice, order.totalPrice) &&
                Objects.equals(user, order.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalPrice, user);
    }
}

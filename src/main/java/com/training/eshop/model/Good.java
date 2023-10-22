package com.training.eshop.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "goods")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Good implements Serializable {

    private static final long serialVersionUID = 3906771677381811334L;

    @Id
    @SequenceGenerator(name = "goodsIdSeq", sequenceName = "goods_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goodsIdSeq")
    @Column(name = "id", nullable = false, updatable = false)
    @ToString.Exclude
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;

    public Good(String title, BigDecimal price) {
        this.title = title;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Good good = (Good) o;
        return Objects.equals(id, good.id) &&
                Objects.equals(title, good.title) &&
                Objects.equals(price, good.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price);
    }
}


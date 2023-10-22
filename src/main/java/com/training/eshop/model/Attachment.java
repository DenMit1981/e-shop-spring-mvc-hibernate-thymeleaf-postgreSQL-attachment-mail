package com.training.eshop.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "attachment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Attachment implements Serializable {

    private static final long serialVersionUID = 3906771677381811334L;

    @Id
    @SequenceGenerator(name = "attachmentIdSeq", sequenceName = "attachment_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachmentIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "file")
    @Type(type = "org.hibernate.type.BinaryType")
    @ToString.Exclude
    private byte[] file;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;

    public Attachment(String name, byte[] file) {
        this.name = name;
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Arrays.equals(file, that.file) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, order);
        result = 31 * result + Arrays.hashCode(file);
        return result;
    }
}

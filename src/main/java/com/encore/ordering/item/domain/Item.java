package com.encore.ordering.item.domain;

import com.encore.ordering.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private int price;
    private int stockQuantity;
    private String imagePath;
    @Builder.Default
    private String delYn = "N";

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    public void deleteItem() {
        this.delYn = "Y";
    }

    public void updateItem(String name, String category, int price, int stockQuantity, String imagePath) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imagePath = imagePath;
    }

    public void updateStockQuantity(int newQuantity) {
        this.stockQuantity = newQuantity;

    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}

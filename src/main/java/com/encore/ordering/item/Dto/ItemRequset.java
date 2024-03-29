package com.encore.ordering.item.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class ItemRequset {
    private String name;
    private String category;
    private int price;
    private int stockQuantity;
    private MultipartFile itemImage;

}

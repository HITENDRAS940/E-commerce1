package com.hitendra.ecommerce.payload;

 import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long OrderItemId;
    private ProductDTO product;
    private Integer quantity;
    private Double discount;
    private Double orderedProductPrice;
}

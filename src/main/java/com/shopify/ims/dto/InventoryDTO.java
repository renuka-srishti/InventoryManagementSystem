package com.shopify.ims.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class InventoryDTO {
    private String name;
    private String description;
    private String quantity;
    private String pricePerItem;
}

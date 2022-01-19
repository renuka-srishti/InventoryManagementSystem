package com.shopify.ims.dto.mapper;

import com.shopify.ims.dto.InventoryDTO;
import com.shopify.ims.model.Inventory;

public class InventoryMapper {
    public static Inventory toInventory(InventoryDTO inventoryDTO) {
        return new Inventory()
                .setName(inventoryDTO.getName())
                .setDescription(inventoryDTO.getDescription())
                .setPricePerItem(Double.parseDouble(inventoryDTO.getPricePerItem()))
                .setQuantity(Long.valueOf(inventoryDTO.getQuantity()));
    }

    public static InventoryDTO toInventoryDTO(Inventory inventory) {
        return new InventoryDTO()
                .setName(inventory.getName())
                .setDescription(inventory.getDescription())
                .setPricePerItem(String.valueOf(inventory.getPricePerItem()))
                .setQuantity(String.valueOf(inventory.getQuantity()));
    }
}

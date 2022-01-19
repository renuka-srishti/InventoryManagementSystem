package com.shopify.ims.exception;

public class InventoryNotExistException extends RuntimeException {
    public InventoryNotExistException(Long inventoryId) {
        super(String.format("Inventory does not exist with id: %d", inventoryId));
    }
}

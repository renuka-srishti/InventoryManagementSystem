package com.shopify.ims.exception;

public class GroupNotExistException extends RuntimeException {
    public GroupNotExistException(Long groupId) {
        super(String.format("Group does not exist with id: %d", groupId));
    }
}

package com.shopify.ims.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssignGroupToInventoriesDTO {
    private List<Long> groupIds;
    private List<Long> inventoryIds;
}

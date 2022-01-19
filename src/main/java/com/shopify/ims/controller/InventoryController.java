package com.shopify.ims.controller;

import com.shopify.ims.dto.InventoryIdsDTO;
import com.shopify.ims.model.Group;
import com.shopify.ims.model.Inventory;
import com.shopify.ims.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    /**
     * This API adds a new inventory.
     *
     * @param inventory new inventory object.
     */
    @PostMapping("/new")
    public ResponseEntity addInventory(@RequestBody Inventory inventory) {
        inventoryService.add(inventory);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * This API deletes an inventory.
     *
     * @param inventoryIdsDTO list of inventoryIds that needs to be deleted.
     */
    @PostMapping("/delete")
    public ResponseEntity deleteInventory(@RequestBody InventoryIdsDTO inventoryIdsDTO) {
        inventoryService.delete(inventoryIdsDTO.getInventoryIds());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * This API updates an inventory.
     *
     * @param inventoryId that needs to be updated.
     * @param inventory updated inventory object.
     */
    @PostMapping("/update/{inventoryId}")
    public ResponseEntity updateInventory(@PathVariable long inventoryId, @RequestBody Inventory inventory) {
        inventoryService.update(inventoryId, inventory);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * This API lists all inventories.*
     */
    @GetMapping("/all")
    public ResponseEntity<List<Inventory>> listInventory() {
        return new ResponseEntity<>(inventoryService.getInventoryList(), HttpStatus.OK);
    }

    /**
     * This API returns an inventory.
     *
     * @param inventoryId that needs to be returned.
     */
    @GetMapping("/get/{inventoryId}")
    public ResponseEntity<Inventory> inventoryById(@PathVariable long inventoryId) {
        return new ResponseEntity<>(inventoryService.getInventoryById(inventoryId), HttpStatus.OK);
    }

//    @PostMapping("/assignToGroups(/{inventoryId}")
//    public ResponseEntity assignInventoryToGroups(@PathVariable long inventoryId, @RequestParam List<Long> groupIds) {
//        inventoryService.assignInventoryToGroups(inventoryId, groupIds);
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }

    /**
     * This API returns all groups of an inventory.
     *
     * @param inventoryId requested inventoryId.
     */
    @GetMapping("/getGroups/{inventoryId}")
    public ResponseEntity<List<Group>> getGroupsByInventoryId(@PathVariable long inventoryId) {
        List<Group> groups = inventoryService.getGroupsByInventoryId(inventoryId);
        if (groups == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
}

package com.shopify.ims.controller;

import com.shopify.ims.dto.AssignGroupToInventoriesDTO;
import com.shopify.ims.dto.GroupDTO;
import com.shopify.ims.model.Group;
import com.shopify.ims.model.Inventory;
import com.shopify.ims.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
@Log4j2
public class GroupController {

    private final GroupService groupService;

    /**
     * This API lists all groups.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Group>> getAllGroups() {
        return new ResponseEntity<>(groupService.getAllGroups(), HttpStatus.OK);
    }

    /**
     * This API returns a group.
     *
     * @param groupId requested groupId.
     */
    @GetMapping("/get/{groupId}")
    public ResponseEntity<Group> getGroupById(@PathVariable long groupId) {
        return new ResponseEntity<>(groupService.getGroupById(groupId), HttpStatus.OK);
    }

    /**
     * This API creates a new group.
     *
     * @param groupDTO new group name.
     */
    @PostMapping("/new")
    public ResponseEntity createGroup(@RequestBody GroupDTO groupDTO) {
        Group g = groupService.createGroup(groupDTO.getName());
        log.info(String.format("Created new Group: %d", g.getId()));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * This API assigns list of inventories to list of groups.
     * Each inventory will be assigned to each group. It ignores if a relationship already exists.
     *
     * @param assignGroupToInventoriesDTO a dto object which contains list of groups and list of inventories.
     */
    @PostMapping("/assignGroupsToInventories")
    public ResponseEntity assignGroupsToInventories(@RequestBody AssignGroupToInventoriesDTO assignGroupToInventoriesDTO) {
        groupService.assignGroupsToInventories(assignGroupToInventoriesDTO.getGroupIds(), assignGroupToInventoriesDTO.getInventoryIds());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * This API deletes a group.
     *
     * @param groupId groupId that needs to be deleted.
     */
    @PostMapping("/delete/{groupId}")
    public ResponseEntity deleteGroup(@PathVariable long groupId) {
        groupService.delete(groupId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * This API returns all inventories in a group.
     *
     * @param groupId requested groupId.
     */
    @GetMapping("/getInventories/{groupId}")
    public ResponseEntity<List<Inventory>> getAllGroups(@PathVariable long groupId) {
        return new ResponseEntity<>(groupService.getInventoriesByGroup(groupId), HttpStatus.OK);
    }

    /**
     * This API removes list of inventories from list of groups.
     * Each inventory will be removed from each group. It ignores if a relationship doesn't exist.
     *
     * @param assignGroupToInventoriesDTO a dto object which contains list of groups and list of inventories.
     */
    @PostMapping("/removeInventoriesFromGroup")
    public ResponseEntity removeInventoriesFromGroup(@RequestBody AssignGroupToInventoriesDTO assignGroupToInventoriesDTO) {
        groupService.removeInventoriesFromGroup(assignGroupToInventoriesDTO.getGroupIds(), assignGroupToInventoriesDTO.getInventoryIds());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}

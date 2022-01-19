package com.shopify.ims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "named_group")
@Getter
@Setter
public class Group extends AbstractPersistable<Long> {
    private String name;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "inventory_group",
            joinColumns = @JoinColumn(name = "named_group_id"),
            inverseJoinColumns = @JoinColumn(name = "inventory_id"))
    private Set<Inventory> inventories = new HashSet<>();
}

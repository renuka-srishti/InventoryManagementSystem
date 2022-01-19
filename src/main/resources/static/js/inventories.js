$(document).ready(function () {
    $("#getAllInventory").click(function () {
        loadInventories();
    });

    $("#inventoryAdd").click(function () {
        if ($("#inventoryAdd").text() == "Add") {
            addNewInventory();
        }
    });

    $("#removeInventories").click(function () {
        deleteMultipleInventories();
    });
});

function addNewInventory() {
    inventoryForm = $("#addInventoryForm")[0];
    if (!inventoryForm.checkValidity()) {
        inventoryForm.classList.add('was-validated')
        return;
    }

    inventoryPayload = {
        name: $("#inventoryName").val(),
        description: $("#inventoryDescription").val(),
        quantity: $("#inventoryQuantity").val(),
        pricePerItem: $("#inventoryPrice").val()
    };

    $.ajax({
        type: "POST",
        url: "/inventory/new",
        data: JSON.stringify(inventoryPayload),
        contentType: 'application/json',
        success: function (response) {
            resetInventoryForm();
            showToast("Added: " + inventoryPayload['name'])
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function loadInventories() {
    $("#selectAll").prop("checked", false);
    $.ajax({
        type: "GET",
        url: "/inventory/all",
        success: function (response) {
            $("#inventoryTable tbody").empty();
            $.each(response, function (index, inventory) {

                let checkboxColumn = '<span class="custom-checkbox">' +
                    '<input type="checkbox" id=\"' + inventory.id + '\" name="options[]" value="1" onClick=\"inventoryRowCheckbox(this)\">' +
                    '<label for="checkbox1"></label>' +
                    '</span>';

                let actionsColumn = '<a id=' + '\"' + inventory.id + '\"' + 'class="link-primary" onClick=\"editInventory(this.id)\" title=\"Edit Inventory\">' +
                    '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-pencil-fill" viewBox="0 0 20 20">' +
                    '<path d="M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708l-3-3zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207l6.5-6.5zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.499.499 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11l.178-.178z"/>' +
                    '</svg></a>' + '&nbsp;&nbsp;&nbsp;' +
                    '<a id=' + '\"' + inventory.id + '\"' + 'class="link-danger" onClick=\"deleteInventory(this.id)\" title=\"Delete Inventory\">' +
                    '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-trash-fill" viewBox="0 0 20 20">' +
                    '<path d="M2.5 1a1 1 0 0 0-1 1v1a1 1 0 0 0 1 1H3v9a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V4h.5a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H10a1 1 0 0 0-1-1H7a1 1 0 0 0-1 1H2.5zm3 4a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 .5-.5zM8 5a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7A.5.5 0 0 1 8 5zm3 .5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 1 0z"/>' +
                    '</svg></a>' + '&nbsp;&nbsp;&nbsp;' +
                    '<a id=' + '\"' + inventory.id + '\"' + 'class="link-info" onClick=\"viewGroupsByInventory(this.id)\" title=\"View Groups\" data-bs-target=\"#viewGroupsOfInventoryModal\" data-bs-toggle=\"modal\">' +
                    '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-collection-fill" viewBox="0 0 20 20">' +
                    '<path d="M0 13a1.5 1.5 0 0 0 1.5 1.5h13A1.5 1.5 0 0 0 16 13V6a1.5 1.5 0 0 0-1.5-1.5h-13A1.5 1.5 0 0 0 0 6v7zM2 3a.5.5 0 0 0 .5.5h11a.5.5 0 0 0 0-1h-11A.5.5 0 0 0 2 3zm2-2a.5.5 0 0 0 .5.5h7a.5.5 0 0 0 0-1h-7A.5.5 0 0 0 4 1z"/>' +
                    '</svg></a>';

                let inventoryRow = '<tr>' +
                    '<td>' + checkboxColumn + '</td>' +
                    '<td>' + inventory.name + '</td>' +
                    '<td>' + inventory.description + '</td>' +
                    '<td>' + inventory.quantity + '</td>' +
                    '<td>' + inventory.pricePerItem + '</td>' +
                    '<td>' + actionsColumn + '</td>' +
                    '</tr>';
                $('#inventoryTable tbody').append(inventoryRow);
            });
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function inventoryRowCheckbox() {
    if (!this.checked) {
        $("#selectAll").prop("checked", false);
    };
}

function viewGroupsByInventory(inventoryId) {
    $.ajax({
        type: "GET",
        url: "/inventory/getGroups/" + inventoryId,
        success: function (response) {
            $("#viewGroupsOfInventoryTable").empty();
            $.each(response, function (index, group) {
                let deleteButton = '<button ' +
                    ' type="button" class="btn btn-danger btn_delete" data-toggle="modal" data-target="#delete-modal"' +
                    ' onClick=\"removeGroupFromInventory(' + inventoryId + ',' + group.id + ')\"' +
                    '>&times</button>';

                let groupRow = '<a href=\"#\" class=\"list-group-item list-group-item-action py-3 lh-tight\">' +
                    '<div class=\"d-flex w-100 align-items-center justify-content-between\">' +
                    '<strong class=\"mb-1\">' + group.name + '</strong>' +
                    '<small>' + deleteButton + '</small>' +
                    '</div>' +
                    '</a>';

                $('#viewGroupsOfInventoryTable').append(groupRow);
            });
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function removeGroupFromInventory(inventoryId, groupId) {
    $.ajax({
        type: "POST",
        url: "/group/removeInventoriesFromGroup",
        data: JSON.stringify({ "groupIds": [groupId], "inventoryIds": [inventoryId] }),
        contentType: 'application/json',
        success: function (response) {
            showToast("Removed: " + groups + "Inventories: " + inventories);
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function deleteMultipleInventories() {
    var inventories = [];
    var inventoriesCheckbox = $('table tbody input[type="checkbox"]');
    inventoriesCheckbox.each(function () {
        if (this.checked) {
            inventories.push(this.id)
        }
    });
    $("#deleteInventoryModal").modal('hide');
    deleteInventory(inventories);
}

function deleteInventory(rowId) {
    if (!Array.isArray(rowId)) {
        rowId = [rowId];
    }
    $.ajax({
        type: "POST",
        url: "/inventory/delete",
        data: JSON.stringify({ "inventoryIds": rowId }),
        contentType: 'application/json',
        success: function (response) {
            showToast("Deleted: " + rowId);
            resetInventoryForm();
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function resetInventoryForm() {
    inventoryForm = $("#addInventoryForm")[0];
    inventoryForm.reset();
    $("#addInventoryModal").modal('hide');
    $(inventoryForm).removeClass('was-validated');
}

function disableInventoryForm() {
    inventoryForm = $("#addInventoryForm")[0];
    inventoryForm.reset();
    $(inventoryForm).removeClass('was-validated');
}

function editInventory(rowId) {
    resetInventoryForm();
    $("#inventoryAdd").text("Update");
    inventoryUpdate(rowId);
}

function inventoryUpdate(inventoryId) {
    $.ajax({
        type: "GET",
        url: "/inventory/get/" + inventoryId,
        success: function (response) {
            updateInventoryForm(response);
            $("#addInventoryModal").modal('show');
            $('#inventoryAdd').click(function () {
                if ($("#inventoryAdd").text() == "Update") {
                    updateInventory(inventoryId);
                }
            });
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function updateInventoryForm(inventory) {
    $("#inventoryName").val(inventory['name']);
    $("#inventoryDescription").val(inventory['description']);
    $("#inventoryQuantity").val(inventory['quantity']);
    $("#inventoryPrice").val(inventory['pricePerItem']);
}

function updateInventory(inventoryId) {
    inventoryForm = $("#addInventoryForm")[0];
    if (!inventoryForm.checkValidity()) {
        inventoryForm.classList.add('was-validated')
        return;
    }

    inventoryPayload = {
        name: $("#inventoryName").val(),
        description: $("#inventoryDescription").val(),
        quantity: $("#inventoryQuantity").val(),
        pricePerItem: $("#inventoryPrice").val()
    };

    $.ajax({
        type: "POST",
        url: "/inventory/update/" + inventoryId,
        data: JSON.stringify(inventoryPayload),
        contentType: 'application/json',
        success: function (response) {
            showToast("Updated: " + inventoryPayload['name'])
            resetInventoryForm();
            $("#inventoryAdd").text("Add");
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function showToast(message) {
    var toastLiveExample = document.getElementById('liveToast');
    $("#toastMessageBody").text(message);
    var toast = new bootstrap.Toast(toastLiveExample);
    toast.show();
}
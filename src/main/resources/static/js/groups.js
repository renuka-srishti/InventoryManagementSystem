$(document).ready(function () {
//    $("#getAllGroup").click(function () {
//        loadGroups();
//    });

    $("#addGroup").click(function () {
        createGroup();
    });

    $('#addInventoryToGroupModal').on('shown.bs.modal', function (e) {
        loadGroupsForAddInventoryToGroupModal();
    });

    $('#removeInventoryFromGroupModal').on('shown.bs.modal', function (e) {
        loadRemoveInventoryFromGroupModal();
    });

    $("#addInventoryToGroup").click(function () {
        updateInventoryToGroup();
    });

    $("#removeInventoryFromGroup").click(function () {
        removeInventoryFromGroup();
    });
});

function createGroup() {
    groupForm = $("#addGroupForm")[0];
    if (!groupForm.checkValidity()) {
        groupForm.classList.add('was-validated')
        return;
    }

    groupPayload = {
            name: $("#groupName").val()
    };

    $.ajax({
        type: "POST",
        url: "/group/new",
        data: JSON.stringify(groupPayload),
        contentType: 'application/json',
        success: function (response) {
            resetGroupForm();
            showToast("Added: " + groupPayload['name'])
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function resetGroupForm() {
    groupForm = $("#addGroupForm")[0];
    groupForm.reset();
    $("#addGroupModal").modal('hide');
    $(groupForm).removeClass('was-validated');
}

function updateInventoryToGroup() {
    var groupCheckbox = $('#groupTable input[type="checkbox"]');
    var inventoriesCheckbox = $('table tbody input[type="checkbox"]');

    var groups = [];
    var inventories = [];

    groupCheckbox.each(function () {
        if (this.checked) {
            groups.push(this.id)
        }
    });

    inventoriesCheckbox.each(function () {
        if (this.checked) {
            inventories.push(this.id)
        }
    });

    $.ajax({
        type: "POST",
        url: "/group/assignGroupsToInventories",
        data: JSON.stringify({ "groupIds": groups, "inventoryIds": inventories }),
        contentType: 'application/json',
        success: function (response) {
            $("#addInventoryToGroupModal").modal('hide');
            showToast("Assigned: " + groups + "Inventories: " + inventories);
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function removeInventoryFromGroup() {
    var groupCheckbox = $('#removeFromGroupTable input[type="checkbox"]');
    var inventoriesCheckbox = $('table tbody input[type="checkbox"]');

    var groups = [];
    var inventories = [];

    groupCheckbox.each(function () {
        if (this.checked) {
            groups.push(this.id)
        }
    });

    inventoriesCheckbox.each(function () {
        if (this.checked) {
            inventories.push(this.id)
        }
    });

    $.ajax({
        type: "POST",
        url: "/group/removeInventoriesFromGroup",
        data: JSON.stringify({ "groupIds": groups, "inventoryIds": inventories }),
        contentType: 'application/json',
        success: function (response) {
            $("#removeInventoryFromGroupModal").modal('hide');
            showToast("Removed: " + groups + "Inventories: " + inventories);
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function loadGroupsForAddInventoryToGroupModal() {
    $.ajax({
        type: "GET",
        url: "/group/all",
        success: function (response) {
            $("#groupTable").empty();
            $.each(response, function (index, group) {

                let groupRow = '<div class="form-check list-group-item list-group-item-action">' +
                    '<input class="form-check-input" type="checkbox" value="" id=\"' + group.id + '\">' +
                    '<label class="form-check-label" for=\"' + group.id + '\">' +
                    group.name +
                    '</label>' +
                    '</div>';
                $('#groupTable').append(groupRow);
            });
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function loadRemoveInventoryFromGroupModal() {
    $.ajax({
        type: "GET",
        url: "/group/all",
        success: function (response) {
            $("#removeFromGroupTable").empty();
            $.each(response, function (index, group) {

                let groupRow = '<div class="form-check list-group-item list-group-item-action">' +
                    '<input class="form-check-input" type="checkbox" value="" id=\"' + group.id + '\">' +
                    '<label class="form-check-label" for=\"' + group.id + '\">' +
                    group.name +
                    '</label>' +
                    '</div>';
                $('#removeFromGroupTable').append(groupRow);
            });
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function loadGroups() {
    $.ajax({
        type: "GET",
        url: "/group/all",
        success: function (response) {
            $("#groupTable").empty();
            $.each(response, function (index, group) {
                let deleteButton = '<button ' +
                    'id=' +
                    '\"' + group.id + '\"' +
                    ' type="button" class="btn btn-danger btn_delete" data-toggle="modal" data-target="#delete-modal"' +
                    ' onClick=\"deleteGroup(this.id)\"' +
                    '>&times</button>';

                let groupRow = '<a href=\"#\" class=\"list-group-item list-group-item-action py-3 lh-tight\" onClick=\"groupRowSelection(\'' + group.id + '\')\">' +
                    '<div class=\"d-flex w-100 align-items-center justify-content-between\">' +
                    '<strong class=\"mb-1\">' + group.name + '</strong>' +
                    '<small>' + deleteButton + '</small>' +
                    '</div>' +
                    '</a>';

                $('#groupTable').append(groupRow);
            });
        },
        error: function (e) {
            showToast("ERROR: ", e);
            console.log("ERROR: ", e);
        }
    });
}

function groupRowSelection(rowId) {
    loadInventoriesByGroup(rowId);
}

//function loadInventoriesByGroup(groupId) {
//    $.ajax({
//        type: "GET",
//        url: "/group/inventories/" + groupId,
//        success: function(response) {
//            $("#inventoriesByGroupTable").empty();
//            $.each(response, function(index, inventory) {
//                let deleteButton = '<button ' +
//                                   'id=' +
//                                   '\"' + inventory.id + '\"'+
//                                   ' type="button" class="btn btn-danger btn_delete" data-toggle="modal" data-target="#delete-modal"' +
//                                   ' onClick=\"removeInventoryFromGroup(this.id,' + groupId + ')\"' +
//                                   '>&times</button>';
//
//                let groupRow = '<a href=\"#\" class=\"list-group-item list-group-item-action py-3 lh-tight\">' +
//                               '<div class=\"d-flex w-100 align-items-center justify-content-between\">' +
//                               '<strong class=\"mb-1\">' + inventory.name + '</strong>' +
//                               '<small>' + deleteButton + '</small>' +
//                               '</div>' +
//                               '</a>';
//
//                $('#inventoriesByGroupTable').append(groupRow);
//            });
//        },
//        error: function(e) {
//            showToast("ERROR: ", e);
//            console.log("ERROR: ", e);
//        }
//    });
//}

function deleteGroup(rowId) {
    $.ajax({
        type: "POST",
        url: "/group/delete/" + rowId,
        success: function (response) {
            showToast("Deleted: " + rowId);
            //                resetInventoryForm();
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
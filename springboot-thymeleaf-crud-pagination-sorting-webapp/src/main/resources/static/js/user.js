var listUser = [];  //global list student

$(document).ready(function () {
	getData();
	getRoles();
});
function getData(){
	$.ajax({
        method: "GET",
        url: "/admin/user/list", 
        contentType: "application/json",
        success: function (result) {
        	console.log("*******"+ JSON.stringify(result));
        	listUser = result;
            addTable(listUser);
        }
    });
}
function addTable(users) {
    var myTableDiv = document.getElementById("jstable");
    let table = "";
    //console.log(JSON.stringify(users));
    table += `<table class="table table-hover">`;
    /* adding header row */
    table += `
    <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Username</th>
            <th scope="col">Email</th>
            <th scope="col">Roles</th>
            <th scope="col">Actions</th>
        </tr>
    </thead>`;

    table += `<tbody>`;
    
    users.forEach((user, index) => {
    	var roles = user.likedRoles;
    	console.log("roles: " + JSON.stringify(roles));
    	var strRoles = [];
    	if (roles != null && roles != undefined ){
    		roles.forEach((role, ix) => {
        		strRoles.push(role.name);
        	});
        	
    	}
    	console.log("2. userid = "+user.id+"; roles: " + JSON.stringify(strRoles));
        //add each row
        table += `
         <tr>
           <th>${index + 1}</th>
           <td>${user.username || ''}</td>
           <td>${user.email || ''}</td>`;
        	
        table += `<td>`+ strRoles.join(',') + `</td>`;
        table += `<td class="action"><i class="fas fa-pen" onClick="editData(${index})"></i>&nbsp;&nbsp;&nbsp;`
        	+`<i class="fas fa-trash" onClick="deleteData(${user.id})"></i>&nbsp;&nbsp;&nbsp;`
        	+`<i class="fas fa-user" onClick="assignRole(${index})"></i>`
        	+`</td>`;
        table += `</tr>`;
    });

    table += `</tbody>`;

    myTableDiv.innerHTML = table;
}

// chuyển giới tính từ bool sang Text
function getGenderText(rawGender) {
    switch (rawGender) {
        case 1:
            return "Nữ"
        case 0:
            return "Nam"
        default:
            return ""
    }
}

//TODO: convert thành dd//mm/yyyy;
function getStudentBirthday(rawData) {
    if (rawData) return new Date(rawData).toString();
    return "";
}
function validateEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}
function editData(indexOfRow) {
    var record = listUser[indexOfRow] || {};
    $('#editModal').modal('show');
    $("#editModal [name=id]").val(record.id);
    $("#editModal [name=username]").val(record.username);
    $("#editModal [name=email]").val(record.email);

}

function createNew() {
    var formEl = document.forms.createForm;
    var formData = new FormData(formEl);
    var username = formData.get('username');
    var email = formData.get('email');
    var password = formData.get('password');
    var confirm_password = formData.get('confirm_password');
    // validate
    if (!validateEmail(email)){
    	$("#message_new").html("Email is invalid!");
    	$("#email").focus();
    	return;
    }
    
    if (password !== confirm_password ){
    	$("#message_new").html("Password is not equals!");
    	$("#confirm_password").focus();
    	return;
    }
    if (password.length < 6){
    	$("#message_new").html("Password must least length is 6 character!");
    	$("#password").focus();
    	return;
    }
    let obj = {
    		username: username ? username.trim() : '',
    		email: email ? email.trim() : '',
    		password: password || ''
    };

    $.ajax({
        method: "POST",
        url: '/admin/user/save',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(obj),
        cache: false,
        success: function (data) {
            //location.reload();
        	getData();
        	$('#createModal').modal('hide');
        	$("#message").html("Created successfully.");
        	$("#message").show();
        },
        error: function(err) {
        	$("#message").html("Created fail: " + err);
        	$("#message").show();
		}
    });
}

function updateData() {
    var formEl = document.forms.editForm;
    var formData = new FormData(formEl);
    var username = formData.get('username');
    var email = formData.get('email');
    var id = formData.get('id');

    let editModel = {
    		username: username ? username.trim() : '',
    		email: email ? email.trim() : '',
    		id: Number(id) || 0
    };

    $.ajax({
        method: "POST",
        url: '/admin/user/save',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(editModel),
        cache: false,
        success: function (data) {
        	getData();
        	$('#editModal').modal('hide');
        	$("#message").html("Updated successfully.");
        	$("#message").show();
        },
        error: function(err) {
        	$("#message").html("Created fail: " + err);
        	$("#message").show();
		}
    });
}

function deleteData(id) {
    if(confirm("Are you sure to delete this record?")){
    	$.ajax({
            method: "DELETE",
            url: '/admin/user/delete/' + id,
            cache: false,
            success: function (data) {
            	console.log("delete: "+ JSON.stringify(data));
            	getData();
            	$("#message").html("Deleted successfully.");
            	$("#message").show();
            },
            error: function(err) {
            	$("#message").html("Created fail: " + JSON.stringify(err));
            	$("#message").show();
    		}
        });
    }
}
function assignRole(indexOfRow) {
    var record = listUser[indexOfRow] || {};
    $("#roleModal [name=id]").val(record.id);
    $("#roleModal [name=username]").val(record.username);
    $('#roleModal [name="roles[]"]').each(function() {
    	 $(this).prop("checked", false);
    });
    var roles = record.likedRoles;
	var strRoles = [];
	roles.forEach((role, ix) => {
		//strRoles.push(role.name);
		$("#roleModal [id=role"+role.id+"]").prop("checked", true);
	});
    $('#roleModal').modal('show');
}
function configRole(){
	var id = $("#roleModal [name=id]").val();
	var roleIds = [];
	$('#roleModal [name="roles[]"]').each(function() {
   	  if ($(this).prop("checked")){
   		roleIds.push($(this).val());
   	  }
   });
	var sroleId = roleIds.join(",");
	let editModel = {
    		roleids: sroleId,
    		id: Number(id) || 0
    };

    $.ajax({
        method: "POST",
        url: '/admin/role/config',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(editModel),
        cache: false,
        success: function (data) {
        	getData();
        	$('#roleModal').modal('hide');
        	$("#message").html("Config role successfully.");
        	$("#message").show();
        },
        error: function(err) {
        	$("#message").html("Config fail: " + err);
        	$("#message").show();
		}
    });
}

function getRoles(){
	var roles = [];
	$.ajax({
        method: "GET",
        url: "/admin/role/list", 
        contentType: "application/json",
        async: false,
        success: function (result) {
        	console.log("+++++role:"+ JSON.stringify(result));
        	roles = result;
        	addTableRoles(roles);
        }
    });
}
function addTableRoles(roles) {
    var myTableDiv = document.getElementById("divRole");
    let table = "";
    console.log(JSON.stringify(roles));
    table += `<table class="table table-hover">`;
    /* adding header row */
    table += `
    <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Role</th>
        </tr>
    </thead>`;

    table += `<tbody>`;
    
    roles.forEach((role, index) => {
        //add each row
        table += `
         <tr>
           <th><input type='checkbox' name="roles[]" id="role${role.id}" value="${role.id}"</th>
           <td>${role.name || ''}</td>`;
        table += `</tr>`
    });

    table += `</tbody></table>`;

    myTableDiv.innerHTML = table;
}
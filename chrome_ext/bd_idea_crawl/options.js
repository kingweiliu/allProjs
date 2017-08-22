alert('ko');

function saveData(){
	var edtMenuUrl = document.getElementById('edt_menu_url_pattern');
	var edtContentUrl = document.getElementById('edt_item_detail_url_pattern');
	
	window.localStorage['menuUrlPattern'] = edtMenuUrl.value;
	window.localStorage['detailUrlPattern'] = edtContentUrl.value;
	alert("save ok");
}

function readData(){
	
	var edt_menu_url_pattern = document.getElementById('edt_menu_url_pattern');
	var edt_item_detail_url_pattern = document.getElementById('edt_item_detail_url_pattern');
	if(window.localStorage['menuUrlPattern'])
	  	edt_menu_url_pattern.value =window.localStorage['menuUrlPattern'];
	if(window.localStorage['detailUrlPattern'])
	 	edt_item_detail_url_pattern.value =  	window.localStorage['detailUrlPattern'] ;
}

window.onload = function(){
	readData();
	var btnSave = document.getElementById('btnSave');
	btnSave.addEventListener('click', saveData);
};


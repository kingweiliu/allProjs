alert('ko');

function saveData(){
	var edtMenuUrl = document.getElementById('edt_menu_url_pattern');
	var edtContentUrl = document.getElementById('edt_item_detail_url_pattern');
	var edtStartUrl = document.getElementById('edt_start_url');
	
	window.localStorage['menuUrlPattern'] = edtMenuUrl.value;
	window.localStorage['detailUrlPattern'] = edtContentUrl.value;
	window.localStorage['startUrl'] = edtStartUrl.value;
	alert("save ok");
}

function readData(){
	
	var edt_menu_url_pattern = document.getElementById('edt_menu_url_pattern');
	var edt_item_detail_url_pattern = document.getElementById('edt_item_detail_url_pattern');
	var edtStartUrl = document.getElementById('edt_start_url');
	
	if(window.localStorage['menuUrlPattern'])
	  	edt_menu_url_pattern.value =window.localStorage['menuUrlPattern'];
	if(window.localStorage['detailUrlPattern'])
	 	edt_item_detail_url_pattern.value =  	window.localStorage['detailUrlPattern'] ;
	 if(window.localStorage['startUrl'])
	 	edtStartUrl.value =  	window.localStorage['startUrl'] ;
}

window.onload = function(){
	readData();
	var btnSave = document.getElementById('btnSave');
	btnSave.addEventListener('click', saveData);
};


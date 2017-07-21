alert('ko');

function saveData(){
	alert(1);
	var edtName = document.getElementById('edtNovelName');
	var edtMenuUrl = document.getElementById('edtNovelUrl');
	var edtContentUrl = document.getElementById('edtContentUrl');
	window.localStorage['name'] = edtName.value;
	window.localStorage['menuUrl'] = edtMenuUrl.value;
	window.localStorage['contentUrlPattern'] = edtContentUrl.value;

}

function readData(){
	var edtName = document.getElementById('edtNovelName');
	var edtMenuUrl = document.getElementById('edtNovelUrl');
	var edtContentUrl = document.getElementById('edtContentUrl');
	if(window.localStorage['name'])
	  	edtName.value =window.localStorage['name'];
	if(window.localStorage['menuUrl'])
	 	edtMenuUrl.value =  	window.localStorage['menuUrl'] ;
	if(window.localStorage['contentUrlPattern'])
	 	edtContentUrl.value = 	window.localStorage['contentUrlPattern'] ;
}

window.onload = function(){
	readData();
	var btnSave = document.getElementById('btnSave');
	btnSave.addEventListener('click', saveData);
};


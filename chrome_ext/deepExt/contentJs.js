
var frame = document.createElement('iframe');
frame.src = chrome.extension.getURL('popup.html');


var div = document.createElement('div');
div.style.width="100%";
div.style.height = "200px";
div.style.backgroundColor = "yellow";
div.innerHTML="hahaha<br/>abc";

var ele = document.getElementById('endText');
if(ele){
	ele.appendChild(div);
	ele.appendChild(frame);
	}
//window.document.body.style.backgroundColor="red";

alert('cs ok');


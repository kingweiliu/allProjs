


function btnClick(tab){
	try{
		if(chrome.extension.sendRequest)
			localStorage["chrome.extension.sendRequest"] = true;
		else
			localStorage["chrome.extension.sendRequest"] = false;
	}	
	catch(e){
		alert(e);
		localStorage["chrome.extension.sendRequest"] = false;
	}
	
	alert(localStorage["chrome.extension.sendRequest"]);
	
}

chrome.browserAction.onClicked.addListener(btnClick);

function getPlugin(){
	plugin = document.getElementById('pluginObj');
	return plugin;
}


chrome.browserAction.onClicked.addListener(function(){
	alert('click');
	var plugin =	document.getElementById('pluginObj');
	alert(plugin.id);
	plugin.showName('ljw');
	alert('click');
})


var pageNeedCrawl =[];

var PageCrawler ={
	cursor:0,
	bRunning : false,

	trigger:function(){
		if (bRunning) {
			return ;
		};
		bRunning = true;
		start();
		bRunning = false;
	},

	addPageNeeded: function(pageInfo){
		/*
			{
				url,
				title,
			}
		*/
		pageNeedCrawl.push(pageInfo);
		trigger();
	},

	start:function(){
		if (cursor >= pageNeedCrawl.length) {
			return;
		};
		//create tab to crawl
		chrome.tabs.create({
			url:pageNeedCrawl[cursor].url
		}, function(tab){
			pageNeedCrawl[cursor].tabId = tab.id;  //make the correspondence.
		});
	}

}



chrome.runtime.onMessage.addListener(
	function (request, sender, sendResponse) {	
		//if(request.title)	
		//	alert("abc"+sender.tab.id + request.title);
		/*

		if (request.isText) {
			console.log(JSON.stringify(request));
			chrome.tabs.create({
				'url':request.url
			}, function(tab){
				alert(tab.id);
			});
			bOpened=true;
		}
		else
		{
			console.log(JSON.stringify(request));

		}
		*/

		switch(request.cmd){
			case 'chapContent':
				console.log(JSON.stringify(request));
		}
		
		
	}
	);

alert('loaded ok');
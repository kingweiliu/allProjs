
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

	trigger: function(){
		if (this.bRunning) {
			return ;
		};
		this.bRunning = true;
		this.start();
		
	},

	start:function(){
		if (this.cursor >= pageNeedCrawl.length) {
			return;
		};
		//create tab to crawl
		chrome.tabs.create({
			url:pageNeedCrawl[this.cursor].url
		}, function(tab){
			pageNeedCrawl[this.cursor].tabId = tab.id;  //make the correspondence.
		});
	},

	OnContentCrawled: function(request, sender){  //正文抓取后触发，保存后关闭当前的页面，然后打开另外一个页面。
		if (request.url == pageNeedCrawl[this.cursor].url) 
		  	this.saveChapToDisk(request.url, request.title, request.content)
		

		var mycars = new Array();
		mycars[0] = sender.tab.id;
		chrome.tabs.remove(mycars);
		this.cursor++;
		this.bRunning = false;
		this.start();
	},

	OnNewChap: function(pageInfo){  //增加需要正文抓取的url
		/*
			{
				url,
				title,
			}
		*/
		pageNeedCrawl.push(pageInfo);
		this.trigger();
	},

	OnNewSection: function(volumeName){ //新卷

	},

	saveChapToDisk: function(url, title, content){ //保存正文
		var plugin =	document.getElementById('pluginObj');	
		plugin.showName(url, title, content);
		return true;
	},

	saveVolumeToDisk: function(){  //保存章节信息

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
			case 'chapContent':   //章节正文
				PageCrawler.OnContentCrawled(request, sender);
				//var plugin =	document.getElementById('pluginObj');	
				//plugin.showName(request.url, request.title, request.content);
				break;
			case 'newSection':  //新卷
				PageCrawler.OnNewSection(request.title);
				break;
			case 'newChap':     //章节标题，url， 等待打开。
				PageCrawler.OnNewChap({
					'url':request.url,
					'title':request.chapTitle
				});
				break;

				//console.log(JSON.stringify(request));
		}
		
		
	}
	);

alert('loaded ok');
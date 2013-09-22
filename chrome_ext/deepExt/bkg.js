chrome.browserAction.onClicked.addListener(function(){
	var plugin =	document.getElementById('pluginObj');
	alert(plugin.id);
	plugin.newSection(1, 'haha');
});

function Config(){
	if(window.localStorage['menuUrl']){
		var obj = new Object;
		obj.menuUrl = window.localStorage['menuUrl'];
		obj.contentUrlPattern = window.localStorage['contentUrlPattern'];
		obj.name = window.localStorage['name'];
		return obj;
	}
	return "";	
}

var pageNeedCrawl =[];
var PageCrawler ={
	cursor:0,
	volumeNo:0,  //卷号
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
			url:pageNeedCrawl[this.cursor].url,
			active:false
		}, function(tab){
			pageNeedCrawl[this.cursor].tabId = tab.id;  //make the correspondence.
		});
	},

	OnContentCrawled: function(request, sender){  //正文抓取后触发，保存后关闭当前的页面，然后打开另外一个页面。
		if (request.url == pageNeedCrawl[this.cursor].url) 
		  	this.saveChapToDisk(request.url, request.title, request.content, pageNeedCrawl[this.cursor].vid)
		

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
		pageInfo.vid = this.volumeNo;
		pageNeedCrawl.push(pageInfo);
		this.trigger();
	},

	OnNewSection: function(volumeName){ //新卷
		this.saveVolumeToDisk(volumeName);
	},

	saveChapToDisk: function(url, title, content, vid){ //保存正文
		var plugin =	document.getElementById('pluginObj');	
		plugin.newChap(url, title, content, vid);
		return true;
	},

	saveVolumeToDisk: function(volumeName){  //保存章节信息
		var plugin =	document.getElementById('pluginObj');			
		this.volumeNo ++;
		plugin.newSection(this.volumeNo, volumeName);
	}
}

function getPageClass(url){

	var config = Config();
	if (!config) return "none";
	if(config.menuUrl == url)
		return "menuPage";

	if (url.match(new RegExp(config.contentUrlPattern))) ;
		return "contentPage";
	
	return "none";

/*
	var urlReg = /http:\/\/read.qidian.com\/BookReader\/\d*,\d*\.aspx/ ; 
	if (url.match(urlReg)) {
		return "contentPage";
	};
	if (url == "http://read.qidian.com/BookReader/2019.aspx") {
		return "menuPage";
	};
*/
}

chrome.runtime.onMessage.addListener(
	function (request, sender, sendResponse) {	
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
			case 'pageClass': 	//url的页面分类
				sendResponse({
					"pageCategory": getPageClass(request.url)
				});
				break;
		}		
	}
);



alert('loaded ok');
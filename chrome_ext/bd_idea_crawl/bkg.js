chrome.browserAction.onClicked.addListener(function(){	
	var jax = new XMLHttpRequest();
    jax.open("POST","http://127.0.0.1:5000/abc");
    jax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    jax.send("text=abc");
    jax.onreadystatechange = function() { 
    	if(jax.readyState==4 && jax.status==200) 
		{ 
			alert(jax.responseText + 1); 
		}
    }
    alert(location);
    alert(JSON.stringify(getConfig()));
});

function getConfig(){
	if(window.localStorage['menuUrlPattern']){
		var obj = new Object;
		obj.menuUrlPattern = window.localStorage['menuUrlPattern'];
		obj.detailUrlPattern = window.localStorage['detailUrlPattern'];
		
		return obj;
	}
	return "";	
}

var pageNeedCrawl =[];
var PageCrawler ={
	cursor:0,
	url_dict : new Set(),
	concurrent : 5,

	trigger: function(){
		if (this.cursor >= this.concurrent) {
			return ;
		};
		this.cursor ++;
		this.start();
	},

	start:function(){
		if (0 >= pageNeedCrawl.length) {
			return;
		};

		//create tab to crawl
		current = pageNeedCrawl.pop();
		chrome.tabs.create({
			url:current.url,
			active:false
		}, function(tab){
			// pageNeedCrawl[this.cursor].tabId = tab.id;  //make the correspondence.
		});
	},

	OnContentCrawled: function(request, sender){  //正文抓取后触发，保存后关闭当前的页面，然后打开另外一个页面。
		if (request.url == pageNeedCrawl[this.cursor].url) 
		  	this.saveChapToDisk(request.url, request.title, request.content, pageNeedCrawl[this.cursor].vid)
		var mycars = new Array();
		mycars[0] = sender.tab.id;
		chrome.tabs.remove(mycars);

		this.start();
	},

	onItemDetailPage: function(pageInfo){  //增加需要正文抓取的url
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
		var plugin = document.getElementById('pluginObj');	
		plugin.newChap(url, title, content, vid);
		return true;
	},

	saveVolumeToDisk: function(volumeName){  //保存章节信息
		var plugin =  	document.getElementById('pluginObj');			
		this.volumeNo ++;
		plugin.newSection(this.volumeNo, volumeName);
	},

	addCrawlUrls: function(urls) {
		for (var i = urls.length - 1; i >= 0; i--) {
			if (urls[i].indexOf("logout") >= 0)
				continue;

			var pc = getPageClass(urls[i]);
			if (pc  != "none" && !this.url_dict.has(urls[i])) {
				var pageInfo = {}
				pageInfo.url = urls[i];
				pageNeedCrawl.push(pageInfo);
				this.url_dict.add(urls[i]);
				console.log("need to crawl:" +pc + ":" + urls[i]);
			}

		}
		this.trigger();
	},

	onFinish: function(request, sender){  //正文抓取后触发，保存后关闭当前的页面，然后打开另外一个页面。
		// if (request.url == pageNeedCrawl[this.cursor].url) 
		//   	this.saveChapToDisk(request.url, request.title, request.content, pageNeedCrawl[this.cursor].vid)
		if (request.url != "http://idea.baidu.com/#/home") {
			var mycars = new Array();
			mycars[0] = sender.tab.id;
			chrome.tabs.remove(mycars);	
		}		
		this.cursor--;
		this.trigger();
	},


}

function getPageClass(url){
	// console.log(url);
	if (url.match(new RegExp("\\.js\\?"))) {
		// console.log("js url:" + url);		
		return "none";
	}
	if (url.match(new RegExp("\\.jpg"))) {
		// console.log("js url:" + url);		
		return "none";
	}
	if (url.match(new RegExp("\\.doc"))) {
		// console.log("js url:" + url);		
		return "none";
	}
	if (url.match(new RegExp("\\.ppt"))) {
		// console.log("js url:" + url);		
		return "none";
	}
	if (url.indexOf("logout") >= 0)
		return "none";
	var config = getConfig();
	if (!config) return "none";


	if (url.match(new RegExp(config.detailUrlPattern))) 
		return "detailPage";

	if (url.match(new RegExp(config.menuUrlPattern))) {
		// console.log("url:" + url);
		// console.log("pattern:" + config.menuUrlPattern);
		return "menuPage";
	}
	return "none";
}

function savedataLocalhost(info)
{
    var jax = new XMLHttpRequest();
    jax.open("POST","http://127.0.0.1:5000/abc");
    jax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    console.log(info);
    jax.send("text="+info);
    jax.onreadystatechange = function() { 
    	if(jax.readyState==4) { 
    		console.log(jax.responseText);  
    	}
    }
}


chrome.runtime.onMessage.addListener(
	function (request, sender, sendResponse) {	
		//alert(JSON.stringify(request.data));
		// alert(request.data.detail);

		// savetext(JSON.stringify(request.data));
		// alert(request.sub_links);
		switch(request.cmd){
			case 'deatilItem':   //章节正文
				savedataLocalhost(JSON.stringify(request.data));

				//var plugin =	document.getElementById('pluginObj');	
				//plugin.showName(request.url, request.title, request.content);
				break;
			case 'pageClass': 	//url的页面分类
				PageCrawler.addCrawlUrls(request.sub_links);
				sendResponse({
					"pageCategory": getPageClass(request.url)
				});
				break;
			case 'finish':
				PageCrawler.onFinish(request, sender);
				break;
		}
	}
);

alert('loaded ok');
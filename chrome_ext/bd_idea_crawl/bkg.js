
function try_flask_server() {
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
}

var pageNeedCrawl =[];

function getConfig(){
	if(window.localStorage['menuUrlPattern']){
		var obj = new Object;
		obj.menuUrlPattern = window.localStorage['menuUrlPattern'];
		obj.detailUrlPattern = window.localStorage['detailUrlPattern'];
		obj.startUrl = window.localStorage['startUrl'];
		obj.visitedUrl = window.localStorage['visitedUrl'];
		return obj;
	}
	return "";	
}

visitedUrls = new Set();

var PageCrawler ={
	cursor:0,
	url_dict : new Set(),
	concurrent : 5,
    config : getConfig(),
    running : false,

	trigger: function(){
		if (this.cursor >= this.concurrent) {
			return ;
		};
		if (!this.running)
			return;
		this.cursor ++;
		this.start();
	},

	start:function(){
		if (0 >= pageNeedCrawl.length) {
			return;
		};

		//create tab to crawl
		current_url = pageNeedCrawl.pop();
		if (current_url.indexOf("attachment") > 0)
			this.cursor --;
		chrome.tabs.create({
			url:current_url,
			active:false
		}, function(tab){
			// pageNeedCrawl[this.cursor].tabId = tab.id;  //make the correspondence.
		});
	},

	addCrawlUrls: function(urls) {
		for (var i = urls.length - 1; i >= 0; i--) {
			if (urls[i].indexOf("logout") >= 0)
				continue;

			var pc = getPageClass(urls[i]);
			if (pc  != "none" && !this.url_dict.has(urls[i])) {
				pageNeedCrawl.push(urls[i]);
				this.url_dict.add(urls[i]);
				console.log("need to crawl:" +pc + ":" + urls[i]);
			}

		}
		this.trigger();
	},

	onFinish: function(request, sender){  //����ץȡ�󴥷��������رյ�ǰ��ҳ�棬Ȼ�������һ��ҳ�档
		// if (request.url == pageNeedCrawl[this.cursor].url) 
		//   	this.saveChapToDisk(request.url, request.title, request.content, pageNeedCrawl[this.cursor].vid)
		if (request.url != this.config.startUrl) {
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
			case 'deatilItem':   // ����ҳ
				data = request.data;
				data.url = request.url;
				savedataLocalhost(JSON.stringify(request.data));

				//var plugin =	document.getElementById('pluginObj');	
				//plugin.showName(request.url, request.title, request.content);
				break;
			case 'pageClass': 	//url��ҳ�����
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

chrome.browserAction.onClicked.addListener(function(){	
	var config = getConfig();
	
	PageCrawler.running = !PageCrawler.running;
	if (!PageCrawler.running) {
		// save visited item detail history
		vistiedItemUrlsStr = JSON.stringify(visitedUrls);
		window.localStorage.setItem('visitedUrls', vistiedItemUrlsStr);
	} else {
		// restore visited history
		vistiedItemUrlsStr = window.localStorage.getItem('visitedUrls');
		if (!vistiedItemUrlsStr != "null") 
			visitedUrls = new Set();
		else 
			visitedUrls = JSON.parse(vistiedItemUrlsStr);
	}
	var startUrls = new Array();
	startUrls.push(config.startUrl);
	PageCrawler.addCrawlUrls(startUrls);
	alert(PageCrawler.running);
});


alert('loaded ok');

// cmd: menu 新卷
// 		itemDetail  详情页
//      pageClass   请求页面分类命令
// 		finish 		当前页面处理完成

// var t1 = window.setTimeout(hello,2000); 
function hello(){
	reg=new RegExp("\\n|\\r","g");
	// get all links in this page
	url_list = document.body.innerHTML.match(/http:\/\/[^\s<>]*\b/g); 
	d = {
		//"comment":$("div.detail-comment").text().replace(reg,""),
		"cate":$("div.detail-product-type").text().replace(reg,""),
		"title" :$("div.detail-title").text(), 
		"summary":$("div.detail-summary").text().replace(reg,""),
		"detail-verify":$("div.detail-verify").text().replace(reg,""),
		"detail":$("div.detail-background-desc").text().replace(reg,"")
	};
	console.log(JSON.stringify(d));
	chrome.runtime.sendMessage({
		"url":window.location.href,
		"cmd":'pageClass',
		"data": d,
		"sub_links" : url_list
	}, function(res){
		switch(res.pageCategory){
			case 'menuPage':
				analyzeMenuPage();
			break;
			case 'contentPage':
				analyzeContent();
			break;
		}
	});
}



var tmp ={
	processChapterTitle: function(divTitle)	{
		chrome.runtime.sendMessage({
			'cmd':'newSection',
			'title': divTitle
		})
	}, 

	processChapterList: function(children){
		for(var i=0; i<children.length; ++i){
			var url = children[i].firstElementChild.href;
			var chapTitle = children[i].textContent;
			console.log(url + "---"+ chapTitle);
			chrome.runtime.sendMessage({
				'cmd':'newChap',
				'url':url,
				'chapTitle':chapTitle
			});
		}
	}
}

function analyzeMenuPage(){	
	var divContent = document.getElementById("content");
	if(divContent){
		for(var child in divContent.children){
			try{
			var divChild = divContent.children[child];
			if(divChild.className == "box_title"){
				tmp.processChapterTitle(divChild.children[0].children[0].textContent);
			}
			else if(divChild.className == "box_cont"){
				var clsList = divChild.getElementsByClassName('list');
				if (clsList.length >0) {
					tmp.processChapterList(clsList[0].children[0].children);
				};				
			}	

			}
			catch(e){
				// alert(e);
			}
		}
	}
}

//var urlReg = /http:\/\/read.qidian.com\/BookReader\/\d*,\d*\.aspx/ ; 
function analyzeContent(){
	var divContent = document.getElementById('content');
	if (!divContent) 
		return;	
	var eleTitle = document.getElementById('lbChapterName');
	if (!eleTitle) 
		return;	
	chrome.runtime.sendMessage({
				'cmd':'chapContent',
				'url':window.location.href,
				'content':divContent.innerText,
				'title':eleTitle.innerText				
	});
}


// 解析页面的数据
function analyzeIdeaContent() {
	url_list = document.body.innerHTML.match(/http:\/\/[^\s<>]*\b/g); 
	reg=new RegExp("\\n|\\r","g");
	d = {
		//"comment":$("div.detail-comment").text().replace(reg,""),
		"cate":$("div.detail-product-type").text().replace(reg,""),
		"title" :$("div.detail-title").text(), 
		"summary":$("div.detail-summary").text().replace(reg,""),
		"detail-verify":$("div.detail-verify").text().replace(reg,""),
		"detail":$("div.detail-background-desc").text().replace(reg,"")
	};
	// console.log(JSON.stringify(d));
	chrome.runtime.sendMessage({
		"url":window.location.href,
		"cmd":'deatilItem',
		"data": d
	});
	// alert(d);
	finish();
}

function finish() {
	chrome.runtime.sendMessage({
		"url":window.location.href,
		"cmd":'finish'
	});
}

// 请求页面的类型，依次进行下一步的动作
function init(){	
	// get all links in this page
	url_list = document.body.innerHTML.match(/http:\/\/[^\s<>]*\b/g); 
	url_list = $("a");
	var b = [];
	for (var i = url_list.length - 1; i >= 0; i--) {

		console.log(url_list[i]);
		if (url_list[i].href.length > 15){
			b.push(url_list[i].href);
		}
	}

	chrome.runtime.sendMessage({
		"url":window.location.href,
		"cmd":'pageClass',
		"sub_links" : b
	}, function(res){
		switch(res.pageCategory){
			case 'detailPage':
				analyzeIdeaContent();
				break;
			default:
				finish();
				break;
		}
	});
}


var t1 = window.setTimeout(init,3000); 
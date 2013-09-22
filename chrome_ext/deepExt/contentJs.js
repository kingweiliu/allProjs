
// cmd: newSection 新卷
// 		newChap    章节
//		chapContent  章节正文


chrome.runtime.sendMessage({
	"url":window.location.href,
	"cmd":'pageClass'
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
				alert(e);
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


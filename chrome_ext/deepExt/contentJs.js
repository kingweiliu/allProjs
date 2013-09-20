
// cmd: newSection 新卷
// 		newChap    章节
//		chapContent  章节正文

// //if(false){
if(window.location.href == "http://read.qidian.com/BookReader/2885992.aspx"){

	function processChapterTitle(divTitle){
		chrome.runtime.sendMessage({
			'cmd':'newSection',
			'title':divTitle.children[0].children[0].textContent
		});
	}

	function processChapterList(children){
		for(var i=0; i<children.length; ++i){
			console.log(i);
			console.log(children[i].outerHTML);

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
	
	var divContent = document.getElementById("content");
	if(divContent){
		for(var child in divContent.children){
			var divChild = divContent.children[child];
			if(divChild.className == "box_title"){
				processChapterTitle(divChild);
			}
			else if(divChild.className == "box_cont"){
				var clsList = divChild.getElementsByClassName('list');
				if (clsList.length >0) {
					processChapterList(clsList[0].children[0].children);
				};
				
			}			
		}
	}
}

var urlReg = /http:\/\/read.qidian.com\/BookReader\/\d*,\d*\.aspx/ ; 
if (window.location.href.match(urlReg)) {
	var divContent = document.getElementById('content');
	if (!divContent) {
		return;
	};

	var eleTitle = document.getElementById('lbChapterName');
	if (!eleTitle) {
		return;
	};


	chrome.runtime.sendMessage({
				'cmd':'chapContent',
				'url':window.location.href,
				'content':divContent.innerText,
				'title':eleTitle.innerText,
				'isText':true
			});

};



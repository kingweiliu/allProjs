//	var objAlls = new Array;
	var KEY = "lst4";
	
	function addToList(txt){
		var lst = document.getElementById('lst');
		var ele = document.createElement('li');
		ele.innerHTML = txt;
		lst.appendChild(ele);		
	}
	
	function btnClick(){		
		
		var txt = document.getElementById('txt');
		addToList(txt.value);		
		objAlls.push(txt.value);	
		localStorage.setItem(KEY, objAlls);
	}

	(function(){
		var btn = document.getElementById('btnAdd');
		btn.addEventListener('click', btnClick);
		oldValue = localStorage.getItem(KEY);
		
		
		if(oldValue){			
			
			
			objAlls = oldValue.split(',');
			
			
			for(var i =0; i<objAlls.length; ++i){
				
				addToList(objAlls[i]);
			}
			
		}
		
	})();
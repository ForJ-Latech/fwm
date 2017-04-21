var keynum = 0;

function searchvalue(text){
	keynum++;
	var sdf = keynum;
	$.ajax({
		        contentType: "text/json",
		        url: "http://localhost:51186/api/Entry/AddData",
		        data: d,
		        method: 'POST',
		        success: function(data){
					changeList(data, sdf);
				}
	});
}

var changing = false;

function changeList(data, key_number){
	if(changing){
		if(key_number = keynum){
			setTimeout(function (){changeList(data, key_number);}, 50);
		}else
		{
			return;
		}
	}
	try{
		changing = true;
		var lcont = $('#listcontainer');
		$.each(data, function(d, dval){
			lcont.appendChild("<div> we found something </div>");
		});
	}
	catch(err){
		alert(err);
	}
	finally{
		changing = false;
	}
}

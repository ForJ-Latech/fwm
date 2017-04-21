var keynum = 0;

var colors = [
'#F7B9B9',
'#BFBFBF'
];

function searchvalue(text){
	keynum++;
	var sdf = keynum;
	$.ajax({
		        url: "searchAll/" + text,
		        method: 'GET',
		        success: function(data){
					changeList(data, sdf);
				}
	});
}

var changing = false;

var that;

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
		// clear out the things. 
		$.each(lcont.children(), function(d, dval){
			dval.remove();
		});
		
		$.each(data, function(d, dval){
			that = dval;
			lcont.append('<div style="background-color:' + colors[d%2] + ' ;" class="row" onclick="open' + dval.class + '(' + dval.id + ');">&nbsp;' + dval.name + "&nbsp;</div>");
		});
	}
	catch(err){
		alert(err);
	}
	finally{
		changing = false;
	}
}

// it will call open + class name, needs to change this. 
function openNpc(id){
	$.ajax({
		        url: "getNpc/" + id,
		        method: 'GET',
		        success: function(data){
					$('#name').html(data.name);
					$('#description').html(data.description);
					$('#cname').html(data.class);
					if(data.imageFileName != undefined){
						document.getElementById('image').src = "/webservice1_0bs/multimediaImage/" + data.imageFileName;
					}
				}
	});
}

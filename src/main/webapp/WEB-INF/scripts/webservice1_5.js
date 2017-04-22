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

var keynum = 0;

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
		that = data;
		changing = true;
		var lcont = $('#listcontainer');
		// clear out the things. 
		$.each(lcont.children(), function(d, dval){
			dval.remove();
		});
		
		$.each(data, function(d, dval){
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

// it will call open + class name, we need to make more of these. 
function openNpc(id){
	$.ajax({
		        url: "getNpc/" + id,
		        method: 'GET',
		        success: function(data){
					$('#name').html(data.name);
					$('#description').html(data.description);
					$('#cname').html(data.class);
					// clear out the container every time. 
					$.each($('#imagecontainer').children(), function(d, dval){
						dval.remove();
					});
					if(data.imageFileName != null){
						var imgCont = $('#imagecontainer');
						
						$('#imagecontainer').append('<img id="image" src="/webservice1_0bs/multimediaImage/' + data.imageFileName + '"/>');
						
							calculateWidthHeightImg($('#imagecontainer'), $('#image'));
						
					}
				}
	});
}

function calculateWidthHeightImg(container, image){
	var width = container.width();
	var height = container.height();
	that = image;
	
	var imgWidth = image.width();
	var imgHeight = image.height();
	
	var rel = width/height;
	var imgRel = imgWidth/imgHeight;
	alert(rel);
	alert(imgRel);
	alert('post rel');
	if(imgRel > rel){
		alert(width);
		// set to max width.
		image.attr('width', width);
		image.attr('height', width / imgWidth * imgHeight);
		alert(width / imgWidth * imgHeight);
	}
	else{
		alert(height);
		// set to max height. 
		image.attr('height', height);
		image.attr('width', height / imgHeight * imgWidth);
		alert(height / imgHeight * imgWidth);
	}
}

// premade debug variable name, use this. 
var that;


// this is the only place that the $(document).ready should exist on webservice1_5.js
$(document).ready(function(){
	// search the thing if enter is pressed
	$("#myInput").on('keyup', function (e) {
		if (e.keyCode == 13) {
			searchvalue(document.getElementById('myInput').value);
		}
	});
	
	fixImageContainerWidthHeight();
});

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

var keynum = 0;

var colors = [
'#F7B9B9',
'#BFBFBF'
];

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
		fillDiv(lcont, data);
	}
	catch(err){
		alert("ERROR: " + err);
	}
	finally{
		changing = false;
	}
}

function fillDiv(lcont, data){
	$.each(lcont.children(), function(d, dval){
		dval.remove();
	});
	$.each(data, function(d, dval){
		lcont.append('<div style="background-color:' + colors[d%2] + ' ;" class="row listItem" onclick="open' + 
		dval.class + '(' + dval.id + ');"><img src="/webservice1_0bs/multimediaImage/' + dval.imageFileName + '" class="listImage"/>&nbsp;' + dval.name + "&nbsp;</div>");
	});
}

// it will call open + class name, we need to make more of these. 
function openNpc(id){
	$.ajax({
		        url: "getNpc/" + id,
		        method: 'GET',
		        success: function(data){
					that = data;
					$('#name').html(data.name);
					$('#description').html(data.description);
					$('#cname').html(data.class);
					// clear out the container every time. 
					$.each($('#imagecontainer').children(), function(d, dval){
						dval.remove();
					});
					if(data.imageFileName != null){
						var imgCont = $('#imagecontainer');
						// need to use .attr here because computed width will be zero if no elements inside div. 
						var width=imgCont.attr('width');
						var height = imgCont.attr('height');
						$('#imagecontainer').append('<img id="image" src="/webservice1_0bs/multimediaImage/' + data.imageFileName + '"/>');
						calculateWidthHeightImg(width, height, $('#image'));
					}
					var relListContainer = $('#rellistcontainer');
					relListContainer.html('Events<div id="events" class="row"> </div>Regions<div id="regions" class="row"> </div>');				
					// give the dom a second to register things. 
					setTimeout(function (){
						fillDiv($('#regions'), data.regions);
						fillDiv($('#events'), data.events);
					}, 50);
				}
	});
}

// do images and stuff. 
function calculateWidthHeightImg(width, height, image){
	var imgWidth = image.width();
	var imgHeight = image.height();
	var rel = width/height;
	var imgRel = imgWidth/imgHeight;
	if(imgRel > rel){
		// set to max width.
		image.attr('width', width);
		image.attr('height', width / imgWidth * imgHeight);
	}
	else{
		// set to max height. 
		image.attr('height', height);
		image.attr('width', height / imgHeight * imgWidth);
	}
}

window.addEventListener('resize', function(event){
  fixImageContainerWidthHeight(event);
});
	
function fixImageContainerWidthHeight(event){
	that = event;
	if(event == undefined)
	{
		var width = $(document).width() / 3;
		var height = ($(document).height() - $('.navbar').height()) * 3 / 5;
		$('#imagecontainer').attr('width', width); 
		$('#imagecontainer').attr('height', height);
		$('#imagecontainer').attr('max-width', width); 
		$('#imagecontainer').attr('max-height', height);
	} 
	else
	{
		var width = $(document).width() / 3;
		var height = ($(document).height() - $('.navbar').height()) * 3 / 5;
		$('#imagecontainer').attr('width', width); 
		$('#imagecontainer').attr('height', height);
		$('#imagecontainer').attr('max-width', width); 
		$('#imagecontainer').attr('max-height', height);
		if($('#image')){
			calculateWidthHeightImg(width, height, $('#image'));
		}
	}
}

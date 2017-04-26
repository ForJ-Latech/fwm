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
'#BFBFBF',
'#6699CC',
'#43ABC9',
'#E0E0E0'
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
		lcont.append('<div class="row listItem ' + dval.class + '" onclick="open' + 
		dval.class + '(' + dval.id + ');"><img src="/webservice1_0bs/multimediaImage/' + dval.imageFileName + '" class="listImage"/>&nbsp;' + dval.name + "&nbsp;</div>");
		
	});
}
 
// arguably spaghetti, but prevents us from needing to copy and paste changes multiple places. 
function makeCell(container, dval){
	if(dval != null) {
		container.append('<div class="row listItem ' + dval.class + '" onclick="open' + 
		dval.class + '(' + dval.id + ');"><img src="/webservice1_0bs/multimediaImage/' + dval.imageFileName + '" class="listImage"/>&nbsp;' + dval.name + "&nbsp;</div>");
		
	}
}

function applyBasicAttributes(data){
	that = data;
	$('#name').html(data.name);
	$('#description').html(data.description);
	$('#cname').html(data.class);
	// clear out the container every time. 
	$.each($('#imagecontainer').children(), function(d, dval){
		dval.remove();
	});	
	var imgCont = $('#imagecontainer');
	// need to use .attr here because computed width will be zero if no elements inside div. 
	var width=imgCont.attr('width');
	var height = imgCont.attr('height');
	$('#imagecontainer').append('<img id="image" src="/webservice1_0bs/multimediaImage/' + data.imageFileName + '"/>');
	calculateWidthHeightImg(width, height, $('#image'));
	fixImageContainerWidthHeight();
}

// it will call open + class name, we need to make more of these. 
function openNpc(id){
	$.ajax({
		        url: "getNpc/" + id,
		        method: 'GET',
		        success: function(data){
					applyBasicAttributes(data);
					var relListContainer = $('#rellistcontainer');
					relListContainer.html('God<div id="god" class="row"></div>'
					+ 'Family<div id="family" class="row">'
					+ '</div>Groups<div id="events" class="row">'
					+ '</div>Regions<div id="regions" class="row"></div>');	
					// give the dom a second to register things. 
					setTimeout(function (){			
						makeCell($('#god'), data.god);
						setTimeout(function(){
							fillDiv($('#family'), data.family);
							setTimeout(function(){
								fillDiv($('#regions'), data.regions);
								setTimeout(function(){
									fillDiv($('#events'), data.events);
								}, 100);
							}, 100);
						}, 100);
					}, 100);
				}
	});
}

function openEvent(id){
	$.ajax({
		        url: "getEvent/" + id,
		        method: 'GET',
		        success: function(data){
					applyBasicAttributes(data);
					// override that shit yo. 
					$('#cname').html('Group');
					var relListContainer = $('#rellistcontainer');
					relListContainer.html('Region<div id="region" class="row"></div>'
					+'Npcs<div id="npcs" class="row"></div>'
					+'Gods<div id="gods" class="row"> </div>');	
					// give the dom a second to register things. 
					// also load sequentially, so that the app looks funky. 
					setTimeout(function (){			
						makeCell($('#region'), data.region);
						setTimeout(function(){
							fillDiv($('#npcs'), data.npcs);
							setTimeout(function(){
								fillDiv($('#gods'), data.gods);
							}, 100);
						}, 100);
					}, 100);
				}
	});
}

function openRegion(id){
	$.ajax({
		        url: "getRegion/" + id,
		        method: 'GET',
		        success: function(data){
					applyBasicAttributes(data);
					var relListContainer = $('#rellistcontainer');
					relListContainer.html('Super&nbsp;Region<div id="superregion" class="row"/>'
					+ 'Sub&nbsp;Regions<div id="subregions" class="row"></div>'
					+ 'Npcs<div id="npcs" class="row"></div>'
					+ 'Gods<div id="gods" class="row"> </div>');	
					// give the dom a second to register things. 
					// also load sequentially, so that the app looks funky. 
					setTimeout(function (){			
						makeCell($('#superregion'), data.superRegion);
						setTimeout(function(){
							fillDiv($('#subregions'), data.regions);
							setTimeout(function(){
								fillDiv($('#npcs'), data.npcs);
								setTimeout(function(){
									fillDiv($('#gods'), data.gods);
								}, 100);
							}, 100);
						}, 100);
					}, 100);
				}
	});
}

function openGod(id){
	$.ajax({
		        url: "getGod/" + id,
		        method: 'GET',
		        success: function(data){
					applyBasicAttributes(data);
					var relListContainer = $('#rellistcontainer');
					relListContainer.html('Pantheon<div id="pantheon" class="row"> </div>'
					+ 'Groups<div id="groups" class="row"></div>'
					+ 'Regions<div id="regions" class="row"> </div>'
					+ 'Npcs<div id="npcs" class="row"></div>');	
					// give the dom a second to register things. 
					// also load sequentially, so that the app looks funky. 
					setTimeout(function (){		
						fillDiv($('#pantheon'), data.pantheon);	
						setTimeout(function(){
							fillDiv($('#groups'), data.events);
							setTimeout(function(){
								fillDiv($('#regions'), data.regions);
								setTimeout(function(){
									fillDiv($('#npcs'), data.npcs);
								}, 100); 
							}, 100);
						}, 100);
					}, 100);
				}
	});
}

window.addEventListener('resize', function(event){
  fixImageContainerWidthHeight(event);
});
	
function fixImageContainerWidthHeight(event){
	that = event;
	console.debug(event);
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

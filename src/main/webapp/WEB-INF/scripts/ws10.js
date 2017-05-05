// this is our currently shown index.
var currentIndex = 0;

// these are our default class, and index.
var objClass = 0;
var objIndex;

/* Page initialization */
$(document).ready(function() {
	// actualFixImage();
	$.ajaxSetup({ cache: true });
	showDefault();
	setInterval(function() {
		$.ajax({
			url: "/webservice1_0bs/getCurrent/",
			type: 'GET',
			contentType : "application/json",
			dataType: 'json',
			success: function(data) {
				// method checks to see if anything new has been pushed, and sets us to that if it has. 
				checkDefault(data);
			},
			fail: function(data) {
				console.log("error");
			}
		});
	}, 2000);
});

function showDefault(){
	$.ajax({
		url: "/webservice1_0bs/getCurrent",
		method: 'GET',
		success: function(data){
			objIndex = data['ObjectId'];
			objClass = data['ObjectClass'];
			currentIndex = data['CurrentIndex'];
			showElement(data);
			// actualFixImage();
		}
	});
}

function getPrev(){
	$.ajax({
		url: "/webservice1_0bs/getPrev/" + currentIndex,
		method: 'GET',
		success: function(data){
			currentIndex = data['CurrentIndex'];
			showElement(data);
			// actualFixImage();
		}
	});
}

function getNext(){
	$.ajax({
		url: "/webservice1_0bs/getNext/" + currentIndex,
		method: 'GET',
		success: function(data){
			currentIndex = data['CurrentIndex'];
			showElement(data);
			// actualFixImage();
		}
	});
}

/* Helper methods */
function checkDefault(obj){
	if(objIndex != obj['ObjectId'] || objClass != obj['ObjectClass']){
		showDefault();
	}
}
	
function showElement(obj){
	currentIndex = obj['CurrentIndex'];
	$('#desc').html(obj['Description']);
	$('#name').html(obj['Name']);
	$.each($('#imagecontainer').children(), function(d, dval){
		dval.remove();
	});	
	$('#imagecontainer').append('<img id="img" style="display:none;" src="/webservice1_0bs/multimediaImage/' + obj['ImageFileName'] + '"/>');
	$('#img').bind('load', function(){
		actualFixImage();
	});
}

/* Image shit */


function actualFixImage(){
	var height = ($(window).height() - $('.navbar').height()) * 6.5 / 10;
	$('#imagecontainer').attr('height', height);
	$('#imagecontainer').attr('max-height', height);
	var imgContainer = $('#imagecontainer');
	setTimeout(function(){
		calculateWidthHeightImg(imgContainer.width(), height, $('#img'));
		$('#img').css('display', '');
	}, 100);
}

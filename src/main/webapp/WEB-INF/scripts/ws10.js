var currentIndex = 0;
var objClass = 0;
var objIndex;
alert('we got called');
var asdf = "/webservice1_0bs/getPrev/";
function start(){
	$.ajaxSetup({ cache: true });
	alert('in $.ready');
	$.ajax({
		url: "getCurrent",
		method: 'GET',
		success: function(data){
			showElement(obj);
			actualFixImage();
		}
	});
}
	
function showElement(obj){
	objIndex = obj['ObjectId'];
	objClass = obj['ObjectClass'];
	currentIndex = obj['CurrentIndex'];
	$('#desc').html(obj['Description']);
	$('#name').html(obj['Name']);
	document.getElementById('img').src = "/webservice1_0bs/multimediaImage/" + obj['ImageFileName'];
}

function fixImageContainerWidthHeight(){
	var height = ($(window).height() - $('.navbar').height()) * 6.5 / 10;
	$('#imagecontainer').attr('height', height);
	$('#imagecontainer').attr('max-height', height);
}

function actualFixImage(){
	fixImageContainerWidthHeight();
	var imgContainer = $('#imagecontainer');
	calculateWidthHeightImg(imgContainer.width(), imgContainer.attr('height'), $('#img'));
}

$(document).ready(function() {
	actualFixImage();
	/*
	var soundFile = "${SoundFileName}";
	var audio = $("#player");
	if (soundFile.length > 0) {
		//if there is sound attached to object
		var soundURL = "/webservice1_0/multimediaSound/" + "${SoundFileName}";
		var a = new Audio(soundURL);
		a.currentTime = 0;
		a.play();
	} else {
		$("#sound").hide();
	}
	*/
});

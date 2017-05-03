
$(document).ready(function(){
	cacheBreak();
	setInterval(function(){
		cacheBreak();
	}, 5000);
});

function cacheBreak(){
	$.each(document.getElementsByClassName('cachebreak'), function(d, dval){
		var x = new Date();
		
		var apstr = "" + x.getMonth() + x.getHours() + x.getMinutes() + x.getSeconds();
		dval.href = dval.href.substring(0, dval.href.lastIndexOf('/') + 1) + apstr;
	});
}

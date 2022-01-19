$(document).ready(function(){
	$("#selectAll").click(function(){
	var checkbox = $('table tbody input[type="checkbox"]');
		if(this.checked){
			checkbox.each(function(){
				this.checked = true;
			});
		} else{
			checkbox.each(function(){
				this.checked = false;
			});
		}
	});
});
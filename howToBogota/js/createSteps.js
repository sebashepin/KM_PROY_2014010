$(document).ready(function(){
 
    var counter = 2;
 
    $("#crearPaso").click(function () {
 
	if(counter>10){
            alert("Only 10 steps are allowed");
            return false;
	}   
 
	var divStep = $(document.createElement('div'))
	     .attr("id", 'divStep' + counter);
 
	divStep.after().html('<label><span class="textoPAso">STEP</span><span class="numeroPaso">'+ counter + ' </span></label>' +
	      '<input type="text" name="step' + counter + 
	      '" id="step' + counter + '" value="" >');
 
	divStep.appendTo("#pasosPregunta");
 
 
	counter++;
     });
 
     $("#borrarPaso").click(function () {
	if(counter==2){
          alert("We need at least 1 step");
          return false;
       }   
 
	counter--;
 
        $("#divStep" + counter).remove();
 
     });
 
    
  });
$(document).ready(function() {
	
    
    	//$.getJSON('http://25.141.65.84:8080/JAXRS-RESTEasy/rest/RESTEasyHelloWorld/answer', function(respuesta) {
          $.getJSON('js/answer3.json', function(respuesta) {
			  
			 /*  var crearTextPaso = function(){
			  $('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
             		$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
			  
		  }*/
			  
			  $('#respuesta .contenedor #contenidoPrincipal').html('<div id="lapregunta"><p class="morados">You wrote</p><p id="howto">How To....</p><p id="questionASked">'+ respuesta.question + '</p></div>');
			  if(respuesta.status == "OK"){
				$('#respuesta .contenedor #contenidoPrincipal').append('<div id="lasrespuestas"><div class="swiper-container"><div class="swiper-wrapper"><div class="swiper-slide">');
				  	$('.swiper-slide').append('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
             		$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
					
					if(respuesta.step.steps_neighborhood.charAt(0) == "1" ){ 
						$('.swiper-slide').append('<a href"#" class="arriba"></a>');
						
					}
					if(respuesta.step.steps_neighborhood.charAt(1) == "1" ){ 
						$('.swiper-slide').append('<a href"#" class="derecha"></a>');
									
					}
					if(respuesta.step.steps_neighborhood.charAt(2) == "1" ){ 
						$('.swiper-slide').append('<a href"#" class="abajo"></a>');
												
					}
					if(respuesta.step.steps_neighborhood.charAt(3) == "1" ){ 
						$('.swiper-slide').append('<a href"#" class="izquierda"></a>');
												
					}
					llamarSiguientePaso();
					
				$('#respuesta .contenedor #contenidoPrincipal').append('</div></div></div></div');
            
			  }
			  else if(respuesta.status == "NEW"){
				  $('#respuesta .contenedor #contenidoPrincipal').append('<div id="lasrespuestas"><p>There are not answers for this Question.</p>'+'<p>Would you like to answer it?</p>'+'<div id="botonera"><a href="#responderPregunta" class="botonPreguntaGrande alLado">YES</a> <a href="#preguntar" class="botonPreguntaGrande alLado">NO</a></div></div> ');
			  }
			  else if(respuesta.status == "INVALID"){
				  $('#respuesta .contenedor #contenidoPrincipal').append('<div id="lasrespuestas"><p>Your question is invalid. Please go back and ask again</p>'+'<a href="#preguntar" class="botonPreguntaGrande alLado">GO BACK</a></div>');				
			  }
          });
		  
	

//primerPaso();
		  
var llamarSiguientePaso= function(){
	var botones = ['.arriba','.derecha','.abajo','.izquierda'];
	var direccion = [
			{bottom:'-300px',opacity:'0'},
			{left:'-300px',opacity:'0'},
			{top:'-300px',opacity:'0'},
			{right:'-300px',opacity:'0'}];
	
	for(var i=0; i<=3;i++){
		
		console.log(botones[i]);
		
	$(botones[i]).click(function(){
		
							$.getJSON('js/nextStep.json', function(respuesta) {
								console.log(i);
								
								$('.swiper-slide').animate(direccion[i-1],500);
					
								
								 $('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
             		$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
				
					
								
								//$('.swiper-slide').animate({left:'300px'},0);
								//$('.swiper-slide').animate({left:'0px',opacity:'1'},500);
								
					
							});
							
							
						});
	}
	
}
		  
		 
   });
$(document).ready(function() {
    
          $.getJSON('http://192.168.0.19:8080/JAXRS-RESTEasy/rest/RESTEasyHelloWorld/answer', function(respuesta) {
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
					
				$('#respuesta .contenedor #contenidoPrincipal').append('</div></div></div></div');
            
			  }
			  else if(respuesta.status == "NEW"){
				  $('#respuesta .contenedor #contenidoPrincipal').append('<div id="lasrespuestas"><p>There are not answers for this Question.</p>'+'<p>Would you like to answer it?</p>'+'<div id="botonera"><a href="#responderPregunta" class="botonPreguntaGrande alLado">YES</a> <a href="#preguntar" class="botonPreguntaGrande alLado">NO</a></div></div> ');
			  }
			  else if(respuesta.status == "INVALID"){
				  $('#respuesta .contenedor #contenidoPrincipal').append('<div id="lasrespuestas"><p>Your question is invalid. Please go back and ask again</p>'+'<a href="#preguntar" class="botonPreguntaGrande alLado">GO BACK</a></div>');				
			  }
          });
      
   });
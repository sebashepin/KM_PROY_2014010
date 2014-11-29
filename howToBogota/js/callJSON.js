$(document).ready(function() { 
			//$.getJSON('js/answer3.json', function(respuesta) {
    		$.getJSON('rest/answer', function(respuesta) {
             	$('#respuesta .contenedor #contenidoPrincipal').html('<div id="lapregunta"><p class="morados">You wrote</p><p id="howto">How To....</p><p id="questionASked">'+ respuesta.question + '</p></div>');
             	if(respuesta.status == "OK"){
             		$('#respuesta .contenedor #contenidoPrincipal').append('<div id="lasrespuestas"><div class="swiper-container"><div class="swiper-wrapper"><div class="swiper-slide">');
             		$('.swiper-slide').append('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
             		$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
             		crearBotones(1);
             		$('#respuesta .contenedor #contenidoPrincipal').append('</div></div></div></div');

             	}
             	else if(respuesta.status == "NEW"){
             		$('#respuesta .contenedor #contenidoPrincipal').append('<div id="lasrespuestas"><p>There are not answers for this Question.</p>'+'<p>Would you like to answer it?</p>'+'<div id="botonera"><a href="#responderPregunta" class="botonPreguntaGrande alLado">YES</a> <a href="#preguntar" class="botonPreguntaGrande alLado">NO</a></div></div> ');
             	}
             	else if(respuesta.status == "INVALID"){
             		$('#respuesta .contenedor #contenidoPrincipal').append('<div id="lasrespuestas"><p>Your question is invalid. Please go back and ask again</p>'+'<a href="#preguntar" class="botonPreguntaGrande alLado">GO BACK</a></div>');				
             	}


             });


var crearBotones = function(setBotones){
	if(setBotones==1){
		//$.getJSON('js/answer3.json', function(respuesta) {	
		$.getJSON('rest/answer', function(respuesta) {		
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
			if(respuesta.step.steps_neighborhood.charAt(1) == "0" ){ 
				$('.swiper-slide').append('<div id="ratingPregunta"></div>');
				crearRating();

			}
			llamarSiguientePaso(1);

		});
	}
	if(setBotones==2){
		//$.getJSON('rest/GetStep?step_id=2&step_direction=0100', function(respuesta) {
		//$.getJSON('js/nextStep.json', function(respuesta) {		
		$.getJSON('rest/answer', function(respuesta) {		
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
			if(respuesta.step.steps_neighborhood.charAt(1) == "0" ){ 
				$('.swiper-slide').append('<div id="ratingPregunta"></div>');
				crearRating();

			}
			llamarSiguientePaso(2);

		});
		
	}

}
//primerPaso();

var llamarSiguientePaso= function(pasoActual){
	var botones = ['.arriba','.derecha','.abajo','.izquierda'];
	var direccion = [
	{bottom:'-300px',opacity:'0'},
	{left:'-300px',opacity:'0'},
	{top:'-300px',opacity:'0'},
	{right:'-300px',opacity:'0'}];

	var velocidad = 500;
	if(pasoActual==1){
		//$.getJSON('js/nextStep.json', function(respuesta) {
		//$.getJSON('rest/GetStep?step_id=2&step_direction=0100', function(respuesta) {
		$.getJSON('rest/answer', function(respuesta) {
			$('.arriba').click(function(){		


				$('.swiper-slide').animate({bottom:'-300px',opacity:'0'},(velocidad-200),function() { $('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
					$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
					$('.swiper-slide').animate({bottom:'100px'},0);
					$('.swiper-slide').animate({bottom:'0px',opacity:'1'},velocidad);
					crearBotones(2);    }   );
				

			});

			$('.derecha').click(function(){		


				$('.swiper-slide').animate({left:'-300px',opacity:'0'},(velocidad-200),function() {
					$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
					$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');					
					$('.swiper-slide').animate({left:'100px'},0);
					$('.swiper-slide').animate({left:'0px',opacity:'1'},velocidad);
					crearBotones(2);
				});


			});
			$('.abajo').click(function(){		


				$('.swiper-slide').animate({bottom:'300px',opacity:'0'},(velocidad-200),function() {
					$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
					$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
					$('.swiper-slide').animate({bottom:'-100px'},0);
					$('.swiper-slide').animate({bottom:'0px',opacity:'1'},velocidad);
					crearBotones(2);


				});
				

			});
			$('.izquierda').click(function(){		

				$('.swiper-slide').animate({left:'600px',opacity:'0'},(velocidad-200),function() {
					$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
					$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
					$('.swiper-slide').animate({left:'-100px'},0);
					$('.swiper-slide').animate({left:'0px',opacity:'1'},velocidad);
					crearBotones(2);

				});
				

			});

		});
}

if(pasoActual==2){
	//$.getJSON('js/answer3.json', function(respuesta) {
	$.getJSON('rest/answer', function(respuesta) {
		$('.arriba').click(function(){		


			$('.swiper-slide').animate({bottom:'-300px',opacity:'0'},(velocidad-200),function() {
				$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
			$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
			$('.swiper-slide').animate({bottom:'100px'},0);
			$('.swiper-slide').animate({bottom:'0px',opacity:'1'},velocidad);
			crearBotones(1);

			});
			

		});

		$('.derecha').click(function(){		


			$('.swiper-slide').animate({left:'-300px',opacity:'0'},(velocidad-200),function() {
				$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
			$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
			$('.swiper-slide').animate({left:'100px'},0);
			$('.swiper-slide').animate({left:'0px',opacity:'1'},velocidad);
			crearBotones(1);

			});
			

		});
		$('.abajo').click(function(){		


			$('.swiper-slide').animate({bottom:'300px',opacity:'0'},(velocidad-200),function() {
$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
			$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
			$('.swiper-slide').animate({bottom:'-100px'},0);
			$('.swiper-slide').animate({bottom:'0px',opacity:'1'},velocidad);
			crearBotones(1);

			});
			

		});
		$('.izquierda').click(function(){		

			$('.swiper-slide').animate({left:'600px',opacity:'0'},(velocidad-200),function() {
$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
			$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
			$('.swiper-slide').animate({left:'-100px'},0);
			$('.swiper-slide').animate({left:'0px',opacity:'1'},velocidad);
			crearBotones(1);
				
			});
			

		});

	});
}

}


});

var crearRating= function(){
	$('.swiper-slide #ratingPregunta').append('<input type="hidden" id="rating" name="rating"><div id="califique">califique la respuesta</div><div id="rateit"></div>');
	$(function () { $('#rateit').rateit({ max: 5, step: 1, backingfld: '#rating' }); });
}

$( document ).delegate("#respuesta", "pageinit", function() {
	crearRating();
});

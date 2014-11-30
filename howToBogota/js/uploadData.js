$(document).ready(function(){
    $('#preguntar #askQuestionForm').submit(function(){  
	
		$('#preguntar .printResults').html("<b>Enviando...</b>");
		//$.post('postReceiver.php', $(this).serialize(), function(data){
       $.post('rest/GetAnswer', $(this).serialize(), function(respuesta){
	   console.log(respuesta);
		$('.contenedor').html('<div id="respuesta"><div id="lapregunta"><p class="morados">You wrote</p><p id="howto">How To....</p><p id="questionASked">'+ respuesta.question + '</p></div>');
		if(respuesta.status == "OK"){
			$('#respuesta').append('<div id="lasrespuestas"><div class="swiper-container"><div class="swiper-wrapper"><div class="swiper-slide">');
			$('.swiper-slide').append('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
			$('.swiper-slide').append('<p class="stepAnswer">  ' + respuesta.step.step_description+ '</p>');
			crearBotones(1, respuesta);
			$('.contenedor').append('</div></div></div></div>');

		}
		else if(respuesta.status == "NEW"){
			$('.contenedor').append('<div id="lasrespuestas"><p>There are not answers for this Question.</p>'+'<p>Would you like to answer it?</p>'+'<div id="botonera"><a  data-ajax="false" href="responderPregunta.html" class="botonPreguntaGrande alLado">YES</a> <a  data-ajax="false" href="index.html" class="botonPreguntaGrande alLado">NO</a></div></div> ');
		}
		else if(respuesta.status == "INVALID"){
			$('.contenedor').append('<div id="lasrespuestas"><p>Your question is invalid. Please go back and ask again</p>'+'<a  data-ajax="false" href="index.html" class="botonPreguntaGrande alLado">GO BACK</a></div>');				
		}
            $('#preguntar .printResults').html(respuesta);   
			   
        }).fail(function() {
			 alert( "Envío fallido." );      
        });
        return false; 
    });

var crearBotones = function(setBotones, respuesta){
	if(setBotones==1){
		//$.getJSON('js/answer3.json', function(respuesta) {	
		//$.getJSON('rest/answer', function(respuesta) {		
		console.log(respuesta.step.steps_neighborhood);
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
			llamarSiguientePaso(1, respuesta.step.step_id);

		//});
	}
	if(setBotones==2){
	//	$.getJSON('rest/GetStep?step_id=2&step_direction=0100', function(respuesta) {
		//$.getJSON('js/nextStep.json', function(respuesta) {		
		//$.getJSON('rest/answer', function(respuesta) {		
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
			llamarSiguientePaso(2, respuesta.step.step_id);

		//});
		
	}

}

var llamarSiguientePaso= function(pasoActual, stepId){
	var botones = ['.arriba','.derecha','.abajo','.izquierda'];
	var direccion = [
	{bottom:'-300px',opacity:'0'},
	{left:'-300px',opacity:'0'},
	{top:'-300px',opacity:'0'},
	{right:'-300px',opacity:'0'}];
	
	console.log(stepId);

	var velocidad = 500;
	if(pasoActual==1){
		
		//$.getJSON('rest/GetStep?step_id=2&step_direction=0100', function(respuesta) {
		//$.getJSON('rest/answer', function(respuesta) {
			$('.arriba').click(function(){		
				var rest='rest/GetStep?step_id='+stepId+'&step_direction=1000';
				$.getJSON(rest, function(respuesta) {

				$('.swiper-slide').animate({bottom:'-300px',opacity:'0'},(velocidad-200),function() { $('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
					$('.swiper-slide').append('<p class="stepAnswer">  ' + respuesta.step.step_description+ '</p>');
					$('.swiper-slide').animate({bottom:'100px'},0);
					$('.swiper-slide').animate({bottom:'0px',opacity:'1'},velocidad);
					crearBotones(2, respuesta);    }   );
				
				});
			});

			$('.derecha').click(function(){		
				var rest='rest/GetStep?step_id='+stepId+'&step_direction=1100';
				$.getJSON(rest, function(respuesta) {
				$('.swiper-slide').animate({left:'-300px',opacity:'0'},(velocidad-200),function() {
					$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
					$('.swiper-slide').append('<p class="stepAnswer">  ' + respuesta.step.step_description+  '</p>');					
					$('.swiper-slide').animate({left:'100px'},0);
					$('.swiper-slide').animate({left:'0px',opacity:'1'},velocidad);
					crearBotones(2, respuesta);
				});
});

			});
			$('.abajo').click(function(){		
				var rest='rest/GetStep?step_id='+stepId+'&step_direction=0010';
				$.getJSON(rest, function(respuesta) {

				$('.swiper-slide').animate({bottom:'300px',opacity:'0'},(velocidad-200),function() {
					$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
					$('.swiper-slide').append('<p class="stepAnswer">  ' + respuesta.step.step_description+  '</p>');
					$('.swiper-slide').animate({bottom:'-100px'},0);
					$('.swiper-slide').animate({bottom:'0px',opacity:'1'},velocidad);
					crearBotones(2, respuesta);


				});
				
});
			});
			$('.izquierda').click(function(){		
				var rest='rest/GetStep?step_id='+stepId+'&step_direction=0001';
				$.getJSON(rest, function(respuesta) {
				$('.swiper-slide').animate({left:'600px',opacity:'0'},(velocidad-200),function() {
					$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
					$('.swiper-slide').append('<p class="stepAnswer"> ' + respuesta.step.step_description+ '</p>');
					$('.swiper-slide').animate({left:'-100px'},0);
					$('.swiper-slide').animate({left:'0px',opacity:'1'},velocidad);
					crearBotones(2, respuesta);

				});
				
});
			});

		
}

if(pasoActual==2){
	//$.getJSON('rest/answer', function(respuesta) {
		$('.arriba').click(function(){		
				var rest='rest/GetStep?step_id='+stepId+'&step_direction=1000';
				$.getJSON(rest, function(respuesta) {

			$('.swiper-slide').animate({bottom:'-300px',opacity:'0'},(velocidad-200),function() {
				$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
			$('.swiper-slide').append('<p class="stepAnswer"> ' + respuesta.step.step_description+  '</p>');
			$('.swiper-slide').animate({bottom:'100px'},0);
			$('.swiper-slide').animate({bottom:'0px',opacity:'1'},velocidad);
			crearBotones(1, respuesta);

			});
			});

		});

		$('.derecha').click(function(){		
				var rest='rest/GetStep?step_id='+stepId+'&step_direction=1100';
				$.getJSON(rest, function(respuesta) {

			$('.swiper-slide').animate({left:'-300px',opacity:'0'},(velocidad-200),function() {
				$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
			$('.swiper-slide').append('<p class="stepAnswer">  ' + respuesta.step.step_description+ '</p>');
			$('.swiper-slide').animate({left:'100px'},0);
			$('.swiper-slide').animate({left:'0px',opacity:'1'},velocidad);
			crearBotones(1, respuesta);

			});
			});

		});
		$('.abajo').click(function(){		

				var rest='rest/GetStep?step_id='+stepId+'&step_direction=0010';
				$.getJSON(rest, function(respuesta) {
			$('.swiper-slide').animate({bottom:'300px',opacity:'0'},(velocidad-200),function() {
$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
			$('.swiper-slide').append('<p class="stepAnswer"> ' + respuesta.step.step_description+  '</p>');
			$('.swiper-slide').animate({bottom:'-100px'},0);
			$('.swiper-slide').animate({bottom:'0px',opacity:'1'},velocidad);
			crearBotones(1, respuesta);

			});
			});

		});
		$('.izquierda').click(function(){		
				var rest='rest/GetStep?step_id='+stepId+'&step_direction=0001';
				$.getJSON(rest, function(respuesta) {
				
			$('.swiper-slide').animate({left:'600px',opacity:'0'},(velocidad-200),function() {
$('.swiper-slide').html('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
			$('.swiper-slide').append('<p class="stepAnswer"> ' + respuesta.step.step_description+  '</p>');
			$('.swiper-slide').animate({left:'-100px'},0);
			$('.swiper-slide').animate({left:'0px',opacity:'1'},velocidad);
			crearBotones(1, respuesta);
				
			});
			
});
		});

}

}

var crearRating= function(){
	$('.swiper-slide #ratingPregunta').append('<input type="hidden" id="rating" name="rating"><div id="califique">califique la respuesta</div><div id="rateit"></div>');
	$(function () { $('#rateit').rateit({ max: 5, step: 1, backingfld: '#rating' }); });
}

$( document ).delegate("#respuesta", "pageinit", function() {
	crearRating();
});

});

$(document).ready(function(){
$('#responderPregunta #createQuestionForm').submit(function(){
	$('#responderPregunta .printResults').html("<b>Enviando...</b>");
	//$.post('postReceiver.php', $(this).serialize(), function(data){
	$.post('rest/CreateAndAnswerQuestion', $(this).serialize(), function(data){
            $('#responderPregunta .printResults').html(data);
        }).fail(function() {
				alert( "Envío fallido." );
        });
		return false;
    });	


});

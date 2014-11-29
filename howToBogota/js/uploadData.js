$(document).ready(function(){
    $('#preguntar #askQuestionForm').submit(function(){  
	
		$('#preguntar .printResults').html("<b>Enviando...</b>");
		$.post('postReceiver.php', $(this).serialize(), function(data){
       //$.post('rest/GetAnswer', $(this).serialize(), function(data){
		   		$.getJSON('js/answer3.json', function(respuesta) {
             	$('.contenedor #contenidoPrincipal').html('<div id="lapregunta"><p class="morados">You wrote</p><p id="howto">How To....</p><p id="questionASked">'+ respuesta.question + '</p></div>');
             	if(respuesta.status == "OK"){
             		$('.contenedor #contenidoPrincipal').append('<div id="lasrespuestas"><div class="swiper-container"><div class="swiper-wrapper"><div class="swiper-slide">');
             		$('.swiper-slide').append('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + respuesta.step.step_id+ '</span></p>');
             		$('.swiper-slide').append('<p class="stepAnswer"> Description: ' + respuesta.step.step_description+ ' y mis vecinos son ' + respuesta.step.steps_neighborhood + '</p>');
             		//crearBotones(1);
             		$('.contenedor #contenidoPrincipal').append('</div></div></div></div');

             	}
             	else if(respuesta.status == "NEW"){
             		$('.contenedor #contenidoPrincipal').append('<div id="lasrespuestas"><p>There are not answers for this Question.</p>'+'<p>Would you like to answer it?</p>'+'<div id="botonera"><a  data-ajax="false" href="responderPregunta.html" class="botonPreguntaGrande alLado">YES</a> <a  data-ajax="false" href="index.html" class="botonPreguntaGrande alLado">NO</a></div></div> ');
             	}
             	else if(respuesta.status == "INVALID"){
             		$('.contenedor #contenidoPrincipal').append('<div id="lasrespuestas"><p>Your question is invalid. Please go back and ask again</p>'+'<a  data-ajax="false" href="index.html" class="botonPreguntaGrande alLado">GO BACK</a></div>');				
             	}
				});
           
        }).fail(function() {
			 alert( "Envío fallido." );      
        });
        return false; 
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

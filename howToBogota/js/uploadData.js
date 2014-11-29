$(document).ready(function(){
    $('#preguntar #askQuestionForm').submit(function(){  
	$( "body" ).load( "respuesta.html" );
		//$.post('postReceiver.php', $(this).serialize(), function(data){
       $('#preguntar .printResults').html("<b>Enviando...</b>");
       $.post('rest/GetAnswer', $(this).serialize(), function(data){
            $('#preguntar .printResults').html(data);             
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

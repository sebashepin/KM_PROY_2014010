$(document).ready(function(){
    $('#preguntar #askQuestionForm').submit(function(){  
       $('#preguntar .printResults').html("<b>Enviando...</b>");
       $.post('postReceiver.php', $(this).serialize(), function(data){
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
	$.post('postReceiver.php', $(this).serialize(), function(data){
            $('#responderPregunta .printResults').html(data);
        }).fail(function() {
				alert( "Envío fallido." );
        });
		return false;
    });	
});
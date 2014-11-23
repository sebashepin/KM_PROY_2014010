$(document).ready(function(){
    $('#preguntar #askQuestionForm').submit(function(){  
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
	
	$.post('postReceiver.php', $(this).serialize(), function(data){
            $('#responderPregunta .printResults').html(data);
        }).fail(function() {
				alert( "Envío fallido." );
        });
		return false;
    });	
	

/*
$( "#responderPregunta #askQuestionForm" ).submit(function( event ) {
 
  // Stop form from submitting normally
  event.preventDefault();
 
  // Get some values from elements on the page:
  var $form = $( this ),
    term = $form.find( "input[name='question']" ).val(),
    url = "http://25.141.65.84:8080/HowToBogota/rest/HowToBogota/GetAnswer";
 
  // Send the data using post
  var posting = $.post( url, { question: term } );
 
  // Put the results in a div
  posting.done(function( data ) {
    var content = $( data ).find( "#printResults" );
    $( "#printResults" ).append( content );
  });
});
*/


});

$(document).ready(function() {
    
          $.getJSON('js/answer3.json', function(pregunta) {
             $('.swiper-slide').html('<p> Status: ' + pregunta.status + '</p>');
			 $('.swiper-slide').append('<p class="answer"> Question: ' + pregunta.question + '</p>');
             $('.swiper-slide').append('<p class="paso"><span class="textoPAso">STEP</span> <span class="numeroPaso">' + pregunta.step.step_id+ '</span></p>');
             $('.swiper-slide').append('<p class="stepAnswer"> Description: ' + pregunta.step.step_description+ ' y mis vecinos son ' + pregunta.step.steps_neighborhood + '</p>');
          });
      
   });
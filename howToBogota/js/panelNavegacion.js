var panel = '<div data-role="panel" data-position="left" data-position-fixed="true" data-display="push" data-theme="a" id="navegacion"><ul data-role="listview"><li data-icon="delete"><a href="#" data-rel="close">Close menu</a></li><li><a href="index.html">Ask a Question</a></li><li><a href="responderPregunta.html">Answer a Question</a></li><li><a href="respuesta.html">***Ejemplo Respuesta</a></li></ul></div>';

$(document).one('pagebeforecreate', function () {
  $.mobile.pageContainer.prepend(panel);
  $("#navegacion").panel();
  $("#navegacion").panel().enhanceWithin();
});

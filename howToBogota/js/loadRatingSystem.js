$( document ).delegate("#respuesta", "pageinit", function() {
  alert('A page with an ID of "respuesta" was just created by jQuery Mobile!');
  $('.swiper-slide #ratingPregunta').append('<input type="hidden" id="rating" name="rating"><div id="rateit6"></div>');
  $(function () { $('#rateit6').rateit({ max: 5, step: 1, backingfld: '#rating' }); });
});

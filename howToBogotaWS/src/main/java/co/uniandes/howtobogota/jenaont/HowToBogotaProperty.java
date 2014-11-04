package co.uniandes.howtobogota.jenaont;

public enum HowToBogotaProperty {
	COMPUESTO_DE("Compuesto_de"), //Respuesta => Pasos
	CALIFICADO_POR("calificado_por"), //Usuarios => Respuestas
	CALIFICATIVO_ASOC("calificativo_asociado"), //Calificativos =>Preguntas/respuestas
	CALIFICATIVO_SINON("calificativo_sinonimo_de"), //Calificativos =>Calificativo
	ENTIDAD_ASOC("entidad_asociada"), //Entidades=>Preguntas/Respuestas
	PASO_ASOC_A("paso_asociado_a"), //Paso=>Paso
	PREGUNTA_ASOC_RTA("pregunta_asociada_respuesta"), //Pregunta=>Respuestas
	RTA_ASOC_PREGUNTA("respuesta_asociada_pregunta"), //Respuesta=>Pregunta
	TENER("tener"), //Thing=>Thing
	VERBO_ASOC("verbo_asociado"), //Verbos=>Preguntas/Respuestas
	VERBO_COMP_POR("verbo_complementado_por"), //Verbos=>Calificativos
	VERBO_SINON("verbo_sinonimo_de"), //Verbos =>Verbos
	CALIFICACION("calificacion"), //Propiedad de las respuestas
	TEXTO_NOMBRE("textoNombre"), //Propiedad de los usuarios
	TEXTO_PASO("textoPaso"), //Propiedad de los pasos
	TEXTO_PREGUNTA("textoPregunta"), //Propiedad de las preguntas
	PRIMER_PASO("primer_paso") //Respuesta => Pasos
	;
	
	private String name;
	 
	private HowToBogotaProperty(String s) {
		name = s;
	}
 
	public String getName() {
		return name;
	}
}

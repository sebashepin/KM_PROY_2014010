package co.uniandes.howtobogota.jenaont;

public enum HowToBogotaClass {
	CALIFICATIVOS("Calificativos"), ENTIDADES("Entidades"), PREGUNTAS("Preguntas"), PASOS("Pasos"),
	RESPUESTAS("Respuestas"), USUARIOS("Usuarios"), VERBOS("Verbos");
	 
	private String name;
 
	private HowToBogotaClass(String s) {
		name = s;
	}
 
	public String getName() {
		return name;
	}
}

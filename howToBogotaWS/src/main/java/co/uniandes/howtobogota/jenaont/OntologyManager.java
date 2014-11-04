package co.uniandes.howtobogota.jenaont;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.VCARD;

public class OntologyManager {

	//Archivo en el cual se encuentra la ontología
	public static final String ONTOLOGY_DATA_FILE = "file:models/howToBogota.owl";
	
	//URI de la ontología
	public static final String URI="http://www.semanticweb.org/ontologies/2014/8/howToBogota.owl#";
	
	public static final String MODEL_NAME="howToBogota";
	
	private static final OntModelSpec ONT_ESPECIFICACION=OntModelSpec.OWL_MEM;
	
	
	private static OntologyManager instancia;
	
	
	private Dataset dataset;

	private OntModel ontModel;
	
	public static OntologyManager darInstancia(){
        if(instancia==null){
        	instancia= new OntologyManager();
        }
        return instancia;
    }
	
	public OntologyManager(){

		//Revisar si la carpeta "dataset" existe, de lo contrario, se crea la ontología a partir del owl.
		
		File file = new File("database/");

		if(!file.exists() ||  !(file.list().length>0)){
			//Se crea un modelo con el lenguaje OWLFull (característica de la ontología), con un modelo de storage in memory
	        //y con un RBS con reglas OWL (Sea lo que sea lo que eso signifique)
	        //TODO Revisar si con otro tipo de razonadores se podrían obtener mejores/diferentes resultados
			OntModel ontModel= ModelFactory.createOntologyModel(ONT_ESPECIFICACION);
	        //Se lee la ontología
			ontModel.read( ONTOLOGY_DATA_FILE );
			
			dataset = TDBFactory.createDataset("database/");
			dataset.begin(ReadWrite.WRITE) ;
			dataset.addNamedModel(MODEL_NAME, ontModel);
			dataset.commit();
			TDB.sync(dataset ) ;
		}else{
			dataset = TDBFactory.createDataset("database/");
		}
	}

	/**
	 * @return the dataset
	 */
	public Dataset getDataset() {
		return dataset;
	}

	/**
	 * @param dataset the dataset to set
	 */
	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	/**
	 * Importante: debe estar abierta una transacción
	 * @return the ontModel
	 */
	public OntModel getOntModel() {
		Model model = dataset.getNamedModel(MODEL_NAME);
		return ModelFactory.createOntologyModel(ONT_ESPECIFICACION, model); 
	}
	
	public Individual createObject(HowToBogotaClass htbClass, String nombre){
		Individual obj=null;
		Resource r = ontModel.getResource(URI + htbClass.getName());
	    OntClass htbOntClass = r.as(OntClass.class);
	    obj=htbOntClass.createIndividual(URI + nombre);
        return obj;
	

	}
	
	//Known URI methods
	public Individual getObject(String nombre){
		Individual obj=null;
		obj=ontModel.getIndividual(URI + nombre);
		return obj;
	}
	
	public void removeObject(HowToBogotaClass htbClass, String nombre){
		Individual obj=ontModel.getIndividual(URI + nombre);
        Resource r = ontModel.getResource(URI+htbClass.getName());
        OntClass htbOntClass = r.as(OntClass.class);
        htbOntClass.dropIndividual(obj);

	}

	public void setProperty(Individual obj, HowToBogotaProperty htbProp, RDFNode value){
			OntProperty p = ontModel.getOntProperty( URI + htbProp.getName());
		    obj.addProperty(p,value);
	}
	
	//Objetos nuestros
	
	public void agregarPregunta(String id,String pregunta, ArrayList<String>verbos, 
			ArrayList<String> entidades, ArrayList<String>calificativos ){
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();

        Individual preg=createObject( HowToBogotaClass.PREGUNTAS, id);
  
       
		Literal l = ontModel.createTypedLiteral(pregunta);
		setProperty(preg, HowToBogotaProperty.TEXTO_PREGUNTA, l);
	       
		for(int i=0; i< verbos.size();i++){
			String verb= verbos.get(i);
			Individual obj = getObject(verb);
			if(obj==null){
				//Es necesario crearlo
				obj =createObject(HowToBogotaClass.VERBOS, verb);
			}
			setProperty(obj, HowToBogotaProperty.VERBO_ASOC, preg);
		}
		
		for(int i=0; i< entidades.size();i++){
			String ent= entidades.get(i);
			Individual obj = getObject(ent);
			if(obj==null){
				//Es necesario crearlo
				obj =createObject(HowToBogotaClass.ENTIDADES, ent);
			}
			setProperty(obj, HowToBogotaProperty.ENTIDAD_ASOC, preg);
			
		}
		
		for(int i=0; i< calificativos.size();i++){
			String calif= calificativos.get(i);
			Individual obj = getObject(calif);
			if(obj==null){
				//Es necesario crearlo
				obj =createObject(HowToBogotaClass.CALIFICATIVOS, calif);
			}
			setProperty(obj, HowToBogotaProperty.CALIFICATIVO_ASOC, preg);
			
		}
		dataset.commit();
		TDB.sync(dataset);
	}
	
	public void agregarUsuario(String id, String nombre){
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		Individual us =createObject(HowToBogotaClass.USUARIOS, id);
		Literal l = ontModel.createTypedLiteral(nombre);
		setProperty(us, HowToBogotaProperty.TEXTO_NOMBRE, l);
		dataset.commit();
		TDB.sync(dataset);
	}
	
	public void agregarCalificacionUsuario(String idUsuario, String idRespuesta, float calificacion){
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		Individual usuario=getObject(idUsuario);
		Individual respuesta=getObject(idRespuesta);
		setProperty(usuario, HowToBogotaProperty.CALIFICADO_POR, respuesta);
		
		//TODO Genera doble calificación. Averiguar como reemplazar.
		Literal l = ontModel.createTypedLiteral(calificacion);
		setProperty(respuesta, HowToBogotaProperty.CALIFICACION, l);
		
		dataset.commit();
		TDB.sync(dataset);
	}
	
	public void agregarRespuesta(String idRespuesta, String idPregunta, ArrayList<String> pasos,  ArrayList<String>verbos, 
			ArrayList<String> entidades, ArrayList<String>calificativos ){
		
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		
		Individual res =createObject(HowToBogotaClass.RESPUESTAS, idRespuesta);
		Individual preg = getObject(idPregunta);
		
		setProperty(preg, HowToBogotaProperty.PREGUNTA_ASOC_RTA ,res);
		setProperty(res, HowToBogotaProperty.RTA_ASOC_PREGUNTA ,preg);
		
		Individual ant=null;
		for(int i=0; i< pasos.size();i++){
			
			String paso= pasos.get(i);
			String idPaso=idRespuesta+i;
			Individual obj2 =createObject(HowToBogotaClass.PASOS, idPaso);
			
			setProperty(res, HowToBogotaProperty.COMPUESTO_DE, obj2);

			Literal l = ontModel.createTypedLiteral(paso);
			setProperty(obj2, HowToBogotaProperty.TEXTO_PASO, l);
			
			if(ant==null){
				setProperty(res, HowToBogotaProperty.PRIMER_PASO, obj2);
				ant=obj2;
			}else{
				setProperty(ant, HowToBogotaProperty.PASO_ASOC_A, obj2 );
				ant=obj2;
			}
		}
		
		for(int i=0; i< verbos.size();i++){
			String verb= verbos.get(i);
			Individual obj = getObject(verb);
			if(obj==null){
				//Es necesario crearlo
				obj =createObject(HowToBogotaClass.VERBOS, verb);
			}
			setProperty(obj, HowToBogotaProperty.VERBO_ASOC, res);
		}
		
		for(int i=0; i< entidades.size();i++){
			String ent= entidades.get(i);
			Individual obj = getObject(ent);
			if(obj==null){
				//Es necesario crearlo
				obj =createObject(HowToBogotaClass.ENTIDADES, ent);
			}
			setProperty(obj, HowToBogotaProperty.ENTIDAD_ASOC, res);
			
		}
		
		for(int i=0; i< calificativos.size();i++){
			String calif= calificativos.get(i);
			Individual obj = getObject(calif);
			if(obj==null){
				//Es necesario crearlo
				obj =createObject(HowToBogotaClass.CALIFICATIVOS, calif);
			}
			setProperty(obj, HowToBogotaProperty.CALIFICATIVO_ASOC, res);
			
		}
		dataset.commit();
		TDB.sync(dataset);
	}
	
	public void darRespuesta(String idPregunta){
		dataset.begin(ReadWrite.READ);
		ontModel= getOntModel();
		Individual us = getObject(idPregunta);
		if(us!=null){
//			{
//			"status": "OK" ,
//			"step":{
//				"step_id":  "1",
//				"step_description":"I am a step",
//		        "steps_neighborhood": "1110"
//			}
			long n = System.currentTimeMillis();
			OntProperty ptAsociadaRta = ontModel.getOntProperty( URI +HowToBogotaProperty.PREGUNTA_ASOC_RTA.getName());
			StmtIterator itert = ontModel.listStatements(
				    new SimpleSelector(us, ptAsociadaRta, (RDFNode) null));
			if (itert.hasNext()) {
			    System.out.println("The database contains vcards for:");
			    while (itert.hasNext()) {
			        System.out.println("  " + itert.next());
			    }
			}
			 System.out.println(System.currentTimeMillis()-n);
			
//			if(res!=null){
//				
//				OntProperty pasoAsoc = ontModel.getOntProperty( URI +HowToBogotaProperty.PASO_ASOC_A.getName());
//				StmtIterator itert = ontModel.listStatements(
//					    new SimpleSelector(null, pasoAsoc, (RDFNode) null));
//				if (itert.hasNext()) {
//				    System.out.println("The database contains vcards for:");
//				    while (itert.hasNext()) {
//				        System.out.println("  " + itert.next());
//				    }
//				}
//				
//				OntProperty p = ontModel.getOntProperty( URI +HowToBogotaProperty.COMPUESTO_DE.getName());
//				StmtIterator iter = ontModel.listStatements(
//					    new SimpleSelector(res, p, (RDFNode) null));
//				if (iter.hasNext()) {
//				    System.out.println("The database contains vcards for:");
//				    while (iter.hasNext()) {
//				        System.out.println("  " + iter.next());
//				    }
//				} else {
//				    System.out.println("No vcards were found in the database");
//				}
//			}
		}
		
		dataset.commit();
		TDB.sync(dataset);
	}
	
}

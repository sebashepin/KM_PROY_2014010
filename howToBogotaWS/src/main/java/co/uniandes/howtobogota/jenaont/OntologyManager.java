package co.uniandes.howtobogota.jenaont;

import java.io.File;
import java.util.ArrayList;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;

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
	
	public void agregarRespuesta(String idRespuesta, String idPregunta, String[] pasos,  ArrayList<String>verbos, 
			ArrayList<String> entidades, ArrayList<String>calificativos ){
		
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		
		Individual res =createObject(HowToBogotaClass.RESPUESTAS, idRespuesta);
		Individual preg = getObject(idPregunta);
		
		setProperty(preg, HowToBogotaProperty.PREGUNTA_ASOC_RTA ,res);
		setProperty(res, HowToBogotaProperty.RTA_ASOC_PREGUNTA ,preg);
		
		Individual ant=null;
		for(int i=0; i< pasos.length;i++){
			
			String paso= pasos[i];
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
	
	public Step darRespuesta(String idPregunta){
		dataset.begin(ReadWrite.READ);
		ontModel= getOntModel();
		Individual us = getObject(idPregunta);
		Step first=null;
		if(us!=null){

			OntProperty ptAsociadaRta = ontModel.getOntProperty( URI +HowToBogotaProperty.PREGUNTA_ASOC_RTA.getName());
			StmtIterator itert = ontModel.listStatements(
				    new SimpleSelector(us, ptAsociadaRta, (RDFNode) null));
			int i=0;
			
			String secondStep=null;
		    while (itert.hasNext() && i <2) {
		    	Resource resp=itert.next().getObject().asResource();
		        System.out.println("  " + resp);
		        OntProperty pPrimerPaso = ontModel.getOntProperty( URI +HowToBogotaProperty.PRIMER_PASO.getName());
		        Resource step =resp.getProperty(pPrimerPaso).getResource();
		      
		        if(i==0){
		        	OntProperty  pDescrip= ontModel.getOntProperty( URI +HowToBogotaProperty.TEXTO_PASO.getName());
		        	first=new Step(step.getLocalName(), step.getProperty(pDescrip).getString());
		        	OntProperty  pSiguiente= ontModel.getOntProperty( URI +HowToBogotaProperty.PASO_ASOC_A.getName());
		        	first.setNextStep(step.getProperty(pSiguiente).getResource().getLocalName());
		        }else{
		        	secondStep=step.getLocalName();
		        	first.setDownStep(secondStep);
		        }
		        i++;
		    }
		}
		dataset.commit();
		TDB.sync(dataset);
		return first;
	}


	public ArrayList<String> buscarPregunta(ArrayList<String>verbos, 
			ArrayList<String> entidades){
		ArrayList<String> res=new ArrayList<String>();
		dataset.begin(ReadWrite.READ);
		ontModel= getOntModel();
		
        String queryString = 
        		"PREFIX uri:<"+URI +">"+
        		"SELECT ?x " +
        		"WHERE {" +
        		"      ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> uri:"+HowToBogotaClass.PREGUNTAS.getName()+" . "; 

        
        for(int i=0; i<verbos.size();i++){
        	queryString=queryString+
        		"		uri:"+verbos.get(i)+"	uri:"+HowToBogotaProperty.VERBO_ASOC.getName()+"	?x . ";
        }
        for(int i=0; i<entidades.size();i++){
        	queryString=queryString+
        		"		uri:"+entidades.get(i)+"	uri:"+HowToBogotaProperty.ENTIDAD_ASOC.getName()+"	?x . ";
        }
        queryString=queryString+
        		"      }";
        
        
        Query query = QueryFactory.create(queryString);

    	QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
    	ResultSet results = qe.execSelect();
    	while(results.hasNext()){
    		QuerySolution qr= results.next();
    		res.add(qr.get("x").asResource().getLocalName());
    	}
		dataset.commit();
		TDB.sync(dataset);
		return res;
	}
	
    public String agregarPrimerPaso(String idRespuesta, String idPregunta, String descPaso,  ArrayList<String>verbos, 
			ArrayList<String> entidades, ArrayList<String>calificativos ){
		
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		
		Individual res =createObject(HowToBogotaClass.RESPUESTAS, idRespuesta);
		Individual preg = getObject(idPregunta);
		
		setProperty(preg, HowToBogotaProperty.PREGUNTA_ASOC_RTA ,res);
		setProperty(res, HowToBogotaProperty.RTA_ASOC_PREGUNTA ,preg);
		
		String idPaso=idRespuesta+0;
		Individual obj2 =createObject(HowToBogotaClass.PASOS, idPaso);
			
		setProperty(res, HowToBogotaProperty.COMPUESTO_DE, obj2);

		Literal l = ontModel.createTypedLiteral(descPaso);
		setProperty(obj2, HowToBogotaProperty.TEXTO_PASO, l);
	
		setProperty(res, HowToBogotaProperty.PRIMER_PASO, obj2);
		
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
		return idPaso;
    }
}

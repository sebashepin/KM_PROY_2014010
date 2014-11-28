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
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;

public class OntologyManager {

	//Archivo en el cual se encuentra la ontolog�a
	public static final String ONTOLOGY_DATA_FILE = "file:/home/sebastian/models/howToBogota.owl";
	
	//URI de la ontolog�a
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

		//Revisar si la carpeta "dataset" existe, de lo contrario, se crea la ontolog�a a partir del owl.
		
		File file = new File("/home/sebastian/database/");
		String path="file:"+System.getProperty("jboss.server.base.dir")+"/HowToBogota.war/models/howToBogota.owl";
		
		if(!file.exists() ||  !(file.list().length>0)){
			//Se crea un modelo con el lenguaje OWLFull (caracter�stica de la ontolog�a), con un modelo de storage in memory
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
	 * Importante: debe estar abierta una transacci�n
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
	
	public String agregarPregunta(String pregunta, ArrayList<String>verbos, 
			ArrayList<String> entidades, ArrayList<String>calificativos ){
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		//Obtener y actualizar los id de las preguntas
		String id="pregunta"+darSiguienteId("idPregunta");
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
		return id;
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
		
		//TODO Genera doble calificaci�n. Averiguar como reemplazar.
		Literal l = ontModel.createTypedLiteral(calificacion);
		setProperty(respuesta, HowToBogotaProperty.CALIFICACION, l);
		
		dataset.commit();
		TDB.sync(dataset);
	}
	
	public void agregarRespuesta(String idPregunta, String[] pasos){
		
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		

		Individual preg = getObject(idPregunta);

		
		Individual ant=null;
		for(int i=0; i< pasos.length;i++){
			
			String paso= pasos[i];
			String idPaso="paso"+darSiguienteId("idPasos");
			Individual step =createObject(HowToBogotaClass.PASOS, idPaso);
			
			Literal l = ontModel.createTypedLiteral(paso);
			setProperty(step, HowToBogotaProperty.TEXTO_PASO, l);
			
			Literal lNumCalificacion= ontModel.createTypedLiteral(0);
			setProperty(step, HowToBogotaProperty.NUM_CALIFICACIONES, lNumCalificacion);
	
			
			if(ant==null){
				setProperty(preg, HowToBogotaProperty.PREGUNTA_ASOC_RTA, step);
				ant=step;
			}else{
				setProperty(ant, HowToBogotaProperty.PASO_ASOC_A, step );
				ant=step;
			}
		}
		
	
		dataset.commit();
		TDB.sync(dataset);
	}
	
	public Step darRespuesta(String idPregunta){
		dataset.begin(ReadWrite.READ);
		ontModel= getOntModel();

		Step s=null;
		String queryString = 
        		"PREFIX uri:<"+URI +">"+
        		"SELECT ?x ?descrip ?next " +
        		"WHERE {" +
        		"      uri:"+idPregunta +" uri:"+HowToBogotaProperty.PREGUNTA_ASOC_RTA.getName()+" ?x ." +
        		"      ?x uri:"+HowToBogotaProperty.CALIFICACION.getName()+"  ?cal . " +
        		"      ?x uri:"+HowToBogotaProperty.TEXTO_PASO.getName()+" ?descrip . " +
        		" OPTIONAL { ?x uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" ?next }"+
        		"} ORDER BY DESC(?cal) LIMIT 2";
        
		Query query = QueryFactory.create(queryString);

    	QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
    	ResultSet results = qe.execSelect();
    	
		String idStep=null;
		String stepDesc=null;
		String idNext=null;
    	boolean gotIn=false;
    	int i=0;
    	while(results.hasNext()){
    		gotIn=true;
    		QuerySolution qr= results.next();
    		if(i==0){
	    		idStep=qr.getResource("x").getLocalName();
	    		stepDesc=qr.getLiteral("descrip").getString();
				if(qr.getResource("next")!=null){
					idNext=qr.getResource("next").getLocalName();
				}
    		}
    		i++;
    	}
    	
    	if(gotIn){
    		if(idStep!=null){
	    		s= new Step(idStep, stepDesc);
	    		s.setNextStep(idNext);
	    		if(i>1){
		    		s.setDownStep(idStep);
		    	}
	    	}
    	}else{
    		//Ning�n paso tiene calificaci�n
    		//Entrega un paso aleatorio 
    		queryString = 
            		"PREFIX uri:<"+URI +">"+
                    		"SELECT ?x ?descrip ?next " +
                    		"WHERE {" +
                    		"      uri:"+idPregunta +" uri:"+HowToBogotaProperty.PREGUNTA_ASOC_RTA+" ?x ." +
                    		"      ?x uri:"+HowToBogotaProperty.TEXTO_PASO.getName()+" ?descrip . " +
                    		" OPTIONAL { ?x uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" ?next }"+
                    		"} LIMIT 2";
            
            query = QueryFactory.create(queryString);

        	qe = QueryExecutionFactory.create(query, ontModel);
        	results = qe.execSelect();
        	gotIn=false;
        	i=0;
        	while(results.hasNext()){
        		gotIn=true;
        		QuerySolution qr= results.next();
        		if(i==0){
    	    		idStep=qr.getResource("x").getLocalName();
    				stepDesc=qr.getResource("descrip").getLocalName();
    				if(qr.getResource("next")!=null){
    					idNext=qr.getResource("next").getLocalName();
    				}
        		}
        		i++;
        	}
        	
        	if(gotIn){
        		if(idStep!=null){
    	    		s= new Step(idStep, stepDesc);
    	    		s.setNextStep(idNext);
    	    		if(i>1){
    		    		s.setDownStep(idStep);
    		    	}
    	    	}
        	}
    	}
    		
    	
		dataset.commit();
		TDB.sync(dataset);
		return s;
	}

	public String buscarPreguntaSimilar(ArrayList<String>verbos, 
			ArrayList<String> entidades, ArrayList<String> calificativos){
		String res=null;
		dataset.begin(ReadWrite.READ);
		ontModel= getOntModel();
		
        String queryString = 
        		"PREFIX uri:<"+URI +">"+
        		"SELECT ?x " +
        		"WHERE {" +
        		"      ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> uri:"+HowToBogotaClass.PREGUNTAS.getName() +" ."; 

     
        for(int i=0; i<verbos.size();i++){
        	   queryString+="{";
        	queryString=queryString+
        		"		uri:"+verbos.get(i)+"	uri:"+HowToBogotaProperty.VERBO_ASOC.getName()+"	?x . ";
            queryString+="} UNION{";
            queryString+="		?v	uri:"+HowToBogotaProperty.VERBO_ASOC.getName()+"	?x . ";
            queryString+="		uri:"+verbos.get(i)+"	uri:"+HowToBogotaProperty.VERBO_SINON.getName()+"	?v. ";
            queryString=queryString+
            		"      }";
        }
        
        for(int i=0; i<entidades.size();i++){
        	queryString=queryString+
        		"		uri:"+entidades.get(i)+"	uri:"+HowToBogotaProperty.ENTIDAD_ASOC.getName()+"	?x . ";
        }

        for(int i=0; i<calificativos.size();i++){
     	   queryString+="{";
     	queryString=queryString+
     		"		uri:"+calificativos.get(i)+"	uri:"+HowToBogotaProperty.CALIFICATIVO_ASOC.getName()+"	?x . ";
         queryString+="} UNION{";
         queryString+="		?v	uri:"+HowToBogotaProperty.CALIFICATIVO_ASOC.getName()+"	?x . ";
         queryString+="		uri:"+calificativos.get(i)+"	uri:"+HowToBogotaProperty.CALIFICATIVO_SINON.getName()+"	?v. ";
         queryString=queryString+
         		"      }";
        }

        queryString=queryString+
        		"      }";

        System.out.println(queryString);
        Query query = QueryFactory.create(queryString);

    	QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
    	ResultSet results = qe.execSelect();
    	while(results.hasNext()){
    		QuerySolution qr= results.next();
    		res=qr.get("x").asResource().getLocalName();
    		System.out.println(res);
    	}
		dataset.commit();
		TDB.sync(dataset);
		return res;
	}
	
	public String buscarPreguntaExacta(ArrayList<String>verbos, 
			ArrayList<String> entidades, ArrayList<String> calificativos){
		String res=null;
		dataset.begin(ReadWrite.READ);
		ontModel= getOntModel();
		
        String queryString = 
        		"PREFIX uri:<"+URI +">"+
        		"SELECT ?x " +
        		"WHERE {" +
        		"      ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> uri:"+HowToBogotaClass.PREGUNTAS.getName() +" ."; 

        
        for(int i=0; i<verbos.size();i++){
        	queryString=queryString+
        		"		uri:"+verbos.get(i)+"	uri:"+HowToBogotaProperty.VERBO_ASOC.getName()+"	?x . ";
        }
        for(int i=0; i<entidades.size();i++){
        	queryString=queryString+
        		"		uri:"+entidades.get(i)+"	uri:"+HowToBogotaProperty.ENTIDAD_ASOC.getName()+"	?x . ";
        }
        for(int i=0; i<calificativos.size();i++){
        	queryString=queryString+
        		"		uri:"+calificativos.get(i)+"	uri:"+HowToBogotaProperty.CALIFICATIVO_ASOC.getName()+"	?x . ";
        }
        queryString=queryString+
        		"      } LIMIT 1";

        Query query = QueryFactory.create(queryString);

    	QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
    	ResultSet results = qe.execSelect();
    	while(results.hasNext()){
    	QuerySolution qr= results.next();
    	res=qr.get("x").asResource().getLocalName();
    	}

		dataset.commit();
		TDB.sync(dataset);
		return res;
	}
	
    public String agregarPrimerPaso(String idPregunta, String descPaso){
		
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		
		Individual preg = getObject(idPregunta);

		String idPaso="paso"+darSiguienteId("idPasos");
		Individual step =createObject(HowToBogotaClass.PASOS, idPaso);
			
		setProperty(preg, HowToBogotaProperty.PREGUNTA_ASOC_RTA, step);

		Literal l = ontModel.createTypedLiteral(descPaso);
		setProperty(step, HowToBogotaProperty.TEXTO_PASO, l);
		Literal lNumCalificacion= ontModel.createTypedLiteral(0);
		setProperty(step, HowToBogotaProperty.NUM_CALIFICACIONES, lNumCalificacion);
	
		dataset.commit();
		TDB.sync(dataset);
		return idPaso;
    }
    
    
	public Step getNextStepNeighbor(String stepId){
		dataset.begin(ReadWrite.READ);
		ontModel= getOntModel();
		Step s=null;
		String idStep=null;
		String stepDesc=null;
		String idNext=null;
        String queryString = 
        		"PREFIX uri:<"+URI +">"+
        		"SELECT ?x ?next ?descrip " +
        		"WHERE {" +
        		"      uri:"+stepId+" uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" ?x . " +
        		"      ?x uri:"+HowToBogotaProperty.CALIFICACION.getName()+" ?cal . " +
        		"      ?x uri:"+HowToBogotaProperty.TEXTO_PASO.getName()+" ?descrip . " +
        		" OPTIONAL { ?x uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" ?next }"+
        		"} "+
        		"ORDER BY DESC(?cal) LIMIT 2"; 

        
      
        
        Query query = QueryFactory.create(queryString);
        
    	QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
    	ResultSet results = qe.execSelect();
    	int i =0;
    	boolean gotIn=false;
    	while(results.hasNext()){
    		gotIn=true;
    		QuerySolution qr= results.next();
    		if(i==0){
    			idStep=qr.getResource("x").getLocalName();
    			stepDesc=qr.getLiteral("descrip").getString();
    			if(qr.getResource("next")!=null){
    				idNext=qr.getResource("next").getLocalName();
    			}
    		}
    		i++;
    	}
    	
    	if(gotIn){
	    	if(idStep!=null){
	    		s= new Step(idStep, stepDesc);
	    		s.setPreviousStep(stepId);
	    		s.setNextStep(idNext);
	    		if(i>1){
	    			s.setDownStep(stepId);
	    		}
	    	}
	    	
    	}else{
    		//Ning�n paso tiene calificaci�n
    		queryString = 
            		"PREFIX uri:<"+URI +">"+
            		"SELECT ?x ?next ?descrip " +
            		"WHERE {" +
            		"      uri:"+stepId+" uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" ?x . " +
            		"      ?x uri:"+HowToBogotaProperty.TEXTO_PASO.getName()+" ?descrip . " +
            		" OPTIONAL { ?x uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" ?next }"+
            		"} "+
            		" LIMIT 2";        
          
            query = QueryFactory.create(queryString);

        	qe = QueryExecutionFactory.create(query, ontModel);
        	results = qe.execSelect();
        	i =0;
        	gotIn=false;
        	while(results.hasNext()){
        		gotIn=true;
        		QuerySolution qr= results.next();
        		if(i==0){
        			idStep=qr.getResource("x").getLocalName();
        			stepDesc=qr.getLiteral("descrip").getString();
        			if(qr.getResource("next")!=null){
        				idNext=qr.getResource("next").getLocalName();
        			}
        		}
        		i++;
        	}
        	
        	if(idStep!=null){
	    		s= new Step(idStep, stepDesc);
	    		s.setPreviousStep(stepId);
	    		s.setNextStep(idNext);
	    		if(i>1){
	    			s.setDownStep(stepId);
	    		}
	    	}
        	
    	}
		dataset.commit();
		TDB.sync(dataset);
		return s;
	}
	
	public Step getUpDownStepNeighbor(String stepId, boolean up){
		
		dataset.begin(ReadWrite.READ);
		ontModel= getOntModel();
		
		Step s=null;
		String order="DESC";
		if(!up){
			order="ASC";
		}
		//TODO: Tener en cuenta el predecesor. 
		//Por ahora se tienen en cuenta todos los predecesores
        String queryString = 
        		"PREFIX uri:<"+URI +">"+
        		"SELECT ?x ?next ?descrip " +
        		"WHERE {" +
        		"      ?pred uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" uri:"+stepId+" ." +
        		"      ?pred uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" ?x ." +
        		"      ?x uri:"+HowToBogotaProperty.TEXTO_PASO.getName()+" ?descrip . " +
        		"      ?x uri:"+HowToBogotaProperty.CALIFICACION.getName()+" ?cal . " + 
        		" OPTIONAL { ?x uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" ?next }"+
        		"} "+
        		"ORDER BY "+order+"(?cal)"; 
        
        Query query = QueryFactory.create(queryString);

    	QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
    	ResultSet results = qe.execSelect();
    	
		String idStep=null;
		String stepDesc=null;
		String idNext=null;
    	boolean gotIn=false;
    	int i=0;
    	while(results.hasNext()){
    		gotIn=true;
    		QuerySolution qr= results.next();
    		String rtaId=qr.getResource("x").getLocalName();
    		if(rtaId.equals(stepId)){
    			break;
    		}else{
    			idStep=rtaId;
    			stepDesc=qr.getLiteral("descrip").getString();
    			if(qr.getResource("next")!=null){
    				idNext=qr.getResource("next").getLocalName();
    			}
    		}
    		i++;
    	}
    	if(gotIn){
    		if(idStep!=null){
	    		s= new Step(idStep, stepDesc);
	    		s.setPreviousStep(stepId);
	    		s.setNextStep(idNext);
	    		if(up){
	    			s.setDownStep(stepId);
	    			if(i>2){
		    			s.setUpStep(stepId);
		    		}
	    		}else{
	    			s.setUpStep(stepId);
	    			if(i>2){
		    			s.setDownStep(stepId);
		    		}
	    		}
	    		
	    	}
    	}if(!gotIn || idStep==null){
    		//Ning�n paso tiene calificaci�n
    		//Entrega un paso aleatorio 
    		queryString = 
            		"PREFIX uri:<"+URI +">"+
            		"SELECT ?x ?next ?descrip "  +
            		"WHERE {" +
            		"      ?pred uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" uri:"+stepId+" ." +
            		"      ?pred uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" ?x ." +
            		"      ?x uri:"+HowToBogotaProperty.TEXTO_PASO.getName()+" ?descrip . " +
            		" OPTIONAL { ?x uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" ?next }"+
            		"} LIMIT 3"; 
            
            query = QueryFactory.create(queryString);

        	qe = QueryExecutionFactory.create(query, ontModel);
        	results = qe.execSelect();
        	gotIn=false;
        	i=0;
        	while(results.hasNext()){
        		gotIn=true;
        		QuerySolution qr= results.next();
        		String rtaId=qr.getResource("x").getLocalName();
        		if(!rtaId.equals(stepId)){
        			idStep=rtaId;
        			stepDesc=qr.getLiteral("descrip").getString();
        			if(qr.getResource("next")!=null){
        				idNext=qr.getResource("next").getLocalName();
        			}
        		}
        		i++;
        	}
        	
        	if(gotIn){
        		if(idStep!=null){
    	    		s= new Step(idStep, stepDesc);
    	    		s.setPreviousStep(stepId);
    	    		s.setNextStep(idNext);
    	    		if(up){
    	    			s.setDownStep(stepId);
    	    			if(i>2){
    		    			s.setUpStep(stepId);
    		    		}
    	    		}else{
    	    			s.setUpStep(stepId);
    	    			if(i>2){
    		    			s.setDownStep(stepId);
    		    		}
    	    		}
    	    		
    	    	}
        	}
    	}
		dataset.commit();
		TDB.sync(dataset);
		return s;

	}
	
	public Step getPrevStepNeighbor(String stepId){
		
		dataset.begin(ReadWrite.READ);
		ontModel= getOntModel();
		
		
		//TODO: Tener en cuenta que puede tener muchos predecesores el predecesor. 
		//Por ahora se escoge el predecesor con mayor calificaci�n
        String queryString = 
        		"PREFIX uri:<"+URI +">"+
        		"SELECT ?x ?descrip " +
        		"WHERE {" +
        		"      ?x uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" uri:"+stepId+" ." +
        		"      ?x uri:"+HowToBogotaProperty.CALIFICACION.getName()+" ?cal . " + 
        		"      ?x uri:"+HowToBogotaProperty.TEXTO_PASO.getName()+" ?descrip . " +
        		"} "+
        		"ORDER BY DESC(?cal) LIMIT 1"; 
        
        Query query = QueryFactory.create(queryString);

    	QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
    	ResultSet results = qe.execSelect();
    	String idStep=null;
		String stepDesc=null;
    	Step s=null;
    	while(results.hasNext()){
    		QuerySolution qr= results.next();
    		idStep=qr.getResource("x").getLocalName();
    		stepDesc=qr.getLiteral("descrip").getString();
    	}
    	
    	if(idStep!=null){
    		s= new Step(idStep, stepDesc);
    		s.setNextStep(stepId);
    	}else{
    		queryString = 
            		"PREFIX uri:<"+URI +">"+
            		"SELECT ?x ?descrip " +
            		"WHERE {" +
            		"      ?x uri:"+HowToBogotaProperty.PASO_ASOC_A.getName()+" uri:"+stepId+" ." +
            		"      ?x uri:"+HowToBogotaProperty.TEXTO_PASO.getName()+" ?descrip . " +
            		"} "+
            		"LIMIT 1"; 
            
            query = QueryFactory.create(queryString);

        	qe = QueryExecutionFactory.create(query, ontModel);
        	results = qe.execSelect();
        	idStep=null;
    		stepDesc=null;
        	s=null;
        	while(results.hasNext()){
        		QuerySolution qr= results.next();
        		idStep=qr.getResource("x").getLocalName();
        		stepDesc=qr.getLiteral("descrip").getString();
        	}
        	
        	if(idStep!=null){
        		s= new Step(idStep, stepDesc);
        		s.setNextStep(stepId);
        	}
    	}
    	
		dataset.commit();
		TDB.sync(dataset);
		return s;

	}
	
	
	public String addStep(String previousStepId, String nextStepId, String stepDescription) {
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		
		//Obtener y actualizar los id de pasos
		String idPaso="paso"+darSiguienteId("idPasos");
		
		//Crear el paso
		Individual pasoA =createObject(HowToBogotaClass.PASOS, idPaso);
		Literal lDesc = ontModel.createTypedLiteral(stepDescription);
		setProperty(pasoA, HowToBogotaProperty.TEXTO_PASO, lDesc);
	
		Literal lNumCalificacion= ontModel.createTypedLiteral(0);
		setProperty(pasoA, HowToBogotaProperty.NUM_CALIFICACIONES, lNumCalificacion);
		
		//Hacer el enlace con su paso anterior
		Individual prePaso=getObject(previousStepId);
		setProperty(prePaso, HowToBogotaProperty.PASO_ASOC_A, pasoA );
		
		if(nextStepId!=null && !nextStepId.equals("")){
			//Hacer el enlace con el paso siguiente
			Individual sgPaso=getObject(nextStepId);
			setProperty(pasoA, HowToBogotaProperty.PASO_ASOC_A, sgPaso );
		}
		
		dataset.commit();
		TDB.sync(dataset);
		return idPaso;
  }
	
	private int darSiguienteId(String elemento){
		Individual contPasos=getObject(elemento);
		OntProperty  pCantidad= ontModel.getOntProperty( URI +HowToBogotaProperty.CANTIDAD.getName());
		Statement cantidad=contPasos.getProperty(pCantidad);
		int cantPasos=cantidad.getInt();
		contPasos.removeProperty(pCantidad, cantidad.getObject());
		cantPasos++;
		Literal l= ontModel.createTypedLiteral(cantPasos);
		contPasos.addProperty(pCantidad, l);
		return cantPasos;
	}
	
	public void calificarCamino(String[] pasos, double calificacion){
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		for(int i=0; i< pasos.length;i++){
			String idPaso=pasos[i];
			Individual paso=getObject(idPaso);
			
			OntProperty  pCalificacion= ontModel.getOntProperty( URI +HowToBogotaProperty.CALIFICACION.getName());
			Statement sCalificacion=paso.getProperty(pCalificacion);
			OntProperty  pNumCalificacion= ontModel.getOntProperty( URI +HowToBogotaProperty.NUM_CALIFICACIONES.getName());

			if(sCalificacion!=null){
				float curCalificacion=sCalificacion.getFloat();
				
				Statement sNumCalificacion=paso.getProperty(pNumCalificacion);
				int numCalificaciones=sNumCalificacion.getInt();
				
				paso.removeProperty(pCalificacion, sCalificacion.getObject());
				paso.removeProperty(pNumCalificacion, sNumCalificacion.getObject());
				
				//Se recalcula todo
				numCalificaciones++;
				curCalificacion=(((float)numCalificaciones-1)/(float)numCalificaciones)*curCalificacion+(1/(float)numCalificaciones)*(float)calificacion;
				
				Literal lCurCalificacion= ontModel.createTypedLiteral(curCalificacion);
				paso.addProperty(pCalificacion, lCurCalificacion);
				
				Literal lNumCalificacion= ontModel.createTypedLiteral(numCalificaciones);
				paso.addProperty(pNumCalificacion, lNumCalificacion);
			}else{
								Statement sNumCalificacion=paso.getProperty(pNumCalificacion);
				if(sNumCalificacion!=null){
					paso.removeProperty(pNumCalificacion, sNumCalificacion.getObject());
				}
				float curCalificacion=(float)calificacion;
				int numCalificaciones=1;
				
				Literal lCurCalificacion= ontModel.createTypedLiteral(curCalificacion);
				paso.addProperty(pCalificacion, lCurCalificacion);
				
				Literal lNumCalificacion= ontModel.createTypedLiteral(numCalificaciones);
				paso.addProperty(pNumCalificacion, lNumCalificacion);
			}
		}
		dataset.commit();
		TDB.sync(dataset);
	}
	
	
	public boolean esValido(String verbo, String calificativo){
		
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		
		boolean resp=true;
		Individual iVerbo=getObject(verbo);
		Individual iCalif=getObject(calificativo);
		OntProperty  pVerboComp= ontModel.getOntProperty( URI +HowToBogotaProperty.VERBO_COMP_POR.getName());
		if(iVerbo!=null && iCalif!=null){
			
			resp=iVerbo.hasProperty(pVerboComp, iCalif);
	
			if (!resp) {
				
		        String queryString = 
		        		"PREFIX uri:<"+URI +">"+
		        		"SELECT ?x " +
		        		"WHERE {" +
		        		"      ?x uri:"+HowToBogotaProperty.VERBO_COMP_POR.getName()+" uri:"+calificativo+" ." +
		        		"{"+
		        		"      ?x uri:"+HowToBogotaProperty.VERBO_SINON.getName()+"  uri:"+verbo+" . " + 
		        		"}UNION{"+
		        		"      uri:"+verbo+" uri:"+HowToBogotaProperty.VERBO_SINON.getName()+" ?x  . " + 
		        		"}"+
		        		"} ";
		        
		        Query query = QueryFactory.create(queryString);
		
		    	QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
		    	ResultSet results = qe.execSelect();
		    	if(results.hasNext()){
		    		resp=true;
		    	}
			}					

		}else{
			//Crearlos
			if(iVerbo==null){
				iVerbo =createObject(HowToBogotaClass.VERBOS, verbo);
			}
//			if(iCalif==null){
//				iCalif =createObject(HowToBogotaClass.CALIFICATIVOS, calificativo);
//			}
//			
//			iVerbo.addProperty(pVerboComp, iCalif);
		}
		dataset.commit();
		TDB.sync(dataset);
		return resp;
	}

	public void agregarSinonimoVerbos(String verbo1, String verbo2){
		
		dataset.begin(ReadWrite.WRITE);
		ontModel= getOntModel();
		
		boolean resp=true;
		Individual iVerbo1=getObject(verbo1);
		Individual iVerbo2=getObject(verbo2);
		OntProperty  pVerboSino= ontModel.getOntProperty( URI +HowToBogotaProperty.VERBO_SINON.getName());
		
		iVerbo1.addProperty(pVerboSino, iVerbo2);
		iVerbo2.addProperty(pVerboSino, iVerbo1);
		dataset.commit();
		TDB.sync(dataset);
	}
}

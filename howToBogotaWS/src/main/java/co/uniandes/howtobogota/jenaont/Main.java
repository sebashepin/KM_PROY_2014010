/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Package
///////////////
package co.uniandes.howtobogota.jenaont;
import java.util.ArrayList;
import java.util.Iterator;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;


public class Main {
	
	public static final String ONTOLOGY_DATA_FILE = "file:models/howToBogota.owl";

    public static void main( String[] args ) {

        String line = "Where can I buy cheap shoes";
        POSTaggerAnswer tg=new POSTaggerAnswer(line);
    	
    	OntologyManager instance = OntologyManager.darInstancia();
//    	instance.agregarPregunta("pregunta1", line, tg.getVerbs(), tg.getEntities(), tg.getAdjectives());
//    	
//    	instance.agregarUsuario("us1", "Usuario1");
//    	
//    	instance.agregarCalificacionUsuario("us1", "Respuesta34209", 3);
//    	
//    	line = "Go on a bus, choose your shoes, buy them";
//        tg=new POSTaggerAnswer(line);
//    	ArrayList<String> pasos= new ArrayList<String>();
//    	pasos.add("Paso 1");
//    	pasos.add("Paso 2");
//    	pasos.add("Paso 3");
//    	
//    	instance.agregarRespuesta("rta1", "pregunta1", pasos, tg.getVerbs(), tg.getEntities(), tg.getAdjectives());
//    	pasos= new ArrayList<String>();
//    	pasos.add("Paso 4");
//    	pasos.add("Paso 5");
//    	pasos.add("Paso 6");
//    	
//    	instance.agregarRespuesta("rta2", "pregunta1", pasos, tg.getVerbs(), tg.getEntities(), tg.getAdjectives());
    	
    	instance.darRespuesta("pregunta1");
    	
    	Dataset d=instance.getDataset();
    	d.begin(ReadWrite.READ);
    	OntModel m= instance.getOntModel();
    	String camNS = "http://www.semanticweb.org/ontologies/2014/8/howToBogota.owl#";
    	Resource r = m.getResource( camNS + "Respuestas" );
        OntClass respuestas = r.as(OntClass.class);
        for (Iterator<? extends OntResource> i = respuestas.listInstances(true); i.hasNext(); ) {
        	OntResource o=i.next() ;
        	  System.out.println( "Instancia"+ o );
        	  for (Iterator<Statement> a = o.listProperties(); a.hasNext(); ) {
              	Statement s = a.next();

              	  System.out.println( "Property"+ s);
                }
          }
        
        r = m.getResource( camNS + "Preguntas" );
        respuestas = r.as(OntClass.class);
        for (Iterator<? extends OntResource> i = respuestas.listInstances(true); i.hasNext(); ) {
        	OntResource o=i.next() ;
        	  System.out.println( "Instancia"+ o );
        	  for (Iterator<Statement> a = o.listProperties(); a.hasNext(); ) {
              	Statement s = a.next();

              	  System.out.println( "Property"+ s);
                }
          }
        
         r = m.getResource( camNS + "Pasos" );
         respuestas = r.as(OntClass.class);
        for (Iterator<? extends OntResource> i = respuestas.listInstances(true); i.hasNext(); ) {
        	OntResource o=i.next() ;
        	  System.out.println( "Instancia Pasos"+ o );
        	  for (Iterator<Statement> a = o.listProperties(); a.hasNext(); ) {
              	Statement s = a.next();

              	  System.out.println( "Property"+ s);
                }
          }
    }

    public static void testMain(){
        String source = ONTOLOGY_DATA_FILE;
        
        //Se crea un modelo con el lenguaje OWLFull (característica de la ontología), con un modelo de storage in memory
        //y con un RBS con reglas OWL (Sea lo que sea lo que eso signifique)
        //TODO Revisar si con otro tipo de razonadores se podrían obtener mejores/diferentes resultados
        OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_RULE_INF );
        //Se lee la ontología
        m.read( source );
        
        //Obtener una clase
        String camNS = "http://www.semanticweb.org/ontologies/2014/8/howToBogota.owl#";
        
        Resource r = m.getResource( camNS + "Respuestas" );
        OntClass respuestas = r.as(OntClass.class);
        
        //Obtener las superclases de una clase
        for (Iterator<OntClass> i = respuestas.listSuperClasses(true); i.hasNext(); ) {
        	  OntClass c = i.next();
        	  System.out.println( c.getURI() );
        }
        
        //Crear un individuo de la clase, pero no guardarlo ):
        Individual p1=respuestas.createIndividual(camNS + "rta1");
        //Obtener un individuo
        Individual p2=m.getIndividual(camNS + "Respuesta34209");
        
        //Indicar de qué tipo es el individo
        for (Iterator<Resource> i = p1.listRDFTypes(true); i.hasNext(); ) {
            System.out.println( p1.getURI() + " is asserted in class " + i.next() );
        }
        
        //Dar todas las instacias de una clase (No da las instancias indirectas. Ej: Si busco respuestas, no me da instancias de pasos)
        for (Iterator<? extends OntResource> i = respuestas.listInstances(true); i.hasNext(); ) {
      	  System.out.println( "Instancia"+ i.next() );
        }
        
        //Crear propiedad
        Individual daniel= m.getIndividual(camNS+"Daniel");
        OntProperty p = m.getOntProperty( camNS +"calificado_por" );
        daniel.addProperty(p,p1);
        
        //Dar las propiedades de un individuo
        for (Iterator<Statement> i = p1.listProperties(); i.hasNext(); ) {
        	Statement s = i.next();

        	  System.out.println( "Property"+ s);
          }
        
         r = m.getResource( camNS + "Usuarios" );
        OntClass us = r.as(OntClass.class);
      //Dar todas las instacias de una clase (No da las instancias indirectas. Ej: Si busco respuestas, no me da instancias de pasos)
        for (Iterator<? extends OntResource> i = us.listInstances(); i.hasNext(); ) {
      	  System.out.println( "Instancia"+ i.next() );
        }
        
        Resource pregs = m.getResource( camNS + "Preguntas" );
        OntClass pregts = pregs.as(OntClass.class);
        Individual preg1=pregts.createIndividual(camNS + "prg1");
        Individual verbo= m.getIndividual(camNS+"Comprar");
        OntProperty property = m.getOntProperty( camNS +"verbo_asociado" );
        verbo.addProperty(property, preg1);
        
        r = m.getResource( camNS + "Verbos" );
       OntClass pr = r.as(OntClass.class);
     //Dar todas las instacias de una clase (No da las instancias indirectas. Ej: Si busco respuestas, no me da instancias de pasos)
       for (Iterator<? extends OntResource> i = pr.listInstances(); i.hasNext(); ) {
     	  System.out.println( "InstanciaPregs"+ i.next() );
       }
        //describeClass(m);
        
        //saveModelTDB( m );
        
/*
        OntProperty p = m.getOntProperty( camNS +"Compuesto_de" );
        ResIterator iter = m.listSubjectsWithProperty(p);
        if (iter.hasNext()) {
            System.out.println("The database contains vcards for:");
            while (iter.hasNext()) {
                System.out.println("  " + iter.nextResource()
                                              );
            }
        } else {
            System.out.println("No vcards were found in the database");
        }
        */

    }
    
    public static void saveModelTDB(OntModel m ){

        Dataset dataset = TDBFactory.createDataset("database/");
        long n = System.currentTimeMillis();
        dataset.begin(ReadWrite.READ) ;
		//dataset.addNamedModel("howToBogota", m);
    	
		 
		 Model model = dataset.getNamedModel("howToBogota");

		 OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF, model);
		 System.out.println(System.currentTimeMillis()-n);
			
			
			
		 //Obtener una clase
	        String camNS = "http://www.semanticweb.org/ontologies/2014/8/howToBogota.owl#";
	        Resource r = ontModel.getResource( camNS + "Pasos" );
	        OntClass respuestas = r.as(OntClass.class);
	     //  Individual p1=respuestas.createIndividual(camNS + "rta3");
	        
	      //Dar todas las instacias de una clase (No da las instancias indirectas. Ej: Si busco respuestas, no me da instancias de pasos)
	        for (Iterator<? extends OntResource> i = respuestas.listInstances(true); i.hasNext(); ) {
	      	  System.out.println( "Instancia"+ i.next() );
	        }
 		 dataset.commit();
		TDB.sync(dataset ) ;
       
        
       
    }
    
    /**
    public static void describeClass(OntModel m){
        //Utilidad para determinar qué se está leyendo
        DescribeClass dc = new DescribeClass();
        for (Iterator<OntClass> i = m.listClasses();  i.hasNext(); ) {
            // now list the classes
        	
            dc.describeClass( System.out, i.next() );
        }
    }*/

}

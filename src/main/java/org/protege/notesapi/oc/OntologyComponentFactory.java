package org.protege.notesapi.oc;

import org.protege.notesapi.NotesException;
import org.protege.notesapi.oc.impl.*;
import org.protege.notesapi.util.OWLUtil;
import org.semanticweb.owlapi.model.*;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/** @author csnyulas */
public class OntologyComponentFactory {

    static {
        OntologyJavaMapping.initMap();
    }

    private static final boolean INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE = false;

    private static final boolean INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS = true;

    private static final boolean TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS = true;

    private OWLOntology ontology;

    private OWLOntologyManager manager;

    public OntologyComponentFactory(OWLOntology ontology) throws NotesException {
        if (ontology != null) {
            this.ontology = ontology;
            this.manager = ontology.getOWLOntologyManager();
        }
        else {
            throw new NotesException("" );
        }
    }

    ////**************** Utility methods ****************////

    /**
     * Returns an OWLNamedIndividual corresponding to the argument.
     * This method always returns a non-null value, but this does not
     * mean that the returned individual is contained in the ontology.
     *
     * @param iri
     * @return
     */
    private OWLNamedIndividual getOWLNamedIndividual(IRI iri) {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLNamedIndividual(iri);
    }


//	/**
//	 * @deprecated Check if this method makes sense and it could be useful in some situation.
//	 */
//    private OWLNamedIndividual getOWLIndividual(String name, OWLClass type) {
//    	OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
//    	Set<OWLIndividualAxiom> axioms = ontology.getAxioms(individual);
//    	for (OWLIndividualAxiom axiom : axioms) {
//    		if (axiom.isOfType(AxiomType.CLASS_ASSERTION) && 
//    				axiom.getClassesInSignature().contains(type)) {
//    			return individual;
//    		}
//    	}
//    	
//    	return null;
//	}

    private OWLNamedIndividual getOWLIndividual(String name) {
        URI uri = URI.create(name);    //this may throw a NullPointerException (if argument is null) OR an IllegalArgumentException (if argument violates RFC 2396)
        return getOWLNamedIndividual(IRI.create(uri));
    }


    ////**************** OntologyComponent ****************////


    public OWLClass getOntologyComponentClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Ontology_Component" ));
    }
//	
//	public OntologyComponent createOntologyComponent(String name) {
//		OWLClass typeClass = getOntologyComponentClass();
//		OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
//		return OntologyJavaMappingUtil.getSpecificObject(ontology, individual, OntologyComponent.class);
//	}


    public OntologyComponent getOntologyComponent(String name) {
        OWLClass typeClass = getOntologyComponentClass();
//    	OWLNamedIndividual individual = getOWLIndividual(name, typeClass);
//    	return OntologyJavaMappingUtil.getSpecificObject(ontology, individual, OntologyComponent.class);
        OWLNamedIndividual individual = getOWLIndividual(name);
        Set<OWLClass> assertedTypes = OWLUtil.getAssertedTypes(
                individual, ontology, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE);
        if (!assertedTypes.contains(typeClass)) {
            OWLUtil.assertTypeOfIndividual(manager, ontology, individual, typeClass);
        }
        return new DefaultOntologyComponent(individual, ontology);
    }

    public Set<OntologyComponent> getAllOntologyComponentObjects() {
        return getAllOntologyComponentObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<OntologyComponent> getAllOntologyComponentObjects(boolean transitive,
                                                                 boolean includeImportsClosure) {
        final OWLClass typeClass = getOntologyComponentClass();

        Set<OntologyComponent> result = new HashSet<OntologyComponent>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, ontology, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
//			result.add(OntologyJavaMappingUtil.getSpecificObject(ontology, individual, OntologyComponent.class));
            result.add(new DefaultOntologyComponent(individual, ontology));
        }
        return result;
    }


    //TODO implement the complete set of methods once we know exactly the desired functionality


    ////**************** OntologyClass ****************////


    public OWLClass getOntologyClassClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Ontology_Class" ));
    }

    public OntologyClass getOntologyClass(String name) {
        OWLClass typeClass = getOntologyClassClass();
        OWLNamedIndividual individual = getOWLIndividual(name);
        Set<OWLClass> assertedTypes = OWLUtil.getAssertedTypes(
                individual, ontology, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE);
        if (!assertedTypes.contains(typeClass)) {
            OWLUtil.assertTypeOfIndividual(manager, ontology, individual, typeClass);
        }
        return new DefaultOntologyClass(individual, ontology);
    }

    public Set<OntologyClass> getAllOntologyClassObjects() {
        return getAllOntologyClassObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<OntologyClass> getAllOntologyClassObjects(boolean transitive,
                                                         boolean includeImportsClosure) {
        final OWLClass typeClass = getOntologyClassClass();

        Set<OntologyClass> result = new HashSet<OntologyClass>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, ontology, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(new DefaultOntologyClass(individual, ontology));
        }
        return result;
    }


    ////**************** OntologyProperty ****************////


    public OWLClass getOntologyPropertyClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Ontology_Property" ));
    }

    public OntologyProperty getOntologyProperty(String name) {
        OWLClass typeClass = getOntologyPropertyClass();
        OWLNamedIndividual individual = getOWLIndividual(name);
        Set<OWLClass> assertedTypes = OWLUtil.getAssertedTypes(
                individual, ontology, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE);
        if (!assertedTypes.contains(typeClass)) {
            OWLUtil.assertTypeOfIndividual(manager, ontology, individual, typeClass);
        }
        return new DefaultOntologyProperty(individual, ontology);
    }

    public Set<OntologyProperty> getAllOntologyPropertyObjects() {
        return getAllOntologyPropertyObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<OntologyProperty> getAllOntologyPropertyObjects(boolean transitive,
                                                               boolean includeImportsClosure) {
        final OWLClass typeClass = getOntologyPropertyClass();

        Set<OntologyProperty> result = new HashSet<OntologyProperty>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, ontology, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(new DefaultOntologyProperty(individual, ontology));
        }
        return result;
    }


    ////**************** OntologyIndividual ****************////


    public OWLClass getOntologyIndividualClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Ontology_Individual" ));
    }

    public OntologyIndividual getOntologyIndividual(String name) {
        OWLClass typeClass = getOntologyIndividualClass();
        OWLNamedIndividual individual = getOWLIndividual(name);
        Set<OWLClass> assertedTypes = OWLUtil.getAssertedTypes(
                individual, ontology, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE);
        if (!assertedTypes.contains(typeClass)) {
            OWLUtil.assertTypeOfIndividual(manager, ontology, individual, typeClass);
        }
        return new DefaultOntologyIndividual(individual, ontology);
    }

    public Set<OntologyIndividual> getAllOntologyIndividualObjects() {
        return getAllOntologyIndividualObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<OntologyIndividual> getAllOntologyIndividualObjects(boolean transitive,
                                                                   boolean includeImportsClosure) {
        final OWLClass typeClass = getOntologyIndividualClass();

        Set<OntologyIndividual> result = new HashSet<OntologyIndividual>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, ontology, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(new DefaultOntologyIndividual(individual, ontology));
        }
        return result;
    }


    ////**************** Ontology ****************////


    public OWLClass getOntologyClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Ontology" ));
    }

    public Ontology getOntology(String name) {
        OWLClass typeClass = getOntologyClass();
        OWLNamedIndividual individual = getOWLIndividual(name);
        Set<OWLClass> assertedTypes = OWLUtil.getAssertedTypes(
                individual, ontology, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE);
        if (!assertedTypes.contains(typeClass)) {
            OWLUtil.assertTypeOfIndividual(manager, ontology, individual, typeClass);
        }
        return new DefaultOntology(individual, ontology);
    }

    public Set<Ontology> getAllOntologyObjects() {
        return getAllOntologyObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<Ontology> getAllOntologyObjects(boolean transitive,
                                               boolean includeImportsClosure) {
        final OWLClass typeClass = getOntologyClass();

        Set<Ontology> result = new HashSet<Ontology>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, ontology, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(new DefaultOntology(individual, ontology));
        }
        return result;
    }


}

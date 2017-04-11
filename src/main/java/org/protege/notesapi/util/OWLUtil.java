package org.protege.notesapi.util;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.*;

/** @author csnyulas */
public class OWLUtil {

    // ---------------- conversion methods ----------------

    public static String getStringValue(OWLLiteral literal) {
        return literal == null ? null : literal.getLiteral();
    }

    public static Boolean getBooleanValue(OWLLiteral literal) {
        return literal == null ? null : Boolean.parseBoolean(literal.getLiteral());
    }

    public static Double getDoubleValue(OWLLiteral literal) {
        return literal == null ? null : Double.parseDouble(literal.getLiteral());
    }

    public static Float getFloatValue(OWLLiteral literal) {
        return literal == null ? null : Float.parseFloat(literal.getLiteral());
    }

    public static Integer getIntegerValue(OWLLiteral literal) {
        return literal == null ? null : Integer.parseInt(literal.getLiteral());
    }

    public static Long getLongValue(OWLLiteral literal) {
        return literal == null ? null : Long.parseLong(literal.getLiteral());
    }

    // ---------------- basic reasoning methods ----------------


    //--- calculate closures ---

    public static Set<OWLClass> calculateSublassClosure(OWLClass annotationClass,
                                                        OWLOntology ontology, boolean includeImportsClosure) {
        Set<OWLClass> res = new HashSet<OWLClass>();
        Set<OWLSubClassOfAxiom> axioms = ontology.getAxioms(AxiomType.SUBCLASS_OF, includeImportsClosure);
        recursiveSubclassTree(annotationClass, axioms, res);
        return res;
    }

    private static void recursiveSubclassTree(OWLClass currClass,
                                              Set<OWLSubClassOfAxiom> axioms, Set<OWLClass> visited) {
        visited.add(currClass);
        Set<OWLClass> currSubClasses = new HashSet<OWLClass>();
        for (OWLSubClassOfAxiom axiom : axioms) {
            if (axiom.getSuperClass().equals(currClass)) {
                OWLClassExpression subClass = axiom.getSubClass();
                if (subClass instanceof OWLClass
                        && (!visited.contains(subClass))) {
                    currSubClasses.add((OWLClass) subClass);
                }
            }
        }
        for (OWLClass subClass : currSubClasses) {
            recursiveSubclassTree(subClass, axioms, visited);
        }
    }


    public static List<OWLClass> calculateSuperclassClosure(OWLClass annotationClass,
                                                            OWLOntology ontology, boolean includeImportsClosure) {
        List<OWLClass> res = new ArrayList<OWLClass>();
        Set<OWLSubClassOfAxiom> axioms = ontology.getAxioms(AxiomType.SUBCLASS_OF, includeImportsClosure);
        recursiveSuperclassTree(annotationClass, axioms, res);
        return res;
    }

    private static void recursiveSuperclassTree(OWLClass currClass,
                                                Set<OWLSubClassOfAxiom> axioms, List<OWLClass> visited) {
        visited.add(currClass);
        Set<OWLClass> currSuperClasses = new HashSet<OWLClass>();
        for (OWLSubClassOfAxiom axiom : axioms) {
            if (axiom.getSubClass().equals(currClass)) {
                OWLClassExpression superClass = axiom.getSuperClass();
                if (superClass instanceof OWLClass
                        && (!visited.contains(superClass))) {
                    currSuperClasses.add((OWLClass) superClass);
                }
            }
        }
        for (OWLClass superClass : currSuperClasses) {
            recursiveSuperclassTree(superClass, axioms, visited);
        }
    }


    //--- get asserted type(s) ---

    public static Set<OWLClass> getAssertedDirectTypes(OWLIndividual individual,
                                                       OWLOntology ontology,
                                                       boolean includeImportsClosure) {
        Set<OWLOntology> ontologies = includeImportsClosure ? ontology.getImportsClosure() : Collections.singleton(
                ontology);
        Set<OWLClass> result = new HashSet<OWLClass>();
        for (OWLClassExpression ce : EntitySearcher.getTypes(individual, ontologies)) {
            if (!ce.isAnonymous()) {
                result.add(ce.asOWLClass());
            }
        }
        return result;
    }

    public static Set<OWLClass> getAssertedTypes(OWLIndividual individual,
                                                 OWLOntology ontology, boolean includeImportsClosure) {
        Set<OWLClass> directTypes = getAssertedDirectTypes(individual, ontology, includeImportsClosure);
        Set<OWLClass> res = new HashSet<OWLClass>();
        for (OWLClass typeClass : directTypes) {
            if (!res.contains(typeClass)) {
                res.addAll(calculateSuperclassClosure(typeClass, ontology, includeImportsClosure));
            }
        }
        return res;
    }


    //--- get asserted individuals ---

    public static Set<OWLNamedIndividual> getAssertedIndividuals(OWLClass owlClass,
                                                                 OWLOntology ontology,
                                                                 boolean includeImportsClosure,
                                                                 boolean transitive) {
        Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();
        if (transitive) {
            Set<OWLClass> owlClasses = OWLUtil.calculateSublassClosure(owlClass, ontology, includeImportsClosure);
            Set<OWLClassAssertionAxiom> axioms = ontology.getAxioms(AxiomType.CLASS_ASSERTION, includeImportsClosure);
            for (OWLClassAssertionAxiom axiom : axioms) {
                Set<OWLClass> classesInSignature = axiom.getClassesInSignature();
                classesInSignature.retainAll(owlClasses); // intersect
                if (!classesInSignature.isEmpty()) {
                    OWLIndividual individual = axiom.getIndividual();
                    if (individual.isNamed()) {
                        result.add(individual.asOWLNamedIndividual());
                    }
                }
            }
            return result;
        }
        else {
            //if not transitive, choose the easier solution of getting the individuals of the owlClass directly
            Set<OWLOntology> ontologies = includeImportsClosure ? ontology.getImportsClosure() : Collections.singleton(
                    ontology);
            for (OWLIndividual individual : EntitySearcher.getIndividuals(owlClass, ontologies)) {
                if (individual.isNamed()) {
                    result.add(individual.asOWLNamedIndividual());
                }
            }
            return result;
        }
    }

    //--- get property values ----

    public static Set<OWLIndividual> getPropertyValues(OWLNamedIndividual individual, OWLObjectProperty property,
                                                       OWLOntology ontology, boolean includeImportsClosure) {

        Set<OWLOntology> ontologies = includeImportsClosure ?
                ontology.getOWLOntologyManager().getImportsClosure(ontology) :
                Collections.singleton(ontology);

        //first add all the values asserted directly through the property
        Set<OWLIndividual> res = new HashSet<OWLIndividual>();
        for (OWLOntology importedOntology : ontologies) {
            res.addAll(EntitySearcher.getObjectPropertyValues(individual, property, importedOntology));
        }

        //calculate the list of inverse properties
        Collection<OWLObjectPropertyExpression> inverseProperties = EntitySearcher.getInverses(property, ontologies);
        //if property does have inverse properties
        if (!inverseProperties.isEmpty()) {
            Set<OWLAxiom> axioms = ontology.getReferencingAxioms(individual, includeImportsClosure);
            for (OWLAxiom axiom : axioms) {
                if (axiom.getAxiomType().equals(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
                    OWLObjectPropertyAssertionAxiom objPropAxiom = (OWLObjectPropertyAssertionAxiom) axiom;
                    if (inverseProperties.contains(objPropAxiom.getProperty()) &&
                            objPropAxiom.getObject().equals(individual)) {
                        res.add(objPropAxiom.getSubject());
                    }
                }
            }
        }

        return res;
    }

    public static void assertTypeOfIndividual(OWLOntologyManager manager, OWLOntology ontology,
                                              OWLNamedIndividual individual, OWLClass type) {
        OWLAxiom createAnnotationAxiom = manager.getOWLDataFactory().getOWLClassAssertionAxiom(type, individual);
        AddAxiom addAxiom = new AddAxiom(ontology, createAnnotationAxiom);
        manager.applyChange(addAxiom);
    }
}

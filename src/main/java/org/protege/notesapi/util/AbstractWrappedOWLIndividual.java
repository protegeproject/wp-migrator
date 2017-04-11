package org.protege.notesapi.util;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.io.Serializable;
import java.util.*;

/** @author csnyulas */
public class AbstractWrappedOWLIndividual implements Serializable {

    private static final long serialVersionUID = 4068780852583017785L;


    protected OWLOntology ontology;

    private OWLNamedIndividual wrappedProtegeIndividual;

    private boolean includeImportsClosure = false;

    protected boolean inLogMode = false;


    protected AbstractWrappedOWLIndividual(OWLNamedIndividual individual, OWLOntology ontology) {
        this(individual, ontology, true);
    }

    protected AbstractWrappedOWLIndividual(OWLNamedIndividual individual,
                                           OWLOntology ontology, boolean includeImportsClosure) {
        this.wrappedProtegeIndividual = individual;
        this.ontology = ontology;
        this.includeImportsClosure = includeImportsClosure;
    }

    public OWLNamedIndividual getWrappedOWLIndividual() {
        return this.wrappedProtegeIndividual;
    }

    /**
     * TODO check if this method is is of any use, and if not get rid of it.
     *
     * @deprecated temporarily deprecated to warn about its usage
     */
    public OWLOntology getOWLOntology() {
        return ontology;
    }

    /**
     * TODO check if this method is is of any use, and if not get rid of it.
     *
     * @deprecated temporarily deprecated to warn about its usage
     */
    public String getName() {
        return (wrappedProtegeIndividual == null ? null : wrappedProtegeIndividual.getIRI().toString());
    }


    protected IRI getIRI() {
        return (wrappedProtegeIndividual == null ? null : wrappedProtegeIndividual.getIRI());
    }


    public void delete() {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontology));
        wrappedProtegeIndividual.accept(remover);
        manager.applyChanges(remover.getChanges());

        //and the buggy implementation...
//		Set<OWLAxiom> axioms = wrappedProtegeIndividual.getReferencingAxioms(ontology, true);
//		List<OWLOntologyChange> removeAxioms = new ArrayList<OWLOntologyChange>();
//		for (OWLAxiom axiom : axioms) {
//			removeAxioms.addAll( manager.removeAxiom(ontology, axiom) );
//		}
//		manager.applyChanges(removeAxioms);
    }


    public void logCreationEvent() {
//		//overriding subclasses should be written like this
//		inLogMode = true;
//		//do something
//		inLogMode = false;
    }


    public void logModificationEvent() {
//		//overriding subclasses should be written like this
//		inLogMode = true;
//		//do something
//		inLogMode = false;
    }

    //----------------------- Tester method(s) ---------------------------//


    protected boolean hasDataPropertyValues(IRI property) {
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();

        OWLDataProperty dataProperty = factory.getOWLDataProperty(property);
        return getDataPropertyValues(dataProperty, includeImportsClosure).size() > 0;
    }


    protected boolean hasObjectPropertyValues(IRI property) {
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();

        OWLObjectProperty objectProperty = factory.getOWLObjectProperty(property);
        return getObjectPropertyValues(objectProperty, includeImportsClosure).size() > 0;
    }


    //----------------------- Getter method(s) ---------------------------//


    protected OWLLiteral getOneDataPropertyValue(IRI property) {
        Set<OWLLiteral> propertyValues = getDataPropertyValues(property);
        if (propertyValues == null || propertyValues.isEmpty()) {
            return null;
        }
        else {
            return propertyValues.iterator().next();
        }
    }

    protected Set<OWLLiteral> getDataPropertyValues(IRI property) {
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
        OWLDataProperty dataProperty = factory.getOWLDataProperty(property);

        return getDataPropertyValues(dataProperty, includeImportsClosure);
    }


    protected Set<OWLLiteral> getDataPropertyValues(
            OWLDataProperty dataProperty, boolean includeImportsClosure) {

        Collection<OWLLiteral> res = EntitySearcher.getDataPropertyValues(wrappedProtegeIndividual, dataProperty, ontology);

        if (includeImportsClosure) {
            Set<OWLOntology> importsClosure = ontology.getOWLOntologyManager().getImportsClosure(ontology);
            for (OWLOntology importedOntology : importsClosure) {
                res.addAll(EntitySearcher.getDataPropertyValues(wrappedProtegeIndividual, dataProperty, importedOntology));
            }
        }

        return new HashSet<>(res);
    }


    protected OWLIndividual getOneObjectPropertyValue(IRI property) {
        Set<OWLIndividual> propertyValues = getObjectPropertyValues(property);
        if (propertyValues == null || propertyValues.isEmpty()) {
            return null;
        }
        else {
            return propertyValues.iterator().next();
        }
    }


    protected Set<OWLIndividual> getObjectPropertyValues(IRI property) {
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
        OWLObjectProperty objProperty = factory.getOWLObjectProperty(property);

        return getObjectPropertyValues(objProperty, includeImportsClosure);
    }


    protected Set<OWLIndividual> getObjectPropertyValues(
            OWLObjectProperty objProperty, boolean includeImportsClosure) {

        return OWLUtil.getPropertyValues(wrappedProtegeIndividual, objProperty, ontology, includeImportsClosure);
    }


    //----------------------- Setter method(s) ---------------------------//


    protected void setDataPropertyValue(IRI property, OWLLiteral value) {
        removeAllDataPropertyValues(property);
        if (value != null) { // || value.getLiteral() != null) {
            addDataPropertyValue(property, value);
        }
    }


    protected void setDataPropertyValues(IRI property, Collection<OWLLiteral> values) {
        removeAllDataPropertyValues(property);
        addDataPropertyValues(property, values);
    }


    protected void setObjectPropertyValue(IRI property, OWLIndividual value) {
        removeAllObjectPropertyValues(property);
        if (value != null) {
            addObjectPropertyValue(property, value);
        }
    }


    protected void setObjectPropertyValues(IRI property, Collection<OWLIndividual> values) {
        removeAllObjectPropertyValues(property);
        addObjectPropertyValues(property, values);
    }


    //----------------------- Add method(s) ---------------------------//


    protected void addDataPropertyValue(IRI property, OWLLiteral value) {
        addDataPropertyValues(property, Collections.singleton(value));
    }


    protected void addDataPropertyValues(IRI property, Collection<OWLLiteral> values) {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLDataProperty dataProperty = factory.getOWLDataProperty(property);

        List<OWLOntologyChange> addAxioms = new ArrayList<OWLOntologyChange>();
        for (OWLLiteral value : values) {
            OWLAxiom propValueAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty,
                                                                                        wrappedProtegeIndividual,
                                                                                        value);
            ChangeApplied changeApplied = manager.addAxiom(ontology, propValueAssertionAxiom);
            addAxioms.add(new AddAxiom(ontology, propValueAssertionAxiom));
        }
        manager.applyChanges(addAxioms);
        if ((!addAxioms.isEmpty()) && (!inLogMode)) {
            logModificationEvent();
        }
    }

    protected void addObjectPropertyValue(IRI property, OWLIndividual value) {
        addObjectPropertyValues(property, Collections.singleton(value));
    }


    protected void addObjectPropertyValues(IRI property, Collection<OWLIndividual> values) {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLObjectProperty objProperty = factory.getOWLObjectProperty(property);

        List<OWLOntologyChange> addAxioms = new ArrayList<OWLOntologyChange>();
        for (OWLIndividual value : values) {
            OWLAxiom propValueAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(objProperty,
                                                                                          wrappedProtegeIndividual,
                                                                                          value);
            ChangeApplied changeApplied = manager.addAxiom(ontology, propValueAssertionAxiom);
            if (changeApplied == ChangeApplied.SUCCESSFULLY) {
                addAxioms.add(new AddAxiom(ontology, propValueAssertionAxiom));
            }
        }
        manager.applyChanges(addAxioms);
        if ((!addAxioms.isEmpty()) && (!inLogMode)) {
            logModificationEvent();
        }
    }


    //----------------------- Remove method(s) ---------------------------//


    protected void removeAllDataPropertyValues(IRI property) {
        Set<OWLLiteral> propertyValues = getDataPropertyValues(property);
        removeDataPropertyValues(property, propertyValues);
    }


    protected void removeDataPropertyValue(IRI property, OWLLiteral value) {
        removeDataPropertyValues(property, Collections.singleton(value));
    }


    private void removeDataPropertyValues(IRI property, Set<OWLLiteral> values) {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLDataProperty dataProperty = factory.getOWLDataProperty(property);

        List<OWLOntologyChange> removeAxioms = new ArrayList<OWLOntologyChange>();
        for (OWLLiteral value : values) {
            OWLAxiom propValueAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty,
                                                                                        wrappedProtegeIndividual,
                                                                                        value);
            ChangeApplied changeApplied = manager.removeAxiom(ontology, propValueAssertionAxiom);
            if (changeApplied == ChangeApplied.SUCCESSFULLY) {
                removeAxioms.add(new RemoveAxiom(ontology, propValueAssertionAxiom));
            }
        }
        manager.applyChanges(removeAxioms);

        if ((!removeAxioms.isEmpty()) && (!inLogMode)) {
            logModificationEvent();
        }
    }


    protected void removeAllObjectPropertyValues(IRI property) {
        Set<OWLIndividual> propertyValues = getObjectPropertyValues(property);
        removeObjectPropertyValues(property, propertyValues);
    }


    protected void removeObjectPropertyValue(IRI property, OWLIndividual value) {
        removeObjectPropertyValues(property, Collections.singleton(value));
    }


    private void removeObjectPropertyValues(IRI property, Set<OWLIndividual> values) {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLObjectProperty dataProperty = factory.getOWLObjectProperty(property);

        List<OWLOntologyChange> removeAxioms = new ArrayList<OWLOntologyChange>();
        for (OWLIndividual value : values) {
            OWLAxiom propValueAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(dataProperty,
                                                                                          wrappedProtegeIndividual,
                                                                                          value);
            ChangeApplied changeApplied = manager.removeAxiom(ontology, propValueAssertionAxiom);
            if (changeApplied == ChangeApplied.SUCCESSFULLY) {
                removeAxioms.add(new RemoveAxiom(ontology, propValueAssertionAxiom));
            }
        }
        manager.applyChanges(removeAxioms);

        if ((!removeAxioms.isEmpty()) && (!inLogMode)) {
            logModificationEvent();
        }
    }


    //----------------------- Utility method(s) ---------------------------//


    protected OWLLiteral getOWLStringLiteral(String literal) {
        if (literal == null) {
            return null;
        }
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
        return factory.getOWLLiteral(literal);
    }

    protected OWLLiteral getOWLBooleanLiteral(Boolean value) {
        if (value == null) {
            return null;
        }
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
        return factory.getOWLLiteral(value);
    }

    protected OWLLiteral getOWLDoubleLiteral(Double value) {
        if (value == null) {
            return null;
        }
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
        return factory.getOWLLiteral(value);
    }

    protected OWLLiteral getOWLFloatLiteral(Float value) {
        if (value == null) {
            return null;
        }
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
        return factory.getOWLLiteral(value);
    }

    protected OWLLiteral getOWLIntLiteral(Integer value) {
        if (value == null) {
            return null;
        }
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
        return factory.getOWLLiteral(value);
    }

    protected OWLLiteral getOWLLongLiteral(Long value) {
        if (value == null) {
            return null;
        }
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
        return factory.getOWLLiteral(value.toString(), OWL2Datatype.XSD_LONG);
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractWrappedOWLIndividual) {
            AbstractWrappedOWLIndividual otherWrappedOwlInd = ((AbstractWrappedOWLIndividual) o);
            return otherWrappedOwlInd.wrappedProtegeIndividual.equals(this.wrappedProtegeIndividual);
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        return wrappedProtegeIndividual == null ? 0 : wrappedProtegeIndividual.hashCode();
    }

    public String toStringAllProperties() {
        return
                " IRI: " + getIRI();
    }

    @Override
    public String toString() {
        return "OWLIndividual(" + toStringAllProperties() + ")";
    }
}

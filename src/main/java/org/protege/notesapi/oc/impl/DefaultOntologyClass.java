package org.protege.notesapi.oc.impl;

import org.protege.notesapi.oc.OntologyClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultOntologyClass extends DefaultOntologyComponent implements OntologyClass {

    public DefaultOntologyClass(OWLNamedIndividual individual,
                                OWLOntology ontology) {
        super(individual, ontology);
    }


    @Override
    public String toStringAllProperties() {
        return
                super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "OntologyClass(" + toStringAllProperties() + ")";
    }

}

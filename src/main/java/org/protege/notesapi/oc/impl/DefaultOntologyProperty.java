package org.protege.notesapi.oc.impl;

import org.protege.notesapi.oc.OntologyProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultOntologyProperty extends DefaultOntologyComponent implements OntologyProperty {

    public DefaultOntologyProperty(OWLNamedIndividual individual,
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
        return "OntologyProperty(" + toStringAllProperties() + ")";
    }

}

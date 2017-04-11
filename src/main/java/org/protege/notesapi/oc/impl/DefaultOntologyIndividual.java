package org.protege.notesapi.oc.impl;

import org.protege.notesapi.oc.OntologyIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultOntologyIndividual extends DefaultOntologyComponent implements OntologyIndividual {

    public DefaultOntologyIndividual(OWLNamedIndividual individual,
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
        return "OntologyIndividual(" + toStringAllProperties() + ")";
    }

}

package org.protege.notesapi.oc.impl;

import org.protege.notesapi.oc.Ontology;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultOntology extends DefaultOntologyComponent implements Ontology {

    public DefaultOntology(OWLNamedIndividual individual,
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
        return "Ontology(" + toStringAllProperties() + ")";
    }

}

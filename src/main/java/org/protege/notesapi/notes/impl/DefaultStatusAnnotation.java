package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.StatusAnnotation;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultStatusAnnotation extends DefaultStatus implements StatusAnnotation {

    private static final long serialVersionUID = 3332175980533356345L;


    public DefaultStatusAnnotation(OWLNamedIndividual individual,
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
        return "StatusAnnotation(" + toStringAllProperties() + ")";
    }

}

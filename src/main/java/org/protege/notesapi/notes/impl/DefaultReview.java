package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.NoteType;
import org.protege.notesapi.notes.Review;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultReview extends DefaultAnnotation implements Review {

    private static final long serialVersionUID = -936316545120766766L;


    public DefaultReview(OWLNamedIndividual individual,
                         OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return Review.Type;
    }


    @Override
    public String toStringAllProperties() {
        return
                super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "Review(" + toStringAllProperties() + ")";
    }

}

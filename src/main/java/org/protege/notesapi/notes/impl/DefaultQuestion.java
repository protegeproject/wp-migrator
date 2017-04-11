package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.NoteType;
import org.protege.notesapi.notes.Question;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultQuestion extends DefaultAnnotation implements Question {

    private static final long serialVersionUID = -6403240019783127834L;


    public DefaultQuestion(OWLNamedIndividual individual,
                           OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return Question.Type;
    }


    @Override
    public String toStringAllProperties() {
        return
                super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "Question(" + toStringAllProperties() + ")";
    }

}

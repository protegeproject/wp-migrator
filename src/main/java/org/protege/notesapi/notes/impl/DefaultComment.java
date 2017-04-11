package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.Comment;
import org.protege.notesapi.notes.NoteType;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultComment extends DefaultAnnotation implements Comment {

    private static final long serialVersionUID = 3332175980533356345L;


    public DefaultComment(OWLNamedIndividual individual,
                          OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return Comment.Type;
    }


    @Override
    public String toStringAllProperties() {
        return
                super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "Comment(" + toStringAllProperties() + ")";
    }

}

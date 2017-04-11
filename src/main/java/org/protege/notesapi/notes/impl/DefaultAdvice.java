package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.Advice;
import org.protege.notesapi.notes.NoteType;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultAdvice extends DefaultAnnotation implements Advice {

    private static final long serialVersionUID = 5748451557811037638L;


    public DefaultAdvice(OWLNamedIndividual individual,
                         OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return Advice.Type;
    }


    @Override
    public String toStringAllProperties() {
        return
                super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "Advice(" + toStringAllProperties() + ")";
    }

}

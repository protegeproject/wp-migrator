package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.Example;
import org.protege.notesapi.notes.NoteType;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultExample extends DefaultAnnotation implements Example {

    private static final long serialVersionUID = 5233971870675144350L;


    public DefaultExample(OWLNamedIndividual individual,
                          OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return Example.Type;
    }


    @Override
    public String toStringAllProperties() {
        return
                super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "Example(" + toStringAllProperties() + ")";
    }

}

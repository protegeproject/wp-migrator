package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.NoteType;
import org.protege.notesapi.notes.SeeAlso;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultSeeAlso extends DefaultAnnotation implements SeeAlso {

    private static final long serialVersionUID = 6001763358609517315L;


    public DefaultSeeAlso(OWLNamedIndividual individual,
                          OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return SeeAlso.Type;
    }


    @Override
    public String toStringAllProperties() {
        return
                super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "SeeAlso(" + toStringAllProperties() + ")";
    }

}

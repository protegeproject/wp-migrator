package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.NoteType;
import org.protege.notesapi.notes.VoteFiveStars;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultVoteFiveStars extends DefaultVote implements VoteFiveStars {

    private static final long serialVersionUID = -1038451747082484642L;


    public DefaultVoteFiveStars(OWLNamedIndividual individual,
                                OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return VoteFiveStars.Type;
    }


    @Override
    public String toStringAllProperties() {
        return
                super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "VoteFiveStars(" + toStringAllProperties() + ")";
    }

}

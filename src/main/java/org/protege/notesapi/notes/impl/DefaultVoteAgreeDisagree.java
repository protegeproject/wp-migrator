package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.NoteType;
import org.protege.notesapi.notes.VoteAgreeDisagree;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultVoteAgreeDisagree extends DefaultVote implements VoteAgreeDisagree {

    private static final long serialVersionUID = -2773464535266222346L;


    public DefaultVoteAgreeDisagree(OWLNamedIndividual individual,
                                    OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return VoteAgreeDisagree.Type;
    }


    @Override
    public String toStringAllProperties() {
        return
                super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "VoteAgreeDisagree(" + toStringAllProperties() + ")";
    }

}

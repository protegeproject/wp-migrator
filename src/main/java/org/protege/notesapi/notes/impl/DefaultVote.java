package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.NoteType;
import org.protege.notesapi.notes.OntologyJavaMapping;
import org.protege.notesapi.notes.Vote;
import org.protege.notesapi.util.OWLUtil;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultVote extends DefaultAnnotation implements Vote {

    private static final long serialVersionUID = 4803134572472165062L;

    private static final IRI PROPERTY_IRI_VOTE_VALUE = IRI.create(OntologyJavaMapping.NAMESPACE + "voteValue" );


    public DefaultVote(OWLNamedIndividual individual,
                       OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return Vote.Type;
    }


    // voteValue

    //@Override
    public String getVoteValue() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_VOTE_VALUE));
    }

    //@Override
    public boolean hasVoteValue() {
        return hasDataPropertyValues(PROPERTY_IRI_VOTE_VALUE);
    }

    //@Override
    public void setVoteValue(String newVoteValue) {
        setDataPropertyValue(PROPERTY_IRI_VOTE_VALUE, getOWLStringLiteral(newVoteValue));
    }


    @Override
    public String toStringAllProperties() {
        return
                " voteValue: '" + getVoteValue() + "'" +
                        super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "Vote(" + toStringAllProperties() + ")";
    }

}

package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.Explanation;
import org.protege.notesapi.notes.NoteType;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultExplanation extends DefaultAnnotation implements Explanation {

    private static final long serialVersionUID = -349113374499650993L;


    public DefaultExplanation(OWLNamedIndividual individual,
                              OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return Explanation.Type;
    }


    @Override
    public String toStringAllProperties() {
        return
                super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "Explanation(" + toStringAllProperties() + ")";
    }

}

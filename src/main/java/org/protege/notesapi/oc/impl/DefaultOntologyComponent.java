package org.protege.notesapi.oc.impl;

import org.protege.notesapi.notes.impl.DefaultAnnotatableThing;
import org.protege.notesapi.oc.OntologyComponent;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultOntologyComponent extends DefaultAnnotatableThing implements OntologyComponent {

    private static final long serialVersionUID = -938509316656375845L;

    public DefaultOntologyComponent(OWLNamedIndividual individual,
                                    OWLOntology ontology) {
        super(individual, ontology);
    }


    @Override
    public String toStringAllProperties() {
        return
                super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "OntologyComponent(" + toStringAllProperties() + ")";
    }

}

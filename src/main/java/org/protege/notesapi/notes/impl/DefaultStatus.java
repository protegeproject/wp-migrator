package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.OntologyJavaMapping;
import org.protege.notesapi.notes.Status;
import org.protege.notesapi.util.AbstractWrappedOWLIndividual;
import org.protege.notesapi.util.OWLUtil;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultStatus extends AbstractWrappedOWLIndividual implements Status {

    private static final long serialVersionUID = -2834373796866344579L;

    private static final IRI PROPERTY_IRI_DESCRIPTION = IRI.create(OntologyJavaMapping.NAMESPACE + "description" );


    public DefaultStatus(OWLNamedIndividual individual,
                         OWLOntology ontology) {
        super(individual, ontology);
    }


    // description

    //@Override
    public String getDescription() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_DESCRIPTION));
    }

    //@Override
    public boolean hasDescription() {
        return hasDataPropertyValues(PROPERTY_IRI_DESCRIPTION);
    }

    //@Override
    public void setDescription(String newDescription) {
        setDataPropertyValue(PROPERTY_IRI_DESCRIPTION, getOWLStringLiteral(newDescription));
    }


    @Override
    public String toStringAllProperties() {
        return
                " description: '" + getDescription() + "'" +
                        super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "Status(" + toStringAllProperties() + ")";
    }

}

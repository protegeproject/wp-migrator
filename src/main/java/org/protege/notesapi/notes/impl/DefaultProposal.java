package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.NoteType;
import org.protege.notesapi.notes.OntologyJavaMapping;
import org.protege.notesapi.notes.Proposal;
import org.protege.notesapi.util.OWLUtil;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultProposal extends DefaultAnnotation implements Proposal {

    private static final long serialVersionUID = -2618948646272309375L;

    private static final IRI PROPERTY_IRI_CONTACT_INFORMATION = IRI.create(OntologyJavaMapping.NAMESPACE + "contactInformation" );

    private static final IRI PROPERTY_IRI_REASON_FOR_CHANGE = IRI.create(OntologyJavaMapping.NAMESPACE + "reasonForChange" );


    public DefaultProposal(OWLNamedIndividual individual,
                           OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return Proposal.Type;
    }


    // contactInformation

    //@Override
    public String getContactInformation() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_CONTACT_INFORMATION));
    }

    //@Override
    public boolean hasContactInformation() {
        return hasDataPropertyValues(PROPERTY_IRI_CONTACT_INFORMATION);
    }

    //@Override
    public void setContactInformation(String newContactInformation) {
        setDataPropertyValue(PROPERTY_IRI_CONTACT_INFORMATION, getOWLStringLiteral(newContactInformation));
    }


    // reasonForChange

    //@Override
    public String getReasonForChange() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_REASON_FOR_CHANGE));
    }

    //@Override
    public boolean hasReasonForChange() {
        return hasDataPropertyValues(PROPERTY_IRI_REASON_FOR_CHANGE);
    }

    //@Override
    public void setReasonForChange(String newReasonForChange) {
        setDataPropertyValue(PROPERTY_IRI_REASON_FOR_CHANGE, getOWLStringLiteral(newReasonForChange));
    }


    @Override
    public String toStringAllProperties() {
        return
                " contactInformation: '" + getContactInformation() + "'" +
                        " reasonForChange: '" + getReasonForChange() + "'" +
                        super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "Proposal(" + toStringAllProperties() + ")";
    }

}

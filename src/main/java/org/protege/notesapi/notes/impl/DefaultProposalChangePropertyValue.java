package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.NoteType;
import org.protege.notesapi.notes.NotesFactory;
import org.protege.notesapi.notes.OntologyJavaMapping;
import org.protege.notesapi.notes.ProposalChangePropertyValue;
import org.protege.notesapi.oc.OntologyProperty;
import org.protege.notesapi.util.OWLUtil;
import org.protege.notesapi.util.OntologyJavaMappingUtil;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.logging.Level;
import java.util.logging.Logger;

/** @author csnyulas */
public class DefaultProposalChangePropertyValue extends DefaultProposal implements ProposalChangePropertyValue {

    private static final long serialVersionUID = -5262189803382651068L;

    private static final IRI PROPERTY_IRI_NEW_VALUE = IRI.create(OntologyJavaMapping.NAMESPACE + "newValue" );

    private static final IRI PROPERTY_IRI_OLD_VALUE = IRI.create(OntologyJavaMapping.NAMESPACE + "oldValue" );

    private static final IRI PROPERTY_IRI_PROPERTY = IRI.create(OntologyJavaMapping.NAMESPACE + "property" );


    public DefaultProposalChangePropertyValue(OWLNamedIndividual individual,
                                              OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return ProposalChangePropertyValue.Type;
    }


    // newValue

    //@Override
    public String getNewValue() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_NEW_VALUE));
    }

    //@Override
    public boolean hasNewValue() {
        return hasDataPropertyValues(PROPERTY_IRI_NEW_VALUE);
    }

    //@Override
    public void setNewValue(String newNewValue) {
        setDataPropertyValue(PROPERTY_IRI_NEW_VALUE, getOWLStringLiteral(newNewValue));
    }


    // oldValue

    //@Override
    public String getOldValue() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_OLD_VALUE));
    }

    //@Override
    public boolean hasOldValue() {
        return hasDataPropertyValues(PROPERTY_IRI_OLD_VALUE);
    }

    //@Override
    public void setOldValue(String newOldValue) {
        setDataPropertyValue(PROPERTY_IRI_OLD_VALUE, getOWLStringLiteral(newOldValue));
    }


    // property

    //@Override
    public OntologyProperty getProperty() {
        OWLIndividual value = getOneObjectPropertyValue(PROPERTY_IRI_PROPERTY);
        if (value instanceof OWLNamedIndividual) {
            return OntologyJavaMappingUtil.getSpecificObject(ontology,
                                                             (OWLNamedIndividual) value,
                                                             OntologyProperty.class);
        }
        return null;
    }

    //@Override
    public boolean hasProperty() {
        return hasObjectPropertyValues(PROPERTY_IRI_PROPERTY);
    }

    //@Override
    public void setProperty(OntologyProperty newProperty) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newProperty);
        if (wrappedOWLIndividual != null) {
            setObjectPropertyValue(PROPERTY_IRI_PROPERTY, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from OntologyProperty: " + newProperty.toString());
        }
    }


    @Override
    public String toStringAllProperties() {
        return
                " property: '" + getProperty() + "'" +
                        " oldValue: '" + getOldValue() + "'" +
                        " newValue: '" + getNewValue() + "'" +
                        super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "Proposal(" + toStringAllProperties() + ")";
    }

}

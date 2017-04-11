package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.LinguisticEntity;
import org.protege.notesapi.notes.OntologyJavaMapping;
import org.protege.notesapi.util.AbstractWrappedOWLIndividual;
import org.protege.notesapi.util.OWLUtil;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author csnyulas */
public class DefaultLinguisticEntity extends AbstractWrappedOWLIndividual implements LinguisticEntity {

    private static final long serialVersionUID = -9057739813427308395L;

    private static final IRI PROPERTY_IRI_LABEL = IRI.create(OntologyJavaMapping.NAMESPACE + "label" );

    private static final IRI PROPERTY_IRI_LANGUAGE = IRI.create(OntologyJavaMapping.NAMESPACE + "language" );


    public DefaultLinguisticEntity(OWLNamedIndividual individual,
                                   OWLOntology ontology) {
        super(individual, ontology);
    }


    // label

    //@Override
    public String getLabel() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_LABEL));
    }

    //@Override
    public boolean hasLabel() {
        return hasDataPropertyValues(PROPERTY_IRI_LABEL);
    }

    //@Override
    public void setLabel(String newLabel) {
        setDataPropertyValue(PROPERTY_IRI_LABEL, getOWLStringLiteral(newLabel));
    }


    // language

    //@Override
    public String getLanguage() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_LANGUAGE));
    }

    //@Override
    public boolean hasLanguage() {
        return hasDataPropertyValues(PROPERTY_IRI_LANGUAGE);
    }

    //@Override
    public void setLanguage(String newLanguage) {
        setDataPropertyValue(PROPERTY_IRI_LANGUAGE, getOWLStringLiteral(newLanguage));
    }


    @Override
    public String toStringAllProperties() {
        return
                " label: '" + getLabel() + "'" +
                        " language: '" + getLanguage() + "'" +
                        super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "LinguisticEntity(" + toStringAllProperties() + ")";
    }

}

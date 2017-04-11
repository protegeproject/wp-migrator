package org.protege.notesapi.notes.impl;

import org.protege.notesapi.NotesException;
import org.protege.notesapi.notes.*;
import org.protege.notesapi.oc.OntologyClass;
import org.protege.notesapi.oc.OntologyComponent;
import org.protege.notesapi.oc.OntologyProperty;
import org.protege.notesapi.util.OWLUtil;
import org.protege.notesapi.util.OntologyJavaMappingUtil;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @author csnyulas */
public class DefaultProposalCreateEntity extends DefaultProposal implements ProposalCreateEntity {

    private static final long serialVersionUID = 137097557919125611L;

    private static final IRI PROPERTY_IRI_DEFINITION = IRI.create(OntologyJavaMapping.NAMESPACE + "definition" );

    private static final IRI PROPERTY_IRI_ENTITY_ID = IRI.create(OntologyJavaMapping.NAMESPACE + "entityId" );

    private static final IRI PROPERTY_IRI_PARENTS = IRI.create(OntologyJavaMapping.NAMESPACE + "parents" );

    private static final IRI PROPERTY_IRI_PREFERRED_NAME = IRI.create(OntologyJavaMapping.NAMESPACE + "preferredName" );

    private static final IRI PROPERTY_IRI_SYNONYM = IRI.create(OntologyJavaMapping.NAMESPACE + "synonym" );

    private static final IRI PROPERTY_IRI_CREATED_ENTITY = IRI.create(OntologyJavaMapping.NAMESPACE + "createdEntity" );


    public DefaultProposalCreateEntity(OWLNamedIndividual individual,
                                       OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return ProposalCreateEntity.Type;
    }


    // definition

    //@Override
    public LinguisticEntity getDefinition() {
        OWLIndividual value = getOneObjectPropertyValue(PROPERTY_IRI_DEFINITION);
        if (value instanceof OWLNamedIndividual) {
            return OntologyJavaMappingUtil.getSpecificObject(ontology,
                                                             (OWLNamedIndividual) value,
                                                             LinguisticEntity.class);
        }
        return null;
    }

    //@Override
    public boolean hasDefinition() {
        return hasObjectPropertyValues(PROPERTY_IRI_DEFINITION);
    }

    //@Override
    public void setDefinition(LinguisticEntity newDefinition) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newDefinition);
        if (wrappedOWLIndividual != null) {
            setObjectPropertyValue(PROPERTY_IRI_DEFINITION, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from LinguisticEntity: " + newDefinition.toString());
        }
    }


    // entityId

    //@Override
    public String getEntityId() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_ENTITY_ID));
    }

    //@Override
    public boolean hasEntityId() {
        return hasDataPropertyValues(PROPERTY_IRI_ENTITY_ID);
    }

    //@Override
    public void setEntityId(String newEntityId) {
        setDataPropertyValue(PROPERTY_IRI_ENTITY_ID, getOWLStringLiteral(newEntityId));
    }


    // parents

    private void checkParentsType(OntologyComponent parents) throws NotesException {
        if (parents instanceof OntologyClass ||
                parents instanceof OntologyProperty) {
            return;
        }
        throw new NotesException("Invalid value type for property \"parents\": " + parents.getClass());
    }

    //@Override
    public Collection<OntologyComponent> getParents() {
        Collection<OntologyComponent> res = new HashSet<OntologyComponent>();
        Set<OWLIndividual> values = getObjectPropertyValues(PROPERTY_IRI_PARENTS);
        for (OWLIndividual value : values) {
            if (value instanceof OWLNamedIndividual) {
                res.add(OntologyJavaMappingUtil.getSpecificObject(ontology,
                                                                  (OWLNamedIndividual) value,
                                                                  OntologyComponent.class));
            }
        }
        return res;
    }

    //@Override
    public boolean hasParents() {
        return hasObjectPropertyValues(PROPERTY_IRI_PARENTS);
    }

    //@Override
    public void addParents(OntologyClass newParent) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newParent);
        if (wrappedOWLIndividual != null) {
            addObjectPropertyValue(PROPERTY_IRI_PARENTS, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from OntologyClass: " + newParent.toString());
        }
    }

    //@Override
    public void addParents(OntologyProperty newParent) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newParent);
        if (wrappedOWLIndividual != null) {
            addObjectPropertyValue(PROPERTY_IRI_PARENTS, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from OntologyProperty: " + newParent.toString());
        }
    }

    //@Override
    public void removeParents(OntologyClass oldParent) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(oldParent);
        if (wrappedOWLIndividual != null) {
            removeObjectPropertyValue(PROPERTY_IRI_PARENTS, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from OntologyClass: " + oldParent.toString());
        }
    }

    //@Override
    public void removeParents(OntologyProperty oldParent) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(oldParent);
        if (wrappedOWLIndividual != null) {
            removeObjectPropertyValue(PROPERTY_IRI_PARENTS, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from OntologyProperty: " + oldParent.toString());
        }
    }

    //@Override
    public void setParents(Collection<? extends OntologyComponent> newParents) throws NotesException {
        Collection<OWLIndividual> values = new ArrayList<OWLIndividual>();
        if (newParents != null) {
            for (OntologyComponent newValue : newParents) {
                checkParentsType(newValue);
                OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newValue);
                if (wrappedOWLIndividual != null) {
                    values.add(wrappedOWLIndividual);
                }
                else {
                    Logger.global.log(
                            Level.WARNING,
                            "Failed to extract wrapped OWLIndividual from OntologyClass: " + newValue.toString());
                }
            }
        }
        setObjectPropertyValues(PROPERTY_IRI_PARENTS, values);
    }


    // preferredName

    //@Override
    public LinguisticEntity getPreferredName() {
        OWLIndividual value = getOneObjectPropertyValue(PROPERTY_IRI_PREFERRED_NAME);
        if (value instanceof OWLNamedIndividual) {
            return OntologyJavaMappingUtil.getSpecificObject(ontology,
                                                             (OWLNamedIndividual) value,
                                                             LinguisticEntity.class);
        }
        return null;
    }

    //@Override
    public boolean hasPreferredName() {
        return hasObjectPropertyValues(PROPERTY_IRI_PREFERRED_NAME);
    }

    //@Override
    public void setPreferredName(LinguisticEntity newPreferredName) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newPreferredName);
        if (wrappedOWLIndividual != null) {
            setObjectPropertyValue(PROPERTY_IRI_PREFERRED_NAME, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from LinguisticEntity: " + newPreferredName.toString());
        }
    }


    // synonym

    //@Override
    public Collection<LinguisticEntity> getSynonym() {
        Collection<LinguisticEntity> res = new HashSet<LinguisticEntity>();
        Set<OWLIndividual> values = getObjectPropertyValues(PROPERTY_IRI_SYNONYM);
        for (OWLIndividual value : values) {
            if (value instanceof OWLNamedIndividual) {
                res.add(OntologyJavaMappingUtil.getSpecificObject(ontology,
                                                                  (OWLNamedIndividual) value,
                                                                  LinguisticEntity.class));
            }
        }
        return res;
    }

    //@Override
    public boolean hasSynonym() {
        return hasObjectPropertyValues(PROPERTY_IRI_SYNONYM);
    }

    //@Override
    public void addSynonym(LinguisticEntity newSynonym) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newSynonym);
        if (wrappedOWLIndividual != null) {
            addObjectPropertyValue(PROPERTY_IRI_SYNONYM, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from LinguisticEntity: " + newSynonym.toString());
        }
    }

    //@Override
    public void removeSynonym(LinguisticEntity oldSynonym) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(oldSynonym);
        if (wrappedOWLIndividual != null) {
            removeObjectPropertyValue(PROPERTY_IRI_SYNONYM, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from LinguisticEntity: " + oldSynonym.toString());
        }
    }

    //@Override
    public void setSynonym(Collection<? extends LinguisticEntity> newSynonym) {
        Collection<OWLIndividual> values = new ArrayList<OWLIndividual>();
        if (newSynonym != null) {
            for (LinguisticEntity newValue : newSynonym) {
                OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newValue);
                if (wrappedOWLIndividual != null) {
                    values.add(wrappedOWLIndividual);
                }
                else {
                    Logger.global.log(
                            Level.WARNING,
                            "Failed to extract wrapped OWLIndividual from LinguisticEntity: " + newValue.toString());
                }
            }
        }
        setObjectPropertyValues(PROPERTY_IRI_SYNONYM, values);
    }


    // createdEntity

    //@Override
    public OntologyComponent getCreatedEntity() {
        OWLIndividual value = getOneObjectPropertyValue(PROPERTY_IRI_CREATED_ENTITY);
        if (value instanceof OWLNamedIndividual) {
            return OntologyJavaMappingUtil.getSpecificObject(ontology,
                                                             (OWLNamedIndividual) value,
                                                             OntologyComponent.class);
        }
        return null;
    }

    //@Override
    public boolean hasCreatedEntity() {
        return hasObjectPropertyValues(PROPERTY_IRI_CREATED_ENTITY);
    }

    //@Override
    public void setCreatedEntity(OntologyComponent newCreatedEntity) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newCreatedEntity);
        if (wrappedOWLIndividual != null) {
            setObjectPropertyValue(PROPERTY_IRI_CREATED_ENTITY, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from OntologyComponent: " + newCreatedEntity.toString());
        }
    }


    @Override
    public String toStringAllProperties() {
        return
                " entityId: '" + getEntityId() + "'" +
                        " preferredName: '" + getPreferredName() + "'" +
                        " synonym: '" + getSynonym() + "'" +
                        " definition: '" + getDefinition() + "'" +
                        " parents: '" + getParents() + "'" +
                        " createdEntity: '" + getCreatedEntity() + "'" +
                        super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "ProposalCreateEntity(" + toStringAllProperties() + ")";
    }

}

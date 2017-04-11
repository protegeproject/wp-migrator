package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.NoteType;
import org.protege.notesapi.notes.NotesFactory;
import org.protege.notesapi.notes.OntologyJavaMapping;
import org.protege.notesapi.notes.ProposalChangeHierarchy;
import org.protege.notesapi.oc.OntologyComponent;
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
public class DefaultProposalChangeHierarchy extends DefaultProposal implements ProposalChangeHierarchy {

    private static final long serialVersionUID = -2618948646272309375L;

    private static final IRI PROPERTY_IRI_NEW_PARENTS = IRI.create(OntologyJavaMapping.NAMESPACE + "newParents" );

    private static final IRI PROPERTY_IRI_OLD_PARENTS = IRI.create(OntologyJavaMapping.NAMESPACE + "oldParents" );

    private static final IRI PROPERTY_IRI_RELATION_TYPE = IRI.create(OntologyJavaMapping.NAMESPACE + "relationType" );


    public DefaultProposalChangeHierarchy(OWLNamedIndividual individual,
                                          OWLOntology ontology) {
        super(individual, ontology);
    }

    @Override
    public NoteType getType() {
        return ProposalChangeHierarchy.Type;
    }


    // newParents

    //@Override
    public Collection<OntologyComponent> getNewParents() {
        Collection<OntologyComponent> res = new HashSet<OntologyComponent>();
        Set<OWLIndividual> values = getObjectPropertyValues(PROPERTY_IRI_NEW_PARENTS);
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
    public boolean hasNewParents() {
        return hasObjectPropertyValues(PROPERTY_IRI_NEW_PARENTS);
    }

    //@Override
    public void addNewParents(OntologyComponent newNewParents) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newNewParents);
        if (wrappedOWLIndividual != null) {
            addObjectPropertyValue(PROPERTY_IRI_NEW_PARENTS, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from OntologyComponent: " + newNewParents.toString());
        }
    }

    //@Override
    public void removeNewParents(OntologyComponent oldNewParents) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(oldNewParents);
        if (wrappedOWLIndividual != null) {
            removeObjectPropertyValue(PROPERTY_IRI_NEW_PARENTS, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from OntologyComponent: " + oldNewParents.toString());
        }
    }

    //@Override
    public void setNewParents(Collection<? extends OntologyComponent> newNewParents) {
        Collection<OWLIndividual> values = new ArrayList<OWLIndividual>();
        if (newNewParents != null) {
            for (OntologyComponent newValue : newNewParents) {
                OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newValue);
                if (wrappedOWLIndividual != null) {
                    values.add(wrappedOWLIndividual);
                }
                else {
                    Logger.global.log(
                            Level.WARNING,
                            "Failed to extract wrapped OWLIndividual from OntologyComponent: " + newValue.toString());
                }
            }
        }
        setObjectPropertyValues(PROPERTY_IRI_NEW_PARENTS, values);
    }


    // oldParents

    //@Override
    public Collection<OntologyComponent> getOldParents() {
        Collection<OntologyComponent> res = new HashSet<OntologyComponent>();
        Set<OWLIndividual> values = getObjectPropertyValues(PROPERTY_IRI_OLD_PARENTS);
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
    public boolean hasOldParents() {
        return hasObjectPropertyValues(PROPERTY_IRI_OLD_PARENTS);
    }

    //@Override
    public void addOldParents(OntologyComponent newOldParents) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newOldParents);
        if (wrappedOWLIndividual != null) {
            addObjectPropertyValue(PROPERTY_IRI_OLD_PARENTS, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from OntologyComponent: " + newOldParents.toString());
        }
    }

    //@Override
    public void removeOldParents(OntologyComponent oldOldParents) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(oldOldParents);
        if (wrappedOWLIndividual != null) {
            removeObjectPropertyValue(PROPERTY_IRI_OLD_PARENTS, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from OntologyComponent: " + oldOldParents.toString());
        }
    }

    //@Override
    public void setOldParents(Collection<? extends OntologyComponent> newOldParents) {
        Collection<OWLIndividual> values = new ArrayList<OWLIndividual>();
        if (newOldParents != null) {
            for (OntologyComponent newValue : newOldParents) {
                OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newValue);
                if (wrappedOWLIndividual != null) {
                    values.add(wrappedOWLIndividual);
                }
                else {
                    Logger.global.log(
                            Level.WARNING,
                            "Failed to extract wrapped OWLIndividual from OntologyComponent: " + newValue.toString());
                }
            }
        }
        setObjectPropertyValues(PROPERTY_IRI_OLD_PARENTS, values);
    }


    // relationType

    //@Override
    public String getRelationType() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_RELATION_TYPE));
    }

    //@Override
    public boolean hasRelationType() {
        return hasDataPropertyValues(PROPERTY_IRI_RELATION_TYPE);
    }

    //@Override
    public void setRelationType(String newRelationType) {
        setDataPropertyValue(PROPERTY_IRI_RELATION_TYPE, getOWLStringLiteral(newRelationType));
    }


    @Override
    public String toStringAllProperties() {
        return
                " oldParents: '" + getOldParents() + "'" +
                        " newParents: '" + getNewParents() + "'" +
                        " relationType: '" + getRelationType() + "'" +
                        super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "ProposalChangeHierarchy(" + toStringAllProperties() + ")";
    }

}

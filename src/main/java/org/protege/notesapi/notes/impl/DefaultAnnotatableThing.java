package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.*;
import org.protege.notesapi.util.AbstractWrappedOWLIndividual;
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
public class DefaultAnnotatableThing extends AbstractWrappedOWLIndividual implements AnnotatableThing {

    private static final long serialVersionUID = -2885057709879470748L;

    private static final IRI PROPERTY_IRI_ASSOCIATED_ANNOTATIONS = IRI.create(OntologyJavaMapping.NAMESPACE + "associatedAnnotations" );

    private static final IRI PROPERTY_IRI_HAS_STATUS = IRI.create(OntologyJavaMapping.NAMESPACE + "hasStatus" );

    protected DefaultAnnotatableThing(OWLNamedIndividual individual,
                                      OWLOntology ontology) {
        super(individual, ontology);
    }


    // associatedAnnotations

    //@Override
    public Collection<Annotation> getAssociatedAnnotations() {
        Collection<Annotation> res = new HashSet<Annotation>();
        Set<OWLIndividual> values = getObjectPropertyValues(PROPERTY_IRI_ASSOCIATED_ANNOTATIONS);
        for (OWLIndividual value : values) {
            if (value instanceof OWLNamedIndividual) {
                res.add(OntologyJavaMappingUtil.getSpecificObject(ontology,
                                                                  (OWLNamedIndividual) value,
                                                                  Annotation.class));
            }
        }
        return res;
    }

    //@Override
    public boolean hasAssociatedAnnotations() {
        return hasObjectPropertyValues(PROPERTY_IRI_ASSOCIATED_ANNOTATIONS);
    }

    //@Override
    public void addAssociatedAnnotations(Annotation newAssociatedAnnotations) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newAssociatedAnnotations);
        if (wrappedOWLIndividual != null) {
            addObjectPropertyValue(PROPERTY_IRI_ASSOCIATED_ANNOTATIONS, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from Annotation: " + newAssociatedAnnotations.toString());
        }
    }

    //@Override
    public void removeAssociatedAnnotations(Annotation oldAssociatedAnnotations) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(oldAssociatedAnnotations);
        if (wrappedOWLIndividual != null) {
            removeObjectPropertyValue(PROPERTY_IRI_ASSOCIATED_ANNOTATIONS, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from Annotation: " + oldAssociatedAnnotations.toString());
        }
    }

    //@Override
    public void setAssociatedAnnotations(Collection<? extends Annotation> newAssociatedAnnotations) {
        Collection<OWLIndividual> values = new ArrayList<OWLIndividual>();
        if (newAssociatedAnnotations != null) {
            for (Annotation newValue : newAssociatedAnnotations) {
                OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newValue);
                if (wrappedOWLIndividual != null) {
                    values.add(wrappedOWLIndividual);
                }
                else {
                    Logger.global.log(
                            Level.WARNING,
                            "Failed to extract wrapped OWLIndividual from Annotation: " + newValue.toString());
                }
            }
        }
        setObjectPropertyValues(PROPERTY_IRI_ASSOCIATED_ANNOTATIONS, values);
    }


    // hasStatus

    //@Override
    public Status getHasStatus() {
        Collection<Status> res = new HashSet<Status>();
        Set<OWLIndividual> values = getObjectPropertyValues(PROPERTY_IRI_HAS_STATUS);
        for (OWLIndividual value : values) {
            if (value instanceof OWLNamedIndividual) {
                res.add(OntologyJavaMappingUtil.getSpecificObject(ontology, (OWLNamedIndividual) value, Status.class));
            }
        }
        if (res.size() == 0) {
            return null;
        }
        else if (res.size() > 1) {
            Logger.global.log(
                    Level.WARNING,
                    "Multiple values attached to the hasStatus property for individual: " + this + ": " + res.toString());
        }
        return res.iterator().next();
    }

    //@Override
    public boolean hasHasStatus() {
        return hasObjectPropertyValues(PROPERTY_IRI_HAS_STATUS);
    }

    //@Override
    public void setHasStatus(Status newHasStatus) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newHasStatus);
        if (wrappedOWLIndividual == null) {
            Logger.global.log(
                    Level.WARNING, "Failed to extract wrapped OWLIndividual from Status: " + newHasStatus.toString());
        }
        setObjectPropertyValue(PROPERTY_IRI_HAS_STATUS, wrappedOWLIndividual);
    }


    //@Override
    public String toStringAllProperties() {
        return
                //" associatedAnnotations: '" + getAssociatedAnnotations() + "'" +
                " associatedAnnotations: (not displayed to avoid infinite cycles)" +
                        " hasStatus: '" + getHasStatus() + "'" +
                        super.toStringAllProperties();
    }

    //@Override
    public String toString() {
        return "AnnotatableThing(" + toStringAllProperties() + ")";
    }

    public String getId() {
        return getWrappedOWLIndividual().getIRI().toString();
    }
}

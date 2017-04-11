package org.protege.notesapi.notes.impl;

import org.protege.notesapi.notes.*;
import org.protege.notesapi.util.OWLUtil;
import org.protege.notesapi.util.OntologyJavaMappingUtil;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import java.text.DateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @author csnyulas */
public class DefaultAnnotation extends DefaultAnnotatableThing implements Annotation {

    private static final long serialVersionUID = 4019322087126685395L;

    private static final IRI PROPERTY_IRI_ANNOTATES = IRI.create(OntologyJavaMapping.NAMESPACE + "annotates" );

    private static final IRI PROPERTY_IRI_ARCHIVED = IRI.create(OntologyJavaMapping.NAMESPACE + "archived" );

    private static final IRI PROPERTY_IRI_ARCHIVED_IN_ONTOLOGY_REVISION = IRI.create(OntologyJavaMapping.NAMESPACE + "archivedInOntologyRevision" );

    private static final IRI PROPERTY_IRI_AUTHOR = IRI.create(OntologyJavaMapping.NAMESPACE + "author" );

    private static final IRI PROPERTY_IRI_BODY = IRI.create(OntologyJavaMapping.NAMESPACE + "body" );

    private static final IRI PROPERTY_IRI_CONTEXT = IRI.create(OntologyJavaMapping.NAMESPACE + "context" );

    /* TODO see if we need this ....
    private static final IRI PROPERTY_IRI_CREATED = IRI.create(OntologyJavaMapping.NAMESPACE + "created");
    private static final IRI PROPERTY_IRI_MODIFIED = IRI.create(OntologyJavaMapping.NAMESPACE + "modified");
    */
    private static final IRI PROPERTY_IRI_CREATED_AT = IRI.create(OntologyJavaMapping.NAMESPACE + "createdAt" );

    private static final IRI PROPERTY_IRI_MODIFIED_AT = IRI.create(OntologyJavaMapping.NAMESPACE + "modifiedAt" );

    private static final IRI PROPERTY_IRI_CREATED_IN_ONTOLOGY_REVISION = IRI.create(OntologyJavaMapping.NAMESPACE + "createdInOntologyRevision" );

    private static final IRI PROPERTY_IRI_RELATED = IRI.create(OntologyJavaMapping.NAMESPACE + "related" );

    private static final IRI PROPERTY_IRI_SUBJECT = IRI.create(OntologyJavaMapping.NAMESPACE + "subject" );


    public DefaultAnnotation(OWLNamedIndividual individual,
                             OWLOntology ontology) {
        super(individual, ontology);
    }

    //@Override
    public NoteType getType() {
        return Annotation.Type;
    }


    // annotates

    //@Override
    public Collection<AnnotatableThing> getAnnotates() {
        Collection<AnnotatableThing> res = new HashSet<AnnotatableThing>();
        Set<OWLIndividual> values = getObjectPropertyValues(PROPERTY_IRI_ANNOTATES);
        for (OWLIndividual value : values) {
            if (value instanceof OWLNamedIndividual) {
                res.add(OntologyJavaMappingUtil.getSpecificObject(ontology,
                                                                  (OWLNamedIndividual) value,
                                                                  AnnotatableThing.class));
            }
        }
        return res;
    }

    //@Override
    public boolean hasAnnotates() {
        return hasObjectPropertyValues(PROPERTY_IRI_ANNOTATES);
    }

    //@Override
    public void addAnnotates(AnnotatableThing newAnnotates) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newAnnotates);
        if (wrappedOWLIndividual != null) {
            addObjectPropertyValue(PROPERTY_IRI_ANNOTATES, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from AnnotatableThing: " + newAnnotates.toString());
        }
    }

    //@Override
    public void removeAnnotates(AnnotatableThing oldAnnotates) {
        OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(oldAnnotates);
        if (wrappedOWLIndividual != null) {
            removeObjectPropertyValue(PROPERTY_IRI_ANNOTATES, wrappedOWLIndividual);
        }
        else {
            Logger.global.log(
                    Level.WARNING,
                    "Failed to extract wrapped OWLIndividual from AnnotatableThing: " + oldAnnotates.toString());
        }
    }

    //@Override
    public void setAnnotates(Collection<? extends AnnotatableThing> newAnnotates) {
        Collection<OWLIndividual> values = new ArrayList<OWLIndividual>();
        if (newAnnotates != null) {
            for (AnnotatableThing newValue : newAnnotates) {
                OWLIndividual wrappedOWLIndividual = NotesFactory.getWrappedOWLIndividual(newValue);
                if (wrappedOWLIndividual != null) {
                    values.add(wrappedOWLIndividual);
                }
                else {
                    Logger.global.log(
                            Level.WARNING,
                            "Failed to extract wrapped OWLIndividual from AnnotatableThing: " + newValue.toString());
                }
            }
        }
        setObjectPropertyValues(PROPERTY_IRI_ANNOTATES, values);
    }


//    // associatedAnnotations
//
//	@Override
//	public Collection<Annotation> getAssociatedAnnotations();
//
//	@Override
//	public boolean hasAssociatedAnnotations();
//
//	@Override
//	public void addAssociatedAnnotations(Annotation newAssociatedAnnotations);
//
//	@Override
//	public void removeAssociatedAnnotations(Annotation oldAssociatedAnnotations);
//
//	@Override
//	public void setAssociatedAnnotations(Collection<? extends Annotation> newAssociatedAnnotations);


    // archived

    //@Override
    public Boolean getArchived() {
        return OWLUtil.getBooleanValue(getOneDataPropertyValue(PROPERTY_IRI_ARCHIVED));
    }

    //@Override
    public boolean hasArchived() {
        return hasDataPropertyValues(PROPERTY_IRI_ARCHIVED);
    }

    //@Override
    public void setArchived(Boolean newArchived) {
        setDataPropertyValue(PROPERTY_IRI_ARCHIVED, getOWLBooleanLiteral(newArchived));
    }


    // archivedInOntologyRevision

    //@Override
    public Integer getArchivedInOntologyRevision() {
        return OWLUtil.getIntegerValue(getOneDataPropertyValue(PROPERTY_IRI_ARCHIVED_IN_ONTOLOGY_REVISION));
    }

    //@Override
    public boolean hasArchivedInOntologyRevision() {
        return hasDataPropertyValues(PROPERTY_IRI_ARCHIVED_IN_ONTOLOGY_REVISION);
    }

    //@Override
    public void setArchivedInOntologyRevision(Integer newArchivedInOntologyRevision) {
        setDataPropertyValue(PROPERTY_IRI_ARCHIVED_IN_ONTOLOGY_REVISION,
                             getOWLIntLiteral(newArchivedInOntologyRevision));
    }


    // author

    //@Override
    public String getAuthor() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_AUTHOR));
    }

    //@Override
    public boolean hasAuthor() {
        return hasDataPropertyValues(PROPERTY_IRI_AUTHOR);
    }

    //@Override
    public void setAuthor(String newAuthor) {
        setDataPropertyValue(PROPERTY_IRI_AUTHOR, getOWLStringLiteral(newAuthor));
    }


    // body

    //@Override
    public String getBody() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_BODY));
    }

    //@Override
    public boolean hasBody() {
        return hasDataPropertyValues(PROPERTY_IRI_BODY);
    }

    //@Override
    public void setBody(String newBody) {
        setDataPropertyValue(PROPERTY_IRI_BODY, getOWLStringLiteral(newBody));
    }


    // context

    //@Override
    public String getContext() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_CONTEXT));
    }

    //@Override
    public boolean hasContext() {
        return hasDataPropertyValues(PROPERTY_IRI_CONTEXT);
    }

    //@Override
    public void setContext(String newContext) {
        setDataPropertyValue(PROPERTY_IRI_CONTEXT, getOWLStringLiteral(newContext));
    }


    /* TODO see if we need this ....
    // created

    @Override
	public Timestamp getCreated();

    @Override
	public boolean hasCreated() {
		return hasPropertyValues(PROPERTY_IRI_CREATED);
	}

    @Override
	public void setCreated(Timestamp newCreated);


    // modified

    @Override
	public Timestamp getModified();

    @Override
	public boolean hasModified() {
		return hasPropertyValues(PROPERTY_IRI_MODIFIED);
	}

    @Override
	public void setModified(Timestamp newModified);
     */


    // createdAt

    //@Override
    public Long getCreatedAt() {
        return OWLUtil.getLongValue(getOneDataPropertyValue(PROPERTY_IRI_CREATED_AT));
    }

    //@Override
    public boolean hasCreatedAt() {
        return hasDataPropertyValues(PROPERTY_IRI_CREATED_AT);
    }

    //@Override
    public void setCreatedAt(Long newCreatedAt) {
        setDataPropertyValue(PROPERTY_IRI_CREATED_AT, getOWLLongLiteral(newCreatedAt));
    }


    // createdInOntologyRevision

    //@Override
    public Integer getCreatedInOntologyRevision() {
        return OWLUtil.getIntegerValue(getOneDataPropertyValue(PROPERTY_IRI_CREATED_IN_ONTOLOGY_REVISION));
    }

    //@Override
    public boolean hasCreatedInOntologyRevision() {
        return hasDataPropertyValues(PROPERTY_IRI_CREATED_IN_ONTOLOGY_REVISION);
    }

    //@Override
    public void setCreatedInOntologyRevision(Integer newCreatedInOntologyRevision) {
        setDataPropertyValue(PROPERTY_IRI_CREATED_IN_ONTOLOGY_REVISION, getOWLIntLiteral(newCreatedInOntologyRevision));
    }


    // modifiedAt

    //@Override
    public Long getModifiedAt() {
        return OWLUtil.getLongValue(getOneDataPropertyValue(PROPERTY_IRI_MODIFIED_AT));
    }

    //@Override
    public boolean hasModifiedAt() {
        return hasDataPropertyValues(PROPERTY_IRI_MODIFIED_AT);
    }

    //@Override
    public void setModifiedAt(Long newModifiedAt) {
        setDataPropertyValue(PROPERTY_IRI_MODIFIED_AT, getOWLLongLiteral(newModifiedAt));
    }


    // hasStatus

    @Override
    public StatusAnnotation getHasStatus() {
        // We needed to change the simple solution:
        //return (StatusAnnotation) super.getHasStatus();
        // in order to deal with multi-typed status individuals,
        // such as "approved" or "rejected", which are of both the type
        // ChangeStatus and ProposalStatus,
        // and would be returned by super.getHasStatus() at random
        // either as instance of DefaultStatus class or as instance of DefaultStatusAnnotation class
        Status status = super.getHasStatus();
        if (status == null) {
            return null;
        }
        OWLNamedIndividual statusOWLIndividual = NotesFactory.getWrappedOWLIndividual(status).asOWLNamedIndividual();
        return OntologyJavaMappingUtil.getSpecificObject(ontology, statusOWLIndividual, StatusAnnotation.class);
    }

    // related

    //@Override
    public String getRelated() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_RELATED));
    }

    //@Override
    public boolean hasRelated() {
        return hasDataPropertyValues(PROPERTY_IRI_RELATED);
    }

    //@Override
    public void setRelated(String newRelated) {
        setDataPropertyValue(PROPERTY_IRI_RELATED, getOWLStringLiteral(newRelated));
    }


    // subject

    //@Override
    public String getSubject() {
        return OWLUtil.getStringValue(getOneDataPropertyValue(PROPERTY_IRI_SUBJECT));
    }

    //@Override
    public boolean hasSubject() {
        return hasDataPropertyValues(PROPERTY_IRI_SUBJECT);
    }

    //@Override
    public void setSubject(String newSubject) {
        setDataPropertyValue(PROPERTY_IRI_SUBJECT, getOWLStringLiteral(newSubject));
    }


    public void logCreationEvent() {
        inLogMode = true;

        //System.out.println("Log creation event...");
        setCreatedAt(System.currentTimeMillis());
        setModifiedAt(null);

        inLogMode = false;
    }

    public void logModificationEvent() {
        inLogMode = true;

        //System.out.println("Log modification event...");
        setModifiedAt(System.currentTimeMillis());

        inLogMode = false;
    }


    private String timestampToDate(Long timestamp) {
        return timestamp == null ? null : DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)
                                                    .format(new Date(timestamp)) + " " + (timestamp % 1000) + "ms";
    }

    @Override
    public String toStringAllProperties() {
        return
                " subject: '" + getSubject() + "'" +
                        " context: '" + getContext() + "'" +
                        " author: '" + getAuthor() + "'" +
                        " body: '" + getBody() + "'" +
                        " annotates: '" + getAnnotates() + "'" +
                        " createdAt: '" + timestampToDate(getCreatedAt()) + "'" +
                        " modifiedAt: '" + timestampToDate(getModifiedAt()) + "'" +
                        " createdInOntologyRevision: '" + getCreatedInOntologyRevision() + "'" +
                        " archived: '" + getArchived() + "'" +
                        " archivedInOntologyRevision: '" + getArchivedInOntologyRevision() + "'" +
                        super.toStringAllProperties();
    }

    @Override
    public String toString() {
        return "Annotation(" + toStringAllProperties() + ")";
    }

}

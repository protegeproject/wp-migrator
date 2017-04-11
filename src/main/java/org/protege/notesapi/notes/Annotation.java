package org.protege.notesapi.notes;

import java.util.Collection;


/** @author csnyulas */
public interface Annotation extends AnnotatableThing {

    public static final NoteType Type = NoteType.Note;

    NoteType getType();


    // annotates

    Collection<AnnotatableThing> getAnnotates();

    boolean hasAnnotates();

    void addAnnotates(AnnotatableThing newAnnotates);

    void removeAnnotates(AnnotatableThing oldAnnotates);

    void setAnnotates(Collection<? extends AnnotatableThing> newAnnotates);


    // associatedAnnotations

    Collection<Annotation> getAssociatedAnnotations();

    boolean hasAssociatedAnnotations();

    void addAssociatedAnnotations(Annotation newAssociatedAnnotations);

    void removeAssociatedAnnotations(Annotation oldAssociatedAnnotations);

    void setAssociatedAnnotations(Collection<? extends Annotation> newAssociatedAnnotations);


    // archived

    Boolean getArchived();

    boolean hasArchived();

    void setArchived(Boolean newArchived);


    // archivedInOntologyRevision

    Integer getArchivedInOntologyRevision();

    boolean hasArchivedInOntologyRevision();

    void setArchivedInOntologyRevision(Integer newArchivedInOntologyRevision);


    // author

    String getAuthor();

    boolean hasAuthor();

    void setAuthor(String newAuthor);


    // body

    String getBody();

    boolean hasBody();

    void setBody(String newBody);


    // context

    String getContext();

    boolean hasContext();

    void setContext(String newContext);


    /* TODO see if we need this ....
    // created

    Timestamp getCreated();

    boolean hasCreated();

    void setCreated(Timestamp newCreated);


    // modified

    Timestamp getModified();

    boolean hasModified();

    void setModified(Timestamp newModified);
     */

    // createdAt

    Long getCreatedAt();

    boolean hasCreatedAt();

    void setCreatedAt(Long newCreatedAt);


    // createdInOntologyRevision

    Integer getCreatedInOntologyRevision();

    boolean hasCreatedInOntologyRevision();

    void setCreatedInOntologyRevision(Integer newCreatedInOntologyRevision);


    // hasStatus

    StatusAnnotation getHasStatus();


    // modifiedAt

    Long getModifiedAt();

    boolean hasModifiedAt();

    void setModifiedAt(Long newModifiedAt);


    // related

    String getRelated();

    boolean hasRelated();

    void setRelated(String newRelated);


    // subject

    String getSubject();

    boolean hasSubject();

    void setSubject(String newSubject);


    void delete();


    public void logCreationEvent();

    public void logModificationEvent();

}

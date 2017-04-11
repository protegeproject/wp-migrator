package org.protege.notesapi.notes;

import java.util.Collection;

/** @author csnyulas */
public interface AnnotatableThing {

    // associatedAnnotations

    Collection<Annotation> getAssociatedAnnotations();

    boolean hasAssociatedAnnotations();

    void addAssociatedAnnotations(Annotation newAssociatedAnnotations);

    void removeAssociatedAnnotations(Annotation oldAssociatedAnnotations);

    void setAssociatedAnnotations(Collection<? extends Annotation> newAssociatedAnnotations);


    // hasStatus

    Status getHasStatus();

    boolean hasHasStatus();

    void setHasStatus(Status newHasStatus);


    String getId();
}

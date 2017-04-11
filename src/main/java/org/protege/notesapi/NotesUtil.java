package org.protege.notesapi;

import org.protege.notesapi.notes.AnnotatableThing;
import org.protege.notesapi.notes.Annotation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/** @author csnyulas */
public class NotesUtil {

    public static <T extends Annotation> Set<T> filterThreadStarterNotes(Set<T> allNotes) {
        Set<T> result = new HashSet<T>();
        for (T note : allNotes) {
            boolean isNoteInsideAThread = false;
            Collection<AnnotatableThing> annotates = note.getAnnotates();
            for (AnnotatableThing annotatedThing : annotates) {
                if (annotatedThing instanceof Annotation) {
                    isNoteInsideAThread = true;
                }
            }
            if (!isNoteInsideAThread) {
                result.add(note);
            }
        }
        return result;
    }

    public static boolean isArchived(Annotation annotation) {
        Boolean archived = annotation.getArchived();
        return (archived != null && archived.booleanValue() == true);
    }

    public static int countAssociatedNotes(AnnotatableThing annotatedThing,
                                           boolean includeArchived) {
        HashSet<AnnotatableThing> allAssociatedAnnotations = new HashSet<AnnotatableThing>();
        Collection<Annotation> directAssociatedAnnotations = annotatedThing.getAssociatedAnnotations();
        visitAllAnnotationsRecursively(directAssociatedAnnotations,
                                       includeArchived, allAssociatedAnnotations);
        return allAssociatedAnnotations.size();
    }

    private static void visitAllAnnotationsRecursively(Collection<Annotation> annotations,
                                                       boolean includeArchived, Collection<AnnotatableThing> visited) {
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                //check whether it should be counted
                if (includeArchived || (!isArchived(annotation))) {
                    //check to avoid infinite cycles
                    if (!visited.contains(annotation)) {
                        visited.add(annotation);
                        visitAllAnnotationsRecursively(annotation.getAssociatedAnnotations(), includeArchived, visited);
                    }
                }
            }
        }
    }

}

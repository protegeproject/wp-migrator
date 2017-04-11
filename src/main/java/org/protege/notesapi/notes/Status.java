package org.protege.notesapi.notes;

/** @author csnyulas */
public interface Status {

    // description

    String getDescription();

    boolean hasDescription();

    void setDescription(String newDescription);


    void delete();

}

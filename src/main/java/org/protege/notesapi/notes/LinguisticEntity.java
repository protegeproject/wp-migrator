package org.protege.notesapi.notes;

/** @author csnyulas */
public interface LinguisticEntity {

    // Slot label

    String getLabel();

    boolean hasLabel();

    void setLabel(String newLabel);


    // Slot language

    String getLanguage();

    boolean hasLanguage();

    void setLanguage(String newLanguage);


    void delete();
}

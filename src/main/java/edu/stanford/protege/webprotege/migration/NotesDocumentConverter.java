package edu.stanford.protege.webprotege.migration;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.notes.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;
import org.protege.notesapi.NotesManager;
import org.protege.notesapi.notes.AnnotatableThing;
import org.protege.notesapi.notes.Annotation;
import org.protege.notesapi.oc.impl.DefaultOntologyComponent;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLParserFactoryRegistry;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType" , "Guava"})
public class NotesDocumentConverter {

    public static final String CHANGES_ONTOLOGY_FILE_NAME = "changes.owl";

    public static final IRI CHANGES_ONTOLOGY_IRI = IRI.create("http://protege.stanford.edu/ontologies/ChAO/changes.owl" );

    @Nonnull
    private final Path notesFile;

    @Nonnull
    private final HasSignature signature;

    @Nonnull
    private final OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    private OWLOntology notesOntology;

    private final String projectId;

    public NotesDocumentConverter(@Nonnull String projectId,
                                  @Nonnull Path notesFile,
                                  @Nonnull HasSignature signature) {
        this.projectId = checkNotNull(projectId);
        this.notesFile = checkNotNull(notesFile);
        this.signature = checkNotNull(signature);
    }

    public List<Document> convert() {
        try {
            notesOntology = loadNotesOntology(notesFile);
            // Believe it or not, the following line appears inconsequential, like a bug almost,
            // but it's absolutely necessary for the proper functioning of this notes API thing.
            NotesManager.createNotesManager(notesOntology);
            return convertNotes();
        } catch (Exception e) {
            System.out.printf("Could not load notes ontology at %s.  Cause: %s.\n" ,
                              notesFile.toAbsolutePath().toString(),
                              e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<Document> convertNotes() {
        List<Document> result = new ArrayList<>();
        // I don't know a more efficient way of doing this with the notes api
        return signature.getSignature().stream()
                .map(this::convertEntityNotes)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private List<Document> convertEntityNotes(@Nonnull OWLEntity entity) {
        List<Document> threadDocuments = new ArrayList<>();
        DiscussionThread discussionThread = getDiscusssionThread(entity);
        if (!discussionThread.getNotes().isEmpty()) {
            DiscussionThreadConverter converter = new DiscussionThreadConverter(projectId,
                                                                                entity,
                                                                                discussionThread);
            threadDocuments.addAll(converter.convert());
        }
        return threadDocuments;
    }

    private static OWLOntology loadNotesOntology(@Nonnull Path notesOntologyDocument) throws OWLOntologyCreationException {
        final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLParserFactoryRegistry.getInstance().registerParserFactory(new BinaryOWLOntologyDocumentParserFactory());
        manager.addIRIMapper(new SimpleIRIMapper(CHANGES_ONTOLOGY_IRI, getChangeOntologyDocumentIRI()));
        return manager.loadOntologyFromOntologyDocument(notesOntologyDocument.toFile());
    }

    private static IRI getChangeOntologyDocumentIRI() {
        URL changeOntologyURL = NotesDocumentConverter.class.getResource("/" + CHANGES_ONTOLOGY_FILE_NAME);
        if (changeOntologyURL == null) {
            throw new RuntimeException(
                    "Changes ontology not found.  Please make sure the changes ontology document is placed in the class path with a file name of " + CHANGES_ONTOLOGY_FILE_NAME);
        }
        String uriString = changeOntologyURL.toString();
        return IRI.create(uriString);
    }


    private Note getNoteForAnnotation(Annotation annotation, Optional<NoteId> inReplyTo) {
        UserId author = UserId.getUserId(annotation.getAuthor());
        String body = annotation.getBody() == null ? "" : annotation.getBody();
        long timestamp = annotation.getCreatedAt();
        Optional<String> subject = annotation.getSubject() == null ? Optional.absent() : Optional.of(annotation.getSubject());

        NoteId noteId = NoteId.createNoteIdFromLexicalForm(annotation.getId());
        NoteHeader noteHeader = new NoteHeader(noteId, inReplyTo, author, timestamp);
        NoteStatus noteStatus = annotation.getArchived() != null && annotation.getArchived() ? NoteStatus.RESOLVED : NoteStatus.OPEN;
        NoteContent noteContent = NoteContent.builder()
                                             .setBody(body)
                                             .setNoteStatus(noteStatus)
                                             .setNoteType(NoteType.COMMENT)
                                             .setSubject(subject)
                                             .build();
        return Note.createNote(noteHeader, noteContent);
    }

    /**
     * Converts an OWLEntity to an AnnotatableThing.  The entity MUST be either an OWLClass, OWLObjectProperty,
     * OWLDataProperty, OWLAnnotationProperty or OWLNamedIndividual.  This method does not support the conversion
     * of OWLDatatype objects.
     *
     * @param entity The entity to be converted.
     * @return The AnnotatableThing corresponding to the entity.
     */
    private AnnotatableThing getAnnotatableThing(OWLEntity entity) {
        return new DefaultOntologyComponent(dataFactory.getOWLNamedIndividual(entity.getIRI()), notesOntology);
    }

    private DiscussionThread getDiscusssionThread(OWLEntity targetEntity) {
        AnnotatableThing annotatableThing = getAnnotatableThing(targetEntity);
        Set<Note> result = new HashSet<>();
        for (Annotation annotation : annotatableThing.getAssociatedAnnotations()) {
            if (annotation != null) {
                getAllNotesForAnnotation(annotation, Optional.<NoteId>absent(), result);
            }
        }
        return new DiscussionThread(result);
    }


    private void getAllNotesForAnnotation(Annotation annotation, Optional<NoteId> inReplyTo, Set<Note> result) {
        final Note noteForAnnotation = getNoteForAnnotation(annotation, inReplyTo);
        result.add(noteForAnnotation);
        for (Annotation anno : annotation.getAssociatedAnnotations()) {
            getAllNotesForAnnotation(anno, Optional.of(noteForAnnotation.getNoteId()), result);
        }
    }
}

package org.protege.notesapi;

import org.protege.notesapi.notes.*;
import org.protege.notesapi.oc.*;
import org.protege.notesapi.util.OWLUtil;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import uk.ac.manchester.cs.owl.owlapi.OWLImportsDeclarationImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/** @author csnyulas */
public class NotesManager {


    public static final IRI CHAO_IRI = IRI.create(NotesConstants.CHAO_ONTOLOGY_NAME);

    public static boolean DEFAULT_IMPORT_DOMAIN_ONTOLOGY_IN_NOTES_KB = false;

    private OWLOntology notesKb;

    private OWLOntologyManager notesKbManager;

    private NotesFactory notesFactory;

    private OntologyComponentFactory ocFactory;


    // --- public static creator methods --- //

    /**
     * Creates a notes KB manager for a notes KB ontology using the specified
     * document IRI and document location IRI.
     * The notes KB ontology is supposed to have been created earlier and
     * is supposed to be initialized, importing a domain ontology and the ChAO.
     *
     * @param notesKbOntologyIRI
     * @param notesKbDocumentIRI
     * @throws NotesException
     */
    public static NotesManager createNotesManager(
            String notesKbOntologyIRI, String notesKbDocumentIRI) throws NotesException {

        return new NotesManager(notesKbOntologyIRI, notesKbDocumentIRI);
    }


    /**
     * Creates a notes KB manager for a notes KB ontology using the specified
     * document IRI, document location IRI and ChAO document IRI.
     * The notes KB ontology is supposed to have been created earlier and
     * is supposed to be initialized, importing a domain ontology and the ChAO.
     *
     * @param notesKbOntologyIRI
     * @param notesKbDocumentIRI
     * @param chaoDocumentIRI
     * @throws NotesException
     */
    public static NotesManager createNotesManager(
            String notesKbOntologyIRI, String notesKbDocumentIRI,
            String chaoDocumentIRI) throws NotesException {

        return new NotesManager(notesKbOntologyIRI, notesKbDocumentIRI, chaoDocumentIRI);
    }


    /**
     * Creates a notes KB manager for a domain ontology using the specified
     * domain ontology, document IRI and document location IRI.
     *
     * @param domainOntology
     * @param notesKbOntologyIRI
     * @param notesKbDocumentIRI
     * @throws NotesException
     */
    public static NotesManager createNotesManager(final OWLOntology domainOntology,
                                                  String notesKbOntologyIRI,
                                                  String notesKbDocumentIRI) throws NotesException {

        return createNotesManager(domainOntology, notesKbOntologyIRI, notesKbDocumentIRI,
                                  DEFAULT_IMPORT_DOMAIN_ONTOLOGY_IN_NOTES_KB);
    }


    /**
     * Creates a notes KB manager for a domain ontology using the specified
     * domain ontology, document IRI and document location IRI.
     *
     * @param domainOntology
     * @param notesKbOntologyIRI
     * @param notesKbDocumentIRI
     * @param importDomainOntology
     * @throws NotesException
     */
    public static NotesManager createNotesManager(final OWLOntology domainOntology,
                                                  String notesKbOntologyIRI, String notesKbDocumentIRI,
                                                  boolean importDomainOntology) throws NotesException {

        return new NotesManager(domainOntology, notesKbOntologyIRI, notesKbDocumentIRI, importDomainOntology);
    }


    /**
     * Creates a notes KB manager for a domain ontology using the specified
     * domain ontology, document IRI, document location IRI and ChAO document IRI.
     *
     * @param domainOntology
     * @param notesKbOntologyIRI
     * @param notesKbDocumentIRI
     * @param chaoDocumentIRI
     * @throws NotesException
     */
    public static NotesManager createNotesManager(final OWLOntology domainOntology,
                                                  String notesKbOntologyIRI, String notesKbDocumentIRI,
                                                  String chaoDocumentIRI) throws NotesException {

        return createNotesManager(domainOntology, notesKbOntologyIRI, notesKbDocumentIRI,
                                  chaoDocumentIRI, DEFAULT_IMPORT_DOMAIN_ONTOLOGY_IN_NOTES_KB);
    }


    /**
     * Creates a notes KB manager for a domain ontology using the specified
     * domain ontology, document IRI, document location IRI and ChAO document IRI.
     *
     * @param domainOntology
     * @param notesKbOntologyIRI
     * @param notesKbDocumentIRI
     * @param chaoDocumentIRI
     * @param importDomainOntology
     * @throws NotesException
     */
    public static NotesManager createNotesManager(final OWLOntology domainOntology,
                                                  String notesKbOntologyIRI,
                                                  String notesKbDocumentIRI,
                                                  String chaoDocumentIRI,
                                                  boolean importDomainOntology) throws NotesException {

        return new NotesManager(domainOntology,
                                notesKbOntologyIRI,
                                notesKbDocumentIRI,
                                chaoDocumentIRI,
                                importDomainOntology);
    }


    /**
     * Creates a notes KB manager for the notesKB OWLOntology.
     *
     * @param notesKb
     * @throws NotesException
     */
    public static NotesManager createNotesManager(final OWLOntology notesKb) throws NotesException {

        return new NotesManager(notesKb, null);
    }


    /**
     * Creates a notes KB manager for the notesKB OWLOntology and ChAO document IRI.
     *
     * @param notesKb
     * @param chaoDocumentIRI
     * @throws NotesException
     */
    public static NotesManager createNotesManager(final OWLOntology notesKb,
                                                  String chaoDocumentIRI) throws NotesException {

        return new NotesManager(notesKb, chaoDocumentIRI);
    }


    // --- (private) constructors --- //

    private NotesManager(
            String notesKbOntologyIRI, String notesKbDocumentIRI) throws NotesException {
        try {
            OWLOntology notesKb = getNotesKb(notesKbOntologyIRI, notesKbDocumentIRI);
            init(notesKb);
        } catch (OWLOntologyCreationException e) {
            throw new NotesException("" , e);
        }
    }

    private NotesManager(
            String notesKbOntologyIRI, String notesKbDocumentIRI,
            String chaoDocumentIRI) throws NotesException {
        try {
            OWLOntology notesKb = getNotesKb(notesKbOntologyIRI, notesKbDocumentIRI, chaoDocumentIRI);
            init(notesKb);
        } catch (OWLOntologyCreationException e) {
            throw new NotesException("" , e);
        }
    }

    private NotesManager(final OWLOntology domainOntology,
                         String notesKbOntologyIRI,
                         String notesKbDocumentIRI,
                         boolean importDomainOntology) throws NotesException {
        try {
            OWLOntology notesKb = getNotesKb(domainOntology,
                                             notesKbOntologyIRI,
                                             notesKbDocumentIRI,
                                             importDomainOntology);
            init(notesKb);
        } catch (OWLOntologyCreationException e) {
            throw new NotesException("" , e);
        }
    }

    private NotesManager(final OWLOntology domainOntology,
                         String notesKbOntologyIRI,
                         String notesKbDocumentIRI,
                         String chaoDocumentIRI,
                         boolean importDomainOntology) throws NotesException {
        try {
            OWLOntology notesKb = getNotesKb(domainOntology,
                                             notesKbOntologyIRI,
                                             notesKbDocumentIRI,
                                             chaoDocumentIRI,
                                             importDomainOntology);
            init(notesKb);
        } catch (OWLOntologyCreationException e) {
            throw new NotesException("" , e);
        }
    }

    @Deprecated    //for now
    private NotesManager(final OWLOntology notesKb) throws NotesException {
        init(notesKb);
    }

    private NotesManager(final OWLOntology notesKb,
                         String chaoDocumentIRI) throws NotesException {
//			OWLOntology notesKb = getNotesKb(domainOntology, notesKbOntologyIRI, notesKbDocumentIRI, chaoDocumentIRI);
            OWLOntologyManager manager = notesKb.getOWLOntologyManager();
            addImportChAOStatementIfNecessary(manager, notesKb, chaoDocumentIRI);
            init(notesKb);
    }


    // --- NotesManager initializer --- //
    private void init(final OWLOntology notesKb) throws NotesException {
        if (notesKb != null) {
            this.notesKb = notesKb;
            this.notesKbManager = notesKb.getOWLOntologyManager();
            this.notesFactory = new NotesFactory(notesKb);
            this.ocFactory = new OntologyComponentFactory(notesKb);
        }
        else {
            throw new NotesException("A NoteskbManager can not be initialized with a null notes KB" );
        }
    }


    // --- static notes KB initializer methods --- //

    /**
     * Returns an initialized notes KB for a domain ontology using the specified
     * document IRI and document location IRI
     *
     * @param domainOntology
     * @param notesKbOntologyIRI
     * @param notesKbDocumentIRI
     * @param importDomainOntology
     * @throws OWLOntologyCreationException
     */
    static OWLOntology getNotesKb(final OWLOntology domainOntology,
                                  String notesKbOntologyIRI,
                                  String notesKbDocumentIRI,
                                  boolean importDomainOntology) throws OWLOntologyCreationException {
        return getNotesKb(domainOntology, notesKbDocumentIRI, notesKbDocumentIRI, null, importDomainOntology);
    }


    /**
     * Returns an initialized notes KB for a domain ontology using the specified
     * document IRI, document location IRI and ChAO document IRI
     *
     * @param domainOntology
     * @param notesKbOntologyIRI
     * @param notesKbDocumentIRI
     * @param chaoDocumentIRI
     * @param importDomainOntology
     * @throws OWLOntologyCreationException
     */
    static OWLOntology getNotesKb(final OWLOntology domainOntology,
                                  String notesKbOntologyIRI,
                                  String notesKbDocumentIRI,
                                  String chaoDocumentIRI,
                                  boolean importDomainOntology) throws OWLOntologyCreationException {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        IRI ontologyIRI = IRI.create(notesKbOntologyIRI);
        OWLOntology ontology = manager.createOntology(ontologyIRI);

        IRI documentIRI = IRI.create(notesKbDocumentIRI);
        manager.setOntologyDocumentIRI(ontology, documentIRI);

        SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
        manager.addIRIMapper(mapper);

        //add import Domain Ontology statement if required
        if (importDomainOntology) {
            OWLImportsDeclaration importsDomainOntDeclaration = new OWLImportsDeclarationImpl(domainOntology.getOntologyID()
                                                                                                            .getOntologyIRI()
                                                                                                            .get());
            AddImport addImportDomainOnt = new AddImport(ontology, importsDomainOntDeclaration);
            manager.applyChange(addImportDomainOnt);
        }

        //add import ChAO statement
        addImportChAOStatementIfNecessary(manager, ontology, chaoDocumentIRI);

        return ontology;
    }


    private static void addImportChAOStatementIfNecessary(
            OWLOntologyManager manager,
            OWLOntology ontology, String chaoDocumentIRI) throws UnloadableImportException {

        if (chaoDocumentIRI != null) {
            IRI chaoDocIRI = IRI.create(chaoDocumentIRI);
            SimpleIRIMapper mapper = new SimpleIRIMapper(CHAO_IRI, chaoDocIRI);
            manager.addIRIMapper(mapper);
        }

        OWLImportsDeclaration importsChAODeclaration = new OWLImportsDeclarationImpl(CHAO_IRI);
        AddImport addImportChAO = new AddImport(ontology, importsChAODeclaration);
        manager.applyChange(addImportChAO);

        manager.makeLoadImportRequest(importsChAODeclaration);
    }

    /**
     * Returns a notes KB for the specified document IRI and document location IRI.
     * The specified notes KB is supposed to have been initialized earlier
     * by importing a domain ontology and the ChAO.
     *
     * @param notesKbOntologyIRI
     * @param notesKbDocumentIRI
     * @throws OWLOntologyCreationException
     */
    static OWLOntology getNotesKb(String notesKbOntologyIRI,
                                  String notesKbDocumentIRI) throws OWLOntologyCreationException {

        return getNotesKb(notesKbDocumentIRI, notesKbDocumentIRI, null);
    }


    /**
     * Returns a notes KB for the specified document IRI and document location IRI.
     * The specified notes KB is supposed to have been initialized earlier
     * by importing a domain ontology and the ChAO.
     *
     * @param notesKbOntologyIRI
     * @param notesKbDocumentIRI
     * @throws OWLOntologyCreationException
     */
    static OWLOntology getNotesKb(String notesKbOntologyIRI,
                                  String notesKbDocumentIRI,
                                  String chaoDocumentIRI) throws OWLOntologyCreationException {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        IRI ontologyIRI = IRI.create(notesKbOntologyIRI);
        IRI documentIRI = IRI.create(notesKbDocumentIRI);

        SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
        manager.addIRIMapper(mapper);

        if (chaoDocumentIRI != null) {
            IRI chaoDocIRI = IRI.create(chaoDocumentIRI);
            mapper = new SimpleIRIMapper(CHAO_IRI, chaoDocIRI);
            manager.addIRIMapper(mapper);
        }

        manager.loadOntology(CHAO_IRI);
        OWLOntology ontology = manager.loadOntology(ontologyIRI);

//		
//		IRI ontologyIRI = IRI.create(notesKbOntologyIRI);
//		OWLOntology ontology = manager.createOntology(ontologyIRI);
//		
//		IRI documentIRI = IRI.create(notesKbDocumentIRI);
//		manager.setOntologyDocumentIRI(ontology, documentIRI);
//		
//		SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
//		manager.addIRIMapper(mapper);
//		
//		OWLDataFactory dataFactory = manager.getOWLDataFactory();
//		
//		//add import ChAO statement
//		if (chaoDocumentIRI != null) {
//			IRI chaoDocIRI = IRI.create(chaoDocumentIRI);
//			mapper = new SimpleIRIMapper(CHAO_IRI, chaoDocIRI);
//			manager.addIRIMapper(mapper);
//		}
//		
//		OWLImportsDeclaration importsChAODeclaration = new OWLImportsDeclarationImpl(dataFactory, CHAO_IRI);
////		AddImport addImportChAO = new AddImport(ontology, importsChAODeclaration);
////		manager.applyChange(addImportChAO);
//
//		manager.makeLoadImportRequest(importsChAODeclaration);
//		manager.getImports(ontology);

        return ontology;
    }

    // --- public utility getters --- //

    public OWLOntology getOWLOntology() {
        return notesKb;
    }

    public OWLOntologyManager getOWLOntologyManager() {
        return notesKbManager;
    }


    // --- private helper method(s) --- //

    private Annotation createSimpleNoteOfType(NoteType noteType) {
        Annotation res = null;
        switch (noteType) {
            case Advice:
                res = notesFactory.createAdvice(null);
                break;
            case Comment:
                res = notesFactory.createComment(null);
                break;
            case Example:
                res = notesFactory.createExample(null);
                break;
            case Explanation:
                res = notesFactory.createExplanation(null);
                break;
            case Question:
                res = notesFactory.createQuestion(null);
                break;
            case Review:
                res = notesFactory.createReview(null);
                break;
            case SeeAlso:
                res = notesFactory.createSeeAlso(null);
                break;

            default:
                System.out.println("Warning: Unknown annotation type: " + noteType);
            case Note:
                res = notesFactory.createAnnotation(null);
                break;
        }
        return res;
    }

    private Annotation createVoteOfType(NoteType noteType) {
        Annotation res = null;
        switch (noteType) {
            case Vote:
                res = notesFactory.createVote(null);
                break;
            case VoteAgreeDisagree:
                res = notesFactory.createVoteAgreeDisagree(null);
                break;
            case VoteFiveStars:
                res = notesFactory.createVoteFiveStars(null);
                break;

            default:
                System.out.println("Warning: Unknown vote annotation type: " + noteType);
                res = createSimpleNoteOfType(noteType);
                break;
        }
        return res;
    }

    private Annotation createProposalOfType(NoteType noteType) {
        Annotation res = null;
        switch (noteType) {
            case Proposal:
                res = notesFactory.createProposal(null);
                break;
            case ProposalForChangeHierarchy:
                res = notesFactory.createProposalChangeHierarchy(null);
                break;
            case ProposalForChangePropertyValue:
                res = notesFactory.createProposalChangePropertyValue(null);
                break;
            case ProposalForCreateEntity:
                res = notesFactory.createProposalCreateEntity(null);
                break;
            case ProposalForMerge:
                //TODO
                break;
            case ProposalForRetire:
                //TODO
                break;
            case ProposalForSplit:
                //TODO
                break;

            default:
                System.out.println("Warning: Unknown proposal annotation type: " + noteType);
                res = createSimpleNoteOfType(noteType);
                break;
        }
        return res;
    }


    // --- public access methods --- //

    // --- ontology component access methods --- //

    public OntologyComponent getOntologyComponent(String iri) {
        return ocFactory.getOntologyComponent(iri);
    }

    public Ontology getOntology(String iri) {
        return ocFactory.getOntology(iri);
    }

    public OntologyClass getOntologyClass(String iri) {
        return ocFactory.getOntologyClass(iri);
    }

    public OntologyProperty getOntologyProperty(String iri) {
        return ocFactory.getOntologyProperty(iri);
    }

    public OntologyIndividual getOntologyIndividual(String iri) {
        return ocFactory.getOntologyIndividual(iri);
    }


    // --- status & linguistic entity access methods --- //

    public LinguisticEntity createLinguisticEntity(String label, String language) throws NotesException {
        LinguisticEntity linguisticEntity = notesFactory.createLinguisticEntity(null);
        linguisticEntity.setLabel(label);
        linguisticEntity.setLanguage(language);
        return linguisticEntity;
    }

    public LinguisticEntity getLinguisticEntity(String iri) {
        return notesFactory.getLinguisticEntity(iri);
    }


    public StatusAnnotation createStatus(String nameIRI, String description) throws NotesException {
        StatusAnnotation status = notesFactory.createStatusAnnotation(nameIRI);
        status.setDescription(description);
        return status;
    }

    public StatusAnnotation getStatus(String iri) {
        return notesFactory.getStatusAnnotation(iri);
    }

    /**
     * Returns all note statuses declared in the notes kb and in ChAO.
     *
     * @return
     */
    public Collection<StatusAnnotation> getAllNoteStatuses() {
        return notesFactory.getAllStatusAnnotationObjects();
    }

    /**
     * Returns all notes statuses defined in ChAO.<br><br>
     * <B>Note:<B> The algorithm to calculate the note statuses to be returned
     * filters all note statuses based on their namespace, and keeps only those
     * that use the ChAO namespace. This implementation would return also instances
     * that are locally declared but use the ChAO namespace (which may or may not desired).
     *
     * @return
     */
    public Collection<StatusAnnotation> getAllChaoNoteStatuses() {
        /* solution 1:
	 * 		filters all note statuses based on their namespace, and keeps only those 
	 * 		that use the ChAO namespace. This implementation would return also instances
	 * 		that are locally declared but use the ChAO namespace (which may or may not desired).
		*/
        Set<StatusAnnotation> allNoteStatuses = notesFactory.getAllStatusAnnotationObjects();
        HashSet<StatusAnnotation> res = new HashSet<StatusAnnotation>();
        for (StatusAnnotation noteStatus : allNoteStatuses) {
            if (NotesFactory.getWrappedOWLIndividual(noteStatus).
                    asOWLNamedIndividual().getIRI().toString().
                                    startsWith(NotesConstants.CHAO_NAMESPACE)) {
                res.add(noteStatus);
            }
        }
        return res;
		
		/* solution 2:
		 * 		filters out all note statuses that were created in the local notes KB
		 * 		and returns all imported ones. Since normally ChAO is the only ontology 
		 * 		that is imported in the notes KB, this method would always return the
		 * 		expected set. This solution is totally agnostic about namespaces.
		*/
//		Set<StatusAnnotation> allNoteStatuses = notesFactory.getAllStatusAnnotationObjects();
//		Set<StatusAnnotation> allLocalNoteStatuses = notesFactory.getAllStatusAnnotationObjects(true, false);
//		Set<StatusAnnotation> res = new HashSet<StatusAnnotation>(allNoteStatuses);
//		res.removeAll(allLocalNoteStatuses);
//		return res;
    }

    /**
     * Returns all notes statuses defined in ChAO which are instances of the
     * class with the specified IRI.<br><br>
     *
     * @param classNameIRI the full name (IRI) of a subclass of AnnotationStatus class in ChAO.
     *                     For example "http://protege.stanford.edu/ontologies/ChAO/changes.owl#ProposalStatus".
     * @return
     */
    public Collection<StatusAnnotation> getAllChaoNoteStatusesOfType(String classNameIRI) {
        Collection<StatusAnnotation> allNoteStatuses = getAllChaoNoteStatuses();
        HashSet<StatusAnnotation> res = new HashSet<StatusAnnotation>();
        for (StatusAnnotation noteStatus : allNoteStatuses) {
            OWLNamedIndividual indNoteStatus = NotesFactory.getWrappedOWLIndividual(noteStatus).asOWLNamedIndividual();
            Set<OWLClass> assertedTypes = OWLUtil.getAssertedTypes(indNoteStatus, notesKb, true);
            for (OWLClass type : assertedTypes) {
                if (type.getIRI().toString().equals(classNameIRI)) {
                    res.add(noteStatus);
                }
            }
        }
        return res;
    }

    // -- delete methods -- //

    public void deleteNoteStatus(String iri) {
        StatusAnnotation status = notesFactory.getStatusAnnotation(iri);
        status.delete();
    }

    public void deleteLinguisticEntity(String iri) {
        LinguisticEntity linguisticEntity = notesFactory.getLinguisticEntity(iri);
        linguisticEntity.delete();
    }


    // --- notes access methods --- //

    // -- create methods -- //

    public Annotation createSimpleNote(String subject) throws NotesException {
        return createSimpleNote(NoteType.Note, subject);
    }

    public Annotation createSimpleNote(NoteType noteType, String subject) throws NotesException {
        Annotation annotation = createSimpleNoteOfType(noteType);
        annotation.setSubject(subject);
        //reset creation timestamp
        annotation.logCreationEvent();
        return annotation;
    }


    public Annotation createSimpleNote(String subject, String content) throws NotesException {
        return createSimpleNote(NoteType.Note, subject, content);
    }

    public Annotation createSimpleNote(NoteType noteType, String subject, String content) throws NotesException {
        Annotation annotation = createSimpleNoteOfType(noteType);
        annotation.setSubject(subject);
        annotation.setBody(content);
        //reset creation timestamp
        annotation.logCreationEvent();
        return annotation;
    }


    public Annotation createSimpleNote(String subject, String content, String author) throws NotesException {
        return createSimpleNote(NoteType.Note, subject, content, author);
    }

    public Annotation createSimpleNote(NoteType noteType,
                                       String subject,
                                       String content,
                                       String author) throws NotesException {
        Annotation annotation = createSimpleNoteOfType(noteType);
        annotation.setSubject(subject);
        annotation.setBody(content);
        annotation.setAuthor(author);
        //reset creation timestamp
        annotation.logCreationEvent();
        return annotation;
    }


    public Annotation createSimpleNote(String subject,
                                       String content,
                                       String author,
                                       AnnotatableThing annotatedThing) throws NotesException {
        return createSimpleNote(NoteType.Note, subject, content, author, annotatedThing);
    }

    public Annotation createSimpleNote(NoteType noteType,
                                       String subject,
                                       String content,
                                       String author,
                                       AnnotatableThing annotatedThing) throws NotesException {
        Annotation annotation = createSimpleNoteOfType(noteType);
        annotation.setSubject(subject);
        annotation.setBody(content);
        annotation.setAuthor(author);
        annotation.addAnnotates(annotatedThing);    //in order to improve performance we can safely use here "add" instead of "set",
        //because annotation has no value set yet for annotates property
        //reset creation timestamp
        annotation.logCreationEvent();
        return annotation;
    }


    public Annotation createSimpleNote(String subject, String content, String author,
                                       AnnotatableThing annotatedThing, Integer revision) throws NotesException {
        return createSimpleNote(NoteType.Note, subject, content, author, annotatedThing, revision);
    }

    public Annotation createSimpleNote(NoteType noteType, String subject, String content, String author,
                                       AnnotatableThing annotatedThing, Integer revision) throws NotesException {
        Annotation annotation = createSimpleNoteOfType(noteType);
        annotation.setSubject(subject);
        annotation.setBody(content);
        annotation.setAuthor(author);
        annotation.addAnnotates(annotatedThing);    //in order to improve performance we can safely use here "add" instead of "set",
        //because annotation has no value set yet for annotates property
        annotation.setCreatedInOntologyRevision(revision);
        //reset creation timestamp
        annotation.logCreationEvent();
        return annotation;
    }


    // -- retrieve methods -- //

    public Annotation getNote(String iri) {
        return notesFactory.getAnnotation(iri);
    }

    public Set<Annotation> getAllNotes() {
        return notesFactory.getAllAnnotationObjects();
    }

    public Set<Annotation> getAllArchivedNotes() {
        Set<Annotation> allNotes = notesFactory.getAllAnnotationObjects();
        Set<Annotation> result = new HashSet<Annotation>();
        for (Annotation note : allNotes) {
            if (NotesUtil.isArchived(note)) {
                result.add(note);
            }
        }
        return result;
    }

    public Set<Annotation> getAllThreadStarterNotes() {
        Set<Annotation> allNotes = notesFactory.getAllAnnotationObjects();
        return NotesUtil.filterThreadStarterNotes(allNotes);
    }


    public Set<Annotation> getAllNotesByAuthor(final String author) {
        Set<Annotation> allNotes = notesFactory.getAllAnnotationObjects();
        Set<Annotation> result = new HashSet<Annotation>();
        for (Annotation note : allNotes) {
            String noteAuthor = note.getAuthor();
            if (noteAuthor == author ||    //simple test for the case when both are null
                    noteAuthor.equals(author)) {
                result.add(note);
            }
        }
        return result;
    }


    public Set<? extends Annotation> getAllNotesOfType(NoteType noteType) {
        Set<? extends Annotation> res;
        switch (noteType) {
            case Advice:
                res = notesFactory.getAllAdviceObjects();
                break;
            case Comment:
                res = notesFactory.getAllCommentObjects();
                ;
                break;
            case Example:
                res = notesFactory.getAllExampleObjects();
                break;
            case Explanation:
                res = notesFactory.getAllExplanationObjects();
                break;
            case Proposal:
                res = notesFactory.getAllProposalObjects();
                break;
            case ProposalForChangeHierarchy:
                res = notesFactory.getAllProposalChangeHierarchyObjects();
                break;
            case ProposalForChangePropertyValue:
                res = notesFactory.getAllProposalChangePropertyValueObjects();
                break;
            case ProposalForCreateEntity:
                res = notesFactory.getAllProposalCreateEntityObjects();
                break;
            case Question:
                res = notesFactory.getAllQuestionObjects();
                break;
            case Review:
                res = notesFactory.getAllReviewObjects();
                break;
            case SeeAlso:
                res = notesFactory.getAllSeeAlsoObjects();
                break;
            case Vote:
                res = notesFactory.getAllVoteObjects();
                break;
            case VoteAgreeDisagree:
                res = notesFactory.getAllVoteAgreeDisagreeObjects();
                break;
            case VoteFiveStars:
                res = notesFactory.getAllVoteFiveStarsObjects();
                break;

            default:
                System.out.println("Warning: Unknown annotation type: " + noteType);
            case Note:
                res = notesFactory.getAllAnnotationObjects();
                break;
        }
        return res;
    }

    public Set<? extends Annotation> getAllThreadStarterNotesOfType(NoteType noteType) {
        Set<? extends Annotation> allNotes = getAllNotesOfType(noteType);
        return NotesUtil.filterThreadStarterNotes(allNotes);
    }

    public int getNotesCount(AnnotatableThing annotated) {
        return getNotesCount(annotated, true);
    }

    public int getNotesCount(AnnotatableThing annotatedThing, boolean countArchivedNotes) {
        return NotesUtil.countAssociatedNotes(annotatedThing, countArchivedNotes);
    }


    // -- archive/delete methods -- //

    public void archiveNote(String iri, Integer revision) {
        Annotation annotation = notesFactory.getAnnotation(iri);
        archiveNote(annotation, revision);
    }

    public void unarchiveNote(String iri) {    //, Integer revision ???
        Annotation annotation = notesFactory.getAnnotation(iri);
        unarchiveNote(annotation);
    }

    public void archiveThread(String iri, Integer revision) {
        Annotation annotation = notesFactory.getAnnotation(iri);
        archiveThreadRecursively(annotation, revision, new ArrayList<Annotation>());
    }

    public void unarchiveThread(String iri) {    //, Integer revision ???
        Annotation annotation = notesFactory.getAnnotation(iri);
        unarchiveThreadRecursively(annotation, new ArrayList<Annotation>());
    }

    private static void archiveNote(Annotation annotation, Integer revision) {
        annotation.setArchived(true);
        annotation.setArchivedInOntologyRevision(revision);
    }

    private static void unarchiveNote(Annotation annotation) {    //, Integer revision ???
        annotation.setArchived(false);
        annotation.setArchivedInOntologyRevision(null);
    }

    private static void archiveThreadRecursively(Annotation annotation,
                                                 Integer revision,
                                                 Collection<Annotation> visited) {
        if (!visited.contains(annotation)) {
            archiveNote(annotation, revision);
            visited.add(annotation);
            for (Annotation note : annotation.getAssociatedAnnotations()) {
                archiveThreadRecursively(note, revision, visited);
            }
        }
    }

    private static void unarchiveThreadRecursively(Annotation annotation,
                                                   Collection<Annotation> visited) {    //, Integer revision ???
        if (!visited.contains(annotation)) {
            unarchiveNote(annotation);
            visited.add(annotation);
            for (Annotation note : annotation.getAssociatedAnnotations()) {
                unarchiveThreadRecursively(note, visited);    //, revision
            }
        }
    }

    public void deleteNote(String iri) {
        Annotation annotation = notesFactory.getAnnotation(iri);
        annotation.delete();
    }


    // --- proposal access methods --- //


    public ProposalChangeHierarchy createProposalChangeHierarchy(String subject,
                                                                 String content,
                                                                 Collection<? extends OntologyComponent> oldParents,
                                                                 Collection<? extends OntologyComponent> newParents,
                                                                 String relationType,
                                                                 String reasonForChange,
                                                                 String contactInfo) throws NotesException {
        //Annotation proposal = createProposalOfType(ProposalForChangeHierarchy);
        ProposalChangeHierarchy proposal = notesFactory.createProposalChangeHierarchy(null);
        proposal.setSubject(subject);
        proposal.setBody(content);
        proposal.setOldParents(oldParents);
        proposal.setNewParents(newParents);
        proposal.setRelationType(relationType);
        proposal.setReasonForChange(reasonForChange);
        proposal.setContactInformation(contactInfo);

        return proposal;
    }


    public ProposalChangePropertyValue createProposalChangePropertyValue(String subject,
                                                                         String content,
                                                                         OntologyProperty property,
                                                                         String oldValue,
                                                                         String newValue,
                                                                         String reasonForChange,
                                                                         String contactInfo) throws NotesException {
        //Annotation proposal = createProposalOfType(NoteType.ProposalForChangePropertyValue);
        ProposalChangePropertyValue proposal = notesFactory.createProposalChangePropertyValue(null);
        proposal.setSubject(subject);
        proposal.setBody(content);
        proposal.setProperty(property);
        proposal.setOldValue(oldValue);
        proposal.setNewValue(newValue);
        proposal.setReasonForChange(reasonForChange);
        proposal.setContactInformation(contactInfo);

        return proposal;
    }


    private ProposalCreateEntity createProposalCreateEntity(String subject,
                                                            String content,
                                                            String entityId,
                                                            LinguisticEntity preferredName,
                                                            Collection<LinguisticEntity> synonyms,
                                                            LinguisticEntity definition,
                                                            Collection<? extends OntologyComponent> parents,
                                                            String reasonForChange,
                                                            String contactInfo) throws NotesException {
        //Annotation proposal = createProposalOfType(NoteType.ProposalForCreateEntity);
        ProposalCreateEntity proposal = notesFactory.createProposalCreateEntity(null);
        proposal.setSubject(subject);
        proposal.setBody(content);
        proposal.setEntityId(entityId);
        proposal.setPreferredName(preferredName);
        proposal.setSynonym(synonyms);
        proposal.setDefinition(definition);
        proposal.setParents(parents);
        proposal.setReasonForChange(reasonForChange);
        proposal.setContactInformation(contactInfo);

        return proposal;
    }


    public ProposalCreateEntity createProposalCreateClass(String subject,
                                                          String content,
                                                          String entityId,
                                                          LinguisticEntity preferredName,
                                                          Collection<LinguisticEntity> synonyms,
                                                          LinguisticEntity definition,
                                                          Collection<? extends OntologyClass> parents,
                                                          String reasonForChange,
                                                          String contactInfo) throws NotesException {
        return createProposalCreateEntity(subject, content, entityId,
                                          preferredName, synonyms, definition, parents, reasonForChange, contactInfo);
    }


    public ProposalCreateEntity createProposalCreateProperty(String subject,
                                                             String content,
                                                             String entityId,
                                                             LinguisticEntity preferredName,
                                                             Collection<LinguisticEntity> synonyms,
                                                             LinguisticEntity definition,
                                                             Collection<? extends OntologyProperty> parents,
                                                             String reasonForChange,
                                                             String contactInfo) throws NotesException {
        return createProposalCreateEntity(subject, content, entityId,
                                          preferredName, synonyms, definition, parents, reasonForChange, contactInfo);
    }


}

package org.protege.notesapi.notes;

import org.protege.notesapi.NotesException;
import org.protege.notesapi.notes.impl.DefaultAnnotatableThing;
import org.protege.notesapi.notes.impl.DefaultLinguisticEntity;
import org.protege.notesapi.notes.impl.DefaultStatus;
import org.protege.notesapi.util.OWLUtil;
import org.protege.notesapi.util.OntologyJavaMappingUtil;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/** @author csnyulas */
public class NotesFactory {

    static {
        OntologyJavaMapping.initMap();
    }

    private static final boolean INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE = true;

    private static final boolean INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS = true;

    private static final boolean TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS = true;

    private OWLOntology notesKb;

    private OWLOntologyManager manager;

    public NotesFactory(OWLOntology notesKb) throws NotesException {
        if (notesKb != null) {
            this.notesKb = notesKb;
            this.manager = notesKb.getOWLOntologyManager();
        }
        else {
            throw new NotesException("" ); //TODO: fix me
        }
    }


    ////**************** Utility methods ****************////

    /**
     * Returns an OWLNamedIndividual corresponding to the argument.
     * This method always returns a non-null value, but this does not
     * mean that the returned individual is contained in the ontology.
     *
     * @param iri
     * @return
     */
    private OWLNamedIndividual getOWLNamedIndividual(IRI iri) {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLNamedIndividual(iri);
    }

    public static OWLIndividual getWrappedOWLIndividual(AnnotatableThing topLevelClass) {
        if (topLevelClass instanceof DefaultAnnotatableThing) {
            return ((DefaultAnnotatableThing) topLevelClass).getWrappedOWLIndividual();
        }
        else {
            return null;
        }
    }

    public static OWLIndividual getWrappedOWLIndividual(Status topLevelClass) {
        if (topLevelClass instanceof DefaultStatus) {
            return ((DefaultStatus) topLevelClass).getWrappedOWLIndividual();
        }
        else {
            return null;
        }
    }

    public static OWLIndividual getWrappedOWLIndividual(LinguisticEntity topLevelClass) {
        if (topLevelClass instanceof DefaultLinguisticEntity) {
            return ((DefaultLinguisticEntity) topLevelClass).getWrappedOWLIndividual();
        }
        else {
            return null;
        }
    }

    private static String generateNoteId() {
        return "Note_" + UUID.randomUUID();
    }

    private OWLNamedIndividual createOWLIndividual(String name, OWLClass type) {
        if (name == null) {
            name = generateNoteId();
        }
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        OWLUtil.assertTypeOfIndividual(manager, notesKb, individual, type);
        return individual;
    }

    /**
     * This method returns an OWLNamedIndividual with the name <code>name</code>, if there
     * is a class assertion stating that the individual is of type <code>type</code>.
     *
     * @deprecated Check if this method makes sense and it could be useful in some situation.
     * If yes feel free to remove deprecation flag.
     */
    private OWLNamedIndividual getOWLIndividual(String name, OWLClass type) {
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        Set<OWLIndividualAxiom> axioms = notesKb.getAxioms(individual);
        for (OWLIndividualAxiom axiom : axioms) {
            if (axiom.isOfType(AxiomType.CLASS_ASSERTION) &&
                    ((OWLClassAssertionAxiom) axiom).getClassExpression().equals(type)) {
                return individual;
            }
        }

        return null;
    }

    ////******************************** non-Notes ********************************////

    ////**************** LinguisticEntity ****************////


    public OWLClass getLinguisticEntityClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "LinguisticEntity" ));
    }

    public LinguisticEntity createLinguisticEntity(String name) {
        final OWLClass typeClass = getLinguisticEntityClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        LinguisticEntity result = OntologyJavaMappingUtil.getSpecificObject(notesKb,
                                                                            individual,
                                                                            LinguisticEntity.class);
        return result;
    }


    public LinguisticEntity getLinguisticEntity(String name) {
        final OWLClass typeClass = getLinguisticEntityClass();
        //OWLNamedIndividual individual = getOWLIndividual(name, typeClass);
        //return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, LinguisticEntity.class);
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, LinguisticEntity.class);
        }
        else {
            return null;
        }
    }

    public Set<LinguisticEntity> getAllLinguisticEntityObjects() {
        return getAllLinguisticEntityObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<LinguisticEntity> getAllLinguisticEntityObjects(boolean transitive,
                                                               boolean includeImportsClosure) {
        final OWLClass typeClass = getLinguisticEntityClass();

        Set<LinguisticEntity> result = new HashSet<LinguisticEntity>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, LinguisticEntity.class));
        }
        return result;
    }


    ////**************** Status ****************////


    public OWLClass getStatusClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Status" ));
    }

    public Status createStatus(String name) {
        final OWLClass typeClass = getStatusClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        Status result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Status.class);
        return result;
    }


    public Status getStatus(String name) {
        final OWLClass typeClass = getStatusClass();
        //OWLNamedIndividual individual = getOWLIndividual(name, typeClass);
        //return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Status.class);
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Status.class);
        }
        else {
            return null;
        }
    }

    public Set<Status> getAllStatusObjects() {
        return getAllStatusObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<Status> getAllStatusObjects(boolean transitive,
                                           boolean includeImportsClosure) {
        final OWLClass typeClass = getStatusClass();

        Set<Status> result = new HashSet<Status>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Status.class));
        }
        return result;
    }


    ////**************** StatusAnnotation ****************////


    public OWLClass getStatusAnnotationClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "AnnotationStatus" ));
    }

    public StatusAnnotation createStatusAnnotation(String name) {
        final OWLClass typeClass = getStatusAnnotationClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        StatusAnnotation result = OntologyJavaMappingUtil.getSpecificObject(notesKb,
                                                                            individual,
                                                                            StatusAnnotation.class);
        return result;
    }


    public StatusAnnotation getStatusAnnotation(String name) {
        final OWLClass typeClass = getStatusAnnotationClass();
        //OWLNamedIndividual individual = getOWLIndividual(name, typeClass);
        //return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, StatusAnnotation.class);
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, StatusAnnotation.class);
        }
        else {
            return null;
        }
    }

    public Set<StatusAnnotation> getAllStatusAnnotationObjects() {
        return getAllStatusAnnotationObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<StatusAnnotation> getAllStatusAnnotationObjects(boolean transitive,
                                                               boolean includeImportsClosure) {
        final OWLClass typeClass = getStatusAnnotationClass();

        Set<StatusAnnotation> result = new HashSet<StatusAnnotation>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, StatusAnnotation.class));
        }
        return result;
    }


    ////******************************** Notes ********************************////

    ////**************** Advice ****************////


    public OWLClass getAdviceClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Advice" ));
    }

    public Advice createAdvice(String name) {
        final OWLClass typeClass = getAdviceClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        Advice result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Advice.class);
        result.logCreationEvent();
        return result;
    }


    public Advice getAdvice(String name) {
        final OWLClass typeClass = getAdviceClass();
        //OWLNamedIndividual individual = getOWLIndividual(name, typeClass);
        //return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Advice.class);
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Advice.class);
        }
        else {
            return null;
        }
    }

    public Set<Advice> getAllAdviceObjects() {
        return getAllAdviceObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<Advice> getAllAdviceObjects(boolean transitive,
                                           boolean includeImportsClosure) {
        final OWLClass typeClass = getAdviceClass();

        Set<Advice> result = new HashSet<Advice>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Advice.class));
        }
        return result;
    }


    ////**************** Annotation ****************////


    public OWLClass getAnnotationClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Annotation" ));
    }

    public Annotation createAnnotation(String name) {
        final OWLClass typeClass = getAnnotationClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        Annotation result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Annotation.class);
        result.logCreationEvent();
        return result;
    }


    public Annotation getAnnotation(String name) {
        final OWLClass typeClass = getAnnotationClass();
        //OWLNamedIndividual individual = getOWLIndividual(name, typeClass);
        //return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Annotation.class);
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Annotation.class);
        }
        else {
            return null;
        }
    }

    public Set<Annotation> getAllAnnotationObjects() {
        return getAllAnnotationObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<Annotation> getAllAnnotationObjects(boolean transitive,
                                                   boolean includeImportsClosure) {
        final OWLClass typeClass = getAnnotationClass();

        Set<Annotation> result = new HashSet<Annotation>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Annotation.class));
        }
        return result;
    }


    ////**************** Comment ****************////


    public OWLClass getCommentClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Comment" ));
    }

    public Comment createComment(String name) {
        final OWLClass typeClass = getCommentClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        Comment result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Comment.class);
        result.logCreationEvent();
        return result;
    }

    public Comment getComment(String name) {
        final OWLClass typeClass = getCommentClass();
        //OWLNamedIndividual individual = getOWLIndividual(name, typeClass);
        //return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Comment.class);
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Comment.class);
        }
        else {
            return null;
        }
    }

    public Set<Comment> getAllCommentObjects() {
        return getAllCommentObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<Comment> getAllCommentObjects(boolean transitive,
                                             boolean includeImportsClosure) {
        final OWLClass typeClass = getCommentClass();

        Set<Comment> result = new HashSet<Comment>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Comment.class));
        }
        return result;
    }


    ////**************** Example ****************////


    public OWLClass getExampleClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Example" ));
    }

    public Example createExample(String name) {
        final OWLClass typeClass = getExampleClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        Example result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Example.class);
        result.logCreationEvent();
        return result;
    }

    public Example getExample(String name) {
        final OWLClass typeClass = getExampleClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Example.class);
        }
        else {
            return null;
        }
    }

    public Set<Example> getAllExampleObjects() {
        return getAllExampleObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<Example> getAllExampleObjects(boolean transitive,
                                             boolean includeImportsClosure) {
        final OWLClass typeClass = getExampleClass();

        Set<Example> result = new HashSet<Example>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Example.class));
        }
        return result;
    }


    ////**************** Explanation ****************////


    public OWLClass getExplanationClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Explanation" ));
    }

    public Explanation createExplanation(String name) {
        final OWLClass typeClass = getExplanationClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        Explanation result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Explanation.class);
        result.logCreationEvent();
        return result;
    }

    public Explanation getExplanation(String name) {
        final OWLClass typeClass = getExplanationClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Explanation.class);
        }
        else {
            return null;
        }
    }

    public Set<Explanation> getAllExplanationObjects() {
        return getAllExplanationObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<Explanation> getAllExplanationObjects(boolean transitive,
                                                     boolean includeImportsClosure) {
        final OWLClass typeClass = getExplanationClass();

        Set<Explanation> result = new HashSet<Explanation>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Explanation.class));
        }
        return result;
    }


    // --- proposals --- //


    ////**************** Proposal ****************////


    public OWLClass getProposalClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Proposal" ));
    }

    public Proposal createProposal(String name) {
        final OWLClass typeClass = getProposalClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        Proposal result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Proposal.class);
        result.logCreationEvent();
        return result;
    }

    public Proposal getProposal(String name) {
        final OWLClass typeClass = getProposalClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Proposal.class);
        }
        else {
            return null;
        }
    }

    public Set<Proposal> getAllProposalObjects() {
        return getAllProposalObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<Proposal> getAllProposalObjects(boolean transitive,
                                               boolean includeImportsClosure) {
        final OWLClass typeClass = getProposalClass();

        Set<Proposal> result = new HashSet<Proposal>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Proposal.class));
        }
        return result;
    }


    ////**************** ProposalChangeHierarchy ****************////


    public OWLClass getProposalChangeHierarchyClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "ChangeHierarchyProposal" ));        //ATTENTION!!!
    }

    public ProposalChangeHierarchy createProposalChangeHierarchy(String name) {
        final OWLClass typeClass = getProposalChangeHierarchyClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        ProposalChangeHierarchy result = OntologyJavaMappingUtil.getSpecificObject(notesKb,
                                                                                   individual,
                                                                                   ProposalChangeHierarchy.class);
        result.logCreationEvent();
        return result;
    }

    public ProposalChangeHierarchy getProposalChangeHierarchy(String name) {
        final OWLClass typeClass = getProposalChangeHierarchyClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, ProposalChangeHierarchy.class);
        }
        else {
            return null;
        }
    }

    public Set<ProposalChangeHierarchy> getAllProposalChangeHierarchyObjects() {
        return getAllProposalChangeHierarchyObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<ProposalChangeHierarchy> getAllProposalChangeHierarchyObjects(boolean transitive,
                                                                             boolean includeImportsClosure) {
        final OWLClass typeClass = getProposalChangeHierarchyClass();

        Set<ProposalChangeHierarchy> result = new HashSet<ProposalChangeHierarchy>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, ProposalChangeHierarchy.class));
        }
        return result;
    }


    ////**************** ProposalChangePropertyValue ****************////


    public OWLClass getProposalChangePropertyValueClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "ChangePropertyValueProposal" ));        //ATTENTION!!!
    }

    public ProposalChangePropertyValue createProposalChangePropertyValue(String name) {
        final OWLClass typeClass = getProposalChangePropertyValueClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        ProposalChangePropertyValue result = OntologyJavaMappingUtil.getSpecificObject(notesKb,
                                                                                       individual,
                                                                                       ProposalChangePropertyValue.class);
        result.logCreationEvent();
        return result;
    }

    public ProposalChangePropertyValue getProposalChangePropertyValue(String name) {
        final OWLClass typeClass = getProposalChangePropertyValueClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, ProposalChangePropertyValue.class);
        }
        else {
            return null;
        }
    }

    public Set<ProposalChangePropertyValue> getAllProposalChangePropertyValueObjects() {
        return getAllProposalChangePropertyValueObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<ProposalChangePropertyValue> getAllProposalChangePropertyValueObjects(boolean transitive,
                                                                                     boolean includeImportsClosure) {
        final OWLClass typeClass = getProposalChangePropertyValueClass();

        Set<ProposalChangePropertyValue> result = new HashSet<ProposalChangePropertyValue>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb,
                                                                 individual,
                                                                 ProposalChangePropertyValue.class));
        }
        return result;
    }


    ////**************** ProposalCreateEntity ****************////


    public OWLClass getProposalCreateEntityClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "CreateEntityProposal" ));        //ATTENTION!!!
    }

    public ProposalCreateEntity createProposalCreateEntity(String name) {
        final OWLClass typeClass = getProposalCreateEntityClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        ProposalCreateEntity result = OntologyJavaMappingUtil.getSpecificObject(notesKb,
                                                                                individual,
                                                                                ProposalCreateEntity.class);
        result.logCreationEvent();
        return result;
    }

    public ProposalCreateEntity getProposalCreateEntity(String name) {
        final OWLClass typeClass = getProposalCreateEntityClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, ProposalCreateEntity.class);
        }
        else {
            return null;
        }
    }

    public Set<ProposalCreateEntity> getAllProposalCreateEntityObjects() {
        return getAllProposalCreateEntityObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<ProposalCreateEntity> getAllProposalCreateEntityObjects(boolean transitive,
                                                                       boolean includeImportsClosure) {
        final OWLClass typeClass = getProposalCreateEntityClass();

        Set<ProposalCreateEntity> result = new HashSet<ProposalCreateEntity>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, ProposalCreateEntity.class));
        }
        return result;
    }


    // --- end proposals --- //


    ////**************** Question ****************////


    public OWLClass getQuestionClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Question" ));
    }

    public Question createQuestion(String name) {
        final OWLClass typeClass = getQuestionClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        Question result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Question.class);
        result.logCreationEvent();
        return result;
    }

    public Question getQuestion(String name) {
        final OWLClass typeClass = getQuestionClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Question.class);
        }
        else {
            return null;
        }
    }

    public Set<Question> getAllQuestionObjects() {
        return getAllQuestionObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<Question> getAllQuestionObjects(boolean transitive,
                                               boolean includeImportsClosure) {
        final OWLClass typeClass = getQuestionClass();

        Set<Question> result = new HashSet<Question>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Question.class));
        }
        return result;
    }


    ////**************** Review ****************////


    public OWLClass getReviewClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Review" ));
    }

    public Review createReview(String name) {
        final OWLClass typeClass = getReviewClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        Review result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Review.class);
        result.logCreationEvent();
        return result;
    }

    public Review getReview(String name) {
        final OWLClass typeClass = getReviewClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Review.class);
        }
        else {
            return null;
        }
    }

    public Set<Review> getAllReviewObjects() {
        return getAllReviewObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<Review> getAllReviewObjects(boolean transitive,
                                           boolean includeImportsClosure) {
        final OWLClass typeClass = getReviewClass();

        Set<Review> result = new HashSet<Review>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Review.class));
        }
        return result;
    }


    ////**************** SeeAlso ****************////


    public OWLClass getSeeAlsoClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "SeeAlso" ));
    }

    public SeeAlso createSeeAlso(String name) {
        final OWLClass typeClass = getSeeAlsoClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        SeeAlso result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, SeeAlso.class);
        result.logCreationEvent();
        return result;
    }

    public SeeAlso getSeeAlso(String name) {
        final OWLClass typeClass = getSeeAlsoClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, SeeAlso.class);
        }
        else {
            return null;
        }
    }

    public Set<SeeAlso> getAllSeeAlsoObjects() {
        return getAllSeeAlsoObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<SeeAlso> getAllSeeAlsoObjects(boolean transitive,
                                             boolean includeImportsClosure) {
        final OWLClass typeClass = getSeeAlsoClass();

        Set<SeeAlso> result = new HashSet<SeeAlso>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, SeeAlso.class));
        }
        return result;
    }


    // --- votes --- //


    ////**************** Vote ****************////


    public OWLClass getVoteClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "Vote" ));
    }

    public Vote createVote(String name) {
        final OWLClass typeClass = getVoteClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        Vote result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Vote.class);
        result.logCreationEvent();
        return result;
    }

    public Vote getVote(String name) {
        final OWLClass typeClass = getVoteClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Vote.class);
        }
        else {
            return null;
        }
    }

    public Set<Vote> getAllVoteObjects() {
        return getAllVoteObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<Vote> getAllVoteObjects(boolean transitive,
                                       boolean includeImportsClosure) {
        final OWLClass typeClass = getVoteClass();

        Set<Vote> result = new HashSet<Vote>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, Vote.class));
        }
        return result;
    }


    ////**************** VoteAgreeDisagree ****************////


    public OWLClass getVoteAgreeDisagreeClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "AgreeDisagreeVote" ));        //ATTENTION!!!
    }

    public VoteAgreeDisagree createVoteAgreeDisagree(String name) {
        final OWLClass typeClass = getVoteAgreeDisagreeClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        VoteAgreeDisagree result = OntologyJavaMappingUtil.getSpecificObject(notesKb,
                                                                             individual,
                                                                             VoteAgreeDisagree.class);
        result.logCreationEvent();
        return result;
    }

    public VoteAgreeDisagree getVoteAgreeDisagree(String name) {
        final OWLClass typeClass = getVoteAgreeDisagreeClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, VoteAgreeDisagree.class);
        }
        else {
            return null;
        }
    }

    public Set<VoteAgreeDisagree> getAllVoteAgreeDisagreeObjects() {
        return getAllVoteAgreeDisagreeObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<VoteAgreeDisagree> getAllVoteAgreeDisagreeObjects(boolean transitive,
                                                                 boolean includeImportsClosure) {
        final OWLClass typeClass = getVoteAgreeDisagreeClass();

        Set<VoteAgreeDisagree> result = new HashSet<VoteAgreeDisagree>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, VoteAgreeDisagree.class));
        }
        return result;
    }


    ////**************** VoteFiveStars ****************////


    public OWLClass getVoteFiveStarsClass() {
        OWLDataFactory factory = manager.getOWLDataFactory();
        return factory.getOWLClass(IRI.create(OntologyJavaMapping.NAMESPACE + "FiveStarsVote" ));        //ATTENTION!!!
    }

    public VoteFiveStars createVoteFiveStars(String name) {
        final OWLClass typeClass = getVoteFiveStarsClass();
        OWLNamedIndividual individual = createOWLIndividual(name, typeClass);
        VoteFiveStars result = OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, VoteFiveStars.class);
        result.logCreationEvent();
        return result;
    }

    public VoteFiveStars getVoteFiveStars(String name) {
        final OWLClass typeClass = getVoteFiveStarsClass();
        OWLNamedIndividual individual = getOWLNamedIndividual(IRI.create(name));
        if (OWLUtil.getAssertedTypes(individual, notesKb, INCLUDE_IMPORT_CLOSURE_OPTION_FOR_INDIVIDUAL_TYPE)
                   .contains(typeClass)) {
            return OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, VoteFiveStars.class);
        }
        else {
            return null;
        }
    }

    public Set<VoteFiveStars> getAllVoteFiveStarsObjects() {
        return getAllVoteFiveStarsObjects(
                TRANSITIVE_OPTION_FOR_GET_ALL_INDIVIDUALS,
                INCLUDE_IMPORT_CLOSURE_OPTION_FOR_GET_ALL_INDIVIDUALS);
    }

    public Set<VoteFiveStars> getAllVoteFiveStarsObjects(boolean transitive,
                                                         boolean includeImportsClosure) {
        final OWLClass typeClass = getVoteFiveStarsClass();

        Set<VoteFiveStars> result = new HashSet<VoteFiveStars>();
        Set<OWLNamedIndividual> individuals = OWLUtil.getAssertedIndividuals(
                typeClass, notesKb, includeImportsClosure, transitive);
        for (OWLNamedIndividual individual : individuals) {
            result.add(OntologyJavaMappingUtil.getSpecificObject(notesKb, individual, VoteFiveStars.class));
        }
        return result;
    }


    // --- end votes --- //


}


package edu.stanford.protege.webprotege.migration;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.User;
import org.bson.Document;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2017
 *
 * Converts a Protege 3 MetaProject {@link ProjectInstance} to a Bson {@link Document}.
 */
public class BasicProjectDetailsConverter {

    private static final String DISPLAY_NAME = "displayName";

    private static final String IN_TRASH = "inTrash";

    private static final String DESCRIPTION = "description";

    private static final String OWNER = "owner";

    private final ProjectInstance projectInstance;

    /**
     * Creates a converter for the specified instance.
     * @param projectInstance The instance.
     */
    public BasicProjectDetailsConverter(@Nonnull ProjectInstance projectInstance) {
        this.projectInstance = checkNotNull(projectInstance);
    }

    /**
     * Converts the project instance to a document.
     * @return The document.
     */
    public Optional<Document> convertToDocument() {
        /*
            Example Document

            {
                "_id" : "3ac16b3a-6fcf-4a57-81a6-dcddfe201410",
                "displayName" : "Aero",
                "owner" : "Matthew Horridge",
                "inTrash" : true,
                "createdAt" : NumberLong("1490304075901"),
                "createdBy" : "Matthew Horridge",
                "modifiedAt" : NumberLong("1490324457829"),
                "modifiedBy" : "Matthew Horridge"
            }
         */
        Instance protegeInstance = projectInstance.getProtegeInstance();
        KnowledgeBase kb = protegeInstance.getKnowledgeBase();
        Slot displayNameSlot = kb.getSlot(DISPLAY_NAME);
        if(displayNameSlot == null) {
            throw new RuntimeException("displayName slot was not found in knowledge base");
        }
        Slot inTrashSlot = kb.getSlot(IN_TRASH);
        if(inTrashSlot == null) {
            throw new RuntimeException("inTrash slot was not found in knowledge base");
        }

        String displayName = Optional.ofNullable((String) protegeInstance.getOwnSlotValue(displayNameSlot)).orElse("");
        boolean inTrash = Optional.ofNullable((Boolean) protegeInstance.getOwnSlotValue(inTrashSlot)).orElse(false);
        String description = Optional.ofNullable(projectInstance.getDescription()).orElse("");

        String projectId = projectInstance.getName();
        User owner = projectInstance.getOwner();
        if(owner == null) {
            System.out.printf("[Project %s] Owner is not present. Skipping.\n", projectId);
            return Optional.empty();
        }
        String ownerName = owner.getName();
        if(ownerName == null) {
            System.out.printf("[Project %s] Owner name is not present. Skipping.\n", projectId);
            return Optional.empty();
        }

        Document document = new Document("_id", checkNotNull(projectId));
        document.append(OWNER, ownerName);
        document.append(DISPLAY_NAME, displayName);
        document.append(DESCRIPTION, description);
        document.append(IN_TRASH, inTrash);
        return Optional.of(document);
    }
}

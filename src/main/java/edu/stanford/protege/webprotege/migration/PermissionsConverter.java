package edu.stanford.protege.webprotege.migration;

import edu.stanford.smi.protege.server.metaproject.*;
import org.bson.Document;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2017
 *
 * Converts permissions from the Protege 3 metaproject into WebProtege 3 role assignments.  The converter
 * only sets assigned roles.  This means that the assigned roles collection needs to be rebuilt
 * once the conversion has been applied.
 */
public class PermissionsConverter {

    private static final String USER_NAME = "userName";

    private static final String CAN_MANAGE = "CanManage";


    private static final String PROJECT_ID = "projectId";

    private static final String ASSIGNED_ROLES = "assignedRoles";

    private static final String ROLE_CLOSURE = "roleClosure";

    private static final String ACTION_CLOSURE = "actionClosure";

    private final OperationConverter operationConverter = new OperationConverter();


    @Nonnull
    private final ProjectInstance projectInstance;

    public PermissionsConverter(@Nonnull ProjectInstance projectInstance) {
        this.projectInstance = checkNotNull(projectInstance);
    }

    @Nonnull
    public List<Document> convert() {
        List<Document> result = new ArrayList<>();
        String projectId = projectInstance.getName();
        // Owners can manage their own projects
        Document ownerDocument = new Document();
        result.add(ownerDocument);
        User owner = projectInstance.getOwner();
        if(owner == null) {
            System.out.printf("Owner of project is null.  Skipping permissions.\n");
            return Collections.emptyList();
        }
        String projectOwner = owner.getName();
        ownerDocument.append(USER_NAME, projectOwner);
        appendProjectRoles(ownerDocument, projectId, Collections.singleton(CAN_MANAGE));

        // Now for each user that is not the owner and has some permissions specified for the project
        for (GroupOperation groupOperation : projectInstance.getAllowedGroupOperations()) {
            Set<String> actions = toActions(groupOperation.getAllowedOperations());
            if (!actions.isEmpty()) {
                Group allowedGroup = groupOperation.getAllowedGroup();
                if(isWorld(allowedGroup)) {
                    // One document for all signed in users
                    Document document = new Document();
                    result.add(document);
                    appendProjectRoles(document, projectId, actions);
                }
                else {
                    // One document for each user that is not the owner
                    for (User user : allowedGroup.getMembers()) {
                        if (user != null && !user.getName().equals(projectOwner)) {
                            Document document = new Document();
                            result.add(document);
                            document.append(USER_NAME, user.getName());
                            appendProjectRoles(document, projectId, actions);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Nonnull
    private Set<String> toActions(@Nonnull Collection<Operation> operations) {
        return operationConverter.convert(operations);
    }

    private static void appendProjectRoles(@Nonnull Document document,
                                           @Nonnull String projectId,
                                           @Nonnull Set<String> roles) {
        document.append(PROJECT_ID, projectId);
        document.append(ASSIGNED_ROLES, roles);
        document.append(ROLE_CLOSURE, Collections.emptyList());
        document.append(ACTION_CLOSURE, Collections.emptyList());
    }

    private static boolean isWorld(@Nonnull Group allowedGroup) {
        return "World".equals(allowedGroup.getName());
    }


}

package edu.stanford.protege.webprotege.migration;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2017
 */
public class BasicProjectDetailsConverter_TestCase {

    private MetaProject metaProject;

    @Before
    public void setUp() throws Exception {
        URI metaProjectUri = getClass().getResource("/metaproject/metaproject.pprj").toURI();
        metaProject = new MetaProjectImpl(metaProjectUri);
    }


    @Test
    public void shouldConvertProject() {
        for(ProjectInstance pi : metaProject.getProjects()) {
            Optional<Document> document = new BasicProjectDetailsConverter(pi).convertToDocument();
            assertThat(document.isPresent(), is(true));
            Document theDocument = document.get();
            assertThat(theDocument.getString("_id"), is(pi.getName()));
            assertThat(theDocument.getString("description"), is(pi.getDescription()));
            assertThat(theDocument.getString("owner"), is(pi.getOwner().getName()));
            Slot inTrashSlot = pi.getProtegeInstance().getKnowledgeBase().getSlot("inTrash");
            assertThat(theDocument.getBoolean("inTrash"), is(((Boolean) pi.getProtegeInstance().getOwnSlotValue(inTrashSlot))));
            Slot displayNameSlot = pi.getProtegeInstance().getKnowledgeBase().getSlot("displayName");
            String displayName = (String) Optional.ofNullable(pi.getProtegeInstance().getOwnSlotValue(displayNameSlot)).orElse("");
            assertThat(theDocument.getString("displayName"), is(displayName));
        }
    }
}

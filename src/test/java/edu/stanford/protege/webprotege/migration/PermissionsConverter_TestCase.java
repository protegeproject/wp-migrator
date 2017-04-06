package edu.stanford.protege.webprotege.migration;

import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.List;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2017
 */
public class PermissionsConverter_TestCase {

    private MetaProject metaProject;

    @Before
    public void setUp() throws Exception {
        URI metaProjectUri = getClass().getResource("/metaproject/metaproject.pprj").toURI();
        metaProject = new MetaProjectImpl(metaProjectUri);
    }


    @Test
    public void shouldConvertProject() {
        for(ProjectInstance pi : metaProject.getProjects()) {
            PermissionsConverter converter = new PermissionsConverter(pi);
            List<Document> documents = converter.convert();
            documents.forEach(System.out::println);
        }
    }
}

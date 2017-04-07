package edu.stanford;

import com.google.common.base.Stopwatch;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Apr 2017
 */
public class MPStripper {

    public static void main(String[] args) {
        //  Group
        //  e27a98c2-ab30-4d53-9131-2680962c62c8
        //  e27a98c2-ab30-4d53-9131-2680962c62c8_Writers
        //  e27a98c2-ab30-4d53-9131-2680962c62c8_Commenters
        //  e27a98c2-ab30-4d53-9131-2680962c62c8_Readers

        // User
        // M Horridge

        // AllowedGroupOperation
        //

        // Project
        // e27a98c2-ab30-4d53-9131-2680962c62c8

        Project project = Project.loadProjectFromURI(URI.create("file:/tmp/srv/protege/webprotege-data/metaproject/metaproject.pprj"), new ArrayList());
        KnowledgeBase knowledgeBase = project.getKnowledgeBase();
        knowledgeBase.setGenerateEventsEnabled(false);
        knowledgeBase.setGenerateDeletingFrameEventsEnabled(false);
        knowledgeBase.setDispatchEventsEnabled(false);
        MetaProject metaProject = new MetaProjectImpl(project);

        ProjectInstance pi = metaProject.getProject("e27a98c2-ab30-4d53-9131-2680962c62c8");
        Instance instance = pi.getProtegeInstance();
        Set<Instance> instances = new HashSet<>();
        instances.add(instance);
        Set<Slot> excluded = new HashSet<>();
        excluded.add(knowledgeBase.getSlot("group"));
        collectGraph(instance, instances, excluded);
        System.out.println(instances.size() + " Instances");
        instances.forEach(System.out::println);
        System.out.println("Deleting instances");
        Stopwatch stopwatch = Stopwatch.createStarted();
        Collection<Instance> ins = knowledgeBase.getInstances();
        ins.forEach(i -> {
            if(!instances.contains(i) && !i.isSystem() && i instanceof SimpleInstance) {
                i.delete();
            }
        });
        System.out.println("Deleted instances in " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        System.out.println("Saving");
        project.save(new ArrayList());;

    }


    private static void collectGraph(Instance instance, Set<Instance> instances, Set<Slot> excluded) {
        for(Slot slot : instance.getOwnSlots()) {
            if (!slot.isSystem() && !excluded.contains(slot)) {
                for(Object val : instance.getOwnSlotValues(slot)) {
                    if(val instanceof Instance) {
                        Instance valInstance = (Instance) val;
                        if (!valInstance.getName().equals("metaproject_Instance_0")) {
                            if(instances.add(valInstance)) {
                                collectGraph(valInstance, instances, excluded);
                            }
                        }
                    }
                }
            }
        }
    }
}

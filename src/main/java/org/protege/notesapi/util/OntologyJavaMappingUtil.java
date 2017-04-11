package org.protege.notesapi.util;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/** @author csnyulas */
public class OntologyJavaMappingUtil {

    private static final Class<?>[] CONSTRUCTOR_PARAMETERS = {OWLNamedIndividual.class, OWLOntology.class};

    private static Map<IRI, Entry> ontologyClassNameMap = new HashMap<IRI, Entry>();

    private static Map<Class<?>, Entry> interfaceMap = new HashMap<Class<?>, Entry>();

    private static Map<Class<?>, Entry> implementationMap = new HashMap<Class<?>, Entry>();

    public static void add(String protegeClassName,
                           Class<?> javaInterface,
                           Class<?> javaImplementation) {
        Entry entry = new Entry(protegeClassName, javaInterface, javaImplementation);
        ontologyClassNameMap.put(IRI.create(protegeClassName), entry);
        interfaceMap.put(javaInterface, entry);
        implementationMap.put(javaImplementation, entry);
    }

//	/**
//	 * Creates object as the java interface passed as argument. Argument name is the name of the
//	 * created wrapped instance. If an instance with the name "name" already exists, it will return null.
//	 */
//	public static <X> X createObjectAs(OWLOntology kb, String name, Class<? extends X> javaInterface) {
//		if (name != null) {
//			if (kb.getInstance(name) != null) { //instance with this name already exists
//				return null;
//			}
//		}
//		Entry entry = interfaceMap.get(javaInterface);
//		if (entry == null) {
//			return null;
//		}
//		String clsName = entry.getOntologyClassName();
//		Cls	cls = kb.getCls(clsName);
//		OWLIndividual inst = cls.createDirectInstance(name);
//		return createJavaObject(getJavaImplementation(entry.getJavaImplementation(), javaInterface), inst, kb);
//	}
//
//
//	public static <X> X createObject(OWLOntology kb, String name, String protegeClsName, Class<? extends X> javaReturnInterface) {
//    	if (name != null && kb.getFrame(name) != null) {
//    		return null;
//    	}
//		Cls cls = kb.getCls(protegeClsName);
//		if (cls == null) {
//			return null;
//		}
//		String returnClsName = javaReturnInterface.getSimpleName();
//		Cls returnCls = kb.getCls(returnClsName);
//		if (returnClsName == null) {
//			return null;
//		}
//		if (!cls.equals(returnClsName) && !cls.hasSuperclass(returnCls)) {
//			return null;
//		}
//		Entry entry = ontologyClassNameMap.get(protegeClsName);
//		if (entry != null) { //hopefully most cases
//			return createJavaObject(getJavaImplementation(entry.getJavaImplementation(), javaReturnInterface), cls.createDirectInstance(name));
//		} else { //corresponding java class not found
//			for (Iterator iterator = cls.getSuperclasses().iterator(); iterator.hasNext();) {
//				Cls supercls = (Cls) iterator.next();
//				Entry e = ontologyClassNameMap.get(supercls.getName());
//				if (e != null) {
//					OWLIndividual wrappedInst = cls.createDirectInstance(name);
//					return createJavaObject(getJavaImplementation(e.getJavaImplementation(), javaReturnInterface), wrappedInst, kb);
//				}
//			}
//		}
//		return null;
//	}


    private static <X> X createJavaObject(Class<? extends X> javaImplementationClass,
                                          OWLNamedIndividual instance,
                                          OWLOntology ontology) {
        if (javaImplementationClass == null || instance == null) {
            return null;
        }

        X obj = null;
        try {
            Constructor<? extends X> constructor = javaImplementationClass.getConstructor(CONSTRUCTOR_PARAMETERS);
            obj = constructor.newInstance(new Object[]{instance, ontology});
        } catch (Exception e) {
            Logger.global.log(Level.SEVERE, "Creating Java Object failed. (Java Impl Class: " +
                    javaImplementationClass + ", Wrapped Protege instance: " +
                    instance + ")" , e);
        }
        return obj;
    }


//	public static <X> X getJavaObjectAs(OWLOntology kb, String name, Class<? extends X> javaInterface) {
//		OWLIndividual instance = kb.getInstance(name);
//		if (instance == null) {
//			return null;
//		}
//		Entry e = interfaceMap.get(javaInterface);
//		if (e == null) { return null; }
//		return createJavaObject (getJavaImplementation(e.getJavaImplementation(), javaInterface), instance, kb);
//	}

    public static <X> X getSpecificObject(OWLOntology kb,
                                          OWLNamedIndividual wrappedInst,
                                          Class<? extends X> javaReturnInterface) {
        if (wrappedInst == null) {
            return null;
        }
        Set<OWLClass> directTypes = OWLUtil.getAssertedDirectTypes(wrappedInst, kb, true);
        for (OWLClass type : directTypes) {
            Class<?> implClass = getJavaImplementation(type, kb);
            if (implClass != null) {
                //return the implementation corresponding to the FIRST VALID type that we have found
                //	THIS BEHAVIOR IS NOT DETERMINISTIC,
                //	but should work fine for individuals with single asserted type
                X specJavaObject = createJavaObject(getJavaImplementation(implClass, javaReturnInterface),
                                                    wrappedInst,
                                                    kb);
                if (specJavaObject != null) {
                    return specJavaObject;
                }
            }
        }
        //no implementation classes have been found for asserted types
        return null;
    }


//	public static boolean canAs(Object impl, OWLOntology ontology, Class<?> javaInterface) {
//		if (javaInterface.isAssignableFrom(impl.getClass())) {
//            return true;
//        }
//		if (!(impl instanceof AbstractWrappedOWLIndividual)) {
//			return false; //for now
//		}
//		OWLIndividual inst = ((AbstractWrappedOWLIndividual) impl).getWrappedOWLIndividual();
//		Class<?> implClass = getJavaImplementation(inst.getDirectType(), ontology);
//		if (implClass == null) { return false; }
//		return javaInterface.isAssignableFrom(implClass);
//	}
//
//
//	public static <X> X as(Object impl, Class<? extends X> javaInterface) {
//	        if (javaInterface.isAssignableFrom(impl.getClass())) {
//	            return javaInterface.cast(impl);
//	        }
//	        if (!(impl instanceof AbstractWrappedOWLIndividual)) {
//				return null; //for now
//			}
//			OWLIndividual inst = ((AbstractWrappedOWLIndividual) impl).getWrappedOWLIndividual();
//			return getSpecificObject(inst.getKnowledgeBase(), inst, javaInterface);
//	    }


    private static Class<?> getJavaImplementation(OWLClass cls, OWLOntology ontology) {
        if (cls == null) {
            return null;
        }
        Entry entry = ontologyClassNameMap.get(cls.getIRI());
        if (entry != null) { //hopefully most cases
            return entry.getJavaImplementation();
        }
        else { //corresponding java class not found
            List<OWLClass> superclassClosure = OWLUtil.calculateSuperclassClosure(cls, ontology, true);
            for (OWLClass supercls : superclassClosure) {
                Entry e = ontologyClassNameMap.get(supercls.getIRI());
                if (e != null) {
                    return e.getJavaImplementation();
                }
            }
        }
        return null;
    }

    private static <X> Class<X> getJavaImplementation(Class<?> implClass, Class<? extends X> javaInterface) {
        return (Class<X>) (javaInterface.isAssignableFrom(implClass) ? implClass : null);
    }


//	public static void dispose() {
//		implementationMap.clear();
//		interfaceMap.clear();
//		ontologyClassNameMap.clear();
//	}


    private static class Entry {

        private String ontologyClassName;

        private Class<?> javaInterface;

        private Class<?> javaImplementation;

        public Entry(String protegeClass,
                     Class<?> javaInterface,
                     Class<?> javaImplementation) {
            this.ontologyClassName = protegeClass;
            this.javaInterface = javaInterface;
            this.javaImplementation = javaImplementation;
        }

        public String getOntologyClassName() {
            return ontologyClassName;
        }

        /** @author csnyulas */
public Class<?> getJavaInterface() {
            return javaInterface;
        }

        /** @author csnyulas */
public Class<?> getJavaImplementation() {
            return javaImplementation;
        }
    }

}

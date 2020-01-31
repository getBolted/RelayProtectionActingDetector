import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.Collection;
import java.util.Set;

public class GetDataFromOntology {

    public static Set<OWLNamedIndividual> getIndividualsOfClass(OWLClass cls, OWLReasoner reasoner) {
        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(cls, true);
        Set<OWLNamedIndividual> instancesFlattened = instances.getFlattened();
        return instancesFlattened;
    }

    public static Collection<OWLIndividual> getIndividualsFromProperty(OWLIndividual ind, OWLOntology ont,
                                                                      OWLObjectProperty property) {
        Collection<OWLIndividual> objectPropertyValues =  EntitySearcher.getObjectPropertyValues(ind, property, ont);
        return objectPropertyValues;
    }

    public static Collection<OWLLiteral> getDataPropertyValue(OWLIndividual ind, OWLOntology ont,
                                                               OWLDataProperty property) {
        Collection<OWLLiteral> dataPropertyValue = null;
        dataPropertyValue =  EntitySearcher.getDataPropertyValues(ind, property, ont);
        return dataPropertyValue;
    }

}


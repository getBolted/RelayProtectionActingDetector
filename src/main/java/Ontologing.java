import com.google.common.collect.Iterables;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.drools.owl.axioms.A;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

public class Ontologing {
    public static String faultName;
    private File ontFile;
    private OWLDataFactory dataFactory;
    private OWLReasoner reasoner;
    private String ontNameSpace;
    private OWLOntologyManager manager;
    private OWLOntology ontology;
    public OWLClass eqClass;
    public OWLClass faultClass;
    private ArrayList<String> eqIndls = new ArrayList<>();
    private ArrayList<String> fauIndls = new ArrayList<>();

    private ArrayList<String> items = new ArrayList<String>();
    private Set<OWLAxiom> inferredAxioms;

    public Ontologing(Object[] args){
        this.ontFile = (File) args[0];
        this.ontology = (OWLOntology) args[1];
        this.dataFactory = (OWLDataFactory) args[2];
        this.reasoner = (OWLReasoner) args[3];
        this.ontNameSpace = (String) args[4];
        this.manager = (OWLOntologyManager) args[5];
        this.eqClass = dataFactory.getOWLClass(IRI.create(ontNameSpace + "Equipment"));
        this.faultClass = dataFactory.getOWLClass(IRI.create(ontNameSpace + "Fault"));
    }

    public ArrayList<String> getEqIndls() {
        return eqIndls;
    }

    public ArrayList<String> getFauIndls() {
        return fauIndls;
    }

    private ArrayList<String> getIndividualsByClass (OWLClass owlClass, OWLReasoner owlReasoner){
        ArrayList<String> list = new ArrayList<String>();
        Set<OWLNamedIndividual> individuals = GetDataFromOntology.getIndividualsOfClass(owlClass, owlReasoner);
            for (OWLNamedIndividual currentIndividual : individuals) {
                String individualName = currentIndividual.getIRI().getShortForm();
                list.add(individualName);
            }
        return list;
    }


    public boolean ifEqOccurs(OWLClass cl, String name){
        if (getIndividualsByClass(cl, reasoner).contains(name)) {
            items.add(name);
            return true;
        }else {
            return false;
        }
    }

    public void propertyAssertion(){
        OWLNamedIndividual eqIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontNameSpace+items.get(0)));
        OWLNamedIndividual fauIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ontNameSpace+items.get(1)));
        OWLObjectProperty isOn = dataFactory.getOWLObjectProperty(IRI.create(ontNameSpace
                + "isOn"));
        OWLObjectPropertyAssertionAxiom isOn_propertyAssertion =
                dataFactory.getOWLObjectPropertyAssertionAxiom(isOn, fauIndividual, eqIndividual );
        AddAxiom isONFunctionAxiom = new AddAxiom(ontology, isOn_propertyAssertion);
        manager.applyChange(isONFunctionAxiom);
        System.out.println(items.get(1) + " now is on " + items.get(0));
    }

    public void ruleApplying() throws SWRLBuiltInException, SWRLParseException {
        System.out.println("Wait a min, i need to do some stuff...");
        SWRLRuleEngine ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);
        SWRLAPIRule S1ActProt = ruleEngine.createSWRLRule ("S1ActProt",
                "Equipment(?y)^Fault(?x)^Protection(?z)^ProtectFrom(?z,?x)^isProtectedBy(?y,?z)^isOn(?x,?y)->act(?z,1)");
        ruleEngine.infer();
        inferredAxioms = ruleEngine.getInferredOWLAxioms();
    }

    public void getProtectionsActed(){
        ArrayList<String> actedProts = new ArrayList<String>();
        OWLDataProperty act = dataFactory.getOWLDataProperty(IRI.create(ontNameSpace + "act"));
        Set<OWLNamedIndividual> individuals = ontology.getIndividualsInSignature();
        for (OWLNamedIndividual ind: individuals) {
            Collection<OWLLiteral> dpValues = GetDataFromOntology.getDataPropertyValue(ind, ontology, act);
            if (!dpValues.isEmpty()){
                int dpValue = Iterables.get(dpValues, 0).parseInteger();
                if (dpValue == 1){
                    actedProts.add(ind.getIRI().getShortForm());
                }
            }
        }
        System.out.println("Well, here are some acted prots list: ");
        actedProts.forEach(System.out::println);
    }

    public void getHierarchyTree(){
        GetDataFromOntology.getIndividualsOfClass(eqClass, reasoner).forEach(ind -> eqIndls.add(ind.getIRI().getShortForm()));
        GetDataFromOntology.getIndividualsOfClass(faultClass, reasoner).forEach(ind -> fauIndls.add(ind.getIRI().getShortForm()));
    }

    public void removeInferredAxioms(){
        manager.removeAxioms(ontology, inferredAxioms);
        System.out.println("Removing recent axioms for the next try! ");
    }
}

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.parser.SWRLParseException;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static boolean end = false;
    public static boolean ok = false;
    public static String eqName;
    public static String fauName;


    public static void main(String[] args) throws SWRLParseException, SWRLBuiltInException {
        OntPreps inputOnt = new OntPreps();
        inputOnt.fileOpen("src/main/resources/ontDetProt.owl");
        inputOnt.ontologyProcessing();
        Ontologing ontologing = new Ontologing(inputOnt.returnOntObjects());

            while (ok == false){
                System.out.println("Enter Equipment: ");
                Scanner eq = new Scanner(System.in);
                eqName = eq.next();
                ok = ontologing.ifEqOccurs(ontologing.eqClass, eqName);
                if (ok){
                    System.out.println("Nice!");
                }else {
                    System.out.println("Bad try! Repeat again!");
                }
            }
            ok = false;
            while (ok == false){
                System.out.println("Enter Fault: ");
                Scanner fau = new Scanner(System.in);
                fauName = fau.next();
                ok = ontologing.ifEqOccurs(ontologing.faultClass, fauName);
                if (ok){
                    System.out.println("Nice!");
                } else {
                System.out.println("Bad try! Repeat again!");
                }
            }
            ok = false;
            ontologing.propertyAssertion();
            ontologing.ruleApplying();
            ontologing.getProtectionsActed();
    }
}


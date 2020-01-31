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
        ontologing.getHierarchyTree();

        while (end == false) {
            while (ok == false) {
                System.out.println("Here are some pieces of substation equipment to check protection on: ");
                ontologing.getEqIndls().forEach(System.out::println);
                System.out.print("Enter your choice for the equipment:");
                Scanner eq = new Scanner(System.in);
                eqName = eq.next();
                ok = ontologing.ifEqOccurs(ontologing.eqClass, eqName);
                if (ok) {
                    System.out.println("Nice!");
                } else {
                    System.out.println("Bad try! Repeat again!");
                }
            }
            ok = false;
            while (ok == false) {
                System.out.println("And also faults list:");
                ontologing.getFauIndls().forEach(System.out::println);
                System.out.print("Enter your choice for the fault:");
                Scanner fau = new Scanner(System.in);
                fauName = fau.next();
                ok = ontologing.ifEqOccurs(ontologing.faultClass, fauName);
                if (ok) {
                    System.out.println("Nice!");
                } else {
                    System.out.println("Bad try! Repeat again!");
                }
            }
            ok = false;
            ontologing.propertyAssertion();
            ontologing.ruleApplying();
            ontologing.getProtectionsActed();

            System.out.println("That's all? 1/0");
            Scanner endScan = new Scanner(System.in);
            int yn = endScan.nextInt();
            if (yn == 0){
                ontologing.removeInferredAxioms();
            }else if (yn == 1){
                System.out.println("Bye!");
                end = true;
            }else {
                System.out.println("Is it really hard to enter just 1 or 0??? Bye again!");
                end = true;
            }
        }
    }
}


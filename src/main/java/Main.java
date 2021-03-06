import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.parser.SWRLParseException;

import java.io.*;
import java.util.ArrayList;
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

        System.out.println("Here are some pieces of substation equipment to check protection on: ");
        for (int i = 0; i < ontologing.getEqIndls().size(); i++) {
            if (i % 10 == 0 && i != 0) {
                System.out.print("\n");
            }
            System.out.print(ontologing.getEqIndls().get(i) + ", ");
        }
        System.out.print("\n");
        while (ok == false) {
            System.out.print("Enter your choice for the equipment: ");
            Scanner scanner = new Scanner(System.in);
            eqName = scanner.next();
            ok = ontologing.ifEqOccurs(ontologing.eqClass, eqName);
            if (ok) {
                System.out.println("Nice!");
            } else {
                System.out.println("Bad try! Repeat again!");
            }
        }
        ok = false;
        System.out.println("And also faults list:");
        for (int i = 0; i < ontologing.getFauIndls().size(); i++) {
            if (i % 5 == 0 && i != 0) {
                System.out.print("\n");
            }
            System.out.print(ontologing.getFauIndls().get(i) + ", ");
        }
        System.out.print("\n");
        while (ok == false) {
            System.out.print("Enter your choice for the fault: ");
            Scanner scanner = new Scanner(System.in);
            fauName = scanner.next();
            ok = ontologing.ifEqOccurs(ontologing.faultClass, fauName);
            if (ok) {
                System.out.println("Nice!");
            } else {
                System.out.println("Bad try! Repeat again!");
            }
        }
        ok = false;
        eqName = null;
        ontologing.propertyAssertion();
        ontologing.ruleApplying();
        ontologing.getProtectionsActed();
        System.out.println("DONE!");
    }
}


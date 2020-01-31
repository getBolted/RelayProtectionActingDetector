import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.parser.SWRLParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputOutput {
    public static boolean ok = false;
    public static String eqName;
    public static String fauName;
    private Ontologing ontologing;

    public InputOutput(Ontologing ontologing) throws SWRLParseException, SWRLBuiltInException {
        this.ontologing = ontologing;
    }

    public void go(){
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
            BufferedReader eq = new BufferedReader(new InputStreamReader(System.in));
            try {
                eqName = eq.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            BufferedReader fau = new BufferedReader(new InputStreamReader(System.in));
            try {
                fauName = fau.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        try {
            ontologing.ruleApplying();
        } catch (SWRLBuiltInException e) {
            e.printStackTrace();
        } catch (SWRLParseException e) {
            e.printStackTrace();
        }
        ontologing.getProtectionsActed();
    }
}

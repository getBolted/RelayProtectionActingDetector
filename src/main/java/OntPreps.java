import org.apache.commons.lang3.StringUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.factory.SWRLAPIFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class OntPreps {

    private File file = null;
    private OWLOntology ontology = null;
    private OWLDataFactory dataFactory = null;
    private OWLReasoner reasoner = null;
    private String ontNameSpace = null;
    private OWLOntologyManager manager = null;
    private String inputFilePath = null;

    public OntPreps() {
    }

    public Object[] returnOntObjects(){
        return new Object[]{file, ontology, dataFactory, reasoner, ontNameSpace, manager};
    }

    public void fileWindowOpen() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Ontology .owl files", "owl");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setVisible(true);
            fileChooser.setFileFilter(filter);
            int ret = fileChooser.showDialog(null, "File open");
            if (ret == JFileChooser.APPROVE_OPTION) {
                fileChooser.setVisible(false);
                file = fileChooser.getSelectedFile();
                inputFilePath = file.getPath();
                System.out.println("Ontology file " + inputFilePath + " has been opened!");
            }else if (ret == JFileChooser.CANCEL_OPTION){
                System.exit(0);
            }
    }

    public void fileOpen(String filePath){
        file = new File(filePath);
        inputFilePath = filePath;
    }

    public void ontologyProcessing() {
        manager = OWLManager.createOWLOntologyManager();
        try {
            ontology = manager.loadOntologyFromOntologyDocument(file);
            System.out.println("Loading ontology structure...");
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }

        ontNameSpace = StringUtils.substringBetween(ontology.getOntologyID().getOntologyIRI().toString(),
                "(", ")") + "#";

        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        dataFactory = manager.getOWLDataFactory();
        reasoner = reasonerFactory.createReasoner(ontology);

        SWRLRuleEngine ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);
        ruleEngine.infer();
    }

    public String outputtingOnt(boolean done){
        String outputFilePath = "";
        if (done){
            OutputStream outputStream;
            try {
                outputFilePath = inputFilePath.split("\\.")[0] + "_changed.owl";
                System.out.println("Processed ontology outputting to file: " + outputFilePath);
                outputStream = new FileOutputStream(outputFilePath);
                try {
                    manager.saveOntology(ontology, outputStream);
                } catch (OWLOntologyStorageException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return outputFilePath;
    }
}

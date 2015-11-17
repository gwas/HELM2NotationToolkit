package org.helm2;

import java.io.IOException;

import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerStore;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.ComplexNotationParser;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.StateMachineParser;
import org.helm.notation2.parser.ExceptionParser.ExceptionState;
import org.helm2.Calculation.ExtinctionCoefficient;
import org.helm2.exception.HELM2HandledException;
import org.jdom.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SMILESTest {
  ParserHELM2 parser;

  @Test
  public void testSMILES() throws ExceptionState, IOException, JDOMException, NotationException, MonomerException, org.jdom2.JDOMException, StructureException, CalculationException,
      HELM2HandledException {
    parser = new ParserHELM2();

    String test = "PEPTIDE1{D}|PEPTIDE2{E}|PEPTIDE3{L.R}|CHEM1{[MCC]}$PEPTIDE1,PEPTIDE2,1:R2-1:R3|PEPTIDE3,PEPTIDE1,2:R2-1:R3$$$";
    test += "V2.0";
    parser.parse(test);
    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.getHELM2Notation(),
        new InterConnections());
    SMILES smiles = new SMILES();
    String smile = smiles.getSMILESForAll(containerhelm2.getListOfHandledMonomers(containerhelm2.getListOfMonomerNotation(containerhelm2.getHELM2Notation().getListOfPolymers())));
    // Assert.assertEquals(smile,
    // "OC(=O)C1CCC(CN2C(=O)C=CC2=O)CC1.[H]N[C@@H](CCC(=O)C(=O)[C@@H](N[H])CC(=O)C(=O)[C@H](CCCNC(N)=N)NC(=O)[C@@H](N[H])CC(C)C)C(O)=O");
  }

  @Test
  public void testSMILESOLD() throws ExceptionState, IOException, JDOMException, NotationException, MonomerException, org.jdom2.JDOMException, StructureException, CalculationException,
      HELM2HandledException {

    String test = "PEPTIDE1{D}|PEPTIDE2{E}|PEPTIDE3{L.R}|CHEM1{[MCC]}$PEPTIDE1,PEPTIDE2,1:R2-1:R3|PEPTIDE3,PEPTIDE1,2:R2-1:R3$$$";

    String smiles = ComplexNotationParser.getComplexPolymerSMILES(test);
    System.out.println(smiles);
  }

}

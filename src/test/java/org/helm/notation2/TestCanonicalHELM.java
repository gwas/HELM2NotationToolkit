package org.helm.notation2;

import java.io.IOException;
import java.math.BigDecimal;

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.ComplexNotationParser;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.StateMachineParser;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.exceptionparser.HELM1ConverterException;
import org.jdom.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCanonicalHELM {

  StateMachineParser parser;

  @Test
  public void testCanonicalSHELM() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{C}|PEPTIDE2{Y.V.N.L.I}$PEPTIDE2,PEPTIDE1,5:R2-2:R3$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());

    containerhelm2.getCanonicalHELM();
  }

  @Test
  public void testCanonicalHELMExtended() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|RNA3{R(A)P.R(C)}$RNA2,RNA3,5:pair-2:pair|RNA2,RNA3,2:pair-5:pair$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());

    containerhelm2.getCanonicalHELM();
  }

  @Test
  public void testCanonicalHELMExtended2() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{R.I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    Assert.assertEquals(containerhelm2.getCanonicalHELM(), ComplexNotationParser.getCanonicalNotation(test, false));
  }

  @Test
  public void testCanonicalHELMExtendedWithCounts() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{R'3'.I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    containerhelm2.getCanonicalHELM();

  }

  @Test(expectedExceptions = HELM1ConverterException.class)
  public void testCanonicalHELMExtendedWithCountsWithException() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{R'3-5'.I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    containerhelm2.getCanonicalHELM();

  }

  @Test
  public void testCanonicalHELMExtendedWithMonomerNotationList() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{(R.T.L)'3'.I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    System.out.println(containerhelm2.getCanonicalHELM());

  }

  @Test(expectedExceptions = HELM1ConverterException.class)
  public void testCanonicalHELMExtendedWithMonomerNotationGroup() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{(R,T).I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    containerhelm2.getCanonicalHELM();

  }

  @Test(expectedExceptions = HELM1ConverterException.class)
  public void testCanonicalHELMExtendedWithBLOB() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|BLOB1{Hallo}|PEPTIDE1{I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    containerhelm2.getCanonicalHELM();

  }

  @Test(expectedExceptions = HELM1ConverterException.class)
  public void testCanonicalHELMExtendedWithUnknownMonomer() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(N)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{_.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    containerhelm2.getCanonicalHELM();
  }

  @Test(expectedExceptions = HELM1ConverterException.class)
  public void testCanonicalHELMExtendedWithUnknownMonomerTypeforPeptide() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{X.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    containerhelm2.getCanonicalHELM();
  }

  @Test(expectedExceptions = HELM1ConverterException.class)
  public void testCanonicalHELMExtendedWithUnknownMonomerTypeforRNA() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(N)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    containerhelm2.getCanonicalHELM();
  }

  @Test(expectedExceptions = HELM1ConverterException.class)
  public void testCanonicalHELMConnectionWithGroup() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE2{R.E}|PEPTIDE3{C}$G1,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());

    containerhelm2.getCanonicalHELM();
  }

  @Test
  public void testCanonicalHELMConnection() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException, CTKException, ClassNotFoundException,
      HELM1ConverterException

  {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{C}|RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE2{R.E}|PEPTIDE3{R.I.P}|RNA3{R(A)P.R(C)}$PEPTIDE1,PEPTIDE2,1:R2-2:R3|RNA2,RNA3,2:pair-5:pair|RNA2,RNA3,5:pair-2:pair$$$";
    for (int i = 0; i < test.length(); i++) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());

    Assert.assertEquals(containerhelm2.getCanonicalHELM(), ComplexNotationParser.getCanonicalNotation("PEPTIDE1{C}|RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE2{R.E}|PEPTIDE3{R.I.P}|RNA3{R(A)P.R(C)}$PEPTIDE1,PEPTIDE2,1:R2-2:R3$RNA2,RNA3,2:pair-5:pair|RNA2,RNA3,5:pair-2:pair$$", false));
  }

  @Test
  public void testCanonicalHELMCHEM() throws ExceptionState, IOException, JDOMException, ClassNotFoundException, HELM1ConverterException, MonomerException, org.jdom2.JDOMException, CTKSmilesException,
      CTKException {
    parser = new StateMachineParser();

    String test = "CHEM1{MCC}$$$$";
    for (int i = 0; i < test.length(); i++) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    containerhelm2.getCanonicalHELM();

  }

  @Test
  public void testCanonicalSMILES() throws ExceptionState, IOException,
      JDOMException, ClassNotFoundException, HELM1ConverterException,
      MonomerException, org.jdom2.JDOMException, CTKSmilesException, CTKException, NotationException, StructureException {

    parser = new StateMachineParser();

    String test = "CHEM1{[[*]OCCOCCOCCO[*] |$_R1;;;;;;;;;;;_R3$|]}$$$$";
    for (int i = 0; i < test.length(); i++) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    containerhelm2.getCanonicalHELM();

  }

  @Test
  public void testCanonicalSMILES2() throws ExceptionState, IOException,
      JDOMException, ClassNotFoundException, HELM1ConverterException,
      MonomerException, org.jdom2.JDOMException, CTKSmilesException, CTKException, NotationException, StructureException {

    parser = new StateMachineParser();

    String test = "CHEM1{[[*]OCCOCCOCCO[*] |$_R1;;;;;;;;;;;_R3$|]}$$$$";
    for (int i = 0; i < test.length(); i++) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    containerhelm2.getCanonicalHELM();

  }

}

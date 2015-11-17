package org.helm2;

import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.ExtinctionCoefficientCalculator;
import org.helm.notation2.parser.StateMachineParser;
import org.helm.notation2.parser.ExceptionParser.ExceptionState;
import org.helm2.Calculation.ExtinctionCoefficient;
import org.helm2.exception.HELM2HandledException;
import org.jdom.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExtinctionCalculatorTest {
  StateMachineParser parser;


  @Test
  public void testCalculationOnePeptide() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException

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

    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();

    Float f = (float) 1.55;
    Assert.assertEquals(BigDecimal.valueOf(calculator.calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);

  }

  @Test
  public void testCalculationOneRNA() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException

  {
    parser = new StateMachineParser();

    String test = "RNA1{P.R(A)P.R([5meC])P.R(G)P.[mR](A)}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();

    Float f = (float) 46.20;
    Assert.assertEquals(BigDecimal.valueOf(calculator.calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);

  }

  @Test
  public void testCalculationRepeatingRNA() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException

  {
    parser = new StateMachineParser();

    String test = "RNA1{P.(R(A)P.R(G)P)'2'.R([5meC])P.R(G)P.[mR](A)}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();

    Float f = (float) 80.58;
    Assert.assertEquals(BigDecimal.valueOf(calculator.calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);

  }

  @Test
  public void testCalculationRepeatingMonomer() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException

  {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{C'2'}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();

    Float f = (float) 0.12;
    Assert.assertEquals(BigDecimal.valueOf(calculator.calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);


  }



  @Test
  public void testCalculationRepeatingList() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException

  {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{(F.C.F)'3'}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();
    Float f = (float) 0.19;
    Assert.assertEquals(BigDecimal.valueOf(calculator.calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(), f);
  }

  @Test
  public void testCalculationWithCHEMAndBlob() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException

  {
    parser = new StateMachineParser();

    String test = "CHEM1{[MCC]}|RNA1{R(U)}|BLOB1{?}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();
    Float f = (float) 10.21;
    Assert.assertEquals(BigDecimal.valueOf(calculator.calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(), f);
  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void testCalculationWithException() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException

  {
    parser = new StateMachineParser();

    String test = "CHEM1{[MCC]}|RNA1{(R(U)+R(A))}|BLOB1{?}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();
    Float f = (float) 10.21;
    Assert.assertEquals(BigDecimal.valueOf(calculator.calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(), f);
  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void testCalculationWithException2() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException

  {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{?}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();
    calculator.calculate(containerhelm2);
  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void testCalculationWithException3() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException

  {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{A.C._}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();
    calculator.calculate(containerhelm2);
  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void testCalculationWithException4() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException, StructureException, CalculationException, HELM2HandledException

  {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{A.C.(_.K)}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();
    calculator.calculate(containerhelm2);
  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void testCalculationWithException5() throws ExceptionState, IOException, JDOMException, NotationException, MonomerException, org.jdom2.JDOMException, StructureException, CalculationException,
      HELM2HandledException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{X}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();
    System.out.println(calculator.calculate(containerhelm2));
  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void testCalculationWithException6() throws ExceptionState, IOException, JDOMException, NotationException, MonomerException, org.jdom2.JDOMException, StructureException, CalculationException,
      HELM2HandledException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{?}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();
    System.out.println(calculator.calculate(containerhelm2));
  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void testCalculationWithException7() throws ExceptionState, IOException, JDOMException, NotationException, MonomerException, org.jdom2.JDOMException, StructureException, CalculationException,
      HELM2HandledException {
    parser = new StateMachineParser();

    String test = "RNA1{R(N)P}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();
    System.out.println(calculator.calculate(containerhelm2));
  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void testCalculationWithException8() throws ExceptionState, IOException, JDOMException, NotationException, MonomerException, org.jdom2.JDOMException, StructureException, CalculationException,
      HELM2HandledException {
    parser = new StateMachineParser();

    String test = "RNA1{[Cc1cn([C@H]2O[C@H](CO)[C@@H](OP(O)(=O)OC[C@H]3O[C@@H]([C@H](O)[C@@H]3O)n3ccc(=O)[nH]c3=O)[C@H]2O)c(=O)[nH]c1=O]}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient calculator = ExtinctionCoefficient.getInstance();
    System.out.println(calculator.calculate(containerhelm2));
  }

}

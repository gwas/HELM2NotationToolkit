/**
 * ***************************************************************************** Copyright C 2015, The Pistoia Alliance
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *****************************************************************************
 */

package org.helm.notation2;

import java.io.IOException;
import java.math.BigDecimal;

import org.helm.notation.CalculationException;
import org.helm.notation2.calculation.ExtinctionCoefficient;
import org.helm.notation2.exception.ExtinctionCoefficientException;
import org.helm.notation2.parser.StateMachineParser;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * ExtinctionCalculatorTest
 * 
 * @author hecht
 */
public class ExtinctionCalculatorTest {
  StateMachineParser parser;

  @Test
  public void testCalculationOnePeptide() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException

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

    Float f = (float) 1.55;
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);

  }

  @Test
  public void testCalculationOneRNA() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test = "RNA1{P.R(A)P.R([5meC])P.R(G)P.[mR](A)}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());

    Float f = (float) 46.20;
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);

  }

  @Test
  public void testCalculationRepeatingRNA() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test = "RNA1{P.(R(A)P.R(G)P)'2'.R([5meC])P.R(G)P.[mR](A)}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());

    Float f = (float) 80.58;
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);

  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationRepeatingMonomer() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{C'2'}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());

    Float f = (float) 0.12;
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);

  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationRepeatingList() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{(F.C.F)'3'}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    Float f = (float) 0.19;
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(), f);
  }

  @Test
  public void testCalculationWithCHEMAndBlob() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test = "CHEM1{[MCC]}|RNA1{R(U)}|BLOB1{?}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    Float f = (float) 10.21;
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(), f);
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException() throws ExtinctionCoefficientException, CalculationException,
      ExceptionState, IOException, JDOMException {
    parser = new StateMachineParser();

    String test = "CHEM1{[MCC]}|RNA1{(R(U)+R(A))}|BLOB1{?}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    Float f = (float) 10.21;
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(), f);
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException2() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{?}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient.getInstance().calculate(containerhelm2);

  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException3() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{A.C._}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient.getInstance().calculate(containerhelm2);
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException4() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{A.C.(_.K)}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient.getInstance().calculate(containerhelm2);
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException5() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{X}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient.getInstance().calculate(containerhelm2);
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException6() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{?}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    System.out.println(ExtinctionCoefficient.getInstance().calculate(containerhelm2));
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException7() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test = "RNA1{R(N)P}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient.getInstance().calculate(containerhelm2);
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException8() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException {
    parser = new StateMachineParser();

    String test =
        "RNA1{[[H]OC[C@H]1O[C@@H]([C@H](O)[C@@H]1OP(O)(=O)OC[C@H]1O[C@@H]([C@H](O)[C@@H]1OP(O)(=O)OC[C@H]1O[C@@H]([C@H](O)[C@@H]1O[H])N1C=CC(=O)NC1=O)N1C=CC(=O)NC1=O)N1C=CC(=O)NC1=O]}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient.getInstance().calculate(containerhelm2);
  }

}

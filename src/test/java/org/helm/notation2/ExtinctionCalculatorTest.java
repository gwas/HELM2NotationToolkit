/**
 * *****************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *****************************************************************************
 */

package org.helm.notation2;

import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.ExtinctionCoefficientCalculator;
import org.helm.notation2.calculation.ExtinctionCoefficient;
import org.helm.notation2.exception.ExtinctionCoefficientException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
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
      ExtinctionCoefficientException, CalculationException, NotationException

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
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation())).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);

  }

  @Test
  public void testCalculationOneRNA() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
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
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation())).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);

  }

  @Test
  public void testCalculationRepeatingRNA() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
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
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation())).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);

  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationRepeatingMonomer() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
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
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation())).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), f);

  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationRepeatingList() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
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
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(), f);
  }

  @Test
  public void testCalculationWithCHEMAndBlob() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
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
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(), f);
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException() throws ExtinctionCoefficientException, CalculationException,
      ExceptionState, IOException, JDOMException, NotationException {
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
    Assert.assertEquals(BigDecimal.valueOf(ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(), f);
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException2() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{?}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation());

  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException3() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{A.C._}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation());
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException4() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{A.C.(_.K)}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation());
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException5() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{X}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    System.out.println(ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation()));
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException6() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{?}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    System.out.println(ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation()));
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException7() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
    parser = new StateMachineParser();

    String test = "RNA1{R(N)P}$$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.notationContainer,
        new InterConnections());
    ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation());
  }

  @Test(expectedExceptions = ExtinctionCoefficientException.class)
  public void testCalculationWithException8() throws ExceptionState, IOException, JDOMException,
      ExtinctionCoefficientException, CalculationException, NotationException {
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
    ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation());
  }

  @Test
  public void testCalculateAminoAcidSequence() throws CalculationException, org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, ExtinctionCoefficientException,
      NotationException {

    /* amino acid sequence */
    String input = "AGGDDDDDDDDDDDDDDDDDDFFFFFFFFFFFFF";
    float result = ExtinctionCoefficientCalculator.getInstance().calculateFromAminoAcidSequence(input);
    assertEquals(getExtinctionNewImplementationPEPTIDE(input), result, 1e-15);

    input = "AGGCFFFFFFFFFF";
    result = ExtinctionCoefficientCalculator.getInstance().calculateFromAminoAcidSequence(input);
    assertEquals(getExtinctionNewImplementationPEPTIDE(input), result, 62.5);

    input = "AGGYEEEEEEEEEEEEEEEEEEE";
    result = ExtinctionCoefficientCalculator.getInstance().calculateFromAminoAcidSequence(input);
    assertEquals(getExtinctionNewImplementationPEPTIDE(input), result, 1e-15);

    input = "AGGWEEEEEEEEEEEEEEEEEEE";
    result = ExtinctionCoefficientCalculator.getInstance().calculateFromAminoAcidSequence(input);
    assertEquals(getExtinctionNewImplementationPEPTIDE(input), result, 1e-15);

  }

  @Test
  public void testCalculateFromPeptidePolymerNotation()
      throws NotationException, MonomerException, CalculationException,
      IOException, JDOMException, StructureException, ExtinctionCoefficientException, ParserException {
    String input = "A.G.G.W.E.E.E.E.E.W";
    String notation = "PEPTIDE1{A.G.G.W.E.E.E.E.E.W}$$$$";
    float result = ExtinctionCoefficientCalculator.getInstance().calculateFromPeptidePolymerNotation(input);
    assertEquals(ExtinctionCoefficient.getInstance().calculate(readNotation(notation).getHELM2Notation(), ExtinctionCoefficientCalculator.PEPTIDE_UNIT_TYPE), result, 1e-15);

  }

  private Float getExtinctionNewImplementationPEPTIDE(String sequence) throws org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, ExtinctionCoefficientException,
      NotationException {
    ContainerHELM2 containerhelm2 = SequenceConverter.readPeptide(sequence);
    float number = ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation(), ExtinctionCoefficientCalculator.PEPTIDE_UNIT_TYPE);
    return number;
  }

  @Test
  public void testCalculateFromComplexNotation() throws NotationException,
      MonomerException, IOException, JDOMException, StructureException,
      CalculationException, ExtinctionCoefficientException, ParserException {
    String input = "PEPTIDE1{A.G.G.W.E.E.E.E.E.W}$$$$";
    float result = ExtinctionCoefficientCalculator.getInstance().calculateFromComplexNotation(input, ExtinctionCoefficientCalculator.PEPTIDE_UNIT_TYPE);
    float newResult = ExtinctionCoefficient.getInstance().calculate(readNotation(input).getHELM2Notation(), ExtinctionCoefficientCalculator.PEPTIDE_UNIT_TYPE);
    System.out.println(result + " :: " + newResult);
    assertEquals(newResult, result, 1e-15);

    input = "PEPTIDE1{A.G.G.W.E.E.E.E.E.W}|PEPTIDE2{A.G.G.W.E.Y.E.E.E.E.W}$$$$";
    result = ExtinctionCoefficientCalculator.getInstance().calculateFromComplexNotation(input);
    newResult = ExtinctionCoefficient.getInstance().calculate(readNotation(input).getHELM2Notation());
    System.out.println(result + " :: " + newResult);
    assertEquals(newResult, result, 1e-6);

    input = "PEPTIDE1{A.G.G.W.E.E.E.E.E.W}|PEPTIDE2{A.G.G.W.E.Y.E.E.E.E.W}$$$$";
    result = ExtinctionCoefficientCalculator.getInstance().calculateFromComplexNotation(input, ExtinctionCoefficientCalculator.PEPTIDE_UNIT_TYPE);
    newResult = ExtinctionCoefficient.getInstance().calculate(readNotation(input).getHELM2Notation(), ExtinctionCoefficientCalculator.PEPTIDE_UNIT_TYPE);
    System.out.println(result + " :: " + newResult);
    assertEquals(newResult, result, 1e-15);

    input = "RNA1{P.R(A)P.R([5meC])P.R(G)P.[mR](A)}$$$$";
    result = ExtinctionCoefficientCalculator.getInstance().calculateFromComplexNotation(input);
    newResult = ExtinctionCoefficient.getInstance().calculate(readNotation(input).getHELM2Notation());
    System.out.println(result + " :: " + newResult);
    assertEquals(newResult, result, 1e-15);

    input = "RNA1{P.R(A)P.R([5meC])P.R(G)P.[mR](A)}$$$$";
    result = ExtinctionCoefficientCalculator.getInstance().calculateFromComplexNotation(input, ExtinctionCoefficientCalculator.PEPTIDE_UNIT_TYPE);
    newResult = ExtinctionCoefficient.getInstance().calculate(readNotation(input).getHELM2Notation(), ExtinctionCoefficientCalculator.PEPTIDE_UNIT_TYPE);
    System.out.println(result + " :: " + newResult);
    assertEquals(newResult, result, 1e-15);

    input = "RNA1{P.R(A)P.R([5meC])P.R(G)P.[mR](A)}|CHEM1{PEG2}|PEPTIDE1{A.G.G.W.E.E.E.E.E.W}|PEPTIDE2{A.G.G.W.E.Y.E.E.E.E.W}$$$$";
    result = ExtinctionCoefficientCalculator.getInstance().calculateFromComplexNotation(input);
    newResult = ExtinctionCoefficient.getInstance().calculate(readNotation(input).getHELM2Notation());
    System.out.println(result + " :: " + newResult);
    assertEquals(newResult, result, 1e-15);

  }

  @Test
  public void testCalculateFromNucleotideSequence()
      throws CalculationException, org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, IOException, JDOMException, ExtinctionCoefficientException, NotationException {
    String input = "ACGTACGT";
    float result = ExtinctionCoefficientCalculator.getInstance().calculateFromNucleotideSequence(input);
    assertEquals(getExtinctionReadRNA(input), result, 0.01);

  }

  @Test
  public void testCalculateFromModifiedNucleotideSequence()
      throws CalculationException, NotationException, IOException,
      JDOMException, org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, ExtinctionCoefficientException {
    String input = "ACGmTACmGT";
    float result = ExtinctionCoefficientCalculator.getInstance().calculateFromModifiedNucleotideSequence(input);
    assertEquals(BigDecimal.valueOf(getExtinctionReadRNA(input)).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_DOWN).floatValue(), 0.1);

  }

  private Float getExtinctionReadRNA(String sequence) throws org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, IOException, JDOMException,
      ExtinctionCoefficientException, NotationException {
    ContainerHELM2 containerhelm2 = SequenceConverter.readRNA(sequence);
    float number = ExtinctionCoefficient.getInstance().calculate(containerhelm2.getHELM2Notation());
    return number;
  }

  @Test
  public void testCalculateFromRnaPolymerNotation() throws NotationException,
      MonomerException, CalculationException, IOException, JDOMException,
      StructureException, ExtinctionCoefficientException, ParserException {
    String input = "P.R(A)P.R(C)P.R(G)P.[mR](A)";
    String notation = "RNA1{P.R(A)P.R(C)P.R(G)P.[mR](A)}$$$$";
    float result = ExtinctionCoefficientCalculator.getInstance().calculateFromRnaPolymerNotation(input);
    assertEquals(ExtinctionCoefficient.getInstance().calculate(readNotation(notation).getHELM2Notation()), result, 1e-6);
  }

  private ContainerHELM2 readNotation(String notation) throws ParserException, JDOMException {
    /* HELM1-Format -> */
    if (!(notation.contains("V2.0"))) {
      notation = new ConverterHELM1ToHELM2().doConvert(notation);
    }
    /* parses the HELM notation and generates the necessary notation objects */
    ParserHELM2 parser = new ParserHELM2();
    try {
      parser.parse(notation);
    } catch (ExceptionState | IOException e) {
      throw new ParserException(e.getMessage());
    }
    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.getHELM2Notation(), new InterConnections());
    return containerhelm2;
  }

}

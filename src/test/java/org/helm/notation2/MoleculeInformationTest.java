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

import java.io.IOException;
import java.math.BigDecimal;

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.ManipulatorFactory.ManipulatorType;
import org.helm.notation.NotationException;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MoleculeInformationTest {

  @Test
  public void testgetMolecularFormularExamples() throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException, NotationException {
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    String result = testMolecularFormular(notation);
    Assert.assertEquals(result, "C16H20N4O4");
  }

  @Test
  public void testgetExactMass() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException, NotationException {
    Double resultEditor = (double) 332.15;
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    Assert.assertEquals(BigDecimal.valueOf(testExactMass(notation)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), resultEditor.toString());
  }

  @Test
  public void testgetMolecularWeight() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException, NotationException {
    Double resultEditor = (double) 332.35;
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    if (Chemistry.getInstance().getManipulatorType().equals(ManipulatorType.MARVIN)) {
      Assert.assertEquals(BigDecimal.valueOf(testMolecularWeight(notation)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), resultEditor.toString());
    }
  }

  @Test
  public void testgetMolecularWeightPair() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException, NotationException {
    Double resultEditor = (double) 2462.60;
    String notation = "RNA1{R(G)P.R(A)P.R(G)P.R(G)}|RNA2{R(C)P.R(C)P.R(U)P.R(C)}$$RNA1,RNA2,5:pair-8:pair|RNA1,RNA2,11:pair-2:pair|RNA1,RNA2,8:pair-5:pair|RNA1,RNA2,2:pair-11:pair$$";
    if (Chemistry.getInstance().getManipulatorType().equals(ManipulatorType.MARVIN)) {
      Assert.assertEquals(BigDecimal.valueOf(testMolecularWeight(notation)).setScale(1, BigDecimal.ROUND_HALF_UP).toString(), resultEditor.toString());
    }
  }

  @Test
  public void testgetMolecularWeightWithSMILES() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException, NotationException {
    Double resultEditor = (double) 106.12;
    String notation = "CHEM1{[[*]OCCOCCO[*] |$_R1;;;;;;;;_R2$|]}$$$$";
    Assert.assertEquals(BigDecimal.valueOf(testMolecularWeight(notation)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), resultEditor.toString());
  }

  private String testMolecularFormular(String notation) throws ExceptionState, IOException, JDOMException,
      BuilderMoleculeException, CTKException, NotationException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);
    ContainerHELM2 containerhelm2 = new ContainerHELM2(parserHELM2.getHELM2Notation(), new InterConnections());
    return MoleculeInformation.getMolecularFormular(containerhelm2.getHELM2Notation());
  }

  private Double testExactMass(String notation) throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException, NotationException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parserHELM2.getHELM2Notation(), new InterConnections());
    return MoleculeInformation.getExactMass(containerhelm2.getHELM2Notation());

  }

  private Double testMolecularWeight(String notation) throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException, NotationException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);
    ContainerHELM2 containerhelm2 = new ContainerHELM2(parserHELM2.getHELM2Notation(), new InterConnections());
    return MoleculeInformation.getMolecularWeight(containerhelm2.getHELM2Notation());

  }
}

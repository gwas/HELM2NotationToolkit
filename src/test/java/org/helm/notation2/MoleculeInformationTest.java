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
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerLoadingException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.ComplexNotationParser;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import chemaxon.marvin.plugin.PluginException;

public class MoleculeInformationTest {

  @Test
  public void testgetMolecularFormularExamples() throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException, NotationException, ChemistryException {
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    String result = testMolecularFormular(notation);
    Assert.assertEquals(result, "C16H20N4O4");
  }

  @Test
  public void testgetExactMass() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException, NotationException, ChemistryException {
    Double resultEditor = (double) 332.15;
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    Assert.assertEquals(BigDecimal.valueOf(testExactMass(notation)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), resultEditor.toString());
  }

  @Test
  public void testgetMolecularWeight() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException, NotationException, ChemistryException {
    Double resultEditor = (double) 332.35;
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    if (Chemistry.getInstance().getManipulatorType().equals(ManipulatorType.MARVIN)) {
      Assert.assertEquals(BigDecimal.valueOf(testMolecularWeight(notation)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), resultEditor.toString());
    }
  }

  @Test
  public void testgetMolecularWeightPair() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException, NotationException, ChemistryException {
    Double resultEditor = (double) 2462.60;
    String notation = "RNA1{R(G)P.R(A)P.R(G)P.R(G)}|RNA2{R(C)P.R(C)P.R(U)P.R(C)}$$RNA1,RNA2,5:pair-8:pair|RNA1,RNA2,11:pair-2:pair|RNA1,RNA2,8:pair-5:pair|RNA1,RNA2,2:pair-11:pair$$";
    if (Chemistry.getInstance().getManipulatorType().equals(ManipulatorType.MARVIN)) {
      Assert.assertEquals(BigDecimal.valueOf(testMolecularWeight(notation)).setScale(1, BigDecimal.ROUND_HALF_UP).toString(), resultEditor.toString());
    }
  }

  @Test
  public void testgetMolecularWeightWithSMILES() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException, NotationException, ChemistryException {
    Double resultEditor = (double) 106.12;
    String notation = "CHEM1{[[*]OCCOCCO[*] |$_R1;;;;;;;;_R2$|]}$$$$";
    Assert.assertEquals(BigDecimal.valueOf(testMolecularWeight(notation)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), resultEditor.toString());
  }

  private String testMolecularFormular(String notation) throws ExceptionState, IOException, JDOMException,
      BuilderMoleculeException, CTKException, NotationException, ChemistryException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);
    return MoleculeInformation.getMolecularFormular(parserHELM2.getHELM2Notation());
  }

  private Double testExactMass(String notation) throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException, NotationException, ChemistryException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    return MoleculeInformation.getExactMass(parserHELM2.getHELM2Notation());

  }

  private Double testMolecularWeight(String notation) throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException, NotationException, ChemistryException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);
    return MoleculeInformation.getMolecularWeight(parserHELM2.getHELM2Notation());

  }

  @Test
  public void testExtended() throws MonomerLoadingException, NotationException, MonomerException, IOException, JDOMException, PluginException, StructureException, BuilderMoleculeException,
      CTKException, ParserException, ChemistryException {

    // siRNA
    String oldNotation =
        "RNA1{R(A)P.R(U)P.R(C)P.R(C)P.R(A)P.R(A)P.R(A)P.R(G)P.R(A)P.R(U)P.R(A)P.R(C)P.R(U)P.R(A)P.R(G)P.R(C)P.R(U)P.R(U)P.R(U)P.R(G)P.R(C)P.R(A)P.R(G)P.R(A)P.R(A)P.R(U)P.R(G)}|RNA2{R(U)P.R(U)P.R(C)P.R(U)P.R(G)P.R(C)P.R(A)P.R(A)P.R(A)P.R(G)P.R(C)P.R(U)P.R(A)P.R(G)P.R(U)P.R(A)P.R(U)P.R(C)P.R(U)P.R(U)P.R(U)P.R(G)P.R(G)P.[dR](A)P.[dR](T)}$$RNA1,RNA2,2:pair-74:pair|RNA1,RNA2,5:pair-71:pair|RNA1,RNA2,8:pair-68:pair|RNA1,RNA2,11:pair-65:pair|RNA1,RNA2,14:pair-62:pair|RNA1,RNA2,17:pair-59:pair|RNA1,RNA2,20:pair-56:pair|RNA1,RNA2,23:pair-53:pair|RNA1,RNA2,26:pair-50:pair|RNA1,RNA2,29:pair-47:pair|RNA1,RNA2,32:pair-44:pair|RNA1,RNA2,35:pair-41:pair|RNA1,RNA2,38:pair-38:pair|RNA1,RNA2,41:pair-35:pair|RNA1,RNA2,44:pair-32:pair|RNA1,RNA2,47:pair-29:pair|RNA1,RNA2,50:pair-26:pair|RNA1,RNA2,53:pair-23:pair|RNA1,RNA2,56:pair-20:pair|RNA1,RNA2,59:pair-17:pair|RNA1,RNA2,62:pair-14:pair|RNA1,RNA2,65:pair-11:pair|RNA1,RNA2,68:pair-8:pair|RNA1,RNA2,71:pair-5:pair|RNA1,RNA2,74:pair-2:pair$$";
    String newNotation =
        "RNA1{R(A)P.R(U)P.R(C)P.R(C)P.R(A)P.R(A)P.R(A)P.R(G)P.R(A)P.R(U)P.R(A)P.R(C)P.R(U)P.R(A)P.R(G)P.R(C)P.R(U)P.R(U)P.R(U)P.R(G)P.R(C)P.R(A)P.R(G)P.R(A)P.R(A)P.R(U)P.R(G)}|RNA2{R(U)P.R(U)P.R(C)P.R(U)P.R(G)P.R(C)P.R(A)P.R(A)P.R(A)P.R(G)P.R(C)P.R(U)P.R(A)P.R(G)P.R(U)P.R(A)P.R(U)P.R(C)P.R(U)P.R(U)P.R(U)P.R(G)P.R(G)P.[dR](A)P.[dR](T)}$RNA1,RNA2,2:pair-74:pair|RNA1,RNA2,5:pair-71:pair|RNA1,RNA2,8:pair-68:pair|RNA1,RNA2,11:pair-65:pair|RNA1,RNA2,14:pair-62:pair|RNA1,RNA2,17:pair-59:pair|RNA1,RNA2,20:pair-56:pair|RNA1,RNA2,23:pair-53:pair|RNA1,RNA2,26:pair-50:pair|RNA1,RNA2,29:pair-47:pair|RNA1,RNA2,32:pair-44:pair|RNA1,RNA2,35:pair-41:pair|RNA1,RNA2,38:pair-38:pair|RNA1,RNA2,41:pair-35:pair|RNA1,RNA2,44:pair-32:pair|RNA1,RNA2,47:pair-29:pair|RNA1,RNA2,50:pair-26:pair|RNA1,RNA2,53:pair-23:pair|RNA1,RNA2,56:pair-20:pair|RNA1,RNA2,59:pair-17:pair|RNA1,RNA2,62:pair-14:pair|RNA1,RNA2,65:pair-11:pair|RNA1,RNA2,68:pair-8:pair|RNA1,RNA2,71:pair-5:pair|RNA1,RNA2,74:pair-2:pair$$$V2.0";

    // AssertJUnit.assertEquals(ComplexNotationParser.getMoleculeInfo(oldNotation).getExactMass(),
    // MoleculeInformation.getExactMass(readNotation(newNotation).getHELM2Notation()));
    AssertJUnit.assertEquals(ComplexNotationParser.getMoleculeInfo(oldNotation).getMolecularFormula(), MoleculeInformation.getMolecularFormular(readNotation(newNotation)));
    AssertJUnit.assertEquals(ComplexNotationParser.getMoleculeInfo(oldNotation).getMolecularWeight(), MoleculeInformation.getMolecularWeight(readNotation(newNotation)), 0.001);

    // conjugate
    oldNotation = "PEPTIDE1{A.G.G.G.C.C.K.K.K.K}|CHEM1{MCC}$PEPTIDE1,CHEM1,10:R3-1:R1$$$";
    AssertJUnit.assertEquals(ComplexNotationParser.getMoleculeInfo(oldNotation).getMolecularFormula(), MoleculeInformation.getMolecularFormular(readNotation(oldNotation)));
    AssertJUnit.assertEquals(ComplexNotationParser.getMoleculeInfo(oldNotation).getMolecularWeight(), MoleculeInformation.getMolecularWeight(readNotation(oldNotation)), 0.001);

  }

  private HELM2Notation readNotation(String notation) throws ParserException, JDOMException {
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
    return parser.getHELM2Notation();

  }
}

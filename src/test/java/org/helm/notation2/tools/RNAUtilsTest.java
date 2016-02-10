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
package org.helm.notation2.tools;

import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;

import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.NucleotideSequenceParser;
import org.helm.notation.tools.SimpleNotationParser;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.tools.HELM2NotationUtils;
import org.helm.notation2.tools.RNAUtils;
import org.helm.notation2.tools.SequenceConverter;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class RNAUtilsTest {

  @Test
  public void getReverseSequenceTest() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException, NotationException,
      org.jdom2.JDOMException, ChemistryException {
    String notation = "UTA";

    Assert.assertEquals(RNAUtils.getReverseSequence(produceHELM2Notation(notation).getListOfPolymers().get(0)), NucleotideSequenceParser.getReverseSequence(notation));
  }

  @Test
  public void getNaturalAnalogSequenceTest() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException,
      NotationException,
      org.jdom2.JDOMException, ChemistryException {
    String notation = "UTA";

    Assert.assertEquals(RNAUtils.getNaturalAnalogSequence(produceHELM2Notation(notation).getListOfPolymers().get(0)), NucleotideSequenceParser.getNaturalAnalogSequence(notation));

  }

  @Test
  public void testGetTrimmedNucleotideSequence() throws NotationException, MonomerException, IOException, JDOMException, StructureException, RNAUtilsException, HELM2HandledException, ParserException {
    String notation = "RNA1{R(G)P.R(A)[sP].RP.R(G)P.[LR]([5meC])}$$$$";
    Assert.assertEquals(RNAUtils.getTrimmedNucleotideSequence(readNotation(notation).getListOfPolymers().get(0)), SimpleNotationParser.getTrimmedNucleotideSequence("R(G)P.R(A)[sP].RP.R(G)P.[LR]([5meC])"));

  }

  @Test
  public void getNaturalAnalogSequenceTestExtended() throws ParserException, JDOMException, HELM2HandledException, RNAUtilsException, NotationException, ChemistryException {
    String notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R(C)P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";

    String nucleotideSeq = RNAUtils.getNaturalAnalogSequence(readNotation(notation).getListOfPolymers().get(0));

    assertEquals("CGAUAUGGGCUGAAUACAAUU", nucleotideSeq);

    // replaced a C with 5meC (modified C)
    notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R([5meC])P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";

    nucleotideSeq = RNAUtils.getNaturalAnalogSequence(readNotation(notation).getListOfPolymers().get(0));

    assertEquals("CGAUAUGGGCUGAAUACAAUU", nucleotideSeq);

  }

  @Test
  public void getModifiedNucleotideSequence() throws ParserException, JDOMException, HELM2HandledException, RNAUtilsException, NotationException,
      org.helm.notation2.parser.exceptionparser.NotationException,
      MonomerException, IOException, StructureException {
    String notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R(C)P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";

    String nucleotideSeq = RNAUtils.getModifiedNucleotideSequence(readNotation(notation).getListOfPolymers().get(0));

    assertEquals(SimpleNotationParser.getModifiedNucleotideSequence("R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R(C)P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)"), nucleotideSeq);

    // replaced a C with 5meC (modified C)
    notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R([5meC])P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";

    nucleotideSeq = RNAUtils.getModifiedNucleotideSequence(readNotation(notation).getListOfPolymers().get(0));
    assertEquals(SimpleNotationParser.getModifiedNucleotideSequence("R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R([5meC])P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)"), nucleotideSeq);

  }

  @Test
  public void generateComplementTest() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException,
      NotationException,
      org.jdom2.JDOMException, ChemistryException, ParserException {
    String notation = "UTACCGG";
    Assert.assertEquals(RNAUtils.getNaturalAnalogSequence(RNAUtils.getComplement(produceHELM2Notation(notation).getListOfPolymers().get(0))), "AAUGGCC");

  }

  @Test
  public void generateAntiparallel() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException,
      NotationException,
      org.jdom2.JDOMException, ChemistryException, ParserException {
    String notation = "UTACCGG";
    Assert.assertEquals(RNAUtils.getNaturalAnalogSequence(RNAUtils.getAntiparallel(produceHELM2Notation(notation).getListOfPolymers().get(0))), "CCGGUAA");
  }

  @Test
  public void generateInverse() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException,
      NotationException,
      org.jdom2.JDOMException, ChemistryException {
    String notation = "UTA";
    Assert.assertEquals(RNAUtils.getNaturalAnalogSequence(RNAUtils.getInverse(produceHELM2Notation(notation).getListOfPolymers().get(0))), "ATU");
  }

  @Test
  public void testGetComplementSequence() throws NotationException,
      IOException, JDOMException, org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, RNAUtilsException, HELM2HandledException, ChemistryException {

    String sequence = "5'-UAU GUC UCC AGA AUG UAG CdTdT-3'";

    String normal = NucleotideSequenceParser.getNormalComplementSequence(sequence);
    AssertJUnit.assertEquals("5'-AAGCUACAUUCUGGAGACAUA-3'", normal);

    Assert.assertEquals(new StringBuilder(
        RNAUtils.getNucleotideSequence(RNAUtils.getComplement(produceHELM2Notation(sequence).getListOfPolymers().get(0)))).reverse().toString(), "AAGCUACAUUCUGGAGACAUA");

    String reverse = NucleotideSequenceParser.getReverseComplementSequence(sequence);
    AssertJUnit.assertEquals("3'-AUACAGAGGUCUUACAUCGAA-5'", reverse);
    String notation = NucleotideSequenceParser.getNotation(sequence);
    AssertJUnit.assertEquals("R(U)P.R(A)P.R(U)P.R(G)P.R(U)P.R(C)P.R(U)P.R(C)P.R(C)P.R(A)P.R(G)P.R(A)P.R(A)P.R(U)P.R(G)P.R(U)P.R(A)P.R(G)P.R(C)P.[dR](T)P.[dR](T)P", notation);
    Assert.assertEquals(new StringBuilder(
        RNAUtils.getNucleotideSequence(RNAUtils.getReverseComplement(produceHELM2Notation(sequence).getListOfPolymers().get(0)))).reverse().toString(), "AUACAGAGGUCUUACAUCGAA");

  }

  @Test
  public void AreInOppositeDirectionTest() throws RNAUtilsException, HELM2HandledException,
      org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, NotationException,
      IOException, org.jdom2.JDOMException, ChemistryException {
    String notationOne = "CAGTT";
    String notationTwo = "AACUG";

    Assert.assertTrue(RNAUtils.areAntiparallel(produceHELM2Notation(notationOne).getListOfPolymers().get(0), produceHELM2Notation(notationTwo).getListOfPolymers().get(0)));
  }

  @Test
  public void addLastPAndRemoveLastPTest() throws org.helm.notation2.parser.exceptionparser.NotationException, RNAUtilsException, IOException, FastaFormatException, HELM2HandledException,
      JDOMException {
    String notation = "CAGTT";
    HELM2Notation helm2notation = produceHELM2Notation(notation);
    /* add phosphate */
    RNAUtils.addLastP(helm2notation.getListOfPolymers().get(0));

    /* check if the RNA Notation contains now the added phosphate */
    Assert.assertEquals(helm2notation.toHELM2(), "RNA1{R(C)P.R(A)P.R(G)P.R(T)P.R(T)P}$$$$V2.0");

    /* remove last phosphate */
    RNAUtils.removeLastP(helm2notation.getListOfPolymers().get(0));

    /* check if the RNA Notation contains now the removed phosphate */
    Assert.assertEquals(helm2notation.toHELM2(), "RNA1{R(C)P.R(A)P.R(G)P.R(T)P.R(T)}$$$$V2.0");
  }

  @Test
  public void hasNucleotideModificationTest() throws ParserException, JDOMException, org.helm.notation2.parser.exceptionparser.NotationException {
    String notation = "RNA1{R(U)P.R([dabA])}$$$$V2.0";
    Assert.assertTrue(RNAUtils.hasNucleotideModification(HELM2NotationUtils.getRNAPolymers(readNotation(notation).getListOfPolymers()).get(0)));
    notation = "RNA1{R(U)P.R(A)P}$$$$V2.0";
    Assert.assertFalse(RNAUtils.hasNucleotideModification(HELM2NotationUtils.getRNAPolymers(readNotation(notation).getListOfPolymers()).get(0)));
    notation = "RNA1{R(U)P.(R(A)P.R(G)P)'3'}$$$$V2.0";
    Assert.assertFalse(RNAUtils.hasNucleotideModification(HELM2NotationUtils.getRNAPolymers(readNotation(notation).getListOfPolymers()).get(0)));
    notation = "RNA1{R(U)P.(R(A)P+R([dabA])P)}$$$$V2.0";
    Assert.assertTrue(RNAUtils.hasNucleotideModification(HELM2NotationUtils.getRNAPolymers(readNotation(notation).getListOfPolymers()).get(0)));
  }

  private HELM2Notation produceHELM2Notation(String notation)
      throws org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, IOException, JDOMException {
    return SequenceConverter.readRNA(notation);

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

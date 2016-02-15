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

import java.io.IOException;

import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.NotationException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.tools.HELM2NotationUtils;
import org.helm.notation2.tools.RNAUtils;
import org.helm.notation2.tools.SequenceConverter;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RNAUtilsTest {

  @Test
  public void getReverseSequenceTest() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException, NotationException,
      org.jdom2.JDOMException, ChemistryException {
    String notation = "UTA";

    Assert.assertEquals(RNAUtils.getReverseSequence(produceHELM2Notation(notation).getListOfPolymers().get(0)), "ATU");
  }

  @Test
  public void getNaturalAnalogSequenceTest() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException,
      NotationException,
      org.jdom2.JDOMException, ChemistryException {
    String notation = "UTA";

    Assert.assertEquals(RNAUtils.getNaturalAnalogSequence(produceHELM2Notation(notation).getListOfPolymers().get(0)), "UTA");

  }

  @Test
  public void testGetTrimmedNucleotideSequence() throws RNAUtilsException, HELM2HandledException, ChemistryException, ParserException, JDOMException {
    String notation = "RNA1{R(G)P.R(A)[sP].RP.R(G)P.[LR]([5meC])}$$$$";
    Assert.assertEquals(RNAUtils.getTrimmedNucleotideSequence(HELM2NotationUtils.readNotation(notation).getListOfPolymers().get(0)), "GAXGC");
  }

  @Test
  public void getNaturalAnalogSequenceTestExtended() throws ParserException, JDOMException, HELM2HandledException, RNAUtilsException, NotationException, ChemistryException {
    String notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R(C)P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";

    String nucleotideSeq = RNAUtils.getNaturalAnalogSequence(HELM2NotationUtils.readNotation(notation).getListOfPolymers().get(0));

    Assert.assertEquals("CGAUAUGGGCUGAAUACAAUU", nucleotideSeq);

    // replaced a C with 5meC (modified C)
    notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R([5meC])P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";

    nucleotideSeq = RNAUtils.getNaturalAnalogSequence(HELM2NotationUtils.readNotation(notation).getListOfPolymers().get(0));

    Assert.assertEquals("CGAUAUGGGCUGAAUACAAUU", nucleotideSeq);

  }

  @Test
  public void getModifiedNucleotideSequence() throws RNAUtilsException, HELM2HandledException, ChemistryException, ParserException, JDOMException {
    String notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R(C)P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";

    String nucleotideSeq = RNAUtils.getModifiedNucleotideSequence(HELM2NotationUtils.readNotation(notation).getListOfPolymers().get(0));

    Assert.assertEquals("CGAUAUGGGCUGAAUACAAdUdU", nucleotideSeq);

    // replaced a C with 5meC (modified C)
    notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R([5meC])P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";

    nucleotideSeq = RNAUtils.getModifiedNucleotideSequence(HELM2NotationUtils.readNotation(notation).getListOfPolymers().get(0));
    Assert.assertEquals("CGAUAUGGGCUGAAUACAAdUdU", nucleotideSeq);

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

    Assert.assertEquals(new StringBuilder(
        RNAUtils.getNucleotideSequence(RNAUtils.getComplement(produceHELM2Notation(sequence).getListOfPolymers().get(0)))).reverse().toString(), "AAGCUACAUUCUGGAGACAUA");

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
      JDOMException, ChemistryException {
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
    Assert.assertTrue(RNAUtils.hasNucleotideModification(HELM2NotationUtils.getRNAPolymers(HELM2NotationUtils.readNotation(notation).getListOfPolymers()).get(0)));
    notation = "RNA1{R(U)P.R(A)P}$$$$V2.0";
    Assert.assertFalse(RNAUtils.hasNucleotideModification(HELM2NotationUtils.getRNAPolymers(HELM2NotationUtils.readNotation(notation).getListOfPolymers()).get(0)));
    notation = "RNA1{R(U)P.(R(A)P.R(G)P)'3'}$$$$V2.0";
    Assert.assertFalse(RNAUtils.hasNucleotideModification(HELM2NotationUtils.getRNAPolymers(HELM2NotationUtils.readNotation(notation).getListOfPolymers()).get(0)));
    notation = "RNA1{R(U)P.(R(A)P+R([dabA])P)}$$$$V2.0";
    Assert.assertTrue(RNAUtils.hasNucleotideModification(HELM2NotationUtils.getRNAPolymers(HELM2NotationUtils.readNotation(notation).getListOfPolymers()).get(0)));
  }

  private HELM2Notation produceHELM2Notation(String notation)
      throws org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, IOException, JDOMException, ChemistryException {
    return SequenceConverter.readRNA(notation);

  }

}

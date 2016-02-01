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

import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.NucleotideLoadingException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.NucleotideSequenceParser;
import org.helm.notation.tools.SimpleNotationParser;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RNAUtilsTest {

  @Test
  public void getReverseSequenceTest() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException, NotationException,
      org.jdom2.JDOMException {
    String notation = "UTA";
    ContainerHELM2 containerhelm2 = produceContainerHELM2(notation);
    Assert.assertEquals(RNAUtils.getReverseSequence(containerhelm2.getAllPolymers().get(0)), NucleotideSequenceParser.getReverseSequence(notation));

  }

  @Test
  public void getNaturalAnalogSequenceTest() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException,
      NotationException,
      org.jdom2.JDOMException {
    String notation = "UTA";
    ContainerHELM2 containerhelm2 = produceContainerHELM2(notation);
    Assert.assertEquals(RNAUtils.getNaturalAnalogSequence(containerhelm2.getAllPolymers().get(0)), NucleotideSequenceParser.getNaturalAnalogSequence(notation));

  }

  @Test
  public void getNaturalAnalogSequenceTestExtended() throws ParserException, JDOMException, HELM2HandledException, RNAUtilsException, NotationException {
    String notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R(C)P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";
    ContainerHELM2 containerhelm2 = readNotation(notation);

    String nucleotideSeq = RNAUtils.getNaturalAnalogSequence(containerhelm2.getHELM2Notation().getListOfPolymers().get(0));

    assertEquals("CGAUAUGGGCUGAAUACAAUU", nucleotideSeq);

    // replaced a C with 5meC (modified C)
    notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R([5meC])P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";
    containerhelm2 = readNotation(notation);

    nucleotideSeq = RNAUtils.getNaturalAnalogSequence(containerhelm2.getHELM2Notation().getListOfPolymers().get(0));

    assertEquals("CGAUAUGGGCUGAAUACAAUU", nucleotideSeq);

  }

  // @Test
  public void getSequenceNucleotide() throws ParserException, JDOMException, HELM2HandledException, RNAUtilsException, NotationException, org.helm.notation2.parser.exceptionparser.NotationException,
      MonomerException, IOException, StructureException {
    String notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R(C)P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";
    ContainerHELM2 containerhelm2 = readNotation(notation);

    String nucleotideSeq = RNAUtils.getNucleotideSequence(containerhelm2.getHELM2Notation().getListOfPolymers().get(0));

    assertEquals(SimpleNotationParser.getModifiedNucleotideSequence("R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R(C)P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)"), nucleotideSeq);

    // replaced a C with 5meC (modified C)
    notation = "RNA1{R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R([5meC])P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)}$$$$";
    containerhelm2 = readNotation(notation);

    nucleotideSeq = RNAUtils.getSequence((containerhelm2.getHELM2Notation().getListOfPolymers().get(0)));
    System.out.println(SimpleNotationParser.getModifiedNucleotideSequence("R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R([5meC])P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)"));
    assertEquals(SimpleNotationParser.getModifiedNucleotideSequence("R(C)P.R(G)P.R(A)P.R(U)P.R(A)P.R(U)P.R(G)P.R(G)P.R(G)P.R([5meC])P.R(U)P.R(G)P.R(A)P.R(A)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.[dR](U)P.[dR](U)"), nucleotideSeq);

  }

  @Test
  public void generateComplementTest() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException,
      NotationException,
      org.jdom2.JDOMException {
    String notation = "UTACCGG";
    ContainerHELM2 containerhelm2 = produceContainerHELM2(notation);
    Assert.assertEquals(RNAUtils.getNaturalAnalogSequence(RNAUtils.getComplement(containerhelm2.getHELM2Notation().getListOfPolymers().get(0))), "AAUGGCC");

  }

  @Test
  public void generateAntiparallel() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException,
      NotationException,
      org.jdom2.JDOMException {
    String notation = "UTACCGG";
    ContainerHELM2 containerhelm2 = produceContainerHELM2(notation);
    Assert.assertEquals(RNAUtils.getNaturalAnalogSequence(RNAUtils.getAntiparallel(containerhelm2.getHELM2Notation().getListOfPolymers().get(0))), "CCGGUAA");
  }

  @Test
  public void generateInverse() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, RNAUtilsException, HELM2HandledException,
      NotationException,
      org.jdom2.JDOMException {
    String notation = "UTA";
    ContainerHELM2 containerhelm2 = produceContainerHELM2(notation);
    Assert.assertEquals(RNAUtils.getNaturalAnalogSequence(RNAUtils.getInverse(containerhelm2.getHELM2Notation().getListOfPolymers().get(0))), "ATU");
  }

  @Test
  public void AreInOppositeDirectionTest() throws RNAUtilsException, HELM2HandledException,
      org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, NotationException,
      IOException, org.jdom2.JDOMException {
    String notationOne = "CAGTT";
    String notationTwo = "AACUG";
    ContainerHELM2 containerhelm2One = produceContainerHELM2(notationOne);
    ContainerHELM2 containerhelm2Two = produceContainerHELM2(notationTwo);
    Assert.assertTrue(RNAUtils.areAntiparallel(containerhelm2One.getAllPolymers().get(0), containerhelm2Two.getAllPolymers().get(0)));
  }

  @Test
  public void getSirnaSequence() throws RNAUtilsException, HELM2HandledException,
      org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, NotationException,
      IOException, org.jdom2.JDOMException, JDOMException {
    String notation = "CAGTT";

    RNAUtils.getSirnaNotation(notation).getHELM2Notation().toHELM2();
  }

  @Test
  public void addLastPAndRemoveLastPTest() throws org.helm.notation2.parser.exceptionparser.NotationException, RNAUtilsException, IOException, FastaFormatException, HELM2HandledException,
      JDOMException {
    String notation = "CAGTT";

    /* read notation */
    ContainerHELM2 containerHELM2 = produceContainerHELM2(notation);

    /* add phosphate */
    RNAUtils.addLastP(containerHELM2.getHELM2Notation().getListOfPolymers().get(0));

    /* check if the RNA Notation contains now the added phosphate */
    Assert.assertEquals(containerHELM2.getHELM2Notation().toHELM2(), "RNA1{R(C)P.R(A)P.R(G)P.R(T)P.R(T)P}$$$$V2.0");

    /* remove last phosphate */
    RNAUtils.removeLastP(containerHELM2.getHELM2Notation().getListOfPolymers().get(0));

    /* check if the RNA Notation contains now the removed phosphate */
    Assert.assertEquals(containerHELM2.getHELM2Notation().toHELM2(), "RNA1{R(C)P.R(A)P.R(G)P.R(T)P.R(T)}$$$$V2.0");
  }

  @Test
  public void hasNucleotideModificationTest() throws ParserException, JDOMException, org.helm.notation2.parser.exceptionparser.NotationException {
    String notation = "RNA1{R(U)P.R([dabA])}$$$$V2.0";
    ContainerHELM2 containerHELM2 = readNotation(notation);
    Assert.assertTrue(RNAUtils.hasNucleotideModification(containerHELM2.getRNAPolymers().get(0)));
    notation = "RNA1{R(U)P.R(A)P}$$$$V2.0";
    containerHELM2 = readNotation(notation);
    Assert.assertFalse(RNAUtils.hasNucleotideModification(containerHELM2.getRNAPolymers().get(0)));
    notation = "RNA1{R(U)P.(R(A)P.R(G)P)'3'}$$$$V2.0";
    containerHELM2 = readNotation(notation);
    Assert.assertFalse(RNAUtils.hasNucleotideModification(containerHELM2.getRNAPolymers().get(0)));
    notation = "RNA1{R(U)P.(R(A)P+R([dabA])P)}$$$$V2.0";
    containerHELM2 = readNotation(notation);
    Assert.assertTrue(RNAUtils.hasNucleotideModification(containerHELM2.getRNAPolymers().get(0)));
  }

  private ContainerHELM2 produceContainerHELM2(String notation)
      throws org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, IOException, JDOMException {
    ContainerHELM2 containerhelm2 = SequenceConverter.readRNA(notation);
    return containerhelm2;
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

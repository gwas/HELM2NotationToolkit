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

import org.helm.notation.NotationException;
import org.helm.notation.tools.NucleotideSequenceParser;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.RNAUtilsException;
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

  private ContainerHELM2 produceContainerHELM2(String notation)
      throws org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException {
    ContainerHELM2 containerhelm2 = SequenceConverter.readRNA(notation);
    return containerhelm2;
  }


}

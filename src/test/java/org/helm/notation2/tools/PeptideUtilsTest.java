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

import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.NotationException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.exception.PeptideUtilsException;
import org.helm.notation2.tools.PeptideUtils;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PeptideUtilsTest {
  @Test
  public void getNaturalAnalogSequence() throws ParserException, JDOMException, HELM2HandledException, PeptideUtilsException, NotationException, ChemistryException {
    String notation = "PEPTIDE1{K.C.C.C.W.K.[seC]}$$$$V2.0";

    Assert.assertEquals(PeptideUtils.getNaturalAnalogueSequence(HELM2NotationUtils.readNotation(notation).getListOfPolymers().get(0)), "KCCCWKC");

  }

  @Test
  public void getSequence() throws ParserException, JDOMException, HELM2HandledException, PeptideUtilsException, NotationException, ChemistryException {
    String notation = "PEPTIDE1{K.C.C.C.W.K.[seC]}$$$$V2.0";

    Assert.assertEquals(PeptideUtils.getSequence(HELM2NotationUtils.readNotation(notation).getListOfPolymers().get(0)), "KCCCWK[seC]");

  }

}

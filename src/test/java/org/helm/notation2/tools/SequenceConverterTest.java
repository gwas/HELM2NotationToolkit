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

import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.MonomerException;
import org.helm.notation2.exception.MonomerLoadingException;
import org.helm.notation2.exception.NotationException;
import org.helm.notation2.exception.NucleotideLoadingException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.tools.SequenceConverter;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * SequenceConverterTest
 *
 *
 * @author hecht
 */
public class SequenceConverterTest {

  @Test
  public void getNucleotideSequenceFromNotation() throws NucleotideLoadingException, MonomerLoadingException, NotationException, MonomerException, IOException, JDOMException,
      org.helm.notation2.parser.exceptionparser.NotationException, HELM2HandledException, ParserException, RNAUtilsException, ChemistryException {
    String notation = "RNA1{R(T)P.R(G)P.R(U)}$$$$";
    Assert.assertEquals(SequenceConverter.getNucleotideSequenceFromNotation(HELM2NotationUtils.readNotation(notation)), "TGU");
    notation = "RNA1{[dR](U)P.R(T)P.R(G)P.R(U)}$$$$";
    Assert.assertEquals(SequenceConverter.getNucleotideSequenceFromNotation(HELM2NotationUtils.readNotation(notation)), "dUTGU");

  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void getNucleotideSequenceFromNotationWithException() throws NucleotideLoadingException, MonomerLoadingException, NotationException, MonomerException, IOException, JDOMException,

      org.helm.notation2.parser.exceptionparser.NotationException, HELM2HandledException, ParserException, RNAUtilsException, ChemistryException {
    String notation = "RNA1{R(T)P.R(G)P.(R(U))'3'}$$$$";
    Assert.assertEquals(SequenceConverter.getNucleotideSequenceFromNotation(HELM2NotationUtils.readNotation(notation)), "RGU");
  }

}

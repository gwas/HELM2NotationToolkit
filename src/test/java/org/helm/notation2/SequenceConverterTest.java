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

import org.helm.notation.MonomerException;
import org.helm.notation.MonomerLoadingException;
import org.helm.notation.NotationException;
import org.helm.notation.NucleotideLoadingException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.NucleotideConverter;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.notation.HELM2Notation;
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
  public void getNucleotideSequenceFromNotation() throws NucleotideLoadingException, MonomerLoadingException, NotationException, MonomerException, IOException, JDOMException, StructureException,
      org.helm.notation2.parser.exceptionparser.NotationException, HELM2HandledException, ParserException, RNAUtilsException {
    String notation = "RNA1{R(T)P.R(G)P.R(U)}$$$$";
    Assert.assertEquals(SequenceConverter.getNucleotideSequenceFromNotation(readNotation(notation)), NucleotideConverter.getInstance().getNucleotideSequencesFromComplexNotation(notation));
    notation = "RNA1{[dR](U)P.R(T)P.R(G)P.R(U)}$$$$";
    Assert.assertEquals(SequenceConverter.getNucleotideSequenceFromNotation(readNotation(notation)), NucleotideConverter.getInstance().getNucleotideSequencesFromComplexNotation(notation));

  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void getNucleotideSequenceFromNotationWithException() throws NucleotideLoadingException, MonomerLoadingException, NotationException, MonomerException, IOException, JDOMException,
      StructureException,
      org.helm.notation2.parser.exceptionparser.NotationException, HELM2HandledException, ParserException, RNAUtilsException {
    String notation = "RNA1{R(T)P.R(G)P.(R(U))'3'}$$$$";
    System.out.println(SequenceConverter.getNucleotideSequenceFromNotation(readNotation(notation)));
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

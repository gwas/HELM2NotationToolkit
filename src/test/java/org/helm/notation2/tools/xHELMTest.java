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

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM1FormatException;
import org.helm.notation2.exception.ValidationException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.tools.xHelmNotationExporter;
import org.jdom2.JDOMException;
import org.testng.annotations.Test;

public class xHELMTest {

  @Test
  public void testxHELMExamples() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, MonomerException, HELM1FormatException, org.jdom2.JDOMException, NotationException, CTKException, ValidationException, ChemistryException {
    String notation = "RNA1{R(U)P.R(T)P.R(G)P.R(C)}$$$$";
    testxHELM1(notation);

    notation = "PEPTIDE1{(A.G).L}$$$$";
    testxHELM2(notation);

    notation = "PEPTIDE1{[dF].[dN].[dL]}$$$$";
    testxHELM1(notation);
  }

  @Test(expectedExceptions = HELM1FormatException.class)
  public void testxHELMWithException() throws ExceptionState, IOException, JDOMException, FastaFormatException,
      AnalogSequenceException, MonomerException, HELM1FormatException,
      org.jdom2.JDOMException, NotationException, CTKException, ValidationException, ChemistryException {
    String notation;

    notation = "PEPTIDE1{(A+G).L}$$$$";
    testxHELM1(notation);

  }

  private void testxHELM2(String notation) throws ExceptionState, IOException, JDOMException, MonomerException,
      org.jdom2.JDOMException, ChemistryException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    System.out.println(xHelmNotationExporter.getXHELM2(parserHELM2.getHELM2Notation()));
  }

  private void testxHELM1(String notation) throws ExceptionState, IOException, JDOMException, MonomerException,
      HELM1FormatException, org.jdom2.JDOMException, NotationException, CTKException, ValidationException, ChemistryException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    System.out.println(xHelmNotationExporter.getXHELM(parserHELM2.getHELM2Notation()));
  }
}

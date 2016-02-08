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

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.ManipulatorFactory.ManipulatorType;
import org.helm.notation.NotationException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.jdom2.JDOMException;
import org.testng.annotations.Test;

/**
 * OutputFilesTest
 *
 * @author hecht
 */
public class MDLUtilsTest {

  @Test
  public void TestGenerationMDL() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, IOException, NotationException, ChemistryException {
    if (Chemistry.getInstance().getManipulatorType().equals(ManipulatorType.MARVIN)) {
      String notation = "RNA1{R(U)P}|RNA2{R(U)P.R(G)}|RNA3{R(C)P.R(A)}|CHEM1{[MCC]}$RNA1,CHEM1,3:R2-1:R1|RNA2,RNA3,5:pair-2:pair|RNA2,RNA3,2:pair-5:pair$$$";
      System.out.println(MDLUtils.generateMDL(readNotation(notation)));
    }
  }

  @Test
  public void TestGenerationMDLOligo() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, IOException, NotationException, ChemistryException {
    String notation = "RNA1{R(A)P.R(G)}$$$$";

    System.out.println(MDLUtils.generateMDL(readNotation(notation)));
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

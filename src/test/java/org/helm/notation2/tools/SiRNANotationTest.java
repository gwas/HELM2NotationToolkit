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
import org.helm.notation.NotationException;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.tools.SiRNANotation;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SiRNANotationTest {
  @Test
  public void testGetSirnaNotation() throws NotationException, IOException,
      JDOMException, org.helm.notation2.parser.exceptionparser.NotationException, FastaFormatException, HELM2HandledException, RNAUtilsException, ChemistryException, CTKException {
    String senseSeq = "CGAAAUGUUCAUACUGUUGdTdT";
    String antiSenseSeq = "UUACAAUUUGGACUUUCCGdTdT";

    String siNotationNew = SiRNANotation.getSiRNANotation(senseSeq, antiSenseSeq).toHELM2();
    Assert.assertEquals("RNA1{R(C)P.R(G)P.R(A)P.R(A)P.R(A)P.R(U)P.R(G)P.R(U)P.R(U)P.R(C)P.R(A)P.R(U)P.R(A)P.R(C)P.R(U)P.R(G)P.R(U)P.R(U)P.R(G)P.[dR](T)P.[dR](T)}|RNA2{R(U)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.R(U)P.R(U)P.R(U)P.R(G)P.R(G)P.R(A)P.R(C)P.R(U)P.R(U)P.R(U)P.R(C)P.R(C)P.R(G)P.[dR](T)P.[dR](T)}$RNA1,RNA2,5:pair-50:pair|RNA1,RNA2,8:pair-47:pair|RNA1,RNA2,11:pair-44:pair|RNA1,RNA2,14:pair-41:pair$$RNA1{ss}|RNA2{as}$V2.0", siNotationNew);

    siNotationNew = SiRNANotation.getSirnaNotation(senseSeq, antiSenseSeq, NucleotideParser.RNA_DESIGN_TUSCHL_19_PLUS_2).toHELM2();
    Assert.assertEquals("RNA1{R(C)P.R(G)P.R(A)P.R(A)P.R(A)P.R(U)P.R(G)P.R(U)P.R(U)P.R(C)P.R(A)P.R(U)P.R(A)P.R(C)P.R(U)P.R(G)P.R(U)P.R(U)P.R(G)P.[dR](T)P.[dR](T)}|RNA2{R(U)P.R(U)P.R(A)P.R(C)P.R(A)P.R(A)P.R(U)P.R(U)P.R(U)P.R(G)P.R(G)P.R(A)P.R(C)P.R(U)P.R(U)P.R(U)P.R(C)P.R(C)P.R(G)P.[dR](T)P.[dR](T)}$RNA1,RNA2,2:pair-56:pair|RNA1,RNA2,5:pair-53:pair|RNA1,RNA2,11:pair-47:pair|RNA1,RNA2,14:pair-44:pair|RNA1,RNA2,20:pair-38:pair|RNA1,RNA2,23:pair-35:pair|RNA1,RNA2,29:pair-29:pair|RNA1,RNA2,32:pair-26:pair|RNA1,RNA2,38:pair-20:pair|RNA1,RNA2,44:pair-14:pair|RNA1,RNA2,47:pair-11:pair|RNA1,RNA2,50:pair-8:pair$$RNA1{ss}|RNA2{as}$V2.0", siNotationNew);

  }
}

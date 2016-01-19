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

import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;


/**
 * PeptideUtils, class to provide peptide utils
 * 
 * @author hecht
 */
public final class PeptideUtils {

  /**
   * method to produce for a peptide PolymerNotation the natural analogue
   * sequence
   * 
   * @param polymer PolymerNotation
   * @return natural analogue sequence
   * @throws HELM2HandledException if the polymer contains HELM2 features
   */
  protected static String getNaturalAnalogSequence(PolymerNotation polymer) throws HELM2HandledException {
    return FastaFormat.generateFastaFromPeptide(MethodsForContainerHELM2.getListOfHandledMonomers(polymer.getListMonomers()));
  }

}

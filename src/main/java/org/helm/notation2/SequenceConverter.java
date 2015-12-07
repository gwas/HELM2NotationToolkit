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

import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;


/**
 * SequenceConverter class to convert sequence into the ContainerHELM2 object
 * 
 * @author hecht
 */
public class SequenceConverter {

  /**
   * method to read a peptide sequence and generate a containerhelm2 object of it
   * 
   * @param notation
   * @return ContainerHELM2 object
   * @throws FastaFormatException if the peptide sequence is not in the right format
   * @throws NotationException if the notation object can not be built
   * @throws JDOMException
   */
  protected static ContainerHELM2 readPeptide(String notation) throws FastaFormatException, NotationException {
    HELM2Notation helm2notation = new HELM2Notation();
    PolymerNotation polymer = new PolymerNotation("PEPTIDE1");
    helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(), FastaFormat.generateElementsOfPeptide(notation, polymer.getPolymerID())));
    ContainerHELM2 containerhelm2 = new ContainerHELM2(helm2notation, new InterConnections());
    return containerhelm2;
  }

  /**
   * method to read a rna/dna sequence and generate a containerhelm2 object of
   * it
   * 
   * @param notation
   * @return ContainerHELM2 object
   * @throws FastaFormatException if the rna/dna sequence is not in the right
   *           format
   * @throws NotationException if the notation object can not be built
   */
  protected static ContainerHELM2 readRNA(String notation) throws FastaFormatException, NotationException {
    HELM2Notation helm2notation = new HELM2Notation();
    PolymerNotation polymer = new PolymerNotation("RNA1");
    helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(), FastaFormat.generateElementsforRNA(notation, polymer.getPolymerID())));
    ContainerHELM2 containerhelm2 = new ContainerHELM2(helm2notation, new InterConnections());
    return containerhelm2;
  }



}

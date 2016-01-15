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
import java.util.List;

import org.helm.notation.MonomerException;
import org.helm.notation.StructureException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.polymer.PeptideEntity;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.helm.notation2.parser.notation.polymer.RNAEntity;
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

  protected static String getNucleotideSequenceFromNotation(HELM2Notation helm2notation) throws NotationException, HELM2HandledException, org.helm.notation.NotationException, MonomerException,
      IOException, JDOMException, StructureException, RNAUtilsException {
    List<PolymerNotation> polymers = helm2notation.getListOfPolymers();
    StringBuffer sb = new StringBuffer();
    for (PolymerNotation polymer : polymers) {
      if (!(polymer.getPolymerID() instanceof RNAEntity)) {
        throw new NotationException("Input complex notation contains non-nucleic acid polymer");
      }
      sb.append(RNAUtils.getNucleotideSequence(polymer) + " ");
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();
  }

  /**
   * @param helm2Notation
   * @return
   * @throws NotationException
   * @throws RNAUtilsException
   * @throws HELM2HandledException
   */
  public static String getNucleotideNaturalAnalogSequenceFromNotation(HELM2Notation helm2Notation) throws NotationException, HELM2HandledException, RNAUtilsException {
    List<PolymerNotation> polymers = helm2Notation.getListOfPolymers();
    StringBuffer sb = new StringBuffer();
    for (PolymerNotation polymer : polymers) {
      if (!(polymer.getPolymerID() instanceof RNAEntity)) {
        throw new NotationException("Input complex notation contains non-nucleic acid polymer");
      }
      sb.append(RNAUtils.getNaturalAnalogSequence(polymer) + " ");
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();
  }

  /**
   * @param helm2Notation
   * @return
   * @throws NotationException
   * @throws RNAUtilsException
   * @throws HELM2HandledException
   */
  public static String getPeptideNaturalAnalogSequenceFromNotation(HELM2Notation helm2Notation) throws NotationException, HELM2HandledException {
    List<PolymerNotation> polymers = helm2Notation.getListOfPolymers();
    StringBuffer sb = new StringBuffer();
    for (PolymerNotation polymer : polymers) {
      if (!(polymer.getPolymerID() instanceof PeptideEntity)) {
        throw new NotationException("Input complex notation contains non-peptide polymer");
      }
      sb.append(PeptideUtils.getNaturalAnalogSequence(polymer) + " ");
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();
  }


}

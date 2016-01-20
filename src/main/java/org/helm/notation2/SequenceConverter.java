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

import java.util.List;

import org.helm.notation.NucleotideLoadingException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.PeptideUtilsException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.polymer.PeptideEntity;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;

/**
 * SequenceConverter class to convert sequence into the ContainerHELM2 object
 * and vice versa
 *
 * @author hecht
 */
public final class SequenceConverter {

  /**
   * Default constructor.
   */
  private SequenceConverter() {

  }

  /**
   * method to read a peptide sequence and generate a containerhelm2 object of
   * it
   *
   * @param notation
   * @return ContainerHELM2 object
   * @throws FastaFormatException if the peptide sequence is not in the right
   *           format
   * @throws NotationException if the notation object can not be built
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

  /**
   * method to get for all rna/dnas the nucleotide sequence form an
   * HELM2Notation
   *
   * @param helm2notation input HELM2Notation
   * @return rna/dna nucleotide sequences divided with white space
   * @throws NucleotideLoadingException
   * @throws NotationException
   * @throws HELM2HandledException if HELM2 features are involved
   */
  protected static String getNucleotideSequenceFromNotation(HELM2Notation helm2notation) throws NotationException, NucleotideLoadingException, HELM2HandledException {
    List<PolymerNotation> polymers = helm2notation.getListOfPolymers();
    StringBuffer sb = new StringBuffer();
    for (PolymerNotation polymer : polymers) {
      try {
        sb.append(RNAUtils.getNucleotideSequence(polymer) + " ");
      } catch (RNAUtilsException e) {
        throw new NotationException("Input complex notation contains non-nucleic acid polymer");
      }
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();
  }

  /**
   * method to get for all peptides the sequence
   *
   * @param helm2notation HELM2Notation
   * @return rna sequences divided by white space
   * @throws HELM2HandledException if the polymer contains HELM2 features
   * @throws PeptideUtilsException if the polymer is not a peptide
   */
  protected static String getPeptideSequenceFromNotation(HELM2Notation helm2notation) throws HELM2HandledException, PeptideUtilsException {
    List<PolymerNotation> polymers = helm2notation.getListOfPolymers();
    StringBuffer sb = new StringBuffer();
    for (PolymerNotation polymer : polymers) {
      sb.append(PeptideUtils.getSequence(polymer) + " ");

    }
    sb.setLength(sb.length() - 1);
    return sb.toString();
  }

  /**
   * method to generate for all rna polymers the natural analogue sequence
   *
   * @param helm2Notation input HELm2Notation
   * @return natural analogue sequence(s)
   * @throws NotationException if the input complex notation contains
   *           non-nucleid acid polymer(s)
   * @throws HELM2HandledException if the polymer(s) contain(s) HELM2 features
   */
  public static String getNucleotideNaturalAnalogSequenceFromNotation(HELM2Notation helm2Notation) throws NotationException, HELM2HandledException {
    List<PolymerNotation> polymers = helm2Notation.getListOfPolymers();
    StringBuffer sb = new StringBuffer();
    for (PolymerNotation polymer : polymers) {
      try {
        sb.append(RNAUtils.getNaturalAnalogSequence(polymer) + " ");
      } catch (RNAUtilsException e) {
        throw new NotationException("Input complex notation contains non-nucleid acid polymer");
      }
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();
  }

  /**
   * method to generate for all peptide polymers the natural analogue sequence
   *
   * @param helm2Notation input HELM2Notation
   * @return natural analogue sequence(s)
   * @throws NotationException if the input complex notation contains
   *           non-peptide polymer(s)
   * @throws HELM2HandledException if the polymer(s) contain(s) HELM2 features
   * @throws PeptideUtilsException if the polymer is not a peptide
   */
  public static String getPeptideNaturalAnalogSequenceFromNotation(HELM2Notation helm2Notation) throws NotationException, HELM2HandledException, PeptideUtilsException {
    List<PolymerNotation> polymers = helm2Notation.getListOfPolymers();
    StringBuffer sb = new StringBuffer();
    for (PolymerNotation polymer : polymers) {
      if (!(polymer.getPolymerID() instanceof PeptideEntity)) {
        throw new NotationException("Input complex notation contains non-peptide polymer(s)");
      }
      sb.append(PeptideUtils.getNaturalAnalogSequence(polymer) + " ");
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();
  }

}

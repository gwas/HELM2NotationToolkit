/*--
 *
 * @(#) SequenceConverter.java
 *
 *
 */
package org.helm.notation2;

import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;


/**
 * SequenceConverter class to convert sequence into the ContainerHELM2 object
 * 
 * @author hecht
 */
public class SequenceConverter {

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
    helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(), FastaFormat.generateElementsOfPeptide(notation, polymer.getPolymerID()), null));
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
    helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(), FastaFormat.generateElementsforRNA(notation, polymer.getPolymerID()), null));
    ContainerHELM2 containerhelm2 = new ContainerHELM2(helm2notation, new InterConnections());
    return containerhelm2;
  }



}


package org.helm.notation2;


import java.io.IOException;
import java.util.List;

import org.helm.notation.NucleotideFactory;
import org.helm.notation.model.Monomer;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.helm.notation2.parser.notation.polymer.RNAEntity;
import org.jdom2.JDOMException;


/**
 * RNAUtils
 * 
 * @author hecht
 */
public class RNAUtils {


  protected void getSirnaNotation() {

  }

  protected void getFormatedSirnaSequence() {

  }

  /**
   * method to generate the reverse rna/dna sequence of a given polymer
   * 
   * @param polymer
   * @return sequence
   * @throws RNAUtilsException if the polymer is not a RNA/DNA
   * @throws HELM2HandledException if the polymer can not be downcasted
   */
  protected static String getReverseSequence(PolymerNotation polymer) throws RNAUtilsException, HELM2HandledException {
    StringBuilder sb = new StringBuilder(getNaturalAnalogSequence(polymer));
    return sb.reverse().toString();
  }

  /**
   * method to generate the natural analog sequence of a rna/dna of a given
   * polymer
   * 
   * @param polymer
   * @return sequence
   * @throws HELM2HandledException if the polymer can be downcasted to HELM1
   * @throws RNAUtilsException if the polymer is not RNA or DNA
   */
  protected static String getNaturalAnalogSequence(PolymerNotation polymer) throws HELM2HandledException, RNAUtilsException {
    checkRNA(polymer);
    return FastaFormat.generateFastaFromRNA(MethodsForContainerHELM2.getListOfHandledMonomers(polymer.getListMonomers()));
  }


  /**
   * method to check if two given polymers are in the opposite direction
   * 
   * @param polymerOne
   * @param polymerTwo
   * @return true, if they are opposite to each other, false otherwise
   * @throws RNAUtilsException if the polymers are not RNA/DNA
   * @throws HELM2HandledException if the polymers can not be downcasted
   */
  protected static boolean AreInOppositeDirection(PolymerNotation polymerOne, PolymerNotation polymerTwo) throws RNAUtilsException, HELM2HandledException {
    checkRNA(polymerOne);
    checkRNA(polymerTwo);
    String sequenceOne = FastaFormat.generateFastaFromRNA(MethodsForContainerHELM2.getListOfHandledMonomers(polymerOne.getListMonomers()));
    String sequenceTwo = FastaFormat.generateFastaFromRNA(MethodsForContainerHELM2.getListOfHandledMonomers(polymerTwo.getListMonomers()));
    return sequenceOne.equals(new StringBuilder(sequenceTwo).reverse().toString());
  }

  protected void getMaxMatchFragment() {

  }

  /**
   * method to remove the phosphat of the last nucleotide, if it is there
   * 
   * @param polymer
   * @throws RNAUtilsException
   * @throws HELM2HandledException
   */
  protected PolymerNotation removeLastP(PolymerNotation polymer) throws RNAUtilsException, HELM2HandledException {
    checkRNA(polymer);
    List<Monomer> listMonomers = MethodsForContainerHELM2.getListOfHandledMonomers(polymer.getPolymerElements().getListOfElements());
    if (listMonomers.get(listMonomers.size() - 1).getNaturalAnalog().equals("P")) {

    }
    return null;
  }

  /**
   * method to get the reverse complement polymer from a rna/dna polymer
   * 
   * @param polymer
   * @return reverse complement polymer
   * @throws NotationException
   * @throws FastaFormatException
   * @throws HELM2HandledException
   * @throws IOException
   * @throws JDOMException
   * @throws org.helm.notation.NotationException
   * @throws RNAUtilsException
   */
  protected static PolymerNotation getReverseComplement(PolymerNotation polymer) throws NotationException, FastaFormatException, HELM2HandledException, IOException, JDOMException,
      org.helm.notation.NotationException, RNAUtilsException {
    checkRNA(polymer);
    PolymerNotation reversePolymer = SequenceConverter.readRNA(generateReverseComplement(polymer)).getHELM2Notation().getCurrentPolymer();
    reversePolymer = new PolymerNotation(reversePolymer.getPolymerID(), reversePolymer.getPolymerElements(), "ReverseComplement to " + polymer.getPolymerID().getID() );
    return reversePolymer;
  }

  /**
   * method to generate the reverse complement sequence from a rna/dna polymer
   * 
   * @param polymer
   * @return reverse complement sequence
   * @throws HELM2HandledException
   * @throws IOException
   * @throws JDOMException
   * @throws org.helm.notation.NotationException
   * @throws RNAUtilsException
   */
  private static String generateReverseComplement(PolymerNotation polymer) throws HELM2HandledException, IOException, JDOMException, org.helm.notation.NotationException, RNAUtilsException {
    return generateNormalComplement(polymer).reverse().toString();
  }

  /**
   * method to generate the complement sequence from a rna/dna polymer
   * 
   * @param polymer
   * @return complement sequence saved in StringBuilder
   * @throws HELM2HandledException
   * @throws IOException
   * @throws JDOMException
   * @throws org.helm.notation.NotationException
   * @throws RNAUtilsException
   */
  private static StringBuilder generateNormalComplement(PolymerNotation polymer) throws HELM2HandledException, IOException, JDOMException, org.helm.notation.NotationException, RNAUtilsException {
    String sequence = getNaturalAnalogSequence(polymer);
    StringBuilder sb = new StringBuilder();
    for (char c : sequence.toCharArray()) {
      sb.append(NucleotideFactory.getInstance().getReverseNucleotideTemplateMap().get(c));
    }
    return sb;
  }

  /**
   * method to get the normal complement polymer from a given polymer
   * 
   * @param polymer
   * @return normal complement polymer
   * @throws NotationException
   * @throws FastaFormatException
   * @throws HELM2HandledException
   * @throws IOException
   * @throws JDOMException
   * @throws org.helm.notation.NotationException
   * @throws RNAUtilsException
   */
  protected static PolymerNotation getNormalComplement(PolymerNotation polymer) throws NotationException, FastaFormatException, HELM2HandledException, IOException, JDOMException,
      org.helm.notation.NotationException, RNAUtilsException {
    checkRNA(polymer);
    PolymerNotation complementPolymer = SequenceConverter.readRNA(generateNormalComplement(polymer).toString()).getHELM2Notation().getCurrentPolymer();
    complementPolymer = new PolymerNotation(complementPolymer.getPolymerID(), complementPolymer.getPolymerElements(), "NormalComplement to " + polymer.getPolymerID().getID());
    return complementPolymer;
  }



  protected void hasNucleotideModification(PolymerNotation polymer) {

  }

  protected void hybridize() {

  }

  /**
   * methods of this class are only allowed for RNA/DNA polymers
   * 
   * @param polymer
   * @throws RNAUtilsException if the polymer is not a RNA/DNA type
   */
  private static void checkRNA(PolymerNotation polymer) throws RNAUtilsException {
    if (!(polymer.getPolymerID() instanceof RNAEntity)) {
      throw new RNAUtilsException("Functions can only be called for RNA/DNA");
    }
  }


}

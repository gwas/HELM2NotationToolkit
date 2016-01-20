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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.helm.notation.MonomerException;
import org.helm.notation.NucleotideFactory;
import org.helm.notation.StructureException;
import org.helm.notation.model.Monomer;
import org.helm.notation.model.Nucleotide;
import org.helm.notation.tools.SimpleNotationParser;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.helm.notation2.parser.notation.polymer.RNAEntity;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RNAUtils, class to provide methods for rna polymer
 * 
 * @author hecht
 */
public class RNAUtils {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(RNAUtils.class);

  private static Map<String, String> complementMap = null;

  /**
   * initialize Map to get the complement of each nucleotide
   */
  private static void initComplementMap() {
    complementMap = new HashMap<String, String>();
    complementMap.put("A", "U");
    complementMap.put("G", "C");
    complementMap.put("C", "G");
    complementMap.put("U", "A");
    complementMap.put("T", "A");
    complementMap.put("X", "X");
  }

  /**
   * method to read a nucleotide sequence and build a double strand of it
   * 
   * @param sequence nucleotide sequence
   * @return ContainerHELM2
   * @throws NotationException
   * @throws FastaFormatException
   * @throws HELM2HandledException
   * @throws IOException
   * @throws JDOMException
   * @throws org.helm.notation.NotationException
   * @throws RNAUtilsException
   */
  protected static ContainerHELM2 getSirnaNotation(String sequence) throws NotationException, FastaFormatException,
      HELM2HandledException, IOException, JDOMException,
      org.helm.notation.NotationException,
      RNAUtilsException {

    /* Build the sense + antisense sequence */
    ContainerHELM2 sense = SequenceConverter.readRNA(sequence);
    PolymerNotation antisense = new PolymerNotation("RNA2");
    PolymerNotation current = getAntiparallel(sense.getAllPolymers().get(0));
    sense.getHELM2Notation().addPolymer(new PolymerNotation(antisense.getPolymerID(), current.getPolymerElements(),
        current.getAnnotation()));

    /* Build the hydrogenbonds between the two */
    List<ConnectionNotation> connections =
        hybridize(sense.getHELM2Notation().getListOfPolymers().get(0), sense.getHELM2Notation().getListOfPolymers().get(1));
    for (ConnectionNotation connection : connections) {
      sense.getHELM2Notation().addConnection(connection);
    }
    return sense;
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
   * @throws HELM2HandledException if the polymer can not be downcasted to HELM1
   * @throws RNAUtilsException if the polymer is not RNA or DNA
   */
  protected static String getNaturalAnalogSequence(PolymerNotation polymer) throws HELM2HandledException,
      RNAUtilsException {
    checkRNA(polymer);
    return FastaFormat.generateFastaFromRNA(MethodsForContainerHELM2.getListOfHandledMonomers(polymer.getListMonomers()));
  }

  /**
   * method to check if two given polymers are complement to each other
   * 
   * @param polymerOne PolymerNotation of the first polymer
   * @param polymerTwo PolymerNotation of the second polymer
   * @return true, if they are opposite to each other, false otherwise
   * @throws RNAUtilsException if the polymers are not RNA/DNA
   * @throws HELM2HandledException if the polymers can not be downcasted
   * @throws org.helm.notation.NotationException
   * @throws JDOMException
   * @throws IOException
   * @throws FastaFormatException
   * @throws NotationException
   */
  protected static boolean AreAntiparallel(PolymerNotation polymerOne, PolymerNotation polymerTwo)
      throws RNAUtilsException, HELM2HandledException, NotationException, FastaFormatException,
      IOException,
      JDOMException, org.helm.notation.NotationException {
    checkRNA(polymerOne);
    checkRNA(polymerTwo);
    PolymerNotation antiparallel = getAntiparallel(polymerOne);
    String sequenceOne =
        FastaFormat.generateFastaFromRNA(MethodsForContainerHELM2.getListOfHandledMonomers(antiparallel.getListMonomers()));
    String sequenceTwo =
        FastaFormat.generateFastaFromRNA(MethodsForContainerHELM2.getListOfHandledMonomers(polymerTwo.getListMonomers()));
    return sequenceOne.equals(sequenceTwo);
  }

  protected void getMaxMatchFragment() {

  }

  /**
   * method to remove the phosphat of the last nucleotide, if it is there ToDo
   * 
   * @param polymer
   * @throws RNAUtilsException if the polymer is not a rna/dna
   * @throws HELM2HandledException if the polymer contains HELM2 features
   */
  protected PolymerNotation removeLastP(PolymerNotation polymer) throws RNAUtilsException, HELM2HandledException {
    checkRNA(polymer);
    List<Monomer> listMonomers =
        MethodsForContainerHELM2.getListOfHandledMonomers(polymer.getPolymerElements().getListOfElements());
    if (listMonomers.get(listMonomers.size() - 1).getNaturalAnalog().equals("P")) {

    }
    return null;
  }

  /**
   * method to get the antiparallel polymer for a rna/dna polymer
   * 
   * @param polymer PolymerNotation
   * @return anitparallel polymer
   * @throws NotationException
   * @throws FastaFormatException
   * @throws HELM2HandledException
   * @throws IOException
   * @throws JDOMException
   * @throws org.helm.notation.NotationException
   * @throws RNAUtilsException
   */
  protected static PolymerNotation getAntiparallel(PolymerNotation polymer) throws NotationException,
      FastaFormatException, HELM2HandledException, IOException, JDOMException,
      org.helm.notation.NotationException, RNAUtilsException {
    checkRNA(polymer);
    PolymerNotation reversePolymer =
        SequenceConverter.readRNA(generateAntiparallel(polymer)).getHELM2Notation().getCurrentPolymer();
    reversePolymer =
        new PolymerNotation(reversePolymer.getPolymerID(), reversePolymer.getPolymerElements(), "Antiparallel to "
            + polymer.getPolymerID().getID());
    return reversePolymer;
  }

  /**
   * method to generate the antiparallel sequence for a rna/dna polymer
   * 
   * @param polymer PolymerNotation
   * @return antiparallel sequence
   * @throws HELM2HandledException
   * @throws IOException
   * @throws JDOMException
   * @throws org.helm.notation.NotationException
   * @throws RNAUtilsException
   */
  private static String generateAntiparallel(PolymerNotation polymer) throws HELM2HandledException, IOException,
      JDOMException, org.helm.notation.NotationException, RNAUtilsException {
    return generateComplement(polymer).reverse().toString();
  }

  /**
   * method to get the polymer with the inverse sequence of the current polymer
   * 
   * @param polymer PolymerNotation
   * @return reverse complement sequence
   * @throws RNAUtilsException
   * @throws NotationException
   * @throws FastaFormatException
   * @throws HELM2HandledException
   * @throws IOException
   * @throws JDOMException
   * @throws org.helm.notation.NotationException
   */
  protected static PolymerNotation getInverse(PolymerNotation polymer) throws RNAUtilsException, NotationException,
      FastaFormatException, HELM2HandledException, IOException, JDOMException,
      org.helm.notation.NotationException {
    checkRNA(polymer);
    PolymerNotation inverse =
        SequenceConverter.readRNA(generateInverse(polymer).toString()).getHELM2Notation().getListOfPolymers().get(0);
    inverse =
        new PolymerNotation(inverse.getPolymerID(), inverse.getPolymerElements(), "Inverse to "
            + polymer.getPolymerID().getID());
    return inverse;

  }

  /**
   * method to generate the inverse sequence of the given polymer
   * 
   * @param polymer PolymerNotation
   * @return inverse sequence
   * @throws HELM2HandledException
   * @throws RNAUtilsException
   */
  private static StringBuilder generateInverse(PolymerNotation polymer) throws HELM2HandledException, RNAUtilsException {
    initComplementMap();
    String sequence = getNaturalAnalogSequence(polymer);
    StringBuilder sb = new StringBuilder(sequence);
    return sb.reverse();
  }

  /**
   * method to generate the complement sequence for a rna/dna polymer
   * 
   * @param polymer PolymerNotation
   * @return complement sequence saved in StringBuilder
   * @throws HELM2HandledException
   * @throws IOException
   * @throws JDOMException
   * @throws org.helm.notation.NotationException
   * @throws RNAUtilsException
   */
  private static StringBuilder generateComplement(PolymerNotation polymer) throws HELM2HandledException, IOException,
      JDOMException, org.helm.notation.NotationException, RNAUtilsException {
    initComplementMap();
    String sequence = getNaturalAnalogSequence(polymer);
    StringBuilder sb = new StringBuilder();
    for (char c : sequence.toCharArray()) {
      sb.append(complementMap.get(String.valueOf(c)));
    }
    return sb;
  }

  /**
   * method to get the normal complement polymer for a given polymer
   * 
   * @param polymer PolymerNotation
   * @return normal complement polymer
   * @throws NotationException
   * @throws FastaFormatException
   * @throws HELM2HandledException
   * @throws IOException
   * @throws JDOMException
   * @throws org.helm.notation.NotationException
   * @throws RNAUtilsException
   */
  protected static PolymerNotation getComplement(PolymerNotation polymer) throws NotationException,
      FastaFormatException, HELM2HandledException, IOException, JDOMException,
      org.helm.notation.NotationException, RNAUtilsException {
    checkRNA(polymer);
    PolymerNotation complementPolymer =
        SequenceConverter.readRNA(generateComplement(polymer).toString()).getHELM2Notation().getListOfPolymers().get(0);
    complementPolymer =
        new PolymerNotation(complementPolymer.getPolymerID(), complementPolymer.getPolymerElements(),
            "NormalComplement to " + polymer.getPolymerID().getID());
    return complementPolymer;
  }

  protected void hasNucleotideModification(PolymerNotation polymer) {

  }

  /**
   * method to hybridize two PolymerNotations together if they are antiparallel
   * 
   * @param one PolymerNotation first
   * @param two PolymerNotation second
   * @return
   * @throws NotationException
   * @throws RNAUtilsException
   * @throws HELM2HandledException
   * @throws FastaFormatException
   * @throws IOException
   * @throws JDOMException
   * @throws org.helm.notation.NotationException
   */
  protected static List<ConnectionNotation> hybridize(PolymerNotation one, PolymerNotation two)
      throws NotationException, RNAUtilsException, HELM2HandledException, FastaFormatException, IOException,
      JDOMException, org.helm.notation.NotationException {
    checkRNA(one);
    checkRNA(two);

    List<ConnectionNotation> connections = new ArrayList<ConnectionNotation>();
    ConnectionNotation connection;
    /* Length of the two rnas have to be the same */
    if (AreAntiparallel(one, two)) {
      for (int i = 0; i < PolymerUtils.getTotalMonomerCount(one); i++) {
        int backValue = PolymerUtils.getTotalMonomerCount(one) - i;
        int firstValue = i + 1;

        String details = firstValue + ":pair-" + backValue + ":pair";
        connection = new ConnectionNotation(one.getPolymerID(), two.getPolymerID(), details);
        connections.add(connection);
      }
      return connections;
    }
    else {
      throw new RNAUtilsException("The given RNAs are not antiparallel to each other");
    }

  }

  /**
   * methods of this class are only allowed for RNA/DNA polymers
   * 
   * @param polymer PolymerNotation
   * @throws RNAUtilsException if the polymer is not a RNA/DNA type
   */
  private static void checkRNA(PolymerNotation polymer) throws RNAUtilsException {
    if (!(polymer.getPolymerID() instanceof RNAEntity)) {
      throw new RNAUtilsException("Functions can only be called for RNA/DNA");
    }
  }

  /**
   * method to get the nucleotide sequence for the polymer
   * 
   * @param polymer PolymerNotation
   * @return nucleotide sequence
   * @throws HELM2HandledException
   * @throws org.helm.notation.NotationException
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   * @throws StructureException
   * @throws RNAUtilsException
   * @throws NotationException
   */
  protected static String getNucleotideSequence(PolymerNotation polymer) throws HELM2HandledException, org.helm.notation.NotationException, MonomerException, IOException, JDOMException,
      StructureException, RNAUtilsException, NotationException {
    List<Nucleotide> nucleotides = getNucleotideList(polymer);
    StringBuffer sb = new StringBuffer();
    int count = 0;
    Map<String, String> reverseNucMap = NucleotideFactory.getInstance().getReverseNucleotideTemplateMap();
    for (Nucleotide nuc : nucleotides) {
      String nucleotide = nuc.getNotation();
      String nucleoside = nuc.getNucleosideNotation();
      String linker = nuc.getLinkerNotation();

      // it is ok for the first nucleotide not to have a nucleoside
      if (count == 0 && nucleoside.length() == 0) {
        sb.append(nuc.getPhosphateMonomer().getAlternateId());
        count++;
        continue;
      }

      // it is ok for the last nucleotide not to have a linker
      if (count == nucleotides.size() - 1 && linker.length() == 0) {
        nucleotide = nucleotide + Monomer.ID_P;
      }

      if (reverseNucMap.containsKey(nucleotide)) {
        sb.append(reverseNucMap.get(nucleotide));
      } else {
        throw new NotationException("Unknown nucleotide found for "
            + nucleotide + " : missing nucleotide template");
      }

      count++;
    }
    return sb.toString();
  }

  /**
   * method to get all nucleotides for one polymer
   * 
   * @param polymer PolymerNotation
   * @return List of nucleotides
   * @throws HELM2HandledException
   * @throws org.helm.notation.NotationException
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   * @throws StructureException
   * @throws RNAUtilsException
   */
  private static List<Nucleotide> getNucleotideList(PolymerNotation polymer) throws HELM2HandledException, org.helm.notation.NotationException, MonomerException, IOException, JDOMException,
      StructureException, RNAUtilsException {
    checkRNA(polymer);
    List<Nucleotide> nucleotides = new ArrayList<Nucleotide>();
    /* check for HELM2Elements */
    List<MonomerNotation> monomerNotations = polymer.getPolymerElements().getListOfElements();
    for (MonomerNotation monomerNotation : monomerNotations) {
      if ((!(monomerNotation instanceof MonomerNotationUnitRNA)) || Integer.parseInt(monomerNotation.getCount()) != 1) {
        LOG.info("MonomerNotation contains HELM2 Elements " + monomerNotation);
        throw new HELM2HandledException("HELM2 Elements are involved");
      }
      nucleotides.add(SimpleNotationParser.getNucleotideList(monomerNotation.getID()).get(0));
    }
    return nucleotides;
  }

}

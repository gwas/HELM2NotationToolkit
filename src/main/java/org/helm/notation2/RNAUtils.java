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
import org.helm.notation.NucleotideLoadingException;
import org.helm.notation.StructureException;
import org.helm.notation.model.Monomer;
import org.helm.notation.model.Nucleotide;
import org.helm.notation.tools.NucleotideSequenceParser;
import org.helm.notation.tools.SimpleNotationParser;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroup;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroupElement;
import org.helm.notation2.parser.notation.polymer.MonomerNotationList;
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
   * @throws RNAUtilsException if the sense or antisense strand can not be built
   * @throws JDOMException
   * @throws IOException
   * @throws org.helm.notation.NotationException
   */
  protected static ContainerHELM2 getSirnaNotation(String sequence) throws RNAUtilsException, IOException, JDOMException, org.helm.notation.NotationException {
    /* Build the sense + antisense sequence */
    ContainerHELM2 sense;
    try {
      sense = SequenceConverter.readRNA(sequence);
    } catch (NotationException | FastaFormatException e) {
      throw new RNAUtilsException("Sense Strand can not be built");
    }
    try {
      PolymerNotation antisense = new PolymerNotation("RNA2");
      PolymerNotation current;
      current = getAntiparallel(sense.getAllPolymers().get(0));
      sense.getHELM2Notation().addPolymer(new PolymerNotation(antisense.getPolymerID(), current.getPolymerElements(),
          current.getAnnotation()));

      /* Build the hydrogenbonds between the two */
      List<ConnectionNotation> connections =
          hybridize(sense.getHELM2Notation().getListOfPolymers().get(0), sense.getHELM2Notation().getListOfPolymers().get(1));
      for (ConnectionNotation connection : connections) {
        sense.getHELM2Notation().addConnection(connection);
      }
      return sense;
    } catch (NotationException | HELM2HandledException | IOException | JDOMException e) {
      throw new RNAUtilsException("Antisense Strand can not be built");
    }

  }

  /**
   * method to generate the reverse rna/dna sequence of a given polymer
   *
   * @param polymer PolymerNotation
   * @return sequence reverse rna/dna sequence
   * @throws RNAUtilsException if the polymer is not a RNA/DNA
   * @throws HELM2HandledException if the polymer contains HELM2 features
   */
  protected static String getReverseSequence(PolymerNotation polymer) throws RNAUtilsException, HELM2HandledException {
    StringBuilder sb = new StringBuilder(getNaturalAnalogSequence(polymer));
    return sb.reverse().toString();
  }

  /**
   * method to generate the natural analogue sequence of a rna/dna of a given
   * polymer
   *
   * @param polymer PolymerNotation
   * @return sequence natural analogue sequence
   * @throws HELM2HandledException if the polymer contains HELM2 features
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
   * @throws RNAUtilsException if the polymers are not rna/dna or the
   *           antiparallel strand can not be built from polymerOne
   * @throws HELM2HandledException if the polymers contain HELM2 features
   * @throws JDOMException
   * @throws IOException
   */
  protected static boolean areAntiparallel(PolymerNotation polymerOne, PolymerNotation polymerTwo) throws RNAUtilsException, HELM2HandledException, IOException, JDOMException {
    checkRNA(polymerOne);
    checkRNA(polymerTwo);
    PolymerNotation antiparallel = getAntiparallel(polymerOne);
    String sequenceOne =
        FastaFormat.generateFastaFromRNA(MethodsForContainerHELM2.getListOfHandledMonomers(antiparallel.getListMonomers()));
    String sequenceTwo =
        FastaFormat.generateFastaFromRNA(MethodsForContainerHELM2.getListOfHandledMonomers(polymerTwo.getListMonomers()));
    return sequenceOne.equals(sequenceTwo);
  }

  /**
   * method to get the largest matched fragment between two sequences, replace T
   * with U before Match
   *
   * @param seq1 single letter, all upper case nucleotide sequence
   * @param seq2 single letter, all upper case nucleotide sequence
   * @return
   * @throws org.helm.notation.NotationException
   */
  protected static String getMaxMatchFragment(String seq1, String seq2) throws org.helm.notation.NotationException {
    return NucleotideSequenceParser.getMaxMatchFragment(seq1, seq2);
  }

  /**
   * method to remove the phosphate of the last nucleotide
   *
   * @param polymer PolymerNotation
   * @throws RNAUtilsException if the PolmyerNotation is not a rna or dna
   * @throws IOException
   * @throws NotationException if the changed notation object can not be
   *           generated
   * @throws HELM2HandledException if HELM2 features are involved
   */
  protected static void removeLastP(PolymerNotation polymer) throws RNAUtilsException, NotationException, IOException, HELM2HandledException {
    checkRNA(polymer);
    /* Get last monomerNotation */
    MonomerNotation lastObject = polymer.getPolymerElements().getListOfElements().get(polymer.getPolymerElements().getListOfElements().size() - 1);

    /* What happens to HELM2 features */
    if (lastObject instanceof MonomerNotationGroup || lastObject instanceof MonomerNotationList) {
      throw new HELM2HandledException("HELM2 features are involved");
    }

    if (hasPhosphat((MonomerNotationUnitRNA) lastObject)) {
      MonomerNotation lastObjectwithoutPhosphat = new MonomerNotationUnitRNA(lastObject.getID().substring(0, lastObject.getID().length() - 1), "RNA");
      ChangeObjects.changeMonomerNotation(polymer.getPolymerElements().getListOfElements().size() - 1, polymer, lastObjectwithoutPhosphat);
      LOG.info("Last phosphate was removed from the last nucleotide");
    }
  }

  /**
   * method to check if the MonomerNotationUnitRNA has a phosphate
   *
   * @param monomerNotationUnitRNA MonomerNotationUnitRNA
   * @return true, if it ends with "P", false otherwise
   */
  /* To Do check for other modified phosphates? */
  private static boolean hasPhosphat(MonomerNotationUnitRNA monomerNotationUnitRNA) {
    if (monomerNotationUnitRNA.getContents().get(monomerNotationUnitRNA.getContents().size() - 1).getID().endsWith("P")) {
      LOG.info("MonomerNotationUnitRNA " + monomerNotationUnitRNA.getID() + " has a phosphate");
      return true;
    }
    LOG.info("MonomerNotationUnitRNA " + monomerNotationUnitRNA.getID() + " has no phosphate");
    return false;
  }

  /**
   * method to add a phosphate to the last polymer's nucleotide
   *
   * @param polymer PolymerNotation
   * @throws RNAUtilsException if the polymer is no rna or dna
   * @throws NotationException if the changed notation object can not be
   *           generated
   * @throws IOException
   * @throws HELM2HandledException if HELM2 features are involved
   */
  protected static void addLastP(PolymerNotation polymer) throws RNAUtilsException, NotationException, IOException, HELM2HandledException {
    checkRNA(polymer);
    /* Get last monomerNotation */
    MonomerNotation lastObject = polymer.getPolymerElements().getListOfElements().get(polymer.getPolymerElements().getListOfElements().size() - 1);

    /* What happens to HELM2 features */
    if (lastObject instanceof MonomerNotationGroup || lastObject instanceof MonomerNotationList) {
      System.out.println(lastObject.getClass());
      throw new HELM2HandledException("HELM2 features are involved");
    }

    if (!(hasPhosphat((MonomerNotationUnitRNA) lastObject))) {
      MonomerNotation lastObjectwithPhosphat = new MonomerNotationUnitRNA(lastObject.getID() + "P", "RNA");
      ChangeObjects.changeMonomerNotation(polymer.getPolymerElements().getListOfElements().size() - 1, polymer, lastObjectwithPhosphat);
      LOG.info("Phosphate was added to the last nucleotide");
    }

  }

  /**
   * method to get the antiparallel polymer for a rna/dna polymer
   *
   * @param polymer PolymerNotation
   * @return antiparallel polymer
   * @throws RNAUtilsException if the polymer is not rna or dna or the reverse
   *           polymer can not be built
   * @throws JDOMException
   * @throws IOException
   */
  protected static PolymerNotation getAntiparallel(PolymerNotation polymer) throws RNAUtilsException, IOException, JDOMException {
    checkRNA(polymer);
    PolymerNotation reversePolymer;
    try {
      reversePolymer = SequenceConverter.readRNA(generateAntiparallel(polymer)).getHELM2Notation().getCurrentPolymer();
      reversePolymer =
          new PolymerNotation(reversePolymer.getPolymerID(), reversePolymer.getPolymerElements(), "Antiparallel to "
              + polymer.getPolymerID().getID());
      return reversePolymer;
    } catch (NotationException | FastaFormatException | HELM2HandledException e) {
      throw new RNAUtilsException("The reverse polymer can not be built");
    }

  }

  /**
   * method to generate the antiparallel sequence for a rna/dna polymer
   *
   * @param polymer PolymerNotation
   * @return antiparallel sequence
   * @throws HELM2HandledException if th polymer contains HELM2 features
   * @throws RNAUtilsException if the polymer is not RNA or DNA
   */
  private static String generateAntiparallel(PolymerNotation polymer) throws HELM2HandledException, RNAUtilsException {
    return generateComplement(polymer).reverse().toString();
  }

  /**
   * method to get the polymer with the inverse sequence of the current polymer
   *
   * @param polymer PolymerNotation
   * @return reverse complement sequence
   * @throws RNAUtilsException if the polymer is not rna or dna or the inverse
   *           strand can not be built
   * @throws JDOMException
   * @throws IOException
   */
  protected static PolymerNotation getInverse(PolymerNotation polymer) throws RNAUtilsException, IOException, JDOMException {
    checkRNA(polymer);
    PolymerNotation inverse;
    try {
      inverse = SequenceConverter.readRNA(generateInverse(polymer).toString()).getHELM2Notation().getListOfPolymers().get(0);
      inverse =
          new PolymerNotation(inverse.getPolymerID(), inverse.getPolymerElements(), "Inverse to "
              + polymer.getPolymerID().getID());
      return inverse;
    } catch (NotationException | FastaFormatException | HELM2HandledException e) {
      throw new RNAUtilsException("The inverse strand can not be built");
    }

  }

  /**
   * method to generate the inverse sequence of the given polymer
   *
   * @param polymer PolymerNotation
   * @return inverse sequence of the PolymerNotation
   * @throws HELM2HandledException if the polymer contains HELM2 features
   * @throws RNAUtilsException if the polymer is not rna or dna
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
   * @throws HELM2HandledException if the polymer contains HELM2 features
   * @throws RNAUtilsException if the polymer is not RNA or DNA
   */
  private static StringBuilder generateComplement(PolymerNotation polymer) throws HELM2HandledException, RNAUtilsException {
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
   * @throws RNAUtilsException if the polymer is not rna or dna or the
   *           complement polymer can not be built
   * @throws JDOMException
   * @throws IOException
   *
   */
  protected static PolymerNotation getComplement(PolymerNotation polymer) throws RNAUtilsException, IOException, JDOMException {
    checkRNA(polymer);
    PolymerNotation complementPolymer;
    try {
      complementPolymer = SequenceConverter.readRNA(generateComplement(polymer).toString()).getHELM2Notation().getListOfPolymers().get(0);
      complementPolymer =
          new PolymerNotation(complementPolymer.getPolymerID(), complementPolymer.getPolymerElements(),
              "NormalComplement to " + polymer.getPolymerID().getID());
      return complementPolymer;
    } catch (NotationException | FastaFormatException | HELM2HandledException e) {
      throw new RNAUtilsException("Complement polymer can not be built");
    }

  }

  /**
   * method to check if the given PolymerNotation has a nucleotide Modification
   *
   * @param polymer PolymerNotation
   * @return true if the polymer contains at least one modifcation, false
   *         otherwise
   * @throws NotationException
   */
  protected static boolean hasNucleotideModification(PolymerNotation polymer) throws NotationException {
    for (MonomerNotation current : polymer.getPolymerElements().getListOfElements()) {
      if (hasModification(current)) {
        return true;
      }
    }
    return false;
  }

  /**
   * method to check if the MonomerNotation contains a modification
   *
   * @param monomerNotation
   * @return true, if the MonomerNotation contains a modification, false
   *         otherwise
   * @throws NotationException if the MonomerNotation is unknown
   */
  private static boolean hasModification(MonomerNotation monomerNotation) throws NotationException {

    if (monomerNotation instanceof MonomerNotationUnitRNA) {
      if (hasModification((MonomerNotationUnitRNA) monomerNotation)) {
        return true;
      }
    } else if (monomerNotation instanceof MonomerNotationGroup) {
      for (MonomerNotationGroupElement element : ((MonomerNotationGroup) monomerNotation).getListOfElements()) {
        if (hasModification(element.getMonomerNotation())) {
          return true;
        }
      }
    } else if (monomerNotation instanceof MonomerNotationList) {
      for (MonomerNotation element : ((MonomerNotationList) monomerNotation).getListofMonomerUnits()) {
        if (hasModification(element)) {
          return true;
        }
      }
    } else {
      throw new NotationException("Unknown MonomerNotation Type " + monomerNotation.getClass());
    }
    return false;
  }

  /**
   * method to check if the MonomerNotationUnitRNA contains modification
   *
   * @param monomerNotation MonomerNotationUnitRNA
   * @return true, if the MonomerNotationUnitRNA contains modification, false
   *         otherwise
   */
  private static boolean hasModification(MonomerNotationUnitRNA monomerNotation) {
    if (monomerNotation.getID().contains("[") || monomerNotation.getID().contains("(X)") || monomerNotation.getID().endsWith(")")) {
      return true;
    }
    return false;
  }

  /**
   * method to hybridize two PolymerNotations together if they are antiparallel
   *
   * @param one PolymerNotation first
   * @param two PolymerNotation second
   * @return List of ConnectionNotations
   * @throws RNAUtilsException
   * @throws NotationException
   * @throws IOException
   * @throws JDOMException
   * @throws HELM2HandledException
   */
  protected static List<ConnectionNotation> hybridize1(PolymerNotation one, PolymerNotation two) throws RNAUtilsException, NotationException, IOException, JDOMException, HELM2HandledException {
    checkRNA(one);
    checkRNA(two);

    List<ConnectionNotation> connections = new ArrayList<ConnectionNotation>();
    ConnectionNotation connection;
    /* Length of the two rnas have to be the same */
    if (areAntiparallel(one, two)) {
      for (int i = 0; i < PolymerUtils.getTotalMonomerCount(one); i++) {
        int backValue = PolymerUtils.getTotalMonomerCount(one) - i;
        int firstValue = i + 1;

        String details = firstValue + ":pair-" + backValue + ":pair";
        connection = new ConnectionNotation(one.getPolymerID(), two.getPolymerID(), details);
        connections.add(connection);
      }
      return connections;
    } else {
      throw new RNAUtilsException("The given RNAs are not antiparallel to each other");
    }

  }

  protected static String getSequence(PolymerNotation one) throws RNAUtilsException, HELM2HandledException {
    checkRNA(one);

    List<Nucleotide> nucleotideList = getNucleotideList(one);
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < nucleotideList.size(); i++) {
      sb.append(nucleotideList.get(i).getNaturalAnalog());
    }
    return sb.toString();

  }

  protected static List<ConnectionNotation> hybridize(PolymerNotation one, PolymerNotation two) throws RNAUtilsException, NotationException, HELM2HandledException, JDOMException, IOException,
      org.helm.notation.NotationException {
    initComplementMap();
    checkRNA(one);
    checkRNA(two);

    List<ConnectionNotation> connections = new ArrayList<ConnectionNotation>();
    ConnectionNotation connection;
    String seq1 = RNAUtils.getSequence(one).replaceAll("T", "U");
    String seq2 = RNAUtils.getSequence(two).replaceAll("T", "U");

    char[] chars = seq2.toCharArray();
    StringBuffer sb = new StringBuffer();
    for (int i = chars.length; i > 0; i--) {
      String symbol = String.valueOf(chars[i - 1]);
      System.out.println(symbol);
      String compSymbol = complementMap.get(symbol);
      sb.append(compSymbol);
    }
    String compSeq2 = sb.toString();
    String maxSeqMatch = getMaxMatchFragment(seq1, compSeq2);
    int seqMatchLength = maxSeqMatch.length();
    int seq1NucStart = -1;
    int seq1MonomerStart = 0;
    int seq2NucStart = -1;
    int seq2MonomerStart = 0;

    List<Nucleotide> seq1NucList = RNAUtils.getNucleotideList(one);
    List<Nucleotide> seq2NucList = RNAUtils.getNucleotideList(two);

    if (seqMatchLength > 0) {
      // get the starting monomer position for sequence 1
      seq1NucStart = seq1.indexOf(maxSeqMatch);
      for (int i = 0; i < seq1NucStart; i++) {
        Nucleotide nuc = seq1NucList.get(i);
        int monomerCount = SimpleNotationParser.getMonomerCountForRNA(nuc.getNotation());
        seq1MonomerStart = seq1MonomerStart + monomerCount;
      }

      // get the starting monomer position for sequence 2
      int compSeq2NucStart = compSeq2.indexOf(maxSeqMatch);
      seq2NucStart = seq2.length() - seqMatchLength - compSeq2NucStart;
      for (int i = 0; i < seq2NucStart; i++) {
        Nucleotide nuc = seq2NucList.get(i);
        int monomerCount = SimpleNotationParser.getMonomerCountForRNA(nuc.getNotation());
        seq2MonomerStart = seq2MonomerStart + monomerCount;
      }

      // build the matching monomer position
      for (int i = 0; i < seqMatchLength; i++) {

        Nucleotide nuc1 = seq1NucList.get(i + seq1NucStart);
        if (null == nuc1.getBaseMonomer()) {
          throw new NotationException(
              "Nucleotide without base cannot be hybridized with others");
        }
        if (i == 0) {
          seq1MonomerStart = seq1MonomerStart + 2;
        } else {
          seq1MonomerStart = seq1MonomerStart + 3;
        }

        Nucleotide nuc2 = seq2NucList.get(i + seq2NucStart);
        if (null == nuc2.getBaseMonomer()) {
          throw new NotationException(
              "Nucleotide without base cannot be hybridized with others");
        }
        if (i == 0) {
          seq2MonomerStart = seq2MonomerStart + 2;
        } else {
          seq2MonomerStart = seq2MonomerStart + 3;
        }
      }

      // what if there is an X with two monomers in the middle? exception
      // should have been thrown
      // now build the base pair string, offset by 3
      for (int i = seqMatchLength; i > 0; i--) {
        int seq1MonomerPos = seq1MonomerStart - (i - 1) * 3;
        int seq2MonomerPos = seq2MonomerStart - (seqMatchLength - i)
            * 3;

        String details = seq1MonomerPos + ":pair-" + seq2MonomerPos + ":pair";
        connection = new ConnectionNotation(one.getPolymerID(), two.getPolymerID(), details);
        connections.add(connection);
      }
    }

    return connections;
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
   * @throws NotationException
   * @throws RNAUtilsException
   * @throws HELM2HandledException
   * @throws NucleotideLoadingException
   */
  protected static String getNucleotideSequence(PolymerNotation polymer) throws NotationException, RNAUtilsException, HELM2HandledException, NucleotideLoadingException {

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
   * @return List of nucleotides of the polmyer
   * @throws RNAUtilsException if the polymer is not rna or dna or the
   *           nucleotide can not be read
   * @throws HELM2HandledException if the polymer contains HELM2 features
   *
   */
  private static List<Nucleotide> getNucleotideList(PolymerNotation polymer) throws RNAUtilsException, HELM2HandledException {
    checkRNA(polymer);
    List<Nucleotide> nucleotides = new ArrayList<Nucleotide>();
    /* check for HELM2Elements */
    List<MonomerNotation> monomerNotations = polymer.getPolymerElements().getListOfElements();
    for (MonomerNotation monomerNotation : monomerNotations) {
      if ((!(monomerNotation instanceof MonomerNotationUnitRNA)) || Integer.parseInt(monomerNotation.getCount()) != 1) {
        LOG.info("MonomerNotation contains HELM2 Elements " + monomerNotation);
        throw new HELM2HandledException("HELM2 Elements are involved");
      }
      try {
        nucleotides.add(SimpleNotationParser.getNucleotideList(monomerNotation.getID()).get(0));
      } catch (org.helm.notation.NotationException | MonomerException | IOException | JDOMException | StructureException e) {
        throw new RNAUtilsException("Nucleotide can not be read " + e.getMessage());
      }
    }
    return nucleotides;
  }

}

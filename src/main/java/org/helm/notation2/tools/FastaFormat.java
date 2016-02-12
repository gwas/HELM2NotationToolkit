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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.MonomerException;
import org.helm.notation.NucleotideLoadingException;
import org.helm.notation2.Monomer;
import org.helm.notation2.MonomerFactory;
import org.helm.notation2.NotationConstant;
import org.helm.notation2.Nucleotide;
import org.helm.notation2.NucleotideFactory;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.polymer.HELMEntity;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroup;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroupElement;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroupMixture;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroupOr;
import org.helm.notation2.parser.notation.polymer.MonomerNotationList;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.notation.polymer.PeptideEntity;
import org.helm.notation2.parser.notation.polymer.PolymerListElements;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.helm.notation2.parser.notation.polymer.RNAEntity;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FastaFormat, class to convert FastaFiles to HELMNotation and vice versa
 *
 * @author hecht
 */
public final class FastaFormat {

  private static HELM2Notation helm2notation = null;

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(FastaFormat.class);

  private static Map<String, String> nucleotides = null;

  private static Map<String, String> transformNucleotides = null;

  private static Map<String, Monomer> nucleotidesNaturalAnalog = null;

  private static Map<String, Monomer> aminoacids = null;

  /**
   * Default constructor.
   */
  private FastaFormat() {

  }

  /**
   * method to read the information from a FastaFile-Format + generate peptide
   * polymers- be careful -> it produces only polymers in the HELM1 standard, no
   * ambiguity
   *
   * @param fasta FastaFile in string format
   * @return HELM2Notation generated HELM2Notation
   * @throws FastaFormatException if the input is not correct
   * @throws ChemistryException
   */
  public static HELM2Notation generatePeptidePolymersFromFASTAFormatHELM1(String fasta) throws FastaFormatException, ChemistryException {
    helm2notation = new HELM2Notation();
    if (null == fasta) {
      LOG.error("Peptide Sequence must be specified");
      throw new FastaFormatException("Peptide Sequence must be specified");
    }

    initMapAminoAcid();
    StringBuilder elements = new StringBuilder();
    int counter = 0;

    PolymerNotation polymer;
    try {
      polymer = new PolymerNotation("PEPTIDE" + "1");
    } catch (org.helm.notation2.parser.exceptionparser.NotationException e) {
      e.printStackTrace();
      throw new FastaFormatException(e.getMessage());
    }
    String annotation = "";
    for (String line : fasta.split("\n")) {
      if (line.startsWith(">")) {
        counter++;
        if (counter > 1) {
          helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(),
              generateElementsOfPeptide(elements.toString(), polymer.getPolymerID()), annotation));
          elements = new StringBuilder();
          try {
            polymer = new PolymerNotation("PEPTIDE" + counter);
          } catch (org.helm.notation2.parser.exceptionparser.NotationException e) {
            e.printStackTrace();
            throw new FastaFormatException(e.getMessage());
          }
        }
        annotation = line.substring(1);
      } else {
        line = cleanup(line);
        elements.append(line);
      }
    }
    helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(),
        generateElementsOfPeptide(elements.toString(), polymer.getPolymerID()), annotation));

    return helm2notation;
  }

  /**
   * method to read the information from a FastaFile-Format + generate RNA
   * Polymers be careful -> it produces only polymers in the HELM1 standard, no
   * ambiguity
   *
   * @param fasta FastaFile in string format
   * @return HELM2Notation generated HELM2Notation
   * @throws FastaFormatException if the input is not correct
   * @throws JDOMException
   * @throws IOException
   * @throws NotationException
   * @throws ChemistryException
   */
  public static HELM2Notation generateRNAPolymersFromFastaFormatHELM1(String fasta) throws FastaFormatException, IOException, JDOMException, NotationException, ChemistryException {
    helm2notation = new HELM2Notation();
    if (null == fasta) {
      LOG.error("Nucleotide Sequence must be specified");
      throw new FastaFormatException("Nucleotide Sequence must be specified");
    }

    /* initialize Map to get the information for nucleotides! */
    initMapNucleotides();
    /* walk through the fastafile: it can contain more than one sequence */
    StringBuilder elements = new StringBuilder();
    int counter = 0;
    PolymerNotation polymer;
    try {
      polymer = new PolymerNotation("RNA" + "1");
    } catch (org.helm.notation2.parser.exceptionparser.NotationException e) {
      e.printStackTrace();
      throw new FastaFormatException(e.getMessage());
    }
    String annotation = "";
    for (String line : fasta.split("\n")) {
      if (line.startsWith(">")) {
        counter++;
        if (counter > 1) {
          if (!(isNormalDirection(elements.toString()))) {
            annotation += " 3'-5'";
          }
          helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(),
              generateElementsforRNA(elements.toString(), polymer.getPolymerID()), annotation));
          elements = new StringBuilder();
          try {
            polymer = new PolymerNotation("RNA" + counter);
          } catch (org.helm.notation2.parser.exceptionparser.NotationException e) {
            e.printStackTrace();
            throw new FastaFormatException(e.getMessage());
          }

        }
        annotation = line.substring(1);
      } else {
        line = cleanup(line);
        elements.append(line);
      }

    }
    if (!(isNormalDirection(elements.toString()))) {
      annotation += " 3'-5'";
    }
    helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(),
        generateElementsforRNA(elements.toString(), polymer.getPolymerID()), annotation));

    return helm2notation;
  }

  /**
   * method to initialize map of existing nucleotides in the database
   *
   * @throws FastaFormatException if the NucleotideFactory can not be
   *           initalialized
   */
  private static void initMapNucleotides() throws FastaFormatException {
    try {
      nucleotides = NucleotideFactory.getInstance().getNucleotideTemplates().get("HELM Notation");
    } catch (IOException e) {
      e.printStackTrace();
      LOG.error("NucleotideFactory can not be initialized");
      throw new FastaFormatException(e.getMessage());
    }
  }

  /**
   * method to initialize map of transform nucleotides
   */
  private static void initMapTransformNucleotides() {
    transformNucleotides = new HashMap<String, String>();
    for (Map.Entry e : nucleotides.entrySet()) {
      transformNucleotides.put(e.getValue().toString(), e.getKey().toString());
    }
  }

  /**
   * method to initialize map of existing nucleotides with the natural analog
   * sequence in the database
   *
   * @throws FastaFormatException if the NucleotideFactory can not be
   *           initialized
   * @throws ChemistryException
   */
  private static void initMapNucleotidesNaturalAnalog() throws FastaFormatException, ChemistryException {
    try {
      nucleotidesNaturalAnalog = MonomerFactory.getInstance().getMonomerDB().get("RNA");
    } catch (IOException e) {
      e.printStackTrace();
      LOG.error("Nucleotides can not be initialized");
      throw new FastaFormatException(e.getMessage());
    }
  }

  /**
   * method to initialize map of existing amino acids in the database
   *
   * @throws FastaFormatException AminoAcids can not be initialized
   * @throws ChemistryException
   * @throws CTKException
   */
  private static void initMapAminoAcid() throws FastaFormatException, ChemistryException {
    try {
      aminoacids = MonomerFactory.getInstance().getMonomerDB().get("PEPTIDE");
    } catch (IOException e) {
      e.printStackTrace();
      LOG.error("AminoAcids can not be initialized");
      throw new FastaFormatException(e.getMessage());
    }
  }

  /**
   * method to fill a peptide polymer with its elements (MonomerNotationUnits)
   *
   * @param sequence peptide sequence
   * @param entity HELMEntity
   * @return PolymerListElements
   * @throws FastaFormatException if the input sequence is not correct
   * @throws ChemistryException
   * @throws CTKException
   */
  protected static PolymerListElements generateElementsOfPeptide(String sequence, HELMEntity entity)
      throws FastaFormatException, ChemistryException {
    initMapAminoAcid();
    sequence = cleanup(sequence);
    try {
      PolymerListElements elements = new PolymerListElements(entity);
      List<String> aaList = AminoAcidParser.getAminoAcidList(sequence);
      for (String aa : aaList) {
        if (aa.length() > 1) {
          aa = "[" + aa + "]";
        }

        elements.addMonomerNotation(aa);
      }
      return elements;
    } catch (org.helm.notation2.parser.exceptionparser.NotationException | IOException | JDOMException | MonomerException | org.helm.notation.NotationException e) {
      e.printStackTrace();
      LOG.error("PolymerListElements can not be initialized");
      throw new FastaFormatException("PolymerListElements can not be initialized " + e.getMessage());
    }

  }

  /**
   * method to fill a rna polymer with its elements (MonomerNotationUnits)
   *
   * @param sequence rna sequence
   * @param entity HELMEntity
   * @return PolymerListElements
   * @throws FastaFormatException if the input sequence is not correct
   * @throws NotationException
   * @throws JDOMException
   * @throws IOException
   * @throws ChemistryException
   * @throws CTKException
   */
  protected static PolymerListElements generateElementsforRNA(String sequence, HELMEntity entity)
      throws FastaFormatException, IOException, JDOMException, NotationException, ChemistryException {
    initMapNucleotides();
    initMapNucleotidesNaturalAnalog();
    PolymerListElements elements = new PolymerListElements(entity);
    sequence = cleanup(sequence);
    sequence = prepareSequence(sequence);
    List<Nucleotide> normalNucleotideList = getNormalList(sequence);
    for (Nucleotide nucleotide : normalNucleotideList) {
      elements.addMonomerNotation(nucleotide.getNotation());
    }
    /* remove the phosphat of the last group */
    String id = elements.getCurrentMonomerNotation().getID();
    try {
      elements.changeMonomerNotation(new MonomerNotationUnitRNA(id.substring(0, id.length() - 1), "RNA"));
    } catch (org.helm.notation2.parser.exceptionparser.NotationException | IOException e) {
      e.printStackTrace();
      throw new FastaFormatException("PolymerListElements can not be initialized " + e.getMessage());
    }

    return elements;
  }

  /**
   * @param sequence
   * @return
   * @throws NotationException
   * @throws NucleotideLoadingException
   */
  private static List<Nucleotide> getNormalList(String sequence) throws NotationException, NucleotideLoadingException {
    if (null == sequence) {
      throw new NotationException("Sequence must be specified");
    }
    Map<String, Map<String, String>> templates = NucleotideFactory.getInstance().getNucleotideTemplates();
    Map<String, String> nucleotides = null;
    nucleotides = templates.get(NotationConstant.NOTATION_SOURCE);
    Set<String> keySet = nucleotides.keySet();

    // walk the sequence
    List<Nucleotide> l = new ArrayList<Nucleotide>();
    int pos = 0;
    while (pos < sequence.length()) {
      boolean found = false;
      for (Iterator i = keySet.iterator(); i.hasNext();) {
        String symbol = (String) i.next();
        if (sequence.startsWith(symbol, pos)) {
          found = true;
          String notation = nucleotides.get(symbol);
          Nucleotide nuc = new Nucleotide(symbol, notation);
          l.add(nuc);
          pos = pos + symbol.length();
          break;
        }
      }
      if (!found) {
        throw new NotationException(
            "Sequence contains unknown nucleotide starting at "
                + sequence.substring(pos));
      }
    }

    return l;
  }

  /**
   * remove white space, and convert all lower case to upper case
   *
   * @param sequence
   * @return cleaned sequence
   */
  private static String cleanup(String sequence) {
    String result = sequence.replaceAll("\\s", ""); // remove all white
    // space
    if (result.equals(result.toLowerCase())) {
      result = result.toUpperCase();
    }
    return result;
  }

  /**
   * method to generate Fasta for peptide polymers
   *
   * @param polymers List of peptide PolymerNotation
   * @return generated FASTA
   * @throws FastaFormatException if the peptides can not be transformed to
   *           FASTA
   * @throws ChemistryException if the Chemistry Engine can not be initialized
   */
  public static String generateFastaFromPeptidePolymer(List<PolymerNotation> polymers) throws FastaFormatException, ChemistryException {
    initMapAminoAcid();
    StringBuilder fasta = new StringBuilder();
    for (PolymerNotation polymer : polymers) {
      String header = polymer.getPolymerID().getID();
      if (polymer.getAnnotation() != null) {
        header = polymer.getAnnotation();
      }
      fasta.append(">" + header + "\n");
      try {
        fasta.append(generateFastaFromPeptide(MethodsMonomerUtils.getListOfHandledMonomers(polymer.getListMonomers()))
            + "\n");
      } catch (HELM2HandledException e) {
        e.printStackTrace();
        throw new FastaFormatException(e.getMessage());
      }
    }
    return fasta.toString();
  }

  /**
   * method to generate Fasta for a list of peptide monomers
   *
   * @param monomers peptide monomers
   * @return Fasta sequence
   */
  protected static String generateFastaFromPeptide(List<Monomer> monomers) {
    StringBuilder fasta = new StringBuilder();
    for (Monomer monomer : monomers) {
      fasta.append(monomer.getNaturalAnalog());
    }
    return fasta.toString();
  }

  /**
   * method to generate Fasta for rna polymers
   *
   * @param polymers list of rna PolymerNotation
   * @return Fasta
   * @throws FastaFormatException if the polymers can not be transformed into
   *           FASTA
   * @throws ChemistryException if the Chemistry Engine can not be initialized
   */
  public static String generateFastaFromRNAPolymer(List<PolymerNotation> polymers) throws FastaFormatException, ChemistryException {
    StringBuilder fasta = new StringBuilder();
    for (PolymerNotation polymer : polymers) {
      String header = polymer.getPolymerID().getID();
      if (polymer.getAnnotation() != null) {
        header = polymer.getAnnotation();
      }

      fasta.append(">" + header + "\n");
      try {
        fasta.append(generateFastaFromRNA(MethodsMonomerUtils.getListOfHandledMonomers(polymer.getListMonomers()))
            + "\n");
      } catch (HELM2HandledException e) {
        e.printStackTrace();
        throw new FastaFormatException(e.getMessage());
      }
    }
    return fasta.toString();
  }

  /**
   * method to generate Fasta for a list of rna monomers
   *
   * @param listMonomers
   * @return sequence
   */
  protected static String generateFastaFromRNA(List<Monomer> monomers) {
    StringBuilder fasta = new StringBuilder();
    for (Monomer monomer : monomers) {
      if (monomer.getMonomerType().equals(Monomer.BRANCH_MOMONER_TYPE)) {
        fasta.append(monomer.getNaturalAnalog());
      }
    }

    return fasta.toString();
  }

  /**
   * method to generate for the whole HELM2Notation fasta-files -> it contains
   * fasta for all rna and peptides
   *
   * @param helm2Notation2 HELM2Notation
   * @return FASTA-File-Format
   * @throws FastaFormatException if the HELM2Notation can not be transformed to
   *           FASTA
   * @throws ChemistryException if the Chemistry Engine can not be initialized
   * @throws CTKException
   */
  public static String generateFasta(HELM2Notation helm2Notation2) throws FastaFormatException, ChemistryException {
    List<PolymerNotation> polymersPeptides = new ArrayList<PolymerNotation>();
    List<PolymerNotation> polymerNucleotides = new ArrayList<PolymerNotation>();
    StringBuilder fasta = new StringBuilder();
    for (PolymerNotation polymer : helm2Notation2.getListOfPolymers()) {
      if (polymer.getPolymerID() instanceof RNAEntity) {
        polymerNucleotides.add(polymer);
      }
      if (polymer.getPolymerID() instanceof PeptideEntity) {
        polymersPeptides.add(polymer);
      }
    }

    fasta.append(generateFastaFromPeptidePolymer(polymersPeptides));
    fasta.append(generateFastaFromRNAPolymer(polymerNucleotides));

    return fasta.toString();

  }

  /**
   * method to convert all Peptides and RNAs into the natural analogue sequence
   * and generates HELM2Notation
   *
   * @param helm2Notation
   * @throws FastaFormatException
   * @throws AnalogSequenceException if the natural analogue sequence can not be
   *           produced
   * @throws ChemistryException
   * @throws CTKException
   */
  public static HELM2Notation convertIntoAnalogSequence(HELM2Notation helm2Notation) throws FastaFormatException, AnalogSequenceException, ChemistryException, CTKException {
    initMapAminoAcid();
    initMapNucleotides();
    initMapNucleotidesNaturalAnalog();
    initMapTransformNucleotides();
    /* transform/convert only the peptides + rnas into the analog sequence */
    List<PolymerNotation> polymers = helm2Notation.getListOfPolymers();
    for (int i = 0; i < helm2Notation.getListOfPolymers().size(); i++) {
      if (helm2Notation.getListOfPolymers().get(i).getPolymerID() instanceof RNAEntity) {
        helm2Notation.getListOfPolymers().set(i, convertRNAIntoAnalogSequence(polymers.get(i)));
      }
      if (helm2Notation.getListOfPolymers().get(i).getPolymerID() instanceof PeptideEntity) {
        helm2Notation.getListOfPolymers().set(i, convertPeptideIntoAnalogSequence(polymers.get(i)));
      }
    }

    return helm2Notation;
  }

  /**
   * method to convert the sequence of a PolymerNotation into the natural
   * analogue sequence
   *
   * @param polymer PolymerNotation
   * @return PolymerNotation with the natural analogue sequence
   * @throws AnalogSequenceException if the natural analog sequence can not be
   *           produced
   */
  private static PolymerNotation convertPeptideIntoAnalogSequence(PolymerNotation polymer) throws AnalogSequenceException {

    for (int i = 0; i < polymer.getPolymerElements().getListOfElements().size(); i++) {
      /* Change current MonomerNotation */
      polymer.getPolymerElements().getListOfElements().set(i, generateMonomerNotationPeptide(polymer.getPolymerElements().getListOfElements().get(i)));
    }

    return polymer;
  }

  /**
   * method to change the monomerNotation by setting the natural analogue
   * peptide sequence
   *
   * @param current MonomerNotation
   * @return MonomerNotation with the peptide natural analogue sequence
   * @throws AnalogSequenceException
   */
  private static MonomerNotation generateMonomerNotationPeptide(MonomerNotation current) throws AnalogSequenceException {
    MonomerNotation change = null;
    try {
      /* simple MonomerNotationUnit */
      if (current instanceof MonomerNotationUnit) {

        String id = aminoacids.get(current.getID().replace("[", "").replace("]", "")).getNaturalAnalog();
        change = new MonomerNotationUnit(id, current.getType());
        change.setCount(current.getCount());
        if (current.getAnnotation() != null) {
          change.setAnnotation(current.getAnnotation());
        }
      } else if (current instanceof MonomerNotationGroup) {
        if (current instanceof MonomerNotationGroupOr) {
          StringBuilder sb = new StringBuilder();
          String id = current.getID();
          for (String element : id.split(",")) {
            sb.append(aminoacids.get(element.replace("[", "").replace("]", "")).getNaturalAnalog() + ",");
          }
          sb.setLength(sb.length() - 1);
          change = new MonomerNotationList(sb.toString(), current.getType());

        } else if (current instanceof MonomerNotationGroupMixture) {
          StringBuilder sb = new StringBuilder();
          String id = current.getID();
          for (String element : id.split("\\+")) {
            sb.append(aminoacids.get(element.replace("[", "").replace("]", "")).getNaturalAnalog() + "+");
          }
          sb.setLength(sb.length() - 1);
          change = new MonomerNotationList(sb.toString(), current.getType());

        } else {
          /* throw new exception */
          throw new AnalogSequenceException("MonomerNotationGroup is unknown" + current.getClass());
        }

      } else if (current instanceof MonomerNotationList) {
        StringBuilder sb = new StringBuilder();
        String id = current.getID();
        for (String element : id.split("\\.")) {
          sb.append(aminoacids.get(element.replace("[", "").replace("]", "")).getNaturalAnalog() + ".");
        }
        sb.setLength(sb.length() - 1);
        change = new MonomerNotationList(sb.toString(), current.getType());

      } else {
        throw new AnalogSequenceException("MonomerNotation is unknown" + current.getClass());
      }

      change.setCount(current.getCount());
      if (current.getAnnotation() != null) {
        change.setAnnotation(current.getAnnotation());
      }

      return change;
    } catch (NotationException | IOException | JDOMException e) {
      e.printStackTrace();
      throw new AnalogSequenceException("MonomerNotation can not be converted to its natural analogue sequence " + e.getMessage());

    }

  }

  /**
   * method to change the MonomerNotation in its analogue
   *
   * @param current MonomerNotation
   * @return its analogue MonomerNotation
   * @throws AnalogSequenceException
   */
  private static MonomerNotation generateMonomerNotationRNA(MonomerNotation current) throws AnalogSequenceException {
    MonomerNotation change = null;
    try {
      /* simple MonomerNotationUnit */

      if (current instanceof MonomerNotationUnit) {
        change = new MonomerNotationUnit(changeIdForRNA(current), current.getType());
      } else if (current instanceof MonomerNotationGroup) {
        if (current instanceof MonomerNotationGroupOr) {
          StringBuilder sb = new StringBuilder();
          for (MonomerNotationGroupElement element : ((MonomerNotationGroup) current).getListOfElements()) {

            sb.append(changeIdForRNA(element.getMonomerNotation()) + ",");
          }
          sb.setLength(sb.length() - 1);
          change = new MonomerNotationList(sb.toString(), current.getType());

        } else if (current instanceof MonomerNotationGroupMixture) {
          StringBuilder sb = new StringBuilder();
          for (MonomerNotationGroupElement element : ((MonomerNotationGroup) current).getListOfElements()) {
            sb.append(changeIdForRNA(element.getMonomerNotation()) + "+");
          }
          sb.setLength(sb.length() - 1);
          change = new MonomerNotationList(sb.toString(), current.getType());

        } else {
          /* throw new exception */
          throw new AnalogSequenceException("Unknown MonomerNotationGroup " + current.getClass());
        }

      } else if (current instanceof MonomerNotationList) {
        StringBuilder sb = new StringBuilder();
        for (MonomerNotation element : ((MonomerNotationList) current).getListofMonomerUnits()) {
          sb.append(changeIdForRNA(element) + ".");
        }
        sb.setLength(sb.length() - 1);
        change = new MonomerNotationList(sb.toString(), current.getType());

      } else {
        /* throw new exception */
        throw new AnalogSequenceException("Unknown MonomerNotation " + current.getClass());
      }

      change.setCount(current.getCount());
      if (current.getAnnotation() != null) {
        change.setAnnotation(current.getAnnotation());
      }

      return change;

    } catch (JDOMException | NotationException | IOException e) {
      e.printStackTrace();
      throw new AnalogSequenceException("Notation object can not be built");
    }

  }

  /**
   * method to generate the sequence of a rna PolymerNotation into its natural
   * analogue sequence
   *
   * @param polymer PolymerNotation
   * @return PolymerNotation with its natural analogue sequence
   * @throws AnalogSequenceException if the natural analogues MonomerNotations
   *           can not be built
   */
  private static PolymerNotation convertRNAIntoAnalogSequence(PolymerNotation polymer) throws AnalogSequenceException {

    /* change only if it is possible */
    for (int i = 0; i < polymer.getListMonomers().size(); i++) {
      polymer.getPolymerElements().getListOfElements().set(i, generateMonomerNotationRNA(polymer.getPolymerElements().getListOfElements().get(i)));
    }

    return polymer;
  }

  /**
   * method to get the natural analogue sequence of a MonomerNotation
   *
   * @param monomerNotation MonomerNotation
   * @return natural analogue sequence of MonomerNotation
   */
  private static String changeIdForRNA(MonomerNotation monomerNotation) {
    if (monomerNotation instanceof MonomerNotationUnitRNA) {
      StringBuilder changeid = new StringBuilder();

      for (MonomerNotation not : ((MonomerNotationUnitRNA) monomerNotation).getContents()) {
        Monomer monomer = nucleotidesNaturalAnalog.get(not.getID().replace("[", "").replace("]", ""));
        String id = monomer.getNaturalAnalog();
        if (monomer.getMonomerType().equals(Monomer.BRANCH_MOMONER_TYPE)) {
          id = "(" + id + ")";
        }
        changeid.append(id);
      }
      return changeid.toString();
    } else {
      Monomer monomer = nucleotidesNaturalAnalog.get(monomerNotation.getID().replace("[", "").replace("]", ""));
      String id = monomer.getNaturalAnalog();
      if (monomer.getMonomerType().equals(Monomer.BRANCH_MOMONER_TYPE)) {
        id = "(" + id + ")";
      }
      return id;
    }
  }

  /**
   * method to check if the sequence is in normal direction 5' to 3'
   *
   * @param sequence rna sequence
   * @return true, if the sequence is in normal direction, false otherwise
   */
  public static boolean isNormalDirection(String sequence) {
    if (sequence.startsWith("3")) {
      return false;
    } else {
      return true;
    }
  }

  private static String prepareSequence(String sequence) {
    String result = sequence;
    result = result.replace("-", "");
    result = result.replace("5'", "");
    result = result.replace("3'", "");

    if (!(isNormalDirection(sequence))) {
      result = new StringBuffer(result).reverse().toString();
    }

    return result;
  }

}

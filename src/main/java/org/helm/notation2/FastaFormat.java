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

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.NucleotideFactory;
import org.helm.notation.model.Monomer;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
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
   * method to read the information from a FastaFile-Format + generate peptide
   * polymers- be careful -> it produces only polymers in the HELM1 standard, no
   * ambiguity
   * 
   * @param fasta FastaFile in string format
   * @return HELM2Notation generated HELM2Notation
   * @throws FastaFormatException if the input is not correct
   */
  protected static HELM2Notation generatePeptidePolymersFromFASTAFormatHELM1(String fasta) throws FastaFormatException {
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
            throw new FastaFormatException(e.getMessage());
          }
        }
        annotation = line.substring(1);
      }
      else {
        line = cleanup(line);
        elements.append(line);
      }
    }
    helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(),
        generateElementsOfPeptide(elements.toString(), polymer.getPolymerID()), annotation));
    ;
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
   */
  protected static HELM2Notation generateRNAPolymersFromFastaFormatHELM1(String fasta) throws FastaFormatException {
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
      throw new FastaFormatException(e.getMessage());
    }
    String annotation = "";
    for (String line : fasta.split("\n")) {
      if (line.startsWith(">")) {
        counter++;
        if (counter > 1) {

          helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(),
              generateElementsforRNA(elements.toString(), polymer.getPolymerID()), annotation));
          elements = new StringBuilder();
          try {
            polymer = new PolymerNotation("RNA" + counter);
          } catch (org.helm.notation2.parser.exceptionparser.NotationException e) {
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
        LOG.error("NucleotideFactory can not be initialized");
        throw new FastaFormatException(e.getMessage());
      }
  }

  /**
   * method to initialize map of transform nucleotides
   */
  private static void InitMapTransformNucleotides() {
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
   */
  private static void initMapNucleotidesNaturalAnalog() throws FastaFormatException {
    try {
      nucleotidesNaturalAnalog = MonomerFactory.getInstance().getMonomerDB().get("RNA");
    } catch (IOException e) {
      LOG.error("Nucleotides can not be initialized");
      throw new FastaFormatException(e.getMessage());
    }
  }

  /**
   * method to initialize map of existing amino acids in the database
   * 
   * @throws FastaFormatException AminoAcids can not be initialized
   */
  private static void initMapAminoAcid() throws FastaFormatException {
    try {
      aminoacids = MonomerFactory.getInstance().getMonomerDB().get("PEPTIDE");
    } catch (IOException e) {
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
   */
  protected static PolymerListElements generateElementsOfPeptide(String sequence, HELMEntity entity)
      throws FastaFormatException {
    initMapAminoAcid();
    try {
      PolymerListElements elements = new PolymerListElements(entity);
      for (Character c : sequence.toCharArray()) {
        if (aminoacids.get(c.toString()) != null) {
          elements.addMonomerNotation(c.toString());
        }
        else {
          LOG.error("Not appropriate amino acid for HELM " + c.toString());
          throw new FastaFormatException("Not appropriate amino acid for HELM " + c.toString());
        }
      }
      return elements;
    } catch (org.helm.notation2.parser.exceptionparser.NotationException | IOException | JDOMException e) {
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
   */
  protected static PolymerListElements generateElementsforRNA(String sequence, HELMEntity entity)
      throws FastaFormatException {
    initMapNucleotides();
    initMapNucleotidesNaturalAnalog();
    PolymerListElements elements = new PolymerListElements(entity);
    for (Character c : sequence.toCharArray()) {
      /* -> get for each single nucleotide code the contents from the nucleotidefactory */
      try {
        elements.addMonomerNotation(nucleotides.get(c.toString()));
      } catch (org.helm.notation2.parser.exceptionparser.NotationException | IOException | JDOMException
          | NullPointerException e) {
        throw new FastaFormatException("Monomer can not be found:" + c.toString());
      }
    }
    /* remove the phosphat of the last group */
    String id = elements.getCurrentMonomerNotation().getID();
    try {
      elements.changeMonomerNotation(new MonomerNotationUnitRNA(id.substring(0, id.length() - 1), "RNA"));
    } catch (org.helm.notation2.parser.exceptionparser.NotationException | IOException e) {
      throw new FastaFormatException("PolymerListElements can not be initialized " + e.getMessage());
    }

    return elements;
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
   */
  protected static String generateFastaFromPeptidePolymer(List<PolymerNotation> polymers) throws FastaFormatException {
    initMapAminoAcid();
    StringBuilder fasta = new StringBuilder();
    for (PolymerNotation polymer : polymers) {
      String header = polymer.getPolymerID().getID();
      if (polymer.getAnnotation() != null) {
        header = polymer.getAnnotation();
      }
      fasta.append(">" + header + "\n");
      try {
        fasta.append(generateFastaFromPeptide(MethodsForContainerHELM2.getListOfHandledMonomers(polymer.getListMonomers()))
            + "\n");
      } catch (HELM2HandledException e) {
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
   */
  protected static String generateFastaFromRNAPolymer(List<PolymerNotation> polymers) throws FastaFormatException {
    StringBuilder fasta = new StringBuilder();
    for (PolymerNotation polymer : polymers) {
      String header = polymer.getPolymerID().getID();
      if (polymer.getAnnotation() != null) {
        header = polymer.getAnnotation();
      }

      fasta.append(">" + header + "\n");
      try {
        fasta.append(generateFastaFromRNA(MethodsForContainerHELM2.getListOfHandledMonomers(polymer.getListMonomers()))
            + "\n");
      } catch (HELM2HandledException e) {
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
   */
  protected static String generateFasta(HELM2Notation helm2Notation2) throws FastaFormatException {
    List<PolymerNotation> peptides = new ArrayList<PolymerNotation>();
    List<PolymerNotation> nucleotides = new ArrayList<PolymerNotation>();
    StringBuilder fasta = new StringBuilder();
    for (PolymerNotation polymer : helm2Notation2.getListOfPolymers()) {
      if (polymer.getPolymerID() instanceof RNAEntity) {
        nucleotides.add(polymer);
      }
      if (polymer.getPolymerID() instanceof PeptideEntity) {
        peptides.add(polymer);
      }
    }

    fasta.append(generateFastaFromPeptidePolymer(peptides));
    fasta.append(generateFastaFromRNAPolymer(nucleotides));

    return fasta.toString();

  }

  /**
   * method to convert all Peptides and RNAs into the natural analogSequence and
   * generates HELM2Notation
   * 
   * @param helm2Notation
   * @throws org.helm.notation2.parser.exceptionparser.NotationException
   * @throws IOException
   * @throws JDOMException
   * @throws FastaFormatException
   * @throws AnalogSequenceException
   */
  protected static HELM2Notation convertIntoAnalogSequence(HELM2Notation helm2Notation)
      throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, JDOMException,
      FastaFormatException,
      AnalogSequenceException {
    initMapAminoAcid();
    initMapNucleotides();
    initMapNucleotidesNaturalAnalog();
    InitMapTransformNucleotides();
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
   * @throws org.helm.notation2.parser.exceptionparser.NotationException
   * @throws IOException
   * @throws JDOMException
   * @throws AnalogSequenceException
   */
  private static PolymerNotation convertPeptideIntoAnalogSequence(PolymerNotation polymer)
      throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, JDOMException,
      AnalogSequenceException {

    for (int i = 0; i < polymer.getPolymerElements().getListOfElements().size(); i++) {
      /* Change current MonomerNotation */
      polymer.getPolymerElements().getListOfElements().set(i, generateMonomerNotationPeptide(polymer.getPolymerElements().getListOfElements().get(i)));
    }


    return polymer;
  }

  /**
   * method to generate
   * 
   * @param current
   * @return
   * @throws org.helm.notation2.parser.exceptionparser.NotationException
   * @throws IOException
   * @throws JDOMException
   * @throws AnalogSequenceException
   */
  private static MonomerNotation generateMonomerNotationPeptide(MonomerNotation current)
      throws org.helm.notation2.parser.exceptionparser.NotationException, IOException,
      JDOMException,
      AnalogSequenceException {
    MonomerNotation change = null;

    /* simple MonomerNotationUnit */
    if (current instanceof MonomerNotationUnit) {

      String id = aminoacids.get(current.getID().replace("[", "").replace("]", "")).getNaturalAnalog();
      change = new MonomerNotationUnit(id, current.getType());
      change.setCount(current.getCount());
      if (current.getAnnotation() != null) {
        change.setAnnotation(current.getAnnotation());
      }
    }

    else if (current instanceof MonomerNotationGroup) {
      if (current instanceof MonomerNotationGroupOr) {
        StringBuilder sb = new StringBuilder();
        String id = current.getID();
        for (String element : id.split(",")) {
          sb.append(aminoacids.get(element.replace("[", "").replace("]", "")).getNaturalAnalog() + ",");
        }
        sb.setLength(sb.length() - 1);
        change = new MonomerNotationList(sb.toString(), current.getType());

      }
      else if (current instanceof MonomerNotationGroupMixture) {
        StringBuilder sb = new StringBuilder();
        String id = current.getID();
        for (String element : id.split("\\+")) {
          sb.append(aminoacids.get(element.replace("[", "").replace("]", "")).getNaturalAnalog() + "+");
        }
        sb.setLength(sb.length() - 1);
        change = new MonomerNotationList(sb.toString(), current.getType());

      }

      else {
        /* throw new exception */
        throw new AnalogSequenceException("j");
      }

    }

    else if (current instanceof MonomerNotationList) {
      StringBuilder sb = new StringBuilder();
      String id = current.getID();
      for (String element : id.split("\\.")) {
        sb.append(aminoacids.get(element.replace("[", "").replace("]", "")).getNaturalAnalog() + ".");
      }
      sb.setLength(sb.length() - 1);
      change = new MonomerNotationList(sb.toString(), current.getType());

    }

    else {
      /* throw new exception */
      throw new AnalogSequenceException("j");
    }

    change.setCount(current.getCount());
    if (current.getAnnotation() != null) {
      change.setAnnotation(current.getAnnotation());
    }

    return change;

  }

  private static MonomerNotation generateMonomerNotationRNA(MonomerNotation current)
      throws org.helm.notation2.parser.exceptionparser.NotationException, IOException,
      JDOMException,
      AnalogSequenceException {
    MonomerNotation change = null;

    /* simple MonomerNotationUnit */

    if (current instanceof MonomerNotationUnit) {
      change = new MonomerNotationUnit(changeIdForRNA((MonomerNotationUnitRNA) current), current.getType());
    }

    else if (current instanceof MonomerNotationGroup) {
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

      }

      else {
        /* throw new exception */
        throw new AnalogSequenceException("j");
      }

    }

    else if (current instanceof MonomerNotationList) {
      StringBuilder sb = new StringBuilder();
      for (MonomerNotation element : ((MonomerNotationList) current).getListofMonomerUnits()) {
        sb.append(changeIdForRNA(element) + ".");
      }
      sb.setLength(sb.length() - 1);
      change = new MonomerNotationList(sb.toString(), current.getType());

    }

    else {
      /* throw new exception */
      throw new AnalogSequenceException("j");
    }

    change.setCount(current.getCount());
    if (current.getAnnotation() != null) {
      change.setAnnotation(current.getAnnotation());
    }

    return change;

  }

  /**
   * method to generate the sequence of a rna PolymerNotation into its natural
   * analogue sequence
   * 
   * @param polymer PolymerNotation
   * @return PolymerNotation with its natural analogue sequence
   * @throws org.helm.notation2.parser.exceptionparser.NotationException
   * @throws IOException
   * @throws JDOMException
   * @throws AnalogSequenceException
   */
  private static PolymerNotation convertRNAIntoAnalogSequence(PolymerNotation polymer)
      throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, JDOMException,
      AnalogSequenceException {

    /* change only if it is possible */
    for (int i = 0; i < polymer.getListMonomers().size(); i++) {
      polymer.getPolymerElements().getListOfElements().set(i, generateMonomerNotationRNA(polymer.getPolymerElements().getListOfElements().get(i)));
    }

    return polymer;
  }

  /**
   * method to change
   * 
   * @param monomerNotation
   * @return
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
    }
    else {
      Monomer monomer = nucleotidesNaturalAnalog.get(monomerNotation.getID().replace("[", "").replace("]", ""));
      String id = monomer.getNaturalAnalog();
      if (monomer.getMonomerType().equals(Monomer.BRANCH_MOMONER_TYPE)) {
        id = "(" + id + ")";
      }
      return id;
    }
  }

}

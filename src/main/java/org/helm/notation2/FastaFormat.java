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
import org.helm.notation.NotationException;
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
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * FastaFormat
 * 
 * @author hecht
 */
public final class FastaFormat {

  private static HELM2Notation helm2notation = new HELM2Notation();
  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(FastaFormat.class);

  private static Map<String, String> nucleotides = null;

  private static Map<String, String> transformNucleotides = null;

  private static Map<String, Monomer> nucleotidesNaturalAnalog = null;
  private static Map<String, Monomer> aminoacids = null;



  /**
   * method to read the information from a FastaFile-Format + generate Peptide
   * Polymers be careful -> it produces only polymers in the HELM1 standard, no
   * ambiguity
   * 
   * @param fasta FastaFile in string format
   * @return HELM2Notation
   * @throws FastaFormatException
   */
  protected static HELM2Notation generatePeptidePolymersFromFASTAFormatHELM1(String fasta) throws FastaFormatException {
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
        counter ++;
        if (counter > 1) {
          helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(), generateElementsOfPeptide(elements.toString(), polymer.getPolymerID()), annotation));
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
    helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(), generateElementsOfPeptide(elements.toString(), polymer.getPolymerID()), annotation));
    return helm2notation;
  }

  /**
   * method to read the information from a FastaFile-Format + generate RNA
   * Polymers be careful -> it produces only polymers in the HELM1 standard, no
   * ambiguity
   * 
   * @param fasta FastaFile in string format
   * @return HELM2Notation
   * @throws FastaFormatException
   */
  protected static HELM2Notation generateRNAPolymersFromFastaFormatHELM1(String fasta) throws FastaFormatException {
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
          
            helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(), generateElementsforRNA(elements.toString(), polymer.getPolymerID()), annotation));
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

    helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(), generateElementsforRNA(elements.toString(), polymer.getPolymerID()), annotation));

    return helm2notation;
  }

  /**
   * method to initialize map of existing nucleotides in the database
   * 
   * @throws FastaFormatException
   */
  private static void initMapNucleotides() throws FastaFormatException {
    try {
      nucleotides = NucleotideFactory.getInstance().getNucleotideTemplates().get("HELM Notation");
    } catch (IOException | org.jdom2.JDOMException | NotationException e) {
      LOG.error("NucleotideFactory can not be initialized");
      throw new FastaFormatException(e.getMessage());
    }
  }

  private static void InitMapTransformNucleotides(){
    transformNucleotides = new HashMap<String,String>();
    for(Map.Entry e : nucleotides.entrySet()){
      transformNucleotides.put(e.getValue().toString(), e.getKey().toString());
    }
  }
  
  /**
   * method to initialize map of existing nucleotides in the database
   * 
   * @throws FastaFormatException
   */
  private static void initMapNucleotidesNaturalAnalog() throws FastaFormatException {
    try {
      nucleotidesNaturalAnalog = MonomerFactory.getInstance().getMonomerDB().get("RNA");
    } catch (MonomerException | IOException | org.jdom2.JDOMException e) {
      LOG.error("Nucleotides can not be initialized");
      throw new FastaFormatException(e.getMessage());
    }
  }

  /**
   * method to initialize map of existing amino acids in the database
   * 
   * @throws FastaFormatException
   */
  private static void initMapAminoAcid() throws FastaFormatException {
    try {
      aminoacids = MonomerFactory.getInstance().getMonomerDB().get("PEPTIDE");
    } catch (MonomerException | IOException | org.jdom2.JDOMException e) {
      LOG.error("AminoAcids can not be initialized");
      throw new FastaFormatException(e.getMessage());
    }
  }

  /**
   * method to fill a peptide polymer with its elements (MonomerNotationUnits)
   * 
   * @param fasta
   * @param entity
   * @return PolymerListElements
   * @throws FastaFormatException
   */
  private static PolymerListElements generateElementsOfPeptide(String fasta, HELMEntity entity) throws FastaFormatException {
    try{
      PolymerListElements elements = new PolymerListElements(entity);
      for (Character c : fasta.toCharArray()) {
        /* Hier fehlt noch der MonomerCheck */
        if (aminoacids.get(c.toString()) != null) {
          elements.addMonomerNotation(c.toString());
        }
 else {
          throw new FastaFormatException("Not appropriate amino acid for HELM " + c);
        }
      }
      return elements;
    } catch (org.helm.notation2.parser.exceptionparser.NotationException | IOException | JDOMException e) {
      LOG.error("");
      throw new FastaFormatException("");
    }
  }

  /**
   * method to fill a rna polymer with its elements (MonomerNotationUnits)
   * 
   * @param fasta
   * @param entity
   * @return PolymerListElements
   * @throws FastaFormatException
   */
  private static PolymerListElements generateElementsforRNA(String fasta, HELMEntity entity) throws FastaFormatException {
    PolymerListElements elements = new PolymerListElements(entity);
    for (Character c : fasta.toCharArray()) {
      /*-> get for each single nucleotide code the contents from the nucleotidefactory*/
     
        try {
          elements.addMonomerNotation(nucleotides.get(c.toString()));
      } catch (org.helm.notation2.parser.exceptionparser.NotationException | IOException | JDOMException | NullPointerException e) {
          throw new FastaFormatException("Monomer can not be found");
        }
    }
    /* remove the phosphat of the last group */

    String id = elements.getCurrentMonomerNotation().getID();
    try {
      elements.changeMonomerNotation(new MonomerNotationUnitRNA(id.substring(0, id.length() - 1), "RNA"));
    } catch (org.helm.notation2.parser.exceptionparser.NotationException | IOException e) {
      throw new FastaFormatException(e.getMessage());
    }

    /* fertig */
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
   * @param polymers
   * @return
   * @throws FastaFormatException
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
        fasta.append(generateFastaFromPeptide(MethodsForContainerHELM2.getListOfHandledMonomers(polymer.getListMonomers())) + "\n");
      } catch (HELM2HandledException e) {
        throw new FastaFormatException(e.getMessage());
      }
    }
    return fasta.toString();
  }

  /**
   * method to generate Fasta for a list of peptide monomers
   * 
   * @param monomers
   * @return
   */
  private static String generateFastaFromPeptide(List<Monomer> monomers) {
    StringBuilder fasta = new StringBuilder();
    for (Monomer monomer : monomers) {
      fasta.append(monomer.getNaturalAnalog());
    }

    return fasta.toString();
  }

  /**
   * method to generate Fasta for rna polymers
   * 
   * @param polymers
   * @return
   * @throws FastaFormatException
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
        fasta.append(generateFastaFromRNA(MethodsForContainerHELM2.getListOfHandledMonomers(polymer.getListMonomers())) + "\n");
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
   * @return
   */
  private static String generateFastaFromRNA(List<Monomer> monomers) {
    StringBuilder fasta = new StringBuilder();
    for (Monomer monomer : monomers) {
      fasta.append(monomer.getNaturalAnalog());
    }

    return fasta.toString();
  }

  /**
   * method to generate for the whole HELM2Notation fasta-files -> it contains
   * fasta for all rna and peptides
   * 
   * @param helm2Notation2
   * @return
   * @throws FastaFormatException
   * @throws CTKException
   * @throws HELM2HandledException
   * @throws org.jdom2.JDOMException
   * @throws IOException
   * @throws MonomerException
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
   * method to convert all Peptides and RNAs into the analogSequence
   * 
   * @param helm2Notation
   * @throws org.helm.notation2.parser.exceptionparser.NotationException
   * @throws IOException
   * @throws JDOMException
   * @throws FastaFormatException
   * @throws AnalogSequenceException
   */
  protected static void convertIntoAnalogSequence(HELM2Notation helm2Notation) throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, JDOMException, FastaFormatException,
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
    }


  private static PolymerNotation convertPeptideIntoAnalogSequence(PolymerNotation polymer) throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, JDOMException,
      AnalogSequenceException {
    /* ändere dies nur wenn es möglich ist sonst lasse die alte Notation */
    for (int i = 0; i < polymer.getPolymerElements().getListOfElements().size(); i++) {
      /* list, group */
      
      /*Change current MonomerNotation*/
      polymer.getPolymerElements().getListOfElements().set(i, generateMonomerNotationPeptide(polymer.getPolymerElements().getListOfElements().get(i)));
    }
    /* müssen Connections geändert werden ?? */

    return polymer;
  }

  private static MonomerNotation generateMonomerNotationPeptide(MonomerNotation current) throws org.helm.notation2.parser.exceptionparser.NotationException, IOException,
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
    
    else if(current instanceof MonomerNotationGroup){
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
      
   else{
     /*throw new exception*/
        throw new AnalogSequenceException("j");
   }


    }
    

    else if(current instanceof MonomerNotationList){
      StringBuilder sb = new StringBuilder();
      String id = current.getID();
      for (String element : id.split("\\.")) {
        sb.append(aminoacids.get(element.replace("[", "").replace("]", "")).getNaturalAnalog() + ".");
      }
      sb.setLength(sb.length() - 1);
      change = new MonomerNotationList(sb.toString(), current.getType());
  
    }

    else{
      /*throw new exception*/
      throw new AnalogSequenceException("j");
    }
    
    change.setCount(current.getCount());
    if(current.getAnnotation() != null){
      change.setAnnotation(current.getAnnotation());
    }

    return change;

  }

  private static MonomerNotation generateMonomerNotationRNA(MonomerNotation current) throws org.helm.notation2.parser.exceptionparser.NotationException, IOException,
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
        String id = current.getID();
        for (MonomerNotationGroupElement element : ((MonomerNotationGroup) current).getListOfElements()) {

          sb.append(changeIdForRNA(element.getMonomerNotation()) + ",");
        }
        sb.setLength(sb.length() - 1);
        change = new MonomerNotationList(sb.toString(), current.getType());

      } else if (current instanceof MonomerNotationGroupMixture) {
        StringBuilder sb = new StringBuilder();
        String id = current.getID();
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
      String id = current.getID();
      String[] elements = id.split(".");
      for (MonomerNotation element : ((MonomerNotationList) current).getListofMonomerUnits()) {
        sb.append( changeIdForRNA(element) + ".");
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

  private static PolymerNotation convertRNAIntoAnalogSequence(PolymerNotation polymer) throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, JDOMException,
      AnalogSequenceException {

    /* ändere dies nur wenn es möglich ist sonst lasse die alte Notation */
    for (int i = 0; i < polymer.getListMonomers().size(); i++) {
      polymer.getPolymerElements().getListOfElements().set(i, generateMonomerNotationRNA(polymer.getPolymerElements().getListOfElements().get(i)));
    }
    /* müssen Connections geändert werden */

    return polymer;
  }


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
      else{
      Monomer monomer = nucleotidesNaturalAnalog.get(monomerNotation.getID().replace("[", "").replace("]", ""));
        String id = monomer.getNaturalAnalog();
        if (monomer.getMonomerType().equals(Monomer.BRANCH_MOMONER_TYPE)) {
          id = "(" + id + ")";
        }
       return id;
      }
}
}

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
import java.util.List;
import java.util.Map;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.NotationException;
import org.helm.notation.NucleotideFactory;
import org.helm.notation.model.Monomer;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.polymer.HELMEntity;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.notation.polymer.PeptideEntity;
import org.helm.notation2.parser.notation.polymer.PolymerListElements;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.helm.notation2.parser.notation.polymer.RNAEntity;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
      } catch (MonomerException | IOException | org.jdom2.JDOMException | HELM2HandledException | CTKException e) {
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
      } catch (MonomerException | IOException | org.jdom2.JDOMException | HELM2HandledException | CTKException e) {
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


}

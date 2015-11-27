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

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.StructureException;
import org.helm.notation2.calculation.ExtinctionCoefficient;
import org.helm.notation2.exception.ConnectionNotationException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.GroupingNotationException;
import org.helm.notation2.exception.HELM1FormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.exception.PolymerIDsException;
import org.helm.notation2.exception.ValidationException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebService class containing all required methods for the web-service
 * 
 * @author hecht
 */
public class WebService {

  private ContainerHELM2 containerhelm2;
  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(WebService.class);

  /**
   * method to read the HELM string, the HELM can be in version 1 or 2
   * 
   * @param notation
   * @throws ParserException
   */
  private void readNotation(String notation) throws ParserException {
    /* HELM1-Format -> */
    if (!(notation.contains("V2.0"))) {
      LOG.info("Convert HELM1 into HELM2");
      notation = new ConverterHELM1ToHELM2().doConvert(notation);
      LOG.info("Conversion was successful");
    }
    /* parses the HELM notation and generates the necessary notation objects */
    ParserHELM2 parser = new ParserHELM2();
    try {
      LOG.info("Parse HELM2");
      parser.parse(notation);
      LOG.info("Parsing was successful");
    } catch (ExceptionState | IOException | JDOMException e) {
      throw new ParserException(e.getMessage());
    }
    containerhelm2 = new ContainerHELM2(parser.getHELM2Notation(), new InterConnections());
  }

  /**
   * method to validate the input HELM-String
   * 
   * @param helm
   * @throws ParserException
   * @throws ValidationException
   */
  public void validateHELM(String helm) throws ParserException, ValidationException {
    /* Read */
    readNotation(helm);

    /* Validate */
    try {
      LOG.info("Validation of HELM is starting");
      Validation.validateNotationObjects(containerhelm2);
      LOG.info("Validation was successful");
    } catch (MonomerException | GroupingNotationException | ConnectionNotationException | PolymerIDsException e) {
      LOG.info("Validation was not successful");
      LOG.error(e.getMessage());
      throw new ValidationException(e.getMessage());
    }

  }
  
  /**
   * method to convert the input-HELM into a canonical HELM
   * 
   * @param notation
   * @return
   * @throws HELM1FormatException
   * @throws ParserException
   * @throws ValidationException
   */
  public String convertStandardHELMToCanonicalHELM(String notation) throws HELM1FormatException, ParserException, ValidationException {
    validateHELM(notation);
    return HELM1.getCanonical(containerhelm2.getHELM2Notation());
  }

  /**
   * method to convert the input-HELM into a standardHELM
   * 
   * @param notation
   * @return
   * @throws HELM1FormatException
   * @throws ParserException
   * @throws ValidationException
   */
  public String convertIntoStandardHELM(String notation) throws HELM1FormatException, ParserException, ValidationException {
    validateHELM(notation);
    return HELM1.getStandard(containerhelm2.getHELM2Notation());
  }


  /**
   * method to calculate the ExtinctionCoefficient from an input-HELM
   * 
   * @param notation
   * @return
   * @throws ParserException
   * @throws ValidationException
   * @throws HELM1FormatException
   */
  public Float calculateExtinctionCoefficient(String notation) throws ParserException, ValidationException, HELM1FormatException {
    validateHELM(notation);
    try {
      return ExtinctionCoefficient.getInstance().calculate(containerhelm2);
    } catch (org.helm.notation.NotationException | MonomerException | IOException | org.jdom2.JDOMException | StructureException | CalculationException | HELM2HandledException | CTKException e) {
      throw new HELM1FormatException(e.getMessage());
    }

  }
  

  /**
   * method to generate FASTA-Formats for all rna and peptide sequences
   * 
   * @param notation
   * @return
   * @throws ParserException
   * @throws ValidationException
   * @throws FastaFormatException
   */
  public String generateFasta(String notation) throws ParserException, ValidationException, FastaFormatException {
    validateHELM(notation);
    return FastaFormat.generateFasta(containerhelm2.getHELM2Notation());
  }

  /**
   * method to generate HELM from a FASTA containing nucleotides
   * 
   * @param notation
   * @return
   * @throws FastaFormatException
   */
  public String generateHELMFromFastaNucleotide(String notation) throws FastaFormatException {
    containerhelm2 = new ContainerHELM2(FastaFormat.generateRNAPolymersFromFastaFormatHELM1(notation), new InterConnections());
    return containerhelm2.getHELM2Notation().toHELM2();
  }

  /**
   * method to generate HELm from a FASTA containing amino acids
   * 
   * @param notation
   * @return
   * @throws FastaFormatException
   */
  public String generateHELMFromFastaPeptide(String notation) throws FastaFormatException {
    containerhelm2 = new ContainerHELM2(FastaFormat.generatePeptidePolymersFromFASTAFormatHELM1(notation), new InterConnections());
    return containerhelm2.getHELM2Notation().toHELM2();
  }


  /**
   * method to calculate the molecular weight of the HELM string
   * 
   * @param notation
   * @return
   * @throws ParserException
   * @throws ValidationException
   */
  public Double calculateMolecularWeight(String notation) throws ParserException, ValidationException {
    validateHELM(notation);

    /* to Do */
    return containerhelm2.getMolecularWeight();
  }

  /**
   * method to get the molecular formular of one HELM
   * 
   * @param notation
   * @return
   * @throws ValidationException
   * @throws ParserException
   */
  public String getMolecularFormula(String notation) throws ParserException, ValidationException {
    validateHELM(notation);
    /* to Do */
    return containerhelm2.getMolecularFormular();
  }

  /* To Do */
  public void doMonomerManagmentStoreActions() {

  }

  /* To Do */
  public void getImages(String notation) {

  }





}

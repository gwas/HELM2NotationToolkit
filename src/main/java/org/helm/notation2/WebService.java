/**
 * ***************************************************************************** Copyright C 2015, The Pistoia Alliance
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *****************************************************************************
 */
package org.helm.notation2;

import java.io.ByteArrayInputStream;
import java.io.IOException;


import org.helm.chemtoolkit.CTKException;
import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerLoadingException;
import org.helm.notation.MonomerStore;
import org.helm.notation.NotationException;
import org.helm.notation.model.Monomer;
import org.helm.notation.tools.xHelmNotationParser;
import org.helm.notation2.calculation.ExtinctionCoefficient;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.ConnectionNotationException;
import org.helm.notation2.exception.ExtinctionCoefficientException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.GroupingNotationException;
import org.helm.notation2.exception.HELM1FormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.exception.PolymerIDsException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.exception.ValidationException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * WebService class containing all required methods for the web-service
 * 
 * @author hecht
 */
public class WebService {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(WebService.class);

  private Element getXHELMRootElement(String resource) throws JDOMException,
      IOException {

    ByteArrayInputStream stream = new ByteArrayInputStream(resource.getBytes());
    SAXBuilder builder = new SAXBuilder();
    Document doc = builder.build(stream);

    return doc.getRootElement();
  }

  private void updateMonomerStore(MonomerStore monomerStore) throws MonomerLoadingException, IOException, MonomerException {
    for (Monomer monomer : monomerStore.getAllMonomersList()) {
      MonomerFactory.getInstance().getMonomerStore().addNewMonomer(monomer);
      // save monomer db to local file after successful update //
      MonomerFactory.getInstance().saveMonomerCache();
    }
  }

  /**
   * method to read the HELM string, the HELM can be in version 1 or 2
   * 
   * @param notation
   * @return
   * @throws ParserException
   * @throws IOException
   * @throws JDOMException
   * @throws MonomerException
   */
  private ContainerHELM2 readNotation(String notation) throws ParserException, JDOMException, IOException, MonomerException {
    /* xhelm notation */
    if (notation.contains("<Xhelm>")) {
      LOG.info("xhelm is used as input");
      String xhelm = notation;
      Element xHELMRootElement = getXHELMRootElement(xhelm);

      notation = xHelmNotationParser.getHELMNotationString(xHELMRootElement);
      MonomerStore store = xHelmNotationParser.getMonomerStore(xHELMRootElement);
      updateMonomerStore(store);

    }
    /* HELM1-Format -> */
    if (!(notation.contains("V2.0") || notation.contains("v2.0"))) {
      if (notation.endsWith("$")) {
        LOG.info("Convert HELM1 into HELM2");
        notation = new ConverterHELM1ToHELM2().doConvert(notation);
        LOG.info("Conversion was successful: " + notation);
      } else {
        LOG.info("Wrong HELM Input");
        throw new ParserException("HELMNotation is not valid");
      }
    }
    /* parses the HELM notation and generates the necessary notation objects */
    ParserHELM2 parser = new ParserHELM2();
    try {
      LOG.info("Parse HELM2");
      parser.parse(notation);
      LOG.info("Parsing was successful");
    } catch (ExceptionState | IOException | JDOMException e) {
      throw new ParserException("HELMNotation is not valid");
    }
    return new ContainerHELM2(parser.getHELM2Notation(), new InterConnections());
  }

  /**
   * method to validate the input HELM-String
   * 
   * @param helm
   * @throws ParserException
   * @throws ValidationException
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   */
  private ContainerHELM2 validate(String helm) throws ParserException, ValidationException, JDOMException, IOException, MonomerException {
    /* Read */
    ContainerHELM2 containerhelm2 = readNotation(helm);

    /* Validate */
    try {
      LOG.info("Validation of HELM is starting");
      Validation.validateNotationObjects(containerhelm2);
      LOG.info("Validation was successful");
      
      return containerhelm2;

    } catch (MonomerException | GroupingNotationException | ConnectionNotationException | PolymerIDsException e) {
      LOG.info("Validation was not successful");
      LOG.error(e.getMessage());
      throw new ValidationException(e.getMessage());
    }
  }

  public void validateHELM(String helm) throws ParserException, ValidationException, JDOMException, IOException, MonomerException {
    validate(helm);
    setMonomerFactoryToDefault(helm);
  }

  /**
   * method to convert the input-HELM into a canonical HELM
   * 
   * @param notation
   * @return
   * @throws HELM1FormatException
   * @throws ParserException
   * @throws ValidationException
   * @throws NotationException
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   */
  public String convertStandardHELMToCanonicalHELM(String notation) throws HELM1FormatException, ParserException,
      ValidationException, NotationException, JDOMException, IOException, MonomerException {
    String result = HELM1Utils.getCanonical(validate(notation).getHELM2Notation());
    setMonomerFactoryToDefault(notation);
    return result;
  }

  /**
   * method to convert the input-HELM into a standardHELM
   * 
   * @param notation
   * @return
   * @throws HELM1FormatException
   * @throws ParserException
   * @throws ValidationException
   * @throws NotationException
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   */
  public String convertIntoStandardHELM(String notation) throws HELM1FormatException, ParserException,
      ValidationException, NotationException, JDOMException, IOException, MonomerException {
    String result = HELM1Utils.getStandard(validate(notation).getHELM2Notation());
    setMonomerFactoryToDefault(notation);
    return result;
  }

  /**
   * method to calculate the ExtinctionCoefficient from an input-HELM
   * 
   * @param notation
   * @return
   * @throws ParserException
   * @throws ValidationException
   * @throws ExtinctionCoefficientException
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   */
  public Float calculateExtinctionCoefficient(String notation) throws ParserException, ValidationException,
      ExtinctionCoefficientException, CalculationException, JDOMException, IOException, MonomerException {
    Float result = ExtinctionCoefficient.getInstance().calculate(validate(notation));
    setMonomerFactoryToDefault(notation);
    return result;
  }

  /**
   * method to generate FASTA-Formats for all rna and peptide sequences
   * 
   * @param notation
   * @return
   * @throws ParserException
   * @throws ValidationException
   * @throws FastaFormatException
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   */
  public String generateFasta(String notation) throws ParserException, ValidationException, FastaFormatException, JDOMException, IOException, MonomerException {
    String result = FastaFormat.generateFasta(validate(notation).getHELM2Notation());
    setMonomerFactoryToDefault(notation);
    return result;
  }

  /**
   * method to generate HELM from a FASTA containing nucleotides
   * 
   * @param notation
   * @return
   * @throws FastaFormatException
   * @throws MonomerLoadingException
   */
  public String generateHELMFromFastaNucleotide(String notation) throws FastaFormatException, MonomerLoadingException {
    String result = new ContainerHELM2(FastaFormat.generateRNAPolymersFromFastaFormatHELM1(notation), new InterConnections()).getHELM2Notation().toHELM2();
    setMonomerFactoryToDefault(notation);
    return result;
  }

  /**
   * method to generate HELm from a FASTA containing amino acids
   * 
   * @param notation
   * @return
   * @throws FastaFormatException
   * @throws MonomerLoadingException
   */
  public String generateHELMFromFastaPeptide(String notation) throws FastaFormatException, MonomerLoadingException {
    String result = new ContainerHELM2(FastaFormat.generatePeptidePolymersFromFASTAFormatHELM1(notation), new InterConnections()).getHELM2Notation().toHELM2();
    setMonomerFactoryToDefault(notation);
    return result;
  }

  /**
   * method to calculate the molecular weight of the HELM string
   * 
   * @param notation
   * @return
   * @throws ParserException
   * @throws ValidationException
   * @throws BuilderMoleculeException
   * @throws CTKException
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   */
  public Double calculateMolecularWeight(String notation) throws ParserException, ValidationException, BuilderMoleculeException, CTKException, JDOMException, IOException, MonomerException {
    Double result = MoleculeInformation.getMolecularWeight(validate(notation).getHELM2Notation());
    setMonomerFactoryToDefault(notation);
    return result;
  }

  /**
   * method to get the molecular formular of one HELM
   * 
   * @param notation
   * @return
   * @throws ValidationException
   * @throws ParserException
   * @throws BuilderMoleculeException
   * @throws CTKException
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   */
  public String getMolecularFormula(String notation) throws ParserException, ValidationException, BuilderMoleculeException, CTKException, JDOMException, IOException, MonomerException {
    String result = MoleculeInformation.getMolecularFormular(validate(notation).getHELM2Notation());
    setMonomerFactoryToDefault(notation);
    return result;
  }

  public String getNaturalAnalogSequence(String notation) throws ParserException, ValidationException, JDOMException, IOException, MonomerException {
    /* is only possible for peptides + nucleotides */
    /* replace each polymer into the analogsequence and generate HELM2 format */
    HELM2Notation helm2notation;
    try {
      helm2notation = FastaFormat.convertIntoAnalogSequence(validate(notation).getHELM2Notation());
      setMonomerFactoryToDefault(notation);
      return helm2notation.toHELM2();
    } catch (org.helm.notation2.parser.exceptionparser.NotationException | IOException | JDOMException | FastaFormatException | AnalogSequenceException e) {
      return e.getMessage();
    }

  }

  public String readPeptide(String peptide) throws FastaFormatException {
    HELM2Notation helm2notation = new HELM2Notation();
    PolymerNotation polymer;
    try {
      polymer = new PolymerNotation("PEPTIDE" + "1");
      helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(),
          FastaFormat.generateElementsOfPeptide(peptide, polymer.getPolymerID()), null));
      return helm2notation.toHELM2();
    } catch (org.helm.notation2.parser.exceptionparser.NotationException e) {
      return e.getMessage();
    }
  }

  public String readRNA(String rna) throws FastaFormatException {
    HELM2Notation helm2notation = new HELM2Notation();
    PolymerNotation polymer;
    try {
      polymer = new PolymerNotation("RNA" + "1");

      helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(),
          FastaFormat.generateElementsforRNA(rna, polymer.getPolymerID()), null));
      return helm2notation.toHELM2();
    } catch (org.helm.notation2.parser.exceptionparser.NotationException e) {
      return e.getMessage();
    }
  }


  /**
   * method to generate for the whole HELMNotation the molecule
   * 
   * @param notation HELM string
   * @return generated molecule image in byte[]
   * @throws ParserException if the HELM string is not parsable
   * @throws ValidationException if the HELM string is not valid
   * @throws BuilderMoleculeException if the molecule can't be built
   * @throws CTKException
   * @throws IOException
   * @throws JDOMException
   * @throws MonomerException if
   */
  public byte[] generateImageForHELMMolecule(String notation) throws ParserException, ValidationException, BuilderMoleculeException, CTKException, IOException, JDOMException, MonomerException {
    byte[] result = Images.generateImageHELMMolecule(validate(notation).getHELM2Notation());
    setMonomerFactoryToDefault(notation);
    return result;
  }

  public byte[] generateImageForMonomer(Monomer monomer) throws BuilderMoleculeException, CTKException {
    return Images.generateImageofMonomer(monomer);
  }

  public String generateJSON(String helm) throws ParserException, ValidationException, JDOMException, IOException, MonomerException {
    String result = validate(helm).toJSON();
    setMonomerFactoryToDefault(helm);
    return result;
  }


  private void setMonomerFactoryToDefault(String helm) throws MonomerLoadingException {
    if (helm.contains("<Xhelm>")) {
      LOG.info("Refresh local Monomer Store in case of Xhelm");
      MonomerFactory.refreshMonomerCache();
    }
  }

  public String generateNaturalAnalogSequencePeptide(String notation) throws org.helm.notation2.parser.exceptionparser.NotationException, HELM2HandledException, ParserException, ValidationException,
      JDOMException, IOException, MonomerException {
    ContainerHELM2 containerhelm2 = validate(notation);
    return SequenceConverter.getPeptideNaturalAnalogSequenceFromNotation(containerhelm2.getHELM2Notation());

  }

  public String generateNaturalAnalogSequenceRNA(String notation) throws org.helm.notation2.parser.exceptionparser.NotationException, HELM2HandledException, RNAUtilsException, ParserException,
      ValidationException, JDOMException, IOException, MonomerException {
    ContainerHELM2 containerhelm2 = validate(notation);
    return SequenceConverter.getNucleotideNaturalAnalogSequenceFromNotation(containerhelm2.getHELM2Notation());
  }

}

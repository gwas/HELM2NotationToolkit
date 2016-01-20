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
package org.helm.notation2.calculation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.helm.notation.CalculationException;
import org.helm.notation.model.Monomer;
import org.helm.notation2.ContainerHELM2;
import org.helm.notation2.MethodsForContainerHELM2;
import org.helm.notation2.exception.ExtinctionCoefficientException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExtinctionCoefficient class to calculate the extinction coefficient
 *
 * @author hecht
 */
public final class ExtinctionCoefficient {

  /**
   *
   */
  private static final int UNIT = 1000;

  private static final Logger LOG =
      LoggerFactory.getLogger(ExtinctionCoefficient.class);

  private static final int RNA_UNIT_TYPE = 1;

  private static final int PEPTIDE_UNIT_TYPE = 2;

  private static Map<String, Float> monoNucleotideMap = new HashMap<String, Float>();

  private static Map<String, Float> diNucleotideMap = new HashMap<String, Float>();

  private static Map<String, Float> aminoAcidMap = new HashMap<String, Float>();

  private static String rnaPropertyFile = "/org/helm/notation/resources/RNAExtinctionCoefficient.properties";

  private static String peptidePropertyFile = "/org/helm/notation/resources/PEPTIDEExtinctionCoefficient.properties";

  private static ExtinctionCoefficient instance;

  private ExtinctionCoefficient() {

  }

  public static ExtinctionCoefficient getInstance() throws ExtinctionCoefficientException {
    if (null == instance) {
      instance = new ExtinctionCoefficient();
      try {
        initMaps();
      } catch (IOException ex) {
        throw new ExtinctionCoefficientException("Unable to initalize extinction coefficient property files");
      }
    }
    return instance;
  }

  private static void initMaps() throws IOException {
    InputStream ris = ExtinctionCoefficient.class.getResourceAsStream(rnaPropertyFile);
    Properties rp = new Properties();
    rp.load(ris);

    Enumeration<?> re = rp.propertyNames();
    while (re.hasMoreElements()) {
      String key = (String) re.nextElement();
      String value = rp.getProperty(key);
      Float f = new Float(value);
      int len = key.length();
      if (len == 1) {
        monoNucleotideMap.put(key, f);
      } else if (len == 2) {
        diNucleotideMap.put(key, f);
      }
    }
    ris.close();

    InputStream pis = ExtinctionCoefficient.class.getResourceAsStream(peptidePropertyFile);
    Properties pp = new Properties();
    pp.load(pis);

    Enumeration<?> pe = pp.propertyNames();
    while (pe.hasMoreElements()) {
      String key = (String) pe.nextElement();
      String value = pp.getProperty(key);

      Float f = new Float(value);
      aminoAcidMap.put(key, f);
    }
    pis.close();
  }

  public String getUnit(int unitType) {
    switch (unitType) {
    case RNA_UNIT_TYPE:
      return getRnaUnit();
    case PEPTIDE_UNIT_TYPE:
      return getPeptideUnit();
    default:
      return null;
    }
  }

  public String getRnaUnit() {
    return "mM-1cm-1";
  }

  public String getPeptideUnit() {
    return "M-1cm-1";
  }

  public String getDefaultUnit() {
    return getRnaUnit();
  }

  public int getDefaultUnitType() {
    return RNA_UNIT_TYPE;
  }

  /**
   * method to calculate the extinction coefficient for the whole HELM molecule
   *
   * @param helm2container input ContainerHELM2
   * @return extinction coefficient
   * @throws ExtinctionCoefficientException if the HELM contains HELM2 features
   */
  public float calculate(ContainerHELM2 helm2container) throws ExtinctionCoefficientException {
    LOG.debug("ExtinctionCalculation is starting");
    int unitType = 1;
    float result = 0.0f;
    List<PolymerNotation> polymerNodes = helm2container.getHELM2Notation().getListOfPolymers();
    for (PolymerNotation polymerNode : polymerNodes) {
      String polymerType = polymerNode.getPolymerID().getType();
      float ext = 0.0f;
      ArrayList<PolymerNotation> not = new ArrayList<PolymerNotation>();
      not.add(polymerNode);
      if (polymerType.equals(Monomer.NUCLIEC_ACID_POLYMER_TYPE)) {
        try {
          ext = calculateExtinctionFromRNA(MethodsForContainerHELM2.getListOfHandledMonomersOnlyBase(polymerNode.getPolymerElements().getListOfElements()));
        } catch (CalculationException | IOException | HELM2HandledException e) {
          throw new ExtinctionCoefficientException(e.getMessage());
        }
        if (unitType == PEPTIDE_UNIT_TYPE) {
          ext = ext * UNIT;
        }
      } else if (polymerType.equals(Monomer.PEPTIDE_POLYMER_TYPE)) {
        try {
          ext = calculateExtinctionFromPeptide(MethodsForContainerHELM2.getListOfHandledMonomers(polymerNode.getPolymerElements().getListOfElements()));
        } catch (IOException | HELM2HandledException e) {
          throw new ExtinctionCoefficientException(e.getMessage());
        }
        if (unitType == RNA_UNIT_TYPE) {
          ext = ext / UNIT;
        }
      }
      result = result + ext;
    }
    return result;
  }

  /**
   * method to calculate the extinction coefficient for rna
   *
   * @param monomers all Monomers of the RNA/DNA
   * @return extinction coefficient
   * @throws CalculationException if the rna contains not valid nucleotides
   * @throws IOException
   */
  private static float calculateExtinctionFromRNA(List<Monomer> monomers) throws CalculationException, IOException {
    LOG.debug("ExtinctionCalculation of RNA");
    float resultSingle = 0.0f;
    float result = 0.0f;
    float resultDi = 0.0f;
    String previous = "";
    if (monomers.size() == 0) {
      throw new CalculationException("Input sequence cannot be null");
    } else {
      if (monomers.size() == 1) {
        if (monoNucleotideMap.containsKey(monomers.get(0).getNaturalAnalog())) {
          result = monoNucleotideMap.get(monomers.get(0).getNaturalAnalog()).floatValue();
          return result;
        } else {
          throw new CalculationException("Unknown nucleotide found");
        }
      }
      for (int i = 0; i < monomers.size(); i++) {
        if (i > 0 && i < monomers.size() - 1) {
          if (monoNucleotideMap.containsKey(monomers.get(i).getNaturalAnalog())) {
            Float value = monoNucleotideMap.get(monomers.get(i).getNaturalAnalog());
            resultSingle += value;

          }
        }
        if (previous != "") {
          if (diNucleotideMap.containsKey(previous + monomers.get(i).getNaturalAnalog())) {
            Float value = diNucleotideMap.get(previous + monomers.get(i).getNaturalAnalog());
            resultDi += value;
          }
        }

        previous = monomers.get(i).getNaturalAnalog();
      }

    }

    return (2 * resultDi) - resultSingle;
  }

  /**
   * method to calculate the extinction coefficient for peptide
   *
   * @param monomers all monomers of the peptide
   * @return extinction coefficient
   * @throws IOException
   * @throws HELM2HandledException if HELM2 features were there
   */
  private static float calculateExtinctionFromPeptide(List<Monomer> monomers) throws IOException, HELM2HandledException {
    if (monomers.isEmpty()) {
      return 0.0f;
    }

    float result = 0.0f;
    for (Monomer mon : monomers) {
      try {
        String id = mon.getAlternateId();
        if (aminoAcidMap.containsKey(id)) {
          float value = aminoAcidMap.get(id);
          result = result + value;
        }
      } catch (NullPointerException e) {
        throw new HELM2HandledException("Functions can not be called for HELM2 objects");
      }

    }
    return result;
  }

}

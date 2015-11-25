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
package org.helm.notation2.Calculation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.model.Monomer;
import org.helm.notation2.ContainerHELM2;
import org.helm.notation2.MethodsForContainerHELM2;
import org.helm.notation2.Exception.HELM2HandledException;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;



/**
 * ExtinctionCoefficient
 * 
 * @author hecht
 */
public class ExtinctionCoefficient {

  public static final int RNA_UNIT_TYPE = 1;

  public static final int PEPTIDE_UNIT_TYPE = 2;

  private static Map<String, Float> monoNucleotideMap = new HashMap<String, Float>();

  private static Map<String, Float> diNucleotideMap = new HashMap<String, Float>();

  private static Map<String, Float> aminoAcidMap = new HashMap<String, Float>();

  private static String rnaPropertyFile = "/org/helm/notation/resources/RNAExtinctionCoefficient.properties";

  private static String peptidePropertyFile = "/org/helm/notation/resources/PEPTIDEExtinctionCoefficient.properties";

  private static ExtinctionCoefficient instance;
  
  private ExtinctionCoefficient() {
  
  }

  public static ExtinctionCoefficient getInstance() throws CalculationException {
    if (null == instance) {
      instance = new ExtinctionCoefficient();
      try {
        initMaps();
      } catch (IOException ex) {
        throw new CalculationException("Unable to initalize extinction coefficient property files");
      }
    }
    return instance;
  }

  private static void initMaps() throws IOException {
    InputStream ris = ExtinctionCoefficient.class.getResourceAsStream(rnaPropertyFile);
    Properties rp = new Properties();
    rp.load(ris);

    Enumeration re = rp.propertyNames();
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

    Enumeration pe = pp.propertyNames();
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

  public float calculate(ContainerHELM2 helm2container) throws NotationException,
      MonomerException, IOException, JDOMException, StructureException,
      CalculationException, HELM2HandledException, CTKException {
    int unitType = 1;
    float result = 0.0f;
    List<PolymerNotation> polymerNodes = helm2container.getHELM2Notation().getListOfPolymers();
    for (PolymerNotation polymerNode : polymerNodes) {
      String polymerType = polymerNode.getPolymerID().getType();
      float ext = 0.0f;
      ArrayList<PolymerNotation> not = new ArrayList<PolymerNotation>();
      not.add(polymerNode);
      if (polymerType.equals(Monomer.NUCLIEC_ACID_POLYMER_TYPE)) {
        ext = calculateExtinctionFromRNA(MethodsForContainerHELM2.getListOfHandledMonomers(polymerNode.getPolymerElements().getListOfElements()));
        if (unitType == PEPTIDE_UNIT_TYPE) {
          ext = ext * 1000;
        }
      } else if (polymerType.equals(Monomer.PEPTIDE_POLYMER_TYPE)) {
        ext = calculateExtinctionFromPeptide(MethodsForContainerHELM2.getListOfHandledMonomers(polymerNode.getPolymerElements().getListOfElements()));
        if (unitType == RNA_UNIT_TYPE) {
          ext = ext / 1000;
        }
      }
      result = result + ext;
    }
    return result;
  }

  /**
   * @param notation
   * @return
   * @throws CalculationException
   * @throws IOException
   */
  private static float calculateExtinctionFromRNA(List<Monomer> monomers) throws CalculationException, IOException {
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
   * @param notation
   * @return
   * @throws IOException
   * @throws HELM2HandledException
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




/*--
 *
 * @(#) Calculation.java
 *
 *
 */
package org.helm2.Calculation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.model.Monomer;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.Notation.Polymer.PolymerNotation;
import org.helm2.ContainerHELM2;
import org.helm2.exception.HELM2HandledException;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calculation
 * 
 * @author hecht
 */
public class ExtinctionCoefficient {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(ExtinctionCoefficient.class);

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

  public static float calculate(ContainerHELM2 helm2container) throws NotationException,
      MonomerException, IOException, JDOMException, StructureException,
      CalculationException, HELM2HandledException {
    int unitType = 1;
    float result = 0.0f;
    List<PolymerNotation> polymerNodes = helm2container.getHELM2Notation().getListOfPolymers();
    for (PolymerNotation polymerNode : polymerNodes) {
      String polymerType = polymerNode.getPolymerID().getType();
      String notation = polymerNode.getPolymerID().getID();
      float ext = 0.0f;
      ArrayList<PolymerNotation> not = new ArrayList<PolymerNotation>();
      not.add(polymerNode);
      if (polymerType.equals(Monomer.NUCLIEC_ACID_POLYMER_TYPE)) {
        ext = calculateExtinctionFromRNA(helm2container.getListOfHandledMonomers(polymerNode.getPolymerElements().getListOfElements()));
        if (unitType == PEPTIDE_UNIT_TYPE) {
          ext = ext * 1000;
        }
      } else if (polymerType.equals(Monomer.PEPTIDE_POLYMER_TYPE)) {
        ext = calculateExtinctionFromPeptide(helm2container.getListOfHandledMonomers(polymerNode.getPolymerElements().getListOfElements()));
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
    System.out.println(monomers);
    if (monomers.size() == 0) {
      throw new CalculationException("Input sequence cannot be null");
    } else {
      if (monomers.size() == 1) {
        if (monoNucleotideMap.containsKey(monomers.get(0).getNaturalAnalog())) {
          result = monoNucleotideMap.get(monomers.get(0).getNaturalAnalog()).floatValue();
          return result;
        } else {
          throw new CalculationException("Unknown nucleotide ["
              + monomers.get(0).getNaturalAnalog() + "] found");
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
        if (aminoAcidMap.containsKey(mon.getAlternateId())) {
          float value = aminoAcidMap.get(mon.getAlternateId());
          result = result + value;
        }
      } catch (NullPointerException e) {
        throw new HELM2HandledException("Functions can not be called for HELM2 objects");
      }

      }
    return result;
  }




}



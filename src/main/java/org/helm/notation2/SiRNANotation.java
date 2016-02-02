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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.helm.notation.NucleotideFactory;
import org.helm.notation.model.Nucleotide;
import org.helm.notation.tools.NucleotideSequenceParser;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.annotation.AnnotationNotation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SiRNANotation, class to generate SirnaNotation
 *
 * @author hecht
 */
public class SiRNANotation {

  public static Map<String, String> complementMap = new HashMap<String, String>();

  static {
    complementMap.put("A", "U");
    complementMap.put("G", "C");
    complementMap.put("C", "G");
    complementMap.put("U", "A");
    complementMap.put("T", "A");
    complementMap.put("X", "X");
  }

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(SiRNANotation.class);

  /**
   * this method converts nucleotide sequences into ContainerHELM2
   *
   * @param senseSeq 5-3 nucleotide sequence for default notation
   * @param antiSenseSeq 3-5 nucleotide sequence for default notation
   * @return ContainerHELM2 for siRNA
   * @throws NotationException
   * @throws FastaFormatException
   * @throws IOException
   * @throws JDOMException
   * @throws HELM2HandledException
   * @throws RNAUtilsException
   * @throws org.helm.notation.NotationException
   */
  public static ContainerHELM2 getSiRNANotation(String senseSeq, String antiSenseSeq) throws NotationException, FastaFormatException, IOException, JDOMException, HELM2HandledException,
      RNAUtilsException, org.helm.notation.NotationException {
    return getSirnaNotation(senseSeq, antiSenseSeq, NucleotideSequenceParser.RNA_DESIGN_NONE);
  }

  /**
   * this method converts nucleotide sequences into HELM notation based on
   * design pattern
   *
   * @param senseSeq 5-3 nucleotide sequence
   * @param antiSenseSeq 3-5 nucleotide sequence
   * @param rnaDesignType
   * @return ContainerHELM2 for siRNA
   * @throws NotationException
   * @throws FastaFormatException
   * @throws IOException
   * @throws JDOMException
   * @throws HELM2HandledException
   * @throws RNAUtilsException
   * @throws org.helm.notation.NotationException
   */
  public static ContainerHELM2 getSirnaNotation(String senseSeq, String antiSenseSeq, String rnaDesignType) throws NotationException, FastaFormatException, IOException, JDOMException,
      HELM2HandledException, RNAUtilsException, org.helm.notation.NotationException {
    ContainerHELM2 containerHELM2 = null;
    if (senseSeq != null && senseSeq.length() > 0) {
      containerHELM2 = SequenceConverter.readRNA(senseSeq);
    }
    if (antiSenseSeq != null && antiSenseSeq.length() > 0) {
      PolymerNotation antisense = new PolymerNotation("RNA2");
      antisense = new PolymerNotation(antisense.getPolymerID(), FastaFormat.generateElementsforRNA(antiSenseSeq, antisense.getPolymerID()));

      containerHELM2.getHELM2Notation().addPolymer(antisense);
    }
    validateSiRNADesign(containerHELM2.getHELM2Notation().getListOfPolymers().get(0), containerHELM2.getHELM2Notation().getListOfPolymers().get(0), rnaDesignType);
    containerHELM2.getHELM2Notation().getListOfConnections().addAll(hybridization(containerHELM2.getHELM2Notation().getListOfPolymers().get(0), containerHELM2.getHELM2Notation().getListOfPolymers().get(1), rnaDesignType));
    ChangeObjects.addAnnotation(new AnnotationNotation("RNA1{ss}|RNA2{as}"), 0, containerHELM2);
    return containerHELM2;
  }

  /**
   * method to generate a List of ConnectionNotation according to the given two
   * polymerNotations and the rnaDesigntype
   *
   * @param one PolymerNotation
   * @param two PolymerNotation
   * @param rnaDesignType
   * @return List of ConnectionNotations
   * @throws JDOMException
   * @throws IOException
   * @throws NotationException
   * @throws org.helm.notation.NotationException
   * @throws RNAUtilsException
   * @throws HELM2HandledException
   */
  private static List<ConnectionNotation> hybridization(PolymerNotation one, PolymerNotation two, String rnaDesignType) throws NotationException, IOException, JDOMException, HELM2HandledException,
      RNAUtilsException, org.helm.notation.NotationException {
    List<ConnectionNotation> connections = new ArrayList<ConnectionNotation>();
    ConnectionNotation connection;
    if (one.getPolymerElements().getListOfElements() != null && one.getPolymerElements().getListOfElements().size() > 0 && two.getPolymerElements().getListOfElements() != null
        && two.getPolymerElements().getListOfElements().size() > 0) {
      String analogSeqSS = RNAUtils.getNaturalAnalogSequence(one).replaceAll("T", "U");
      String analogSeqAS = new StringBuilder(RNAUtils.getNaturalAnalogSequence(two).replaceAll("T", "U")).toString();

      if (NucleotideSequenceParser.RNA_DESIGN_NONE.equalsIgnoreCase(rnaDesignType)) {
        String normalCompAS = RNAUtils.getNaturalAnalogSequence(RNAUtils.getComplement(two)).replace("T", "U");
        String maxMatch = RNAUtils.getMaxMatchFragment(analogSeqSS, new StringBuilder(normalCompAS).reverse().toString());
        if (maxMatch.length() > 0) {
          int ssStart = analogSeqSS.indexOf(maxMatch);
          int normalCompStart = new StringBuilder(normalCompAS).reverse().toString().indexOf(maxMatch);
          int asStart = analogSeqAS.length() - maxMatch.length()
              - normalCompStart;

          for (int i = 0; i < maxMatch.length(); i++) {
            int ssPos = (i + ssStart) * 3 + 2;
            int asPos = (asStart + maxMatch.length() - 1 - i) * 3 + 2;
            String details = ssPos + ":pair-" + asPos + ":pair";
            connection = new ConnectionNotation(one.getPolymerID(), two.getPolymerID(), details);
            connections.add(connection);
          }
        }
      } else if (NucleotideSequenceParser.RNA_DESIGN_TUSCHL_19_PLUS_2.equalsIgnoreCase(rnaDesignType)) {
        int matchLength = 19;
        connections = hybridizationWithLengthFromStart(one, two, analogSeqSS, analogSeqAS, matchLength);
      } else if (NucleotideSequenceParser.RNA_DESIGN_DICER_27_R.equalsIgnoreCase(rnaDesignType)) {
        int matchLength = 25;
        connections = hybridizationWithLengthFromStart(one, two, analogSeqSS, analogSeqAS, matchLength);
      } else if (NucleotideSequenceParser.RNA_DESIGN_DICER_27_L.equalsIgnoreCase(rnaDesignType)) {
        int matchLength = 25;
        connections = hybridizationWithLengthFromStart(one, two, analogSeqSS, analogSeqAS, matchLength);
      } else {
        new RNAUtilsException("RNA-Design-Type " + rnaDesignType + " is unknown");
      }
    }
    return connections;
  }

  /**
   * method to generate a List of ConnectionNotations given the two
   * PolymerNotations and the sequences and the start point
   *
   * @param one PolymerNotation
   * @param two PolymerNotation
   * @param senseAnalogSeq sense Sequence
   * @param antisenseAnalogSeq antisense Sequence
   * @param lengthFromStart start position
   * @return List of ConnectionNotations
   * @throws NotationException
   * @throws IOException
   * @throws JDOMException
   */
  private static List<ConnectionNotation> hybridizationWithLengthFromStart(PolymerNotation one, PolymerNotation two,
      String senseAnalogSeq, String antisenseAnalogSeq,
      int lengthFromStart) throws NotationException, IOException, JDOMException {
    List<ConnectionNotation> connections = new ArrayList<ConnectionNotation>();
    ConnectionNotation connection;
    for (int i = 0; i < lengthFromStart; i++) {
      int ssPos = i * 3 + 2;
      int asPos = (lengthFromStart - 1 - i) * 3 + 2;
      String ssChar = String.valueOf(senseAnalogSeq.charAt(i));
      String asChar = String.valueOf(antisenseAnalogSeq.charAt(lengthFromStart - 1 - i));
      if (complementMap.get(ssChar).equalsIgnoreCase(asChar)) {
        String details = ssPos + ":pair-" + asPos + ":pair";
        connection = new ConnectionNotation(one.getPolymerID(), two.getPolymerID(), details);
        connections.add(connection);
      }

    }
    return connections;
  }

  /**
   * validates the required siRNA
   * 
   * @param senseSeq
   * @param antiSenseSeq
   * @param rnaDesignType
   * @return true, if it is valid, throws an exception otherwise
   * @throws HELM2HandledException
   * @throws RNAUtilsException
   * @throws NotationException
   */
  private static boolean validateSiRNADesign(PolymerNotation one, PolymerNotation two, String rnaDesignType) throws RNAUtilsException, HELM2HandledException, NotationException {
    if (NucleotideSequenceParser.RNA_DESIGN_NONE.equalsIgnoreCase(rnaDesignType)) {
      return true;
    }

    if (!NucleotideSequenceParser.SUPPORTED_DESIGN_LIST.contains(rnaDesignType)) {
      throw new NotationException("Unsupported RNA Design Type '"
          + rnaDesignType + "'");
    }

    List<Nucleotide> senseNucList = RNAUtils.getNucleotideList(one);
    List<Nucleotide> antisenseNucList = RNAUtils.getNucleotideList(two);

    if (rnaDesignType.equals(NucleotideSequenceParser.RNA_DESIGN_TUSCHL_19_PLUS_2)) {
      if (senseNucList.size() != 21) {
        throw new NotationException(
            "Sense strand for Tuschl 19+2 design must have 21 nucleotides");
      }
      if (antisenseNucList.size() != 21) {
        throw new NotationException(
            "Antisense strand for Tuschl 19+2 design must have 21 nucleotides");
      }
    } else if (rnaDesignType.equals(NucleotideSequenceParser.RNA_DESIGN_DICER_27_R)) {
      if (senseNucList.size() != 25) {
        throw new NotationException(
            "Sense strand for Dicer 27R design must have 25 nucleotides");
      }
      if (antisenseNucList.size() != 27) {
        throw new NotationException(
            "Antisense strand for Dicer 27R design must have 27 nucleotides");
      }
    } else if (rnaDesignType.equals(NucleotideSequenceParser.RNA_DESIGN_DICER_27_L)) {
      if (senseNucList.size() != 27) {
        throw new NotationException(
            "Sense strand for Dicer 27L design must have 27 nucleotides");
      }
      if (antisenseNucList.size() != 25) {
        throw new NotationException(
            "Antisense strand for Dicer 27L design must have 25 nucleotides");
      }
    }

    return true;

  }

}


package org.helm.notation2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.NotationException;
import org.helm.notation.model.Monomer;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.polymer.HELMEntity;
import org.helm.notation2.parser.notation.polymer.PolymerListElements;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FastaFormat
 * 
 * @author hecht
 */
public class FastaFormat {

  HELM2Notation helm2notation = new HELM2Notation();
  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(FastaFormat.class);

  public String getFastaFormatForPeptide(HELM2Notation helm2notation) {
    return null;
  }

  public void readFastaFilePeptide(String fasta) throws FastaFormatException, org.helm.notation2.parser.exceptionparser.NotationException, IOException, JDOMException {
    if (null == fasta) {
      LOG.error("Peptide Sequence must be specified");
      throw new FastaFormatException("Peptide Sequence must be specified");
    }
    StringBuilder elements = new StringBuilder();
    int counter = 0;
    PolymerNotation polymer = new PolymerNotation("PEPTIDE" + "1");
    String annotation = "";
    for (String line : fasta.split("\n")) {
      if (line.startsWith(">")) {
        counter ++;
        if (counter > 1) {
          helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(), generateElementsofPeptide(elements.toString(), polymer.getPolymerID()), annotation));
          polymer = new PolymerNotation("PEPTIDE" + counter);
        }
        annotation = line.substring(1);
      }
 else {
        elements.append(line);
      }



    }
    helm2notation.addPolymer(new PolymerNotation(polymer.getPolymerID(), generateElementsofPeptide(elements.toString(), polymer.getPolymerID()), annotation));
    System.out.println(helm2notation.toHELM2());
  }

  private PolymerListElements generateElementsofPeptide(String fasta, HELMEntity entity) throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, JDOMException {
    PolymerListElements elements = new PolymerListElements(entity);
    for (Character c : fasta.toCharArray()) {
      elements.addMonomerNotation(c.toString());
    }

    return elements;

  }

  public HELM2Notation getHELMNotationForPeptide(String fasta) throws FastaFormatException, MonomerException, IOException, org.jdom2.JDOMException, NotationException {


    /**/

    String cleanSeq = cleanup(fasta);
    Map<String, Monomer> peptideMap = MonomerFactory.getInstance().getMonomerDB().get(Monomer.PEPTIDE_POLYMER_TYPE);
    Set<String> keySet = peptideMap.keySet();

    /* translate each peptide polymer type */
    List<String> l = new ArrayList<String>();
    int pos = 0;
    while (pos < cleanSeq.length()) {
      boolean found = false;
      for (Iterator i = keySet.iterator(); i.hasNext();) {
        String symbol = (String) i.next();

        if (cleanSeq.startsWith(symbol, pos)) {
          found = true;
          l.add(symbol);
          pos = pos + symbol.length();
          break;
        }
      }
      if (!found) {
        throw new NotationException(
            "Sequence contains unknown amino acid starting at "
                + cleanSeq.substring(pos));
      }
    }

    StringBuilder helm = new StringBuilder();
    return null;

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

}

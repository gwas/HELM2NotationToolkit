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
package org.helm.notation2.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerLoadingException;
import org.helm.notation.NotationException;
import org.helm.notation2.Monomer;
import org.helm.notation2.MonomerFactory;
import org.helm.notation2.exception.ChemistryException;
import org.jdom2.JDOMException;

/**
 * AminoAcidParser
 *
 * @author hecht
 */
public class AminoAcidParser {

  public static final String[] SEQUENCE_DELIMETERS = {".", ":", ",", ";"};

  /**
   * This method converts peptide sequence into a List of amino acid
   *
   * @param peptideSequence
   * @return list of amino acid
   * @throws org.helm.notation.MonomerException
   * @throws org.helm.notation.NotationException
   * @throws java.io.IOException
   * @throws org.jdom.JDOMException
   * @throws MonomerLoadingException
   * @throws ChemistryException
   */
  public static List<String> getAminoAcidList(String peptideSequence)
      throws MonomerException, NotationException, IOException,
      JDOMException, MonomerLoadingException, ChemistryException {

    if (null == peptideSequence) {
      throw new NotationException("Peptide Sequence must be specified");
    }

    String cleanSeq = cleanup(peptideSequence);

    Map<String, Monomer> peptideMap = MonomerFactory.getInstance().getMonomerDB().get(Monomer.PEPTIDE_POLYMER_TYPE);
    Set<String> keySet = peptideMap.keySet();

    // walk the sequence
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

    return l;
  }

  /**
   * This method converts peptide sequence into a List of amino acid with
   * optional delimiter
   *
   * @param peptideSequence input sequence
   * @param delimiter optional delimeter in the input sequence
   * @return list of amino acid
   * @throws MonomerException
   * @throws NotationException
   * @throws IOException
   * @throws JDOMException
   * @throws MonomerLoadingException
   * @throws ChemistryException
   */
  public static List<String> getAminoAcidList(String peptideSequence,
      String delimiter) throws MonomerException, NotationException,
          IOException, JDOMException, MonomerLoadingException, ChemistryException {

    if (null == peptideSequence) {
      throw new NotationException("Peptide Sequence must be specified");
    }

    if (null == delimiter || delimiter.length() == 0) {
      return getAminoAcidList(peptideSequence);
    } else {
      if (!isValidDelimiter(delimiter))
        throw new NotationException("Invalid sequence delimiter ["
            + delimiter + "], only the following are supported: ["
            + getValidDelimiters() + "]");
    }

    List<String> blocks = new ArrayList<String>();
    StringTokenizer st = new StringTokenizer(peptideSequence, delimiter);
    while (st.hasMoreTokens()) {
      blocks.add(st.nextToken());
    }
    List<String> l = new ArrayList<String>();

    for (String block : blocks) {
      List<String> tmpL = getAminoAcidList(block);
      l.addAll(tmpL);
    }
    return l;
  }

  /**
   * remove white space, and convert all lower case to upper case
   *
   * @param sequence
   * @return cleaned sequence
   */
  public static String cleanup(String sequence) {
    String result = sequence.replaceAll("\\s", ""); // remove all white
    // space
    if (result.equals(result.toLowerCase())) {
      result = result.toUpperCase();
    }
    return result;
  }

  private static boolean isValidDelimiter(String delimeter) {
    for (String token : SEQUENCE_DELIMETERS) {
      if (token.equals(delimeter))
        return true;
    }
    return false;
  }

  private static String getValidDelimiters() {
    StringBuffer sb = new StringBuffer();
    for (String token : SEQUENCE_DELIMETERS) {
      if (sb.length() > 0)
        sb.append(" ");
      sb.append(token);
    }
    return sb.toString();
  }
}

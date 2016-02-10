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

import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroup;
import org.helm.notation2.parser.notation.polymer.MonomerNotationList;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;

/**
 * PolymerUtils, class to provide methods for polymer
 *
 * @author hecht
 */
public class PolymerUtils {

  /**
   * method to get the total monomer count of one PolymerNotation
   *
   * @param polymer PolymerNotation
   * @return monomer count
   */
  public static int getTotalMonomerCount(PolymerNotation polymer) {
    int count = 0;
    for (MonomerNotation element : polymer.getPolymerElements().getListOfElements()) {
      count += getMonomerCountFromMonomerNotation(element);
    }
    return count;
  }

  /**
   * method to get the number of all existing monomers from one MonomerNotation
   *
   * @param monomerNotation MonomerNotation
   * @return number of monomers in the given MonomerNotation
   */
  private static int getMonomerCountFromMonomerNotation(MonomerNotation monomerNotation) {
    int multiply;
    try {
      multiply = Integer.parseInt(monomerNotation.getCount());
      if (multiply < 1) {
        multiply = 1;
      }
    } catch (NumberFormatException e) {
      multiply = 1;
    }

    if (monomerNotation instanceof MonomerNotationGroup) {
      return 1 * multiply;
    }
    if (monomerNotation instanceof MonomerNotationList) {
      int count = 0;
      for (MonomerNotation unit : ((MonomerNotationList) monomerNotation).getListofMonomerUnits()) {
        count += getMonomerCountFromMonomerNotation(unit);
      }
      return count * multiply;
    }

    if (monomerNotation instanceof MonomerNotationUnitRNA) {
      int count = 0;
      for (MonomerNotationUnit unit : ((MonomerNotationUnitRNA) monomerNotation).getContents()) {
        count += getMonomerCountFromMonomerNotation(unit);
      }
      return count * multiply;
    }
    return 1 * multiply;
  }

}

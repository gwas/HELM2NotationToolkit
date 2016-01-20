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

import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerLoadingException;
import org.helm.notation.model.Monomer;
import org.testng.annotations.Test;

public class MonomerManagementActions {

  @Test
  public void testgetMolecularFormularExamples() throws MonomerLoadingException {
    MonomerFactory.getInstance();
    boolean flag = false;
    while (!flag) {
      try {
        Monomer monomer = MonomerFactory.getInstance().getMonomerStore().getMonomer("CHEM", "Az");
        if (monomer != null) {
          flag = true;
        }
      } catch (IOException e) {
        // TODO Auto-generated catch block
        System.out.println(e.getMessage());
      }
    }
    // Assert.assertEquals(result, "C16H20N4O4");
  }


}

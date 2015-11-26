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


import org.helm.notation2.parser.notation.HELM2Notation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ContainerHELM2
 * 
 * @author hecht
 */
public class ContainerHELM2 {


  /** The Logger for this class */
  private static final Logger LOG =
      LoggerFactory.getLogger(ContainerHELM2.class);

  private HELM2Notation helm2notation;

  private InterConnections interconnection;




  public ContainerHELM2(HELM2Notation helm2notation,
      InterConnections interconnection) {
    this.helm2notation = helm2notation;
    this.interconnection = interconnection;
  }

  public HELM2Notation getHELM2Notation() {
    return helm2notation;
  }

  public InterConnections getInterconnection() {
    return interconnection;
  }

  public void buildMolecule() {

  }

  public double getMolecularWeight() {
    /* First build one big molecule */
    System.out.println("BuildOneBigMolecule");
    buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");
    return 0;

  }

  public double getExaxtMass() {
    return 0;
  }

  public String getMolecularFormular() {
    /* First build one big molecule */
    System.out.println("BuildOneBigMolecule");
    buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");
    return null;
  }

}


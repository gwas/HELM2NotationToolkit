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
package org.helm.notation2.wsadapter;

/**
 * 
 * {@code CategorizedMonomer} used as wrapper class for monomer categorization in the HELMEditor. This means in which
 * category a monomer belongs to, or of which shape the menu entry is.
 * 
 * @author <a href="mailto:lanig@quattro-research.com">Marco Lanig</a>
 * @version $Id$
 */
public class CategorizedMonomer {
  private String monomerID;

  private String monomerName;

  private String naturalAnalogon;

  private String monomerType;

  private String polymerType;

  private String category;

  private String shape;

  private String fontColor;

  private String backgroundColor;

  /**
   * Default constructor.
   */
  public CategorizedMonomer() {
  }

  /**
   * Constructor using all possible fields.
   * 
   * @param monomerID the monomers ID.
   * @param monomerName the monomers name.
   * @param naturalAnalogon the natural analogon.
   * @param monomerType the monomer type.
   * @param polymerType the polymer type.
   * @param category the category.
   * @param shape the menu entries shape.
   * @param fontColor the font color on the menu entry.
   * @param backgroundColor the menu entries background color.
   */
  public CategorizedMonomer(String monomerID, String monomerName, String naturalAnalogon, String monomerType,
      String polymerType, String category, String shape, String fontColor, String backgroundColor) {
    super();
    this.monomerID = monomerID;
    this.monomerName = monomerName;
    this.naturalAnalogon = naturalAnalogon;
    this.monomerType = monomerType;
    this.polymerType = polymerType;
    this.category = category;
    this.shape = shape;
    this.fontColor = fontColor;
    this.backgroundColor = backgroundColor;
  }

  /**
   * Returns the monomer ID.
   * 
   * @return the monomer id
   */
  public String getMonomerID() {
    return monomerID;
  }

  /**
   * Sets the monomer ID.
   * 
   * @param monomerID
   */
  public void setMonomerID(String monomerID) {
    this.monomerID = monomerID;
  }

  /**
   * Returns the monomer name.
   * 
   * @return the monomer name
   */
  public String getMonomerName() {
    return monomerName;
  }

  /**
   * Sets the monomer name.
   * 
   * @param monomerName
   */
  public void setMonomerName(String monomerName) {
    this.monomerName = monomerName;
  }

  /**
   * Returns the natural analogon.
   * 
   * @return the natural analogon
   */
  public String getNaturalAnalogon() {
    return naturalAnalogon;
  }

  /**
   * Sets the natural analogon.
   * 
   * @param naturalAnalogon
   */
  public void setNaturalAnalogon(String naturalAnalogon) {
    this.naturalAnalogon = naturalAnalogon;
  }

  /**
   * Returns the monomer type.
   * 
   * @return the monomer type.
   */
  public String getMonomerType() {
    return monomerType;
  }

  /**
   * Sets the monomer type.
   * 
   * @param monomerType
   */
  public void setMonomerType(String monomerType) {
    this.monomerType = monomerType;
  }

  /**
   * Returns the polymer type.
   * 
   * @return the polymer type.
   */
  public String getPolymerType() {
    return polymerType;
  }

  /**
   * Sets the polymerType.
   * 
   * @param polymerType
   */
  public void setPolymerType(String polymerType) {
    this.polymerType = polymerType;
  }

  /**
   * Returns the category.
   * 
   * @return the category.
   */
  public String getCategory() {
    return category;
  }

  /**
   * Sets the category.
   * 
   * @param category
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * Returns the shape.
   * 
   * @return the shape.
   */
  public String getShape() {
    return shape;
  }

  /**
   * Sets the shape.
   * 
   * @param shape
   */
  public void setShape(String shape) {
    this.shape = shape;
  }

  /**
   * Returns the font color.
   * 
   * @return the font color.
   */
  public String getFontColor() {
    return fontColor;
  }

  /**
   * Sets the font color.
   * 
   * @param fontColor
   */
  public void setFontColor(String fontColor) {
    this.fontColor = fontColor;
  }

  /**
   * Returns the background color.
   * 
   * @return the background color.
   */
  public String getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * Sets the background color.
   * 
   * @param backgroundColor
   */
  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

}

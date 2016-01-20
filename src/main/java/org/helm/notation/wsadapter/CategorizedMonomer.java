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
package org.helm.notation.wsadapter;

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

public CategorizedMonomer() {
  
}

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
public String getMonomerID() {
  return monomerID;
}
public void setMonomerID(String monomerID) {
  this.monomerID = monomerID;
}
public String getMonomerName() {
  return monomerName;
}
public void setMonomerName(String monomerName) {
  this.monomerName = monomerName;
}
public String getNaturalAnalogon() {
  return naturalAnalogon;
}
public void setNaturalAnalogon(String naturalAnalogon) {
  this.naturalAnalogon = naturalAnalogon;
}
public String getMonomerType() {
  return monomerType;
}
public void setMonomerType(String monomerType) {
  this.monomerType = monomerType;
}
public String getPolymerType() {
  return polymerType;
}
public void setPolymerType(String polymerType) {
  this.polymerType = polymerType;
}
public String getCategory() {
  return category;
}
public void setCategory(String category) {
  this.category = category;
}
public String getShape() {
  return shape;
}
public void setShape(String shape) {
  this.shape = shape;
}
public String getFontColor() {
  return fontColor;
}
public void setFontColor(String fontColor) {
  this.fontColor = fontColor;
}
public String getBackgroundColor() {
  return backgroundColor;
}
public void setBackgroundColor(String backgroundColor) {
  this.backgroundColor = backgroundColor;
}


}

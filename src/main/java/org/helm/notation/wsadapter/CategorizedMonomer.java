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

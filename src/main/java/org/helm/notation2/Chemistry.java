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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.ManipulatorFactory;
import org.helm.chemtoolkit.ManipulatorFactory.ManipulatorType;

/**
 * Chemistry
 * 
 * @author hecht
 */
public class Chemistry {
  private static final String CONFIG_FILE_PATH = System.getProperty("user.home") + "/.helm/Chemistry.property";

  private static final String CHEMISTRY_PLUGIN="chemistry.plugin";

  private static Chemistry _instance;

  private static ManipulatorFactory.ManipulatorType type;

  private static String chemistry;

  private static AbstractChemistryManipulator manipulator;

  private Chemistry() {
    readConfigFile();
    setManipulatorType();
    try {
      manipulator = ManipulatorFactory.buildManipulator(type);
    } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   */
  private void setManipulatorType() {
    System.out.println(chemistry);
    if (chemistry.equals("MARVIN")) {
      type = ManipulatorFactory.ManipulatorType.MARVIN;
    }
 else if (chemistry.equals("CDK")) {
      type = ManipulatorFactory.ManipulatorType.CDK;
    }
 else {
      type = null;
    }
  }

  /**
   * 
   */
  private void readConfigFile() {
    File configFile = new File(CONFIG_FILE_PATH);
    /* config file is not there -> create config file with default */
    if (!configFile.exists()) {
      resetConfigToDefault();
    }

    try {
      PropertiesConfiguration conf = new PropertiesConfiguration(CONFIG_FILE_PATH);
      chemistry = conf.getString(CHEMISTRY_PLUGIN);

    } catch (ConfigurationException e) {
      resetConfigToDefault();
      e.printStackTrace();
    }
  }

  private void resetConfigToDefault(){
    chemistry = "MARVIN";
    try {
      PrintWriter writer = new PrintWriter(CONFIG_FILE_PATH, "UTF-8");
      writer.println(CHEMISTRY_PLUGIN + "=" + chemistry);
      writer.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static Chemistry getInstance() {
    if (Chemistry._instance == null) {
      Chemistry._instance = new Chemistry();
    }
    return Chemistry._instance;
  }

  public synchronized AbstractChemistryManipulator getManipulator() {
    return manipulator;
  }

  public ManipulatorType getManipulatorType() {
    return type;
  }

}

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.ManipulatorFactory;
import org.helm.chemtoolkit.ManipulatorFactory.ManipulatorType;
import org.helm.notation.MonomerFactory;
import org.helm.notation2.exception.ChemistryException;

import sun.misc.IOUtils;

/**
 * Chemistry, singleton class to define which Chemistry-Plugin is used
 *
 * @author hecht
 */
public final class Chemistry {
  private static final String CONFIG_FILE_PATH = System.getProperty("user.home") + "/.helm/Chemistry.property";

  private static final String CHEMISTRY_PLUGIN = "chemistry.plugin";

  private static Chemistry _instance;

  private static ManipulatorFactory.ManipulatorType type;

  private static String chemistry;

  private static AbstractChemistryManipulator manipulator;

  /**
   * Default constructor.
   *
   * @throws ChemistryException
   */
  private Chemistry() throws ChemistryException {
    refresh();
    readConfigFile();
    setManipulatorType();
    try {
      manipulator = ManipulatorFactory.buildManipulator(type);
    } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
      throw new ChemistryException("Chemistry Engine could not be initialized");
    }
  }

  /**
   * method to define the ManipulatorType default type is CDK
   */
  private void setManipulatorType() {
    if (chemistry.equals("MARVIN")) {
      type = ManipulatorFactory.ManipulatorType.MARVIN;
    } else if (chemistry.equals("CDK")) {
      type = ManipulatorFactory.ManipulatorType.CDK;
    } else {
      type = ManipulatorFactory.ManipulatorType.CDK;
    }
  }

  /**
   * method to read the configuration file
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

  /**
   * method to set the chemistry-plugin to the default one (MARVIN)
   */
  private void resetConfigToDefault() {
    chemistry = "MARVIN";
  }

  /**
   * method to get the singleton instance
   *
   * @return Chemistry
   * @throws ChemistryException
   */
  public static Chemistry getInstance() throws ChemistryException {
    if (_instance == null) {
      _instance = new Chemistry();
    }
    return _instance;
  }

  /**
   * method to get the Manipulator
   *
   * @return AbstractChemistryManipulator
   */
  public synchronized AbstractChemistryManipulator getManipulator() {
    return manipulator;
  }

  /**
   * method to get the ManipulatorType
   *
   * @return ManipulatorType
   */
  public ManipulatorType getManipulatorType() {
    return type;
  }

  public void refresh() {
    File configFile = new File(CONFIG_FILE_PATH);
    if (!configFile.exists()) {
      InputStream in = Chemistry.class.getResourceAsStream("/org/helm/notation/resources/Chemistry.property");
      try (FileOutputStream str = new FileOutputStream(CONFIG_FILE_PATH)) {
        byte[] bytes = IOUtils.readFully(in, -1, true);
        str.write(bytes);
      } catch (FileNotFoundException e) {
        resetConfigToDefault();
        e.printStackTrace();
      } catch (IOException e) {
        resetConfigToDefault();
        e.printStackTrace();
      }
    }

    try {
      PropertiesConfiguration conf = new PropertiesConfiguration(
          CONFIG_FILE_PATH);
      chemistry = conf.getString(CHEMISTRY_PLUGIN);

    } catch (ConfigurationException | NoSuchElementException e) {
      resetConfigToDefault();
      e.printStackTrace();
    }
  }

}

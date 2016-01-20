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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.helm.notation.MonomerFactory;

import sun.misc.IOUtils;

public class MonomerStoreConfiguration {
	private static final String CONFIG_FILE_PATH = System
			.getProperty("user.home") + "/.helm/MonomerStoreConfig.properties";

	private static final String USE_WEBSERVICE = "use.webservice";
  private static final String UPDATE_AUTOMATIC = "update.automatic";
	private static final String WEBSERVICE_MONOMERS_URL = "webservice.monomers.url";
	private static final String WEBSERVICE_MONOMERS_PATH = "webservice.monomers.path";
	private static final String WEBSERVICE_MONOMERS_PUT_PATH = "webservice.monomers.put.path";
	private static final String WEBSERVICE_EDITOR_CATEGORIZATION_URL = "webservice.editor.categorization.url";
	private static final String WEBSERVICE_EDITOR_CATEGORIZATION_PATH = "webservice.editor.categorization.path";
	private static final String WEBSERVICE_NUCLEOTIDES_URL = "webservice.nucleotides.url";
	private static final String WEBSERVICE_NUCLEOTIDES_PATH = "webservice.nucleotides.path";
	private static final String WEBSERVICE_NUCLEOTIDES_PUT_PATH = "webservice.nucleotides.put.path";

	private static MonomerStoreConfiguration _instance;

	private boolean isUseWebservice;
  private boolean isUpdateAutomatic;
	private String webserviceMonomersURL;
	private String webserviceMonomersPath;
	private String webserviceMonomersPutPath;
	private String webserviceNucleotidesURL;
	private String webserviceNucleotidesPath;
	private String webserviceNucleotidesPutPath;
	private String webserviceEditorCategorizationURL;
	private String webserviceEditorCategorizationPath;

	private MonomerStoreConfiguration() {
		refresh();
	}

	private void resetConfigToDefault() {
		isUseWebservice = false;
    isUpdateAutomatic = true;
		webserviceMonomersURL = "";
		webserviceMonomersPath = "";
		webserviceMonomersPutPath = "";
		webserviceEditorCategorizationURL = "";
		webserviceEditorCategorizationPath = "";
	}

	public static MonomerStoreConfiguration getInstance() {
		if (_instance == null) {
			_instance = new MonomerStoreConfiguration();
		}
		return _instance;
	}

	public boolean isUseWebservice() {
		return isUseWebservice;
	}

  public boolean isUpdateAutomatic() {
    return isUpdateAutomatic;
  }

	public String getWebserviceMonomersURL() {
		return webserviceMonomersURL;
	}

	public String getWebserviceMonomersPath() {
		return webserviceMonomersPath;
	}

	public String getWebserviceMonomersFullURL() {
		return webserviceMonomersURL + "/" + webserviceMonomersPath;
	}

	public String getWebserviceNucleotidesURL() {
		return webserviceNucleotidesURL;
	}

	public String getWebserviceNucleotidesPath() {
		return webserviceNucleotidesPath;
	}

	public String getWebserviceNucleotidesFullURL() {
		return webserviceNucleotidesURL + "/" + webserviceNucleotidesPath;
	}
	
	public String getWebserviceNucleotidesPutFullURL() {
		return webserviceNucleotidesURL + "/" + webserviceNucleotidesPutPath;
	}

	public String getWebserviceMonomersPutFullURL() {
		return webserviceMonomersURL + "/" + webserviceMonomersPutPath;
	}

	public String getWebserviceEditorCategorizationURL() {
		return webserviceEditorCategorizationURL;
	}

	public String getWebserviceEditorCategorizationPath() {
		return webserviceEditorCategorizationPath;
	}

	public String getWebserviceEditorCategorizationFullURL() {
		return webserviceEditorCategorizationURL + "/"
				+ webserviceEditorCategorizationPath;
	}

  public void setUpdateAutomatic(boolean isUpdateAutomatic) {
    this.isUpdateAutomatic = isUpdateAutomatic;
  }

	public void refresh() {
		File configFile = new File(CONFIG_FILE_PATH);
		if (!configFile.exists()) {
			InputStream in = MonomerFactory.class
					.getResourceAsStream("/org/helm/notation/resources/MonomerStoreConfig.properties");
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
			isUseWebservice = conf.getBoolean(USE_WEBSERVICE);
      isUpdateAutomatic = conf.getBoolean(UPDATE_AUTOMATIC);
			webserviceMonomersURL = conf.getString(WEBSERVICE_MONOMERS_URL);
			webserviceMonomersPath = conf.getString(WEBSERVICE_MONOMERS_PATH);
			webserviceMonomersPutPath = conf
					.getString(WEBSERVICE_MONOMERS_PUT_PATH);
			webserviceNucleotidesURL = conf
					.getString(WEBSERVICE_NUCLEOTIDES_URL);
			webserviceNucleotidesPath = conf
					.getString(WEBSERVICE_NUCLEOTIDES_PATH);
			webserviceNucleotidesPutPath = conf
					.getString(WEBSERVICE_NUCLEOTIDES_PUT_PATH);
			webserviceEditorCategorizationURL = conf
					.getString(WEBSERVICE_EDITOR_CATEGORIZATION_URL);
			webserviceEditorCategorizationPath = conf
					.getString(WEBSERVICE_EDITOR_CATEGORIZATION_PATH);

    } catch (ConfigurationException | NoSuchElementException e) {
			resetConfigToDefault();
			e.printStackTrace();
		}
	}

	public String toString() {
		String lineSep = System.getProperty("line.separator");
		String res = String
.format("Webservice configuration:%sUse Webservice: %s%sUpdate Automatic: %s%sGetMonomers: %s%sPutMonomers: %s%sGetNucleotides: %s%sPutNucleotides: %s%sCategorization config: %s",
						lineSep, isUseWebservice(), lineSep,
 isUpdateAutomatic(), lineSep,
						getWebserviceMonomersFullURL(), lineSep,
						getWebserviceMonomersPutFullURL(), lineSep,
						getWebserviceNucleotidesFullURL(), lineSep,
						getWebserviceNucleotidesPutFullURL(), lineSep,
						getWebserviceEditorCategorizationFullURL());

		return res;
	}
}

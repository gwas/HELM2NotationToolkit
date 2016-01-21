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
package org.helm.notation.wsadapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
// import org.apache.http.impl.client.WinHttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

/**
 * 
 * {@code NucleotideWSLoader} loads nucleotides from the webservice configured in {@code MonomerStoreConfiguration}.
 * 
 * @author <a href="mailto:lanig@quattro-research.com">Marco Lanig</a>
 * @version $Id$
 */
public class NucleotideWSLoader {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(NucleotideWSLoader.class);

  /**
   * Default constructor.
   * 
   * @throws IOException
   */
  public NucleotideWSLoader() throws IOException {
  }

  /**
   * Loads the nucleotide store using the URL configured in {@code MonomerStoreConfiguration}.
   * 
   * @return Map containing nucleotides
   * 
   * @throws IOException
   * @throws URISyntaxException
   */
  public Map<String, String> loadNucleotideStore() throws IOException,
      URISyntaxException {

    Map<String, String> nucleotides = new HashMap<String, String>();
    LOG.debug("Loading nucleotide store by Webservice Loader");
    LOG.debug(MonomerStoreConfiguration.getInstance().toString());
    CloseableHttpResponse response = null;
    try {
      response =
          WSAdapterUtils.getResource(MonomerStoreConfiguration.getInstance().getWebserviceNucleotidesFullURL());
      LOG.debug(response.getStatusLine().toString());

      JsonFactory jsonf = new JsonFactory();
      InputStream instream = response.getEntity().getContent();

      JsonParser jsonParser = jsonf.createJsonParser(instream);
      nucleotides = deserializeNucleotideStore(jsonParser);
      LOG.debug(nucleotides.size() + " nucleotides loaded");

      EntityUtils.consume(response.getEntity());

    } catch (ClientProtocolException e) {

      /* read file */
      JsonFactory jsonf = new JsonFactory();
      InputStream instream =
          new FileInputStream(new File(MonomerStoreConfiguration.getInstance().getWebserviceNucleotidesFullURL()));

      JsonParser jsonParser = jsonf.createJsonParser(instream);
      nucleotides = deserializeNucleotideStore(jsonParser);
      LOG.debug(nucleotides.size() + " nucleotides loaded");

    } finally {

      if (response != null) {
        response.close();
      }
    }

    return nucleotides;
  }

  /**
   * Private routine to deserialize nucleotide Store JSON. This is done manually to give more freedom regarding data
   * returned by the webservice.
   * 
   * @param parser the JSONParser containing JSONData.
   * @return Map containing nucleotides
   * 
   * @throws JsonParseException
   * @throws IOException
   */
  private Map<String, String> deserializeNucleotideStore(JsonParser parser)
      throws JsonParseException, IOException {
    Map<String, String> nucleotides = new HashMap<String, String>();
    String currentNucleotideSymbol = "";
    String currentNucleotideNotation = "";
    boolean foundSymbol = false;
    boolean foundNotation = false;

    parser.nextToken();
    while (parser.hasCurrentToken()) {
      String fieldName = parser.getCurrentName();

      if (fieldName != null) {
        switch (fieldName) {
        case "symbol":
          parser.nextToken();
          currentNucleotideSymbol = parser.getText();
          foundSymbol = true;
          break;
        case "notation":
          parser.nextToken();
          currentNucleotideNotation = parser.getText();
          foundNotation = true;
          break;
        default:
          break;
        }

        if (foundSymbol && foundNotation) {
          nucleotides.put(currentNucleotideSymbol,
              currentNucleotideNotation);
          foundNotation = false;
          foundSymbol = false;
        }
      }
      parser.nextToken();
    }

    return nucleotides;
  }

}

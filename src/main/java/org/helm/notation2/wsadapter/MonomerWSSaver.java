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

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.helm.notation2.Monomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * 
 * {@code MonomerWSSaver} saves monomers to the webservice configured in {@code MonomerStoreConfiguration}.
 * 
 * @author <a href="mailto:lanig@quattro-research.com">Marco Lanig</a>
 * @version $Id$
 */
public class MonomerWSSaver {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(MonomerWSSaver.class);

  /**
   * Adds or updates a single monomer to the monomer store using the URL configured in {@code MonomerStoreConfiguration}
   * .
   * 
   * @param monomer to save
   */
  public String saveMonomerToStore(Monomer monomer) {
    String res = "";
    CloseableHttpResponse response = null;

    try {
      response = WSAdapterUtils.putResource(monomer.toJSON(),
          MonomerStoreConfiguration.getInstance()
              .getWebserviceNucleotidesPutFullURL());
      LOG.debug(response.getStatusLine().toString());

      JsonFactory jsonf = new JsonFactory();
      InputStream instream = response.getEntity().getContent();

      JsonParser jsonParser = jsonf.createParser(instream);

      while (!jsonParser.isClosed()) {
        JsonToken jsonToken = jsonParser.nextToken();
        if (JsonToken.FIELD_NAME.equals(jsonToken)) {
          String fieldName = jsonParser.getCurrentName();
          LOG.debug("Field name: " + fieldName);
          jsonParser.nextToken();
          if (fieldName.equals("monomerShortName")) {
            res = jsonParser.getValueAsString();
            break;
          }
        }
      }

      EntityUtils.consume(response.getEntity());

    } catch (Exception e) {
      LOG.error("Saving monomer failed!", e);
      return "";
    } finally {
      try {
        if (response != null) {
          response.close();
        }
      } catch (IOException e) {
        LOG.debug("Closing resources failed.", e);
        return res;
      }
    }

    return res;
  }
}

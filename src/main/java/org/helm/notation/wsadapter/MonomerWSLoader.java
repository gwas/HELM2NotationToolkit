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

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
// import org.apache.http.impl.client.WinHttpClients;
import org.apache.http.util.EntityUtils;
import org.helm.notation.model.Attachment;
import org.helm.notation.model.Monomer;

import b64.Base64;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class MonomerWSLoader {

	// TODO do this in config file
	private final String[] knownPolymerTypes = new String[] { "PEPTIDE", "RNA",
			"CHEM" };

	private String polymerType;

	public MonomerWSLoader(String polymerType) throws IOException {
		if (!Arrays.asList(knownPolymerTypes).contains(polymerType)) {
			throw new IOException("Unknown polymerType '" + polymerType
					+ "'. Supported types are " + knownPolymerTypes);
		}
		this.polymerType = polymerType;
	}

	public Map<String, Monomer> loadMonomerStore(
			Map<String, Attachment> attachmentDB) throws IOException,
			URISyntaxException {
		Map<String, Monomer> monomers = new HashMap<String, Monomer>();

// if (!WinHttpClients.isWinAuthAvailable()) {
// System.out.println("Integrated Win auth is not supported!!!");
// }

    CloseableHttpClient httpclient = HttpClients.createDefault();
		// There is no need to provide user credentials
		// HttpClient will attempt to access current user security context
		// through Windows platform specific methods via JNI.
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(
					new URIBuilder(MonomerStoreConfiguration.getInstance()
							.getWebserviceMonomersFullURL() + polymerType)
							.build());

			System.out.println("Executing request " + httpget.getRequestLine());
			response = httpclient.execute(httpget);
			System.out.println(response.getStatusLine());

			JsonFactory jsonf = new JsonFactory();
			InputStream instream = response.getEntity().getContent();

			JsonParser jsonParser = jsonf.createJsonParser(instream);
			monomers = deserializeMonomerStore(jsonParser, attachmentDB);
			System.out.println(monomers.size() + " " + polymerType
					+ " monomers loaded");

			EntityUtils.consume(response.getEntity());

		} finally {
			if (response != null) {
				response.close();
			}
			if (httpclient != null) {
				httpclient.close();
			}
		}

		return monomers;
	}

	public static List<CategorizedMonomer> loadMonomerCategorization()
			throws IOException, URISyntaxException {
		List<CategorizedMonomer> config = new LinkedList<CategorizedMonomer>();

// if (!WinHttpClients.isWinAuthAvailable()) {
// System.out.println("Integrated Win auth is not supported!!!");
// }

    CloseableHttpClient httpclient = HttpClients.createDefault();
		// There is no need to provide user credentials
		// HttpClient will attempt to access current user security context
		// through Windows platform specific methods via JNI.
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(
					new URIBuilder(MonomerStoreConfiguration.getInstance()
							.getWebserviceEditorCategorizationFullURL())
							.build());

			System.out.println("Executing request " + httpget.getRequestLine());
			response = httpclient.execute(httpget);
			System.out.println(response.getStatusLine());

			JsonFactory jsonf = new JsonFactory();
			InputStream instream = response.getEntity().getContent();

			JsonParser jsonParser = jsonf.createJsonParser(instream);
			config = deserializeEditorCategorizationConfig(jsonParser);
			System.out.println(config.size()
					+ " categorization info entries loaded");

			EntityUtils.consume(response.getEntity());

		} finally {
			if (response != null) {
				response.close();
			}
			if (httpclient != null) {
				httpclient.close();
			}
		}

		return config;
	}

	private Map<String, Monomer> deserializeMonomerStore(JsonParser parser,
			Map<String, Attachment> attachmentDB) throws JsonParseException,
			IOException {
		Map<String, Monomer> monomers = new HashMap<String, Monomer>();
		Monomer currentMonomer = null;

		parser.nextToken();
		while (parser.hasCurrentToken()) {
			String fieldName = parser.getCurrentName();
			JsonToken token = parser.getCurrentToken();

			if (JsonToken.START_OBJECT.equals(token)) {
				currentMonomer = new Monomer();
			} else if (JsonToken.END_OBJECT.equals(token)) {
				monomers.put(currentMonomer.getAlternateId(), currentMonomer);
			}

			if (fieldName != null) {
				switch (fieldName) {
				// id is first field
				case "id":
					parser.nextToken();
					currentMonomer.setId(Integer.parseInt(parser.getText()));
					break;
				case "alternateId":
					parser.nextToken();
					currentMonomer.setAlternateId(parser.getText());
					break;
				case "naturalAnalog":
					parser.nextToken();
					currentMonomer.setNaturalAnalog(parser.getText());
					break;
				case "name":
					parser.nextToken();
					currentMonomer.setName(parser.getText());
					break;
				case "canSMILES":
					parser.nextToken();
					currentMonomer.setCanSMILES(parser.getText());
					break;
				case "molfile":
					parser.nextToken();
					currentMonomer.setMolfile(Base64.decodeToString(parser
							.getText()));
					break;
				case "monomerType":
					parser.nextToken();
					currentMonomer.setMonomerType(parser.getText());
					break;
				case "polymerType":
					parser.nextToken();
					currentMonomer.setPolymerType(parser.getText());
					break;
				case "attachmentList":
					currentMonomer.setAttachmentList(deserializeAttachmentList(
							parser, attachmentDB));
					break;
				case "newMonomer":
					parser.nextToken();
					currentMonomer.setNewMonomer(Boolean.parseBoolean(parser
							.getText()));
					break;
				case "adHocMonomer":
					parser.nextToken();
					currentMonomer.setAdHocMonomer(Boolean.parseBoolean(parser
							.getText()));
					break;
				default:
					break;
				}
			}
			parser.nextToken();
		}

		return monomers;
	}

	private List<Attachment> deserializeAttachmentList(JsonParser parser,
			Map<String, Attachment> attachmentDB) throws JsonParseException,
			IOException {
		List<Attachment> attachments = new ArrayList<Attachment>();
		Attachment currentAttachment = null;

		while (!JsonToken.END_ARRAY.equals(parser.nextToken())) {

			String fieldName = parser.getCurrentName();
			JsonToken token = parser.getCurrentToken();

			if (JsonToken.START_OBJECT.equals(token)) {
				currentAttachment = new Attachment();
			} else if (JsonToken.END_OBJECT.equals(token)) {
				currentAttachment
						.setCapGroupSMILES(attachmentDB.get(
								currentAttachment.getAlternateId())
								.getCapGroupSMILES());
				attachments.add(currentAttachment);
			}

			if (fieldName != null) {
				switch (fieldName) {
				case "id":
					parser.nextToken();
					currentAttachment.setId(Integer.parseInt(parser.getText()));
					break;
				case "alternateId":
					parser.nextToken();
					currentAttachment.setAlternateId(parser.getText());
					break;
				case "label":
					parser.nextToken();
					currentAttachment.setLabel(parser.getText());
					break;
				case "capGroupName":
					parser.nextToken();
					currentAttachment.setCapGroupName(parser.getText());
					break;
				case "capGroupSMILES":
					parser.nextToken();
					currentAttachment.setCapGroupSMILES(parser.getText());
					break;
				default:
					break;
				}
			}

		}

		return attachments;
	}

	private static List<CategorizedMonomer> deserializeEditorCategorizationConfig(
			JsonParser parser) throws JsonParseException, IOException {
		List<CategorizedMonomer> config = new LinkedList<CategorizedMonomer>();
		CategorizedMonomer currentMonomer = null;

		parser.nextToken();
		while (parser.hasCurrentToken()) {
			String fieldName = parser.getCurrentName();
			JsonToken token = parser.getCurrentToken();

			if (JsonToken.START_OBJECT.equals(token)) {
				currentMonomer = new CategorizedMonomer();
			} else if (JsonToken.END_OBJECT.equals(token)) {
				config.add(currentMonomer);
			}

			if (fieldName != null) {
				switch (fieldName) {
				// id is first field
				case "monomerID":
					parser.nextToken();
					currentMonomer.setMonomerID(parser.getText());
					break;
				case "monomerName":
					parser.nextToken();
					currentMonomer.setMonomerName(parser.getText());
					break;
				case "naturalAnalogon":
					parser.nextToken();
					currentMonomer.setNaturalAnalogon(parser.getText());
					break;
				case "monomerType":
					parser.nextToken();
					currentMonomer.setMonomerType(parser.getText());
					break;
				case "polymerType":
					parser.nextToken();
					currentMonomer.setPolymerType(parser.getText());
					break;
				case "category":
					parser.nextToken();
					currentMonomer.setCategory(parser.getText());
					break;
				case "shape":
					parser.nextToken();
					currentMonomer.setShape(parser.getText());
					break;
				case "fontColor":
					parser.nextToken();
					currentMonomer.setFontColor(parser.getText());
					break;
				case "backgroundColor":
					parser.nextToken();
					currentMonomer.setBackgroundColor(parser.getText());
					break;
				default:
					break;
				}
			}
			parser.nextToken();
		}

		return config;
	}

}

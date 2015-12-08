package org.helm.notation.wsadapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
// import org.apache.http.impl.client.WinHttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

public class NucleotideWSLoader {

	public NucleotideWSLoader() throws IOException {
	}

	public Map<String, String> loadNucleotideStore() throws IOException,
			URISyntaxException {

		Map<String, String> nucleotides = new HashMap<String, String>();
		System.out.println("Loading nucleotide store by Webservice Loader");
		System.out.println(MonomerStoreConfiguration.getInstance().toString());

    CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
    URI uri = new URIBuilder(MonomerStoreConfiguration.getInstance().getWebserviceNucleotidesFullURL()).build();

		try {
      /* read url */
      HttpGet httpget = new HttpGet(uri);
			System.out.println("Executing request " + httpget.getRequestLine());
			response = httpclient.execute(httpget);
			System.out.println(response.getStatusLine());

			JsonFactory jsonf = new JsonFactory();
			InputStream instream = response.getEntity().getContent();

			JsonParser jsonParser = jsonf.createJsonParser(instream);
			nucleotides = deserializeNucleotideStore(jsonParser);
			System.out.println(nucleotides.size() + " nucleotides loaded");

			EntityUtils.consume(response.getEntity());

    } catch (ClientProtocolException e) {

      /* read file */
      JsonFactory jsonf = new JsonFactory();
      InputStream instream = new FileInputStream(new File(uri));

      JsonParser jsonParser = jsonf.createJsonParser(instream);
      nucleotides = deserializeNucleotideStore(jsonParser);
      System.out.println(nucleotides.size() + " nucleotides loaded");


    } finally {

			if (response != null) {
				response.close();
			}
			if (httpclient != null) {
				httpclient.close();
			}
		}

		return nucleotides;
	}

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

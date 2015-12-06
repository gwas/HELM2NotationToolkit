package org.helm.notation.wsadapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
// import org.apache.http.impl.client.WinHttpClients;
import org.apache.http.util.EntityUtils;
import org.helm.notation.model.Monomer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class MonomerWSSaver {

	public String saveMonomerToStore(Monomer monomer) {
		String res = "";
    CloseableHttpClient httpclient = HttpClients.createDefault();
		// There is no need to provide user credentials
		// HttpClient will attempt to access current user security context
		// through Windows platform specific methods via JNI.
		CloseableHttpResponse response = null;
		try {
			HttpPut httpput = new HttpPut(new URIBuilder(
					MonomerStoreConfiguration.getInstance()
							.getWebserviceMonomersPutFullURL()).build());
			String monomerJSON = monomer.toJSON();
			httpput.setHeader("Content-Type", "application/json;charset=UTF-8");
			httpput.setEntity(new StringEntity(monomerJSON, "UTF-8"));

			System.out.println("Executing request " + httpput.getRequestLine());
			response = httpclient.execute(httpput);
			System.out.println(response.getStatusLine());

			JsonFactory jsonf = new JsonFactory();
			InputStream instream = response.getEntity().getContent();

			JsonParser jsonParser = jsonf.createParser(instream);

			while (!jsonParser.isClosed()) {
				JsonToken jsonToken = jsonParser.nextToken();
				if (JsonToken.FIELD_NAME.equals(jsonToken)) {
					String fieldName = jsonParser.getCurrentName();
					System.out.println("Field name: " + fieldName);
					jsonParser.nextToken();
					if (fieldName.equals("monomerShortName")) {
						res = jsonParser.getValueAsString();
						break;
					}
				}
			}

			EntityUtils.consume(response.getEntity());

		} catch (URISyntaxException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return res;
			}
		}

		return res;
	}
}

package org.helm.notation.wsadapter;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
// import org.apache.http.impl.client.WinHttpClients;
import org.apache.http.util.EntityUtils;
import org.helm.notation.model.Nucleotide;

public class NucleotideWSSaver {

	public String saveNucleotideToStore(Nucleotide nucleotide) {
		String res = "";
    CloseableHttpClient httpclient = HttpClients.createDefault();
		// There is no need to provide user credentials
		// HttpClient will attempt to access current user security context
		// through Windows platform specific methods via JNI.
		CloseableHttpResponse response = null;
		try {
			HttpPut httpput = new HttpPut(new URIBuilder(
					MonomerStoreConfiguration.getInstance()
							.getWebserviceNucleotidesPutFullURL()).build());
			String nucleotideJSON = nucleotide.toJSON();
			httpput.setHeader("Content-Type", "application/json;charset=UTF-8");
			httpput.setEntity(new StringEntity(nucleotideJSON, "UTF-8"));

			System.out.println("Executing request " + httpput.getRequestLine());
			response = httpclient.execute(httpput);
			System.out.println(response.getStatusLine());

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

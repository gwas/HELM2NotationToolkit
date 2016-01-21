package org.helm.notation.wsadapter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * {@code WSAdapterUtils} is used to generalize webservice calls.
 * 
 * @author <a href="mailto:lanig@quattro-research.com">Marco Lanig</a>
 * @version $Id$
 */
public class WSAdapterUtils {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(WSAdapterUtils.class);

  /**
   * Default constructor is private, because Utility class needs none.
   */
  private WSAdapterUtils() {
  }

  /**
   * Calls a PUT routine with given JSON on given resource URL.
   * 
   * @param json the input JSON
   * @param fullURL the resource URL
   * @return Response
   * @throws ClientProtocolException
   * @throws IOException
   * @throws URISyntaxException
   */
  protected static CloseableHttpResponse putResource(String json, String fullURL) throws ClientProtocolException,
      IOException, URISyntaxException {
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      // There is no need to provide user credentials
      // HttpClient will attempt to access current user security context
      // through Windows platform specific methods via JNI.
      HttpPut httpput = new HttpPut(new URIBuilder(fullURL).build());
      httpput.setHeader("Content-Type", "application/json;charset=UTF-8");
      httpput.setEntity(new StringEntity(json, "UTF-8"));

      LOG.debug("Executing request " + httpput.getRequestLine());
      return httpclient.execute(httpput);
    }

  }

  /**
   * Call a GET routine on given resource URL.
   * 
   * @param fullURL the resource URL
   * @return Response
   * @throws IOException
   * @throws URISyntaxException
   */
  protected static CloseableHttpResponse getResource(String fullURL) throws IOException,
      URISyntaxException {
    URI uri = new URIBuilder(fullURL).build();

    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      /* read url */
      HttpGet httpget = new HttpGet(uri);
      LOG.debug("Executing request " + httpget.getRequestLine());
      return httpclient.execute(httpget);

    }

  }

}

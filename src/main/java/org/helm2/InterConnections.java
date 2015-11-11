
package org.helm2;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InterConnections
 * 
 * @author hecht
 */
public class InterConnections {

  /** The Logger for this class */
  private static final Logger LOG =
      LoggerFactory.getLogger(InterConnections.class);

  HashMap<String, String> mapInterConnections = new HashMap<String, String>();

  public InterConnections(){  
  }

  public InterConnections(HashMap<String, String> map) {
    mapInterConnections = map;
  }
  
  
  public void addConnection(String key, String value) {
    mapInterConnections.put(key, value);
  }

  public void deleteConnection(String key) {
    if (mapInterConnections.containsKey(key)) {
      mapInterConnections.remove(key);
    }
  }

  public HashMap<String, String> getInterConnections() {
    return mapInterConnections;
  }

  public boolean hasKey(String key) {
    return mapInterConnections.containsKey(key);
  }

  
  
}

package edu.brown.cs.student.main.server.handlers;

import java.util.Map;
import spark.Request;

public class OriginVerifier {

  public static boolean isAccessAllowed(Request request, Map<String, Object> responseMap) {
    String origin = request.headers("Origin");
    if (origin == null || !origin.equals("http://localhost:8000")) {
      responseMap.put("access denied", origin + " is an invalid origin");
      return false;
    }
    return true;
  }
}

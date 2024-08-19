package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ApiInfoHandler implements Route {

  public StorageInterface storageHandler;

  public ApiInfoHandler(StorageInterface storageHandler) {
    this.storageHandler = storageHandler;
  }

  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    if (!OriginVerifier.isAccessAllowed(request, responseMap)) {
      return responseMap;
    }

    try {
      String uid = request.queryParams("uid");

      List<Map<String, Object>> apiInfo = this.storageHandler.getCollection(uid, "apiInfo");

      for (Map<String, Object> map : apiInfo) {
        for (String key : map.keySet()) {
          responseMap.put(key, map.get(key));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("response_type", "failure");
      responseMap.put("error", e.getMessage());
    }

    return Utils.toMoshiJson(responseMap);
  }
}

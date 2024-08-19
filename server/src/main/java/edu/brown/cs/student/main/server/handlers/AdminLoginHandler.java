package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class AdminLoginHandler implements Route {

  public StorageInterface storageHandler;

  public AdminLoginHandler(StorageInterface storageHandler) {
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

      System.out.println("Retrieving username for admin");
      List<Map<String, Object>> loginInfo = this.storageHandler.getCollection(uid, "loginInfo");

      String password = loginInfo.get(0).get("password").toString();
      String username = loginInfo.get(1).get("username").toString();

      responseMap.put("response_type", "success");
      responseMap.put("username", username);
      responseMap.put("password", password);
    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("response_type", "failure");
      responseMap.put("error", e.getMessage());
    }

    return Utils.toMoshiJson(responseMap);
  }
}

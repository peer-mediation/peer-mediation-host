package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.*;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetCookiesHandler implements Route {

  public StorageInterface storageHandler;

  public GetCookiesHandler(StorageInterface storageHandler) {
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

      if (uid == null) {
        responseMap.put("failure", "uid must be specified");
        return responseMap;
      }

      List<Map<String, Object>> vals = this.storageHandler.getCollection(uid, "cookies");

      List<String> cookies = vals.stream().map(word -> word.get("cookie").toString()).toList();
      List<String> finalCookies = new ArrayList<>();
      Set<String> cookiesSet = new HashSet<>();
      List<Boolean> hasReplied = new ArrayList<>();

      if (cookies.size() == 0) {
        responseMap.put("response_type", "success");
        responseMap.put("cookies", cookies);
        return responseMap;
      }

      for (int i = cookies.size() - 1; i > -1; i--) {
        String currCookie = cookies.get(i);
        String splitCookie = currCookie.split("@")[0];
        if (!cookiesSet.contains(splitCookie)) {
          cookiesSet.add(splitCookie);
          finalCookies.add(currCookie);
          List<Map<String, Object>> rawMessages =
              this.storageHandler.getCollection(splitCookie, "words");
          int numMessages = rawMessages.size();
          String lastMessage = rawMessages.get(numMessages - 1).get("word").toString();
          System.out.println(lastMessage);
          if (lastMessage.startsWith("Peer Mediator:")) {
            hasReplied.add(true);
          } else {
            hasReplied.add(false);
          }
        }
      }

      System.out.println(finalCookies);

      responseMap.put("response_type", "success");
      responseMap.put("cookies", finalCookies);
      responseMap.put("hasReplied", hasReplied);
    } catch (Exception e) {
      // error likely occurred in the storage handler
      e.printStackTrace();
      responseMap.put("response_type", "failure");
      responseMap.put("error", e.getMessage());
    }

    return Utils.toMoshiJson(responseMap);
  }
}

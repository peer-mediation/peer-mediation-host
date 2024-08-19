package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class AddCookieHandler implements Route {

  public StorageInterface storageHandler;

  public AddCookieHandler(StorageInterface storageHandler) {
    this.storageHandler = storageHandler;
  }

  /**
   * Invoked when a request is made on this route's corresponding path e.g. '/hello'
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   * @return The content to be set in the response
   */
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    if (!OriginVerifier.isAccessAllowed(request, responseMap)) {
      return responseMap;
    }

    try {
      // collect parameters from the request
      String uid = "cookies";
      String cookie = request.queryParams("cookie");
      ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York"));

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
      String formattedDateTime = now.format(formatter);

      Integer hour = Integer.parseInt(formattedDateTime.split(" ")[1].split(":")[0]);
      String amPm;

      if (hour > 12) {
        hour -= 12;
        amPm = "PM";
      } else if (hour == 12) {
        amPm = "PM";
      } else if (hour == 0) {
        hour = 12;
        amPm = "AM";
      } else {
        amPm = "AM";
      }

      String hourString = hour.toString();

      String[] splitDate = formattedDateTime.split(" ");

      formattedDateTime =
          splitDate[0] + " " + hourString + ":" + splitDate[1].split(":")[1] + " " + amPm;

      Map<String, Object> data = new HashMap<>();
      data.put("cookie", cookie + "@" + formattedDateTime);

      System.out.println("adding cookie: " + cookie + " for user: " + uid);

      List<Map<String, Object>> cookieCollection =
          this.storageHandler.getCollection(uid, "cookies");

      int cookieCount = cookieCollection.size() + 1000000;
      String cookieId = "cookie-" + cookieCount;

      // use the storage handler to add the document to the database
      this.storageHandler.addDocument(uid, "cookies", cookieId, data);

      responseMap.put("response_type", "success");
      responseMap.put("word", cookie);
    } catch (Exception e) {
      // error likely occurred in the storage handler
      e.printStackTrace();
      responseMap.put("response_type", "failure");
      responseMap.put("error", e.getMessage());
    }

    return Utils.toMoshiJson(responseMap);
  }
}

package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.server.handlers.*;
import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.io.IOException;
import spark.Filter;
import spark.Spark;

public class Server {

  public static void setUpServer() {
    int port = 8080;
    Spark.port(port);

    Spark.after(
        (Filter)
            (request, response) -> {
              response.header("Access-Control-Allow-Origin", "http://localhost:8000");
              response.header("Access-Control-Allow-Methods", "*");
            });

    StorageInterface firebaseUtils;
    try {
      firebaseUtils = new FirebaseUtilities();

      Spark.get("/", (request, response) -> "Peer Mediation Backend");
      Spark.get("add-word", new AddWordHandler(firebaseUtils));
      Spark.get("list-words", new ListWordsHandler(firebaseUtils));
      Spark.get("clear-user", new ClearUserHandler(firebaseUtils));
      Spark.get("add-cookie", new AddCookieHandler(firebaseUtils));
      Spark.get("get-cookies", new GetCookiesHandler(firebaseUtils));
      Spark.get("get-admin-login", new AdminLoginHandler(firebaseUtils));
      Spark.get("get-api-info", new ApiInfoHandler(firebaseUtils));
      Spark.get("health", ((request, response) -> "Running"));

      Spark.notFound(
          (request, response) -> {
            response.status(404); // Not Found
            System.out.println("ERROR");
            return "404 Not Found - The requested endpoint does not exist.";
          });
      Spark.init();
      Spark.awaitInitialization();

      System.out.println("Server started at http://localhost:" + port);
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println(
          "Error: Could not initialize Firebase. Likely due to firebase_config.json not being found. Exiting.");
      System.exit(1);
    }
  }

  /**
   * Runs Server.
   *
   * @param args none
   */
  public static void main(String[] args) {
    setUpServer();
  }
}

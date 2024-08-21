import { getLoginCookie } from "./cookie";

//backend to be queried
//const HOST = "http://localhost:8080"; 
const HOST = "http://peer-mediation-api.us-east-1.elasticbeanstalk.com"

/**
 * This general function makes a query to the backend specified by the endpoint and appropriate
 * query parameters. 
 * @param endpoint the endpoint to query
 * @param query_params query parameters required for the endpoint
 * @returns 
 */
async function queryAPI(
  endpoint: string,
  query_params: Record<string, string>
) {
  const paramsString = new URLSearchParams(query_params).toString();
  const url = `${HOST}/${endpoint}?${paramsString}`;
  const response = await fetch(url);
  if (!response.ok) {
    console.error(response.status, response.statusText);
  }
  return response.json();
}

/**
 * This function handles the add-word endpoint to add a user's message to the backend. 
 * @param uid the uid of the user to add the message to 
 * @param word the message that needs to be stored under the uid in the Firestore db
 * @returns the backend response
 */
export async function addMessage(uid: string, message: string) {
  return await queryAPI("add-message", {
    uid: uid,
    message: message,
  });
}

/**
 * This handles the list-words endpoint which queries the backend for all previous messages stored
 * under the specific uid
 * @param uid the uid of the user
 */
export async function getMessages(uid: string) {
  return await queryAPI("list-messages", {
    uid: uid,
  });
}

export async function getCookies() {
  return await queryAPI("get-cookies", {
    uid: "cookies"
  });
}

/**
 * This function handles the clear-user endpoint which clears a user and their information from the 
 * database
 * @param uid the uid of the user to be cleared
 */
export async function clearUser(uid: string = getLoginCookie() || "") {
  return await queryAPI("clear-user", {
    uid: uid,
  });
}

/**
 * This function handles the add-cookie endpoint which stores the cookie of a user under the cookie
 * section of the database. This helps render the admin dashboard. 
 * @param cookie the cookie of the user to be added
 * @returns 
 */
export async function addCookie(cookie: string) {
  return await queryAPI("add-cookie", {
    cookie: cookie,
  });
}

/**
 * This function handles the retrieval of all the necessary information to initalize the firebase
 * from the backend
 */
export async function getAPIInfo() {
  return await queryAPI("get-api-info", {
    uid: "api"
  });
}

/**
 * This function takes in a username and password as query parameters and determines if it is the
 * correct username and password for admin login. 
 */
export async function verifyAdmin(username: string, password: string) {
  return await queryAPI("verify-admin", {
    username: username,
    password: password
  });
}
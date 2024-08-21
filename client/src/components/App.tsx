import { initializeApp } from "firebase/app";
import "../styles/App.css";
import HomePage from "./HomePage";
import AuthRoute from "./auth/AuthRoute";
import { getAPIInfo } from "../utils/api";
import { useEffect, useState } from "react";

function App() {
  const [firebaseInitialized, setFirebaseInitialized] = useState(false);

  useEffect(() => {
    const firebaseConfig = {
      apiKey: "",
      authDomain: "",
      projectId: "",
      storageBucket: "",
      messagingSenderId: "",
      appId: ""
    };

    getAPIInfo().then((data) => {
      firebaseConfig.apiKey = data.apiKey;
      firebaseConfig.authDomain = data.authDomain;
      firebaseConfig.projectId = data.projectID;
      firebaseConfig.storageBucket = data.storageBucket;
      firebaseConfig.messagingSenderId = data.messagingSenderID;
      firebaseConfig.appId = data.appId;

      initializeApp(firebaseConfig);
      setFirebaseInitialized(true);
    }).catch(error => {
      console.error("Error fetching API info:", error);
    });
  }, []);

  if (!firebaseInitialized) {
    return (
      <div>
        <div aria-label="loading page" className="loading-container">
          <div aria-label="loading" className="loading"></div>
          <div aria-label="loading label" className="loading-label">Loading...</div>
        </div>
      </div>
    );
  }

  return (
    <div className="App" aria-label="App">
      <AuthRoute gatedContent={<HomePage />} />
    </div>
  );
}

export default App;

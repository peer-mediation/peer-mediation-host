import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";
import React from "react";
import { addLoginCookie, removeLoginCookie } from "../../utils/cookie";
import "../../styles/LoginLogout.css"

export interface ILoginPageProps {
  loggedIn: boolean;
  setLogin: React.Dispatch<React.SetStateAction<boolean>>;
  isAdmin: boolean;
  setAdmin: React.Dispatch<React.SetStateAction<boolean>>;
}

const Login: React.FunctionComponent<ILoginPageProps> = (props) => {
  const auth = getAuth();

  const signInWithGoogle = async () => {
    try {
      const response = await signInWithPopup(auth, new GoogleAuthProvider());
      const userEmail = response.user.email || "";

      if (userEmail.endsWith("clover.k12.sc.us") || userEmail.endsWith("ver@brown.edu")) {
        console.log(response.user.uid);
        addLoginCookie(response.user.uid);
        props.setLogin(true);
      } else {
        await auth.signOut();
        console.log("User not allowed. Signed out.");
      }
    } catch (error) {
      console.log(error);
    }
  };

  const adminClicked = () => {
      props.setAdmin(true)
      props.setLogin(true)
  };

  return (
    <div aria-label="login box" className="login-box">
      <h1>CHS Peer Mediation: Login Page</h1>
      
      <div aria-label="image" className="image">
        <img 
          aria-label="Clover High School Image" 
          src="https://cmsv2-assets.apptegy.net/uploads/21138/logo/23952/Clover_HS_Logo.png">
        </img>
      </div>
      
      <div aria-label="button container" className="button-container">
        <button
          aria-label="sign in with google"
          className="google-login-button"
          onClick={() => signInWithGoogle()}
          disabled={props.loggedIn}
        >
          Sign in with Google
        </button>
        
        <button
          aria-label="admin login button"
          className="admin-login-button"
          onClick={() => adminClicked()}
          disabled={props.loggedIn}
        >
          Sign in as Admin
        </button>
      </div>
    </div>
  );
};

const Logout: React.FunctionComponent<ILoginPageProps> = (props) => {
  const signOut = () => {
    removeLoginCookie();
    props.setLogin(false);
    if (props.isAdmin) {
      props.setAdmin(false)
    }
  };

  return (
    <div aria-label="logout box" className="logout-box">
      <button aria-label="sign out" className="SignOut" onClick={() => signOut()}>
        Sign Out
      </button>
    </div>
  );
};

const LoginLogout: React.FunctionComponent<ILoginPageProps> = (props) => {
  return <>{!props.loggedIn ? <Login {...props} /> : <Logout {...props} />}</>;
};

export default LoginLogout;

import React from 'react';
import { useState } from "react";
import "../../styles/AdminDashboard.css"
import { getCookies, verifyAdmin } from '../../utils/api';
import { useEffect} from "react";
import Chat from "../Chat";

const AdminDashboard: React.FunctionComponent = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [cookies, setCookies] = useState<string[]>([]);
  const [hasReplied, setHasReplied] = useState<boolean[]>([]);
  const [activeChat, setActiveChat] = useState('');

  useEffect(() => {
      getCookies().then((data) => {
          setCookies(data.cookies);
          setHasReplied(data.hasReplied);
      });
  }, []);

  const handleBackPressed = () => {
    getCookies().then(data => setHasReplied(data.hasReplied));
    setActiveChat("");
  };

  const handleLogin = () => {
    verifyAdmin(username, password).then((data) => {
      if (data.isVerified) {
        setIsAuthenticated(true);
        setErrorMessage('');
      } else {
        setErrorMessage('Invalid username or password. Please try again.');
      }
    });
  };

  if (isAuthenticated) {
    if (activeChat !== ""){
      return(
        <div className='admin-chat'>
          <Chat isAdmin={true} uid={activeChat} />
          <div className = "button-page">
            <button className = "back-button" onClick={() => handleBackPressed()}> Back </button>
          </div>
        </div>
      );
    }
    return (
      <div className="admin-dashboard">
        <h1>Admin Dashboard</h1>
        <div className="emails">
          {cookies.map((cookie, index) => {
            let prefix;
            let backgroundColor;
            if (hasReplied[index]) {
              prefix = "Replied";
              backgroundColor = "#007bff";
            } else {
              prefix = "No Reply";
              backgroundColor = "#ff474c";
            }

            return (
              <div key={index} aria-label="message" className="message">
                <button 
                  style={{ backgroundColor: backgroundColor }} 
                  className = "email"
                  aria-label = "email" 
                  onClick={() => setActiveChat(cookie.split("@")[0])}>
                    {prefix + ": New message on " + cookie.split("@")[1].split(" ")[0] + " at " + 
                    cookie.split("@")[1].split(" ")[1] + " " + cookie.split("@")[1].split(" ")[2]}
                </button>
              </div>
            );
          })}
        </div>
      </div>
    );
  } else {
    return (
      <div className="admin-login">
        <h1>Admin Login</h1>
        <div className="login-form">
          <div className="form-group">
            <label htmlFor="username">Username:</label>
            <input
              className="username-input"
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password:</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          {errorMessage && <p className="error-message">{errorMessage}</p>}
          <button onClick={handleLogin}>Login</button>
        </div>
      </div>
    );
  }
};

export default AdminDashboard;
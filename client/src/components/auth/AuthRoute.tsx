import { useState } from "react";
import { getLoginCookie } from "../../utils/cookie";
import LoginLogout from "./LoginLogout";
import AdminDashboard from "./AdminDashboard";

interface AuthRouteProps {
  gatedContent: React.ReactNode;
}

/**
 * This component renders the necessary page given the user's status. If the user is logged in, 
 * it will check if the user is an admin, loading the admin dashboard if so, else it renders the
 * student side component with the about us page. If the login was unsuccessful, the user is denied
 * entry into the rest of the application. 
 */
function AuthRoute(props: AuthRouteProps) {
  const [loggedIn, setLogin] = useState(false);
  const [isAdmin, setAdmin] = useState(false);

  return (
    <>
      <LoginLogout loggedIn={loggedIn} setLogin={setLogin} isAdmin={isAdmin} setAdmin={setAdmin} />

      {loggedIn ? (
        isAdmin ? (
          <AdminDashboard /> 
        ) : (
          <>{props.gatedContent}</> 
        )
      ) : null}
    </>
  );
}

export default AuthRoute;

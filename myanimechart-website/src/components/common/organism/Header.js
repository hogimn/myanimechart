import styled from "styled-components";
import Logo from "./logo/Logo";
import { getImagePath } from "../../../util/pathUtil";
import { Link } from "react-router-dom";
import CommonButton from "../basic/CommonButton";
import { useEffect, useState } from "react";
import SecurityApi from "../../api/animestat/SecurityAPI";
import CommonAlert from "../basic/CommonAlert";

const StyledLink = styled(Link)`
  text-decoration: none;
  color: inherit;
`;

const StyledHeader = styled.header`
  display: flex;
  align-items: center;
  font-size: 1.15em;

  h1 {
    margin-left: 10px;
  }

  ${StyledLink} + ${StyledLink} {
    margin-left: 5px;
  }

  .ant-btn {
    margin-left: auto;
    margin-right: 10px;
  }

  .ant-alert {
    margin-right: 10px;
  }
`;

const Header = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const checkAuthentication = async () => {
      const result = await SecurityApi.isAuthenticated();
      setIsAuthenticated(result);
    };

    checkAuthentication();
  }, []);

  const startOAuth2Flow = async () => {
    const gateway_url = process.env.REACT_APP_GATEWAY_URL;
    const authorizationUrl = `${gateway_url}/security/oauth2/authorize/myanimelist`;
    window.location.href = authorizationUrl;
  };

  const handleLogin = (event) => {
    startOAuth2Flow();
  };

  const handleLogout = (event) => {
    const isSuccess = SecurityApi.logout();
    if (isSuccess) {
      window.location.href = "/";
    } else {
      setError("Logout failed. Please try again.");
    }
  };

  const handleAlertClose = () => {
    setError("");
  };

  return (
    <StyledHeader>
      <StyledLink to="/">
        <Logo
          src={getImagePath("myanimechart-logo.png")}
          alt={"MyAnimeChart Logo"}
          width={"70px"}
        />
      </StyledLink>
      <StyledLink to="/">
        <h2>MyAnimeChart</h2>
      </StyledLink>
      {isAuthenticated ? (
        <CommonButton onClick={handleLogout}>Logout</CommonButton>
      ) : (
        <CommonButton onClick={handleLogin}>Login</CommonButton>
      )}
      {error && (
        <CommonAlert message={error} type="error" onClose={handleAlertClose} />
      )}
    </StyledHeader>
  );
};

export default Header;

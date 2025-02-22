import styled from "styled-components";
import Logo from "./logo/Logo";
import { getImagePath } from "../../../util/pathUtil";
import { Link } from "react-router-dom";
import CommonButton from "../basic/CommonButton";

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
  }
`;

const Header = () => {
  const startOAuthFlow = async () => {
    const gateway_url = process.env.REACT_APP_GATEWAY_URL;
    const authorizationUrl = `${gateway_url}/security/oauth2/authorize/myanimelist`;
    window.location.href = authorizationUrl;
  };

  const handleLogin = (e) => {
    startOAuthFlow();
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
      <CommonButton onClick={handleLogin}>Login</CommonButton>
    </StyledHeader>
  );
};

export default Header;

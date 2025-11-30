import styled from "styled-components";
import Logo from "./logo/Logo";
import { getImagePath } from "../../../util/pathUtil";
import { Link } from "react-router-dom";
import CommonButton from "../basic/CommonButton";
import { useEffect, useState } from "react";
import SecurityApi from "../../api/anime/SecurityApi";
import CommonAlert from "../basic/CommonAlert";
import CommonModal from "../basic/CommonModal";
import { useUser } from "../context/UserContext";
import UserApi from "../../api/anime/UserApi";

const StyledLink = styled(Link)`
  text-decoration: none;
  color: inherit;

  h2 {
    margin-top: 9px;
    margin-bottom: -7px;
  }

  p {
    font-size: 10px;
  }
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
    background-color: rgba(36, 46, 66, 0.7);
    border: 1px solid rgba(25, 26, 46, 0.7);
    padding-left: 7px;
    padding-right: 7px;
  }

  .ant-btn:hover {
    background-color: rgba(96, 119, 160, 0.7) !important;
  }

  .ant-alert {
    margin-right: 10px;
  }
`;

const Avatar = styled.img`
  margin-left: auto;
  margin-right: 10px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
`;

const ModalContent = styled.div`
  text-align: center;
`;

const ModalAvatar = styled.img`
  width: 100px;
  height: 100px;
  border-radius: 50%;
`;

const ModalHeader = styled.h2`
  margin-top: 10px;
`;

const Header = () => {
  const { user, setUser } = useUser(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [error, setError] = useState("");
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const checkAuthentication = async () => {
      setLoading(true);
      const authenticated = await SecurityApi.isAuthenticated();
      setIsAuthenticated(authenticated);

      if (authenticated) {
        const storedUser = sessionStorage.getItem("user");
        if (storedUser) {
          setUser(JSON.parse(storedUser));
        } else {
          const user = await UserApi.getCurrentUser();
          setUser(user);
          sessionStorage.setItem("user", JSON.stringify(user));
        }
      } else {
        setUser(null);
        sessionStorage.removeItem("user");
      }

      setLoading(false);
    };

    checkAuthentication();
  }, [setUser]);

  const startOAuth2Flow = async () => {
    SecurityApi.startOAuth2Flow();
  };

  const handleLogin = (event) => {
    startOAuth2Flow();
  };

  const handleLogout = async (event) => {
    const isSuccess = await SecurityApi.logout();
    if (isSuccess) {
      window.location.href = "/";
    } else {
      setError("Logout failed. Please try again.");
    }
  };

  const handleAlertClose = () => {
    setError("");
  };

  const handleAvatarClick = () => {
    setIsModalVisible(true);
  };

  const handleModalClose = () => {
    setIsModalVisible(false);
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
        <p>Data is sourced from MyAnimeList</p>
      </StyledLink>
      {!loading &&
        (isAuthenticated && user ? (
          <>
            <Avatar
              src={user.pictureUrl}
              alt={user.name}
              onClick={handleAvatarClick}
            />
            <CommonModal
              visible={isModalVisible}
              onCancel={handleModalClose}
              footer={null}
              centered
            >
              <ModalContent>
                <ModalAvatar src={user.pictureUrl} alt={user.name} />
                <ModalHeader>Welcome, {user.name}</ModalHeader>
                <CommonButton onClick={handleLogout}>Logout</CommonButton>
              </ModalContent>
            </CommonModal>
          </>
        ) : (
          <CommonButton
            onClick={handleLogin}
            icon={
              <img
                src={getImagePath("myanimelist-logo.png")}
                width={"20px"}
                alt={"MAL"}
              />
            }
          >
            Login
          </CommonButton>
        ))}
      {error && (
        <CommonAlert message={error} type="error" onClose={handleAlertClose} />
      )}
    </StyledHeader>
  );
};

export default Header;

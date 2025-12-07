import Logo from "./logo/Logo";
import { getImagePath } from "../../../util/pathUtil";
import CommonButton from "../basic/CommonButton";
import { useEffect, useState } from "react";
import OAuth2Api from "../../api/OAuth2Api";
import CommonAlert from "../basic/CommonAlert";
import CommonModal from "../basic/CommonModal";
import { useUser } from "../context/UserContext";
import UserApi from "../../api/UserApi";
import {
  Avatar,
  ModalAvatar,
  ModalContent,
  ModalHeader,
  StyledHeader,
  StyledLink,
} from "./Header.style";

const Header = () => {
  const { user, setUser } = useUser(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [error, setError] = useState("");
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const checkAuthentication = async () => {
      setLoading(true);
      const authenticated = await OAuth2Api.isAuthenticated();
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
    OAuth2Api.startOAuth2Flow();
  };

  const handleLogin = (event) => {
    startOAuth2Flow();
  };

  const handleLogout = async (event) => {
    const isSuccess = await OAuth2Api.logout();
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

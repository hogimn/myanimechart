import Header from "../organism/Header";
import Footer from "../organism/Footer";
import styled from "styled-components";
import { UserProvider } from "../context/UserContext";
import { useNavigate, useLocation } from "react-router-dom";

const BackgroundPage = styled.div`
  z-index: -1;
  background-color: rgb(27, 27, 27);
  min-height: 100vh;
`;

const PageWrapper = styled.div`
  z-index: 1;
  background-color: #161616;
  color: white;
  margin-left: auto;
  margin-right: auto;
  width: 1400px;
  min-height: 100vh;
  @media (max-width: 1359px) {
    width: 100%;
  }
`;

const MainWrapper = styled.main``;

const Navigation = styled.nav`
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #2e51a2;
  font-weight: bold;
`;

const MenuWrapper = styled.ul`
  z-index: 2;
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;
  font-size: 18px;
  position: relative;
`;

const MenuItem = styled.li`
  position: relative;
  cursor: pointer;
  padding: 10px;

  &:hover {
    background-color: #333333;
  }

  &:hover > ul {
    display: block;
  }
`;

const SubMenu = styled.ul`
  z-index: 3;
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  background-color: #3a3a3a;
  list-style: none;
  padding: 0;
  margin: 0;
  width: max-content;
  min-width: 175px;

  li {
    font-weight: normal;
    font-size: 13px;
    padding: 8px;
    cursor: pointer;

    &:hover {
      background-color: #f0f0f0;
      color: black;
    }
  }
`;

const CurrentPagePanel = styled.div`
  border-bottom: 1px solid #555555;
  background-color: #222222;
  color: white;
  padding: 10px;
  font-size: 16px;
  font-weight: bold;
`;

const PageTemplate = ({ children }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const getCurrentPage = () => {
    switch (location.pathname) {
      case "/seasonal-anime":
        return "Seasonal Anime";
      case "/poll-collection-status":
        return "Poll Collection Status";
      default:
        return "Home";
    }
  };

  return (
    <UserProvider>
      <BackgroundPage>
        <PageWrapper>
          <Header />
          <Navigation>
            <MenuWrapper>
              <MenuItem>
                Anime
                <SubMenu>
                  <li onClick={() => navigate("/")}>Seasonal Anime</li>
                </SubMenu>
              </MenuItem>
              <MenuItem>
                Collection
                <SubMenu>
                  <li onClick={() => navigate("/poll-collection-status")}>
                    Poll Collection Status
                  </li>
                </SubMenu>
              </MenuItem>
            </MenuWrapper>
          </Navigation>
          <CurrentPagePanel>{getCurrentPage()}</CurrentPagePanel>
          <MainWrapper>{children}</MainWrapper>
          <Footer />
        </PageWrapper>
      </BackgroundPage>
    </UserProvider>
  );
};

export default PageTemplate;

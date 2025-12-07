import Header from "../organism/Header";
import Footer from "../organism/Footer";
import { UserProvider } from "../context/UserContext";
import { useNavigate, useLocation } from "react-router-dom";
import {
  BackgroundPage,
  CurrentPagePanel,
  MainWrapper,
  MenuItem,
  MenuWrapper,
  Navigation,
  PageWrapper,
  SubMenu,
} from "./PageTemplate.style";

const PageTemplate = ({ children }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const getCurrentPage = () => {
    const path =
      location.pathname === "/" ? "home" : location.pathname.slice(1);
    return path
      .split("-")
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
      .join(" ");
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
                  <li onClick={() => navigate("/season-archive")}>
                    Season Archive
                  </li>
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

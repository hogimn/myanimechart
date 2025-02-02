import Header from "../organism/Header";
import Footer from "../organism/Footer";
import styled from "styled-components";
import Side from "../organism/Side";

const PageWrapper = styled.div`
  display: flex;
`;

const MidWrapper = styled.div`
  min-height: 100vh;
  color: white;
`;

const MainWrapper = styled.main``;

const PageTemplate = ({ children }) => {
  return (
    <PageWrapper>
      <Side />
      <MidWrapper>
        <Header />
        <MainWrapper>{children}</MainWrapper>
        <Footer />
      </MidWrapper>
      <Side />
    </PageWrapper>
  );
};

export default PageTemplate;

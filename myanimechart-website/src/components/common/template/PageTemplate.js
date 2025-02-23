import Header from "../organism/Header";
import Footer from "../organism/Footer";
import styled from "styled-components";

const PageWrapper = styled.div`
  color: white;
  margin-left: auto;
  margin-right: auto;
  width: 1400px;
  @media (max-width: 1359px) {
    width: 100%;
  }
`;

const MainWrapper = styled.main``;

const PageTemplate = ({ children }) => {
  return (
    <PageWrapper>
      <Header />
      <MainWrapper>{children}</MainWrapper>
      <Footer />
    </PageWrapper>
  );
};

export default PageTemplate;

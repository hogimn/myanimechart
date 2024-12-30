import Header from "../organism/Header";
import Footer from "../organism/Footer";
import styled from "styled-components";

const PageWrapper = styled.div`
    min-height: 100vh;
    color: white;
`;

const MainWrapper = styled.main`
`

const PageTemplate = ({children}) => {
    return (
        <>
            <PageWrapper>
                <Header/>
                <MainWrapper>
                    {children}
                </MainWrapper>
                <Footer/>
            </PageWrapper>
        </>
    );
};

export default PageTemplate;

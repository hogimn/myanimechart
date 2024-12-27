import Header from "../organism/Header";
import Footer from "../organism/Footer";
import styled from "styled-components";

const PageWrapper = styled.div`
    min-height: 100vh;
    color: white;
    padding: 5px
`;

const PageTemplate = ({children}) => {
    return (
        <>
            <PageWrapper>
                <Header/>
                {children}
                <Footer/>
            </PageWrapper>
        </>
    );
};

export default PageTemplate;

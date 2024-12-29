import styled from "styled-components";
import Logo from "./logo/Logo";

const HeaderWrapper = styled.header`
    display: flex;
    align-items: center;

    h1 {
        margin-left: 10px;
    }
`;

const Header = () => {
    return (
        <HeaderWrapper>
            <Logo src={"/myanimechart-logo.png"} alt={"MyAnimeChart Logo"} width={"100px"}/>
            <h1>MyAnimeChart</h1>
        </HeaderWrapper>
    );
}

export default Header;
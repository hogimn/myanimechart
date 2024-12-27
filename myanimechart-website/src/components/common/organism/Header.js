import styled from "styled-components";
import Logo from "./logo/Logo";

const HeaderWrapper = styled.header`
    display: flex;
    align-items: center;
`;

const Header = () => {
    return (
        <HeaderWrapper>
            <Logo src={"/myanimechart-logo.png"} alt={"MyAnimeChart Logo"} width={"100px"}/>
        </HeaderWrapper>
    );
}

export default Header;
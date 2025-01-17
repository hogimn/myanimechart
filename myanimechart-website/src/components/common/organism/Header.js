import styled from "styled-components";
import Logo from "./logo/Logo";
import {getImagePath} from "../../../util/pathUtil";
import {Link} from "react-router-dom";

const StyledLink = styled(Link)`
    text-decoration: none;
    color: inherit;
`;

const StyledHeader = styled.header`
    display: flex;
    align-items: center;

    h1 {
        margin-left: 10px;
    }

    ${StyledLink} + ${StyledLink} {
        margin-left: 5px;
    }
`;

const Header = () => {
    return (
        <StyledHeader>
            <StyledLink to="/">
                <Logo src={getImagePath("myanimechart-logo.png")} alt={"MyAnimeChart Logo"} width={"75px"}/>
            </StyledLink>
            <StyledLink to="/">
                <h2>MyAnimeChart</h2>
            </StyledLink>
        </StyledHeader>
    );
}

export default Header;
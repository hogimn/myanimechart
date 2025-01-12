import styled from "styled-components";
import Logo from "./logo/Logo";
import {getImagePath} from "../../../util/pathUtil";
import {Link} from "react-router-dom";

const StyledHeader = styled.header`
    display: flex;
    align-items: center;

    h1 {
        margin-left: 10px;
    }
`;

const StyledLink = styled(Link)`
    text-decoration: none;
    color: inherit;
`;

const Header = () => {
    return (
        <StyledHeader>
            <StyledLink to="/">
                <Logo src={getImagePath("myanimechart-logo.png")} alt={"MyAnimeChart Logo"} width={"100px"} />
            </StyledLink>
            <StyledLink to="/">
                <h1>MyAnimeChart</h1>
            </StyledLink>
        </StyledHeader>
    );
}

export default Header;
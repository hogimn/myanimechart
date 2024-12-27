import styled from "styled-components";

const Logo = styled.img`
    width: ${(props) => props.width || '100px'};
    height: auto;
`;

export default Logo;

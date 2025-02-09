import styled from "styled-components";

const SideWrapper = styled.div`
  @media (min-width: 769px) {
    width: 270px;
    min-width: 270px;
  }
`;

const Side = () => {
  return <SideWrapper />;
};

export default Side;

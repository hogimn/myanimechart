import styled from "styled-components";

const SideWrapper = styled.div`
  @media (min-width: 769px) {
    width: 170px;
    min-width: 170px;
  }
`;

const Side = () => {
  return <SideWrapper />;
};

export default Side;

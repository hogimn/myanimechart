import styled from "styled-components";

export const Wrapper = styled.section`
  width: 100%;
  height: 190px;

  canvas {
    touch-action: pan-y !important;
  }
`;

export const StyledSpin = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #aaa;
`;

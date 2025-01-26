import React from "react";
import { useInView } from "react-intersection-observer";
import styled from "styled-components";

const Wrapper = styled.section`
  width: 100%;
  height: 225px;

  canvas {
    touch-action: pan-y !important;
  }
`;

const StyledSpin = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  font-size: 14px;
  color: #aaa;
`;

const LazyGraphWrapper = ({ children }) => {
  const { ref, inView } = useInView({
    triggerOnce: true,
    threshold: 0.1,
  });

  return (
    <Wrapper ref={ref}>
      {inView ? children : <StyledSpin>Loading Graph...</StyledSpin>}
    </Wrapper>
  );
};

export default LazyGraphWrapper;

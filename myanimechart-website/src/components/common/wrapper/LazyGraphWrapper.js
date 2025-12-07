import { useInView } from "react-intersection-observer";
import { StyledSpin, Wrapper } from "./LazyGraphWrapper.style";

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

import styled from "styled-components";
import CommonButton from "../basic/CommonButton";
import { MdCancel, MdCheckCircle } from "react-icons/md";

const StyledZoomButton = styled(CommonButton)`
  font-size: 0.65rem;
  background-color: ${(props) =>
    props.zoomEnabled ? "rgba(230, 57, 70, 0.6)" : "rgba(42, 157, 143, 0.6)"};
  position: absolute;
  top: ${(props) => (props.top ? props.top : "0px")};
  right: 7px;
  padding: 0 5px;
  margin: 0;
  border-radius: 8px;
  color: #fff;
  border: none;
  transition: background-color 0.3s, transform 0.1s;
  height: 20px;

  &:hover {
    background-color: ${(props) =>
      props.zoomEnabled
        ? "rgba(230, 57, 70, 0.8)"
        : "rgba(42, 157, 143, 0.8)"} !important;
    transform: scale(1.05);
  }

  &:active {
    background-color: ${(props) =>
      props.zoomEnabled
        ? "rgba(214, 40, 40, 0.8)"
        : "rgba(31, 126, 100, 0.8)"} !important;
    transform: scale(0.98);
  }
`;

const ContentWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  svg {
    margin-right: 3px;
  }
`;

const ZoomButton = ({ zoomEnabled, top = "0px", ...restProps }) => {
  return (
    <StyledZoomButton zoomEnabled={zoomEnabled} top={top} {...restProps}>
      <ContentWrapper>
        {zoomEnabled ? <MdCancel /> : <MdCheckCircle />}
        {zoomEnabled ? "Disable Zoom" : "Enable Zoom"}
      </ContentWrapper>
    </StyledZoomButton>
  );
};

export default ZoomButton;

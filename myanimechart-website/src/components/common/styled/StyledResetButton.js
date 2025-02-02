import styled from "styled-components";
import CommonButton from "../basic/CommonButton";

const StyledResetButton = styled(CommonButton)`
  background-color: rgba(0, 0, 0, 0.6);
  position: absolute;
  top: ${(props) => (props.top ? props.top : "0px")};
  right: 7px;
  padding: 7px;
  margin: 0;
  border-radius: 8px;
  color: #fff;

  &:hover {
    background-color: rgba(0, 0, 0, 0.8);
  }
`;

export default StyledResetButton;

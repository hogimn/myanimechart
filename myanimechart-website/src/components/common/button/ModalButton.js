import styled from "styled-components";
import CommonButton from "../basic/CommonButton";

const ModalButton = styled(CommonButton)`
  margin-top: 10px;
  padding: 0.5rem 1rem;
  background-color: rgba(99, 154, 255, 0.7);
  color: white;
  border: none;
  border-radius: 0.375rem;
  cursor: pointer;
  &:hover {
    background-color: rgba(99, 154, 255, 1) !important;
  }
`;

export default ModalButton;

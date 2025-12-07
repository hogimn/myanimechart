import { Input } from "antd";
import styled from "styled-components";

const Styledinput = styled.div`
  display: inline;
  input {
    background-color: rgba(36, 46, 66, 0.7);
    border: 1px solid rgba(25, 26, 46, 0.7);
  }
`;

const CommonInput = ({ placeholder = "Enter text...", ...restProps }) => {
  return (
    <Styledinput>
      <Input placeholder={placeholder} {...restProps} />
    </Styledinput>
  );
};

export default CommonInput;

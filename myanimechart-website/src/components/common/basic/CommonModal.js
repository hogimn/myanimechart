import { Modal } from "antd";
import React from "react";
import styled from "styled-components";

const StyledModal = styled(Modal)`
  .ant-modal-content {
    background-color: rgb(32, 34, 43) !important;
  }
  .ant-modal-title {
    background-color: rgb(32, 34, 43) !important;
  }
`;

const CommonModal = ({ children, ...restProps }) => {
  return <StyledModal {...restProps}>{children}</StyledModal>;
};

export default CommonModal;

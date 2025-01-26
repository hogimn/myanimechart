import { Modal } from "antd";
import React from "react";

const CommonModal = ({ children, ...restProps }) => {
  return <Modal {...restProps}>{children}</Modal>;
};

export default CommonModal;

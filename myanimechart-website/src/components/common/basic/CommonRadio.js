import React from "react";
import { Radio } from "antd";

const CommonRadio = ({ children, ...restProps }) => {
  return <Radio {...restProps}>{children}</Radio>;
};

CommonRadio.Group = Radio.Group;

export default CommonRadio;

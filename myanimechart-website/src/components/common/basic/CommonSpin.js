import React from "react";
import { Spin } from "antd";

const CommonSpin = ({ spinning = true, size = "large", ...restProps }) => {
  return <Spin spinning={spinning} size={size} {...restProps} />;
};

export default CommonSpin;

import React from "react";
import { Spin } from "antd";

const CommonSpin = ({ spinning = true, ...restProps }) => {
  return <Spin spinning={spinning} {...restProps} />;
};

export default CommonSpin;

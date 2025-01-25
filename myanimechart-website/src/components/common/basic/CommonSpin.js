import React from "react";
import { Spin } from "antd";

const CommonSpin = ({ tip = "Loading...", spinning = true, ...restProps }) => {
  return <Spin tip={tip} spinning={spinning} {...restProps} />;
};

export default CommonSpin;

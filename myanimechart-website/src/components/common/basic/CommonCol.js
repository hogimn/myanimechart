import React from "react";
import { Col } from "antd";

const CommonCol = ({
  span = 24,
  offset = 0,
  order = 0,
  children,
  ...restProps
}) => {
  return (
    <Col span={span} offset={offset} order={order} {...restProps}>
      {children}
    </Col>
  );
};

export default CommonCol;

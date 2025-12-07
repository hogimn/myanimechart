import { Col } from "antd";

const CommonCol = ({ children, ...restProps }) => {
  return <Col {...restProps}>{children}</Col>;
};

export default CommonCol;

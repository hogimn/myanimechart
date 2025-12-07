import { Row } from "antd";

const CommonRow = ({ children, ...restProps }) => {
  return <Row {...restProps}>{children}</Row>;
};

export default CommonRow;

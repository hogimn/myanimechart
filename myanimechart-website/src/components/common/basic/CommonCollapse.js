import { Collapse } from "antd";
import CollapsePanel from "antd/es/collapse/CollapsePanel";

const CommonCollapse = ({ ...restProps }) => {
  return <Collapse {...restProps} />;
};

CommonCollapse.Panel = CollapsePanel;

export default CommonCollapse;

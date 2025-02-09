import React from "react";
import { Tabs } from "antd";
import styled from "styled-components";

const StyledTabs = styled(Tabs)`
  .ant-tabs-tab {
    font-size: 0.9rem;
  }
`;

const CommonTabs = ({
  tabs = [],
  defaultActiveKey = "1",
  onChange,
  tabBarExtraContent,
  size = "medium",
  style = {},
  ...restProps
}) => {
  const tabItems = tabs.map((tab) => ({
    label: tab.label,
    key: tab.key,
    children: tab.content,
  }));

  return (
    <StyledTabs
      defaultActiveKey={defaultActiveKey}
      onChange={onChange}
      tabBarExtraContent={tabBarExtraContent}
      size={size}
      style={style}
      items={tabItems}
      {...restProps}
    />
  );
};

export default CommonTabs;

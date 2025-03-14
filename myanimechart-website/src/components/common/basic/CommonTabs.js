import React from "react";
import { Tabs } from "antd";
import styled from "styled-components";

const StyledTabs = styled(Tabs)`
  .ant-tabs-nav::before {
    border-bottom: none !important;
  }

  .ant-tabs-tab-active {
    background-color: rgba(65, 65, 65, 0.7);
  }

  .ant-tabs-nav-wrap {
    border-radius: 9px;
    background-color: rgba(34, 34, 34, 0.45);
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

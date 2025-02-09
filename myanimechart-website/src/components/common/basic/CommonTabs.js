import React from "react";
import { Tabs } from "antd";
import styled from "styled-components";

const StyledTabs = styled(Tabs)`
  .ant-tabs-tab {
    font-size: 0.9rem;
    background-color: rgba(36, 46, 66, 0.7);
  }

  .ant-tabs-tab-active {
    font-size: 0.9rem;
    background-color: rgba(66, 84, 122, 0.7);
  }

  .ant-tabs-nav-wrap {
    border-radius: 9px;
    background-color: rgba(30, 34, 39, 0.45);
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

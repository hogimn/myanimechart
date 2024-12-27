import React from "react";
import {Tabs} from "antd";

const CommonTabs = ({
                        tabs = [],
                        defaultActiveKey = "1",
                        onChange,
                        tabBarExtraContent,
                        size = "default",
                        style = {},
                        ...restProps
                    }) => {
    const tabItems = tabs.map((tab) => ({
        label: tab.label,
        key: tab.key,
        children: tab.content,
    }));

    return (
        <Tabs
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

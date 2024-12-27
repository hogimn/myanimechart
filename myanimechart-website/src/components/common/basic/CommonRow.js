import React from "react";
import {Row} from "antd";

const CommonRow = ({gutter = [16, 16], justify = "start", align = "top", children, ...restProps}) => {
    return (
        <Row gutter={gutter} justify={justify} align={align} {...restProps}>
            {children}
        </Row>
    );
};

export default CommonRow;

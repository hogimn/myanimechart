import React from "react";
import {Input} from "antd";

const CommonInput = ({ value, onChange, placeholder = "Enter text...", ...restProps }) => {
    return (
        <Input
            value={value}
            onChange={onChange}
            placeholder={placeholder}
            {...restProps}
        />
    );
};

export default CommonInput;

import React from "react";
import {Select} from "antd";

const CommonSelect = ({options, placeholder, value, onChange, allowClear = true, children, ...restProps}) => {
    return (
        <Select
            value={value}
            onChange={onChange}
            placeholder={placeholder}
            allowClear={allowClear}

            {...restProps}
        >
            {options && options.map((option, index) => (
                <Select.Option key={index} value={option.value}>
                    {option.label}
                </Select.Option>
            ))}
            {children}
        </Select>
    );
};

CommonSelect.Option = Select.Option;

export default CommonSelect;

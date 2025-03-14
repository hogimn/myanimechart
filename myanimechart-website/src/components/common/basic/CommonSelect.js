import React from "react";
import { Select } from "antd";
import styled from "styled-components";

const StyledSelect = styled(Select)``;

const CommonSelect = ({
  options,
  allowClear = false,
  children,
  ...restProps
}) => {
  return (
    <StyledSelect allowClear={allowClear} {...restProps}>
      {options &&
        options.map((option, index) => (
          <Select.Option key={index} value={option.value}>
            {option.label}
          </Select.Option>
        ))}
      {children}
    </StyledSelect>
  );
};

CommonSelect.Option = Select.Option;

export default CommonSelect;

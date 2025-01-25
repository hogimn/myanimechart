import React from "react";
import { Button } from "antd";

const CommonButton = ({
  type = "default",
  onClick,
  children,
  ...restProps
}) => {
  return (
    <Button type={type} onClick={onClick} {...restProps}>
      {children}
    </Button>
  );
};

export default CommonButton;

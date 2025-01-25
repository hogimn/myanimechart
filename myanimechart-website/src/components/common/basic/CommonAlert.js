import React from "react";
import { Alert } from "antd";

const CommonAlert = ({
  message,
  type = "info",
  showIcon = true,
  description,
  ...restProps
}) => {
  return (
    <Alert
      message={message}
      type={type}
      showIcon={showIcon}
      description={description}
      style={{ color: "black" }}
      {...restProps}
    />
  );
};

export default CommonAlert;

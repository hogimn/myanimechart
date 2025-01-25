import React from "react";
import { Pagination } from "antd";

const CommonPagination = ({
  current,
  pageSize,
  total,
  onChange,
  ...restProps
}) => {
  return (
    <Pagination
      current={current}
      pageSize={pageSize}
      total={total}
      onChange={onChange}
      {...restProps}
    />
  );
};

export default CommonPagination;

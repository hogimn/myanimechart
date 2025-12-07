import styled from "styled-components";
import CommonTabs from "../../../common/basic/CommonTabs";

export const StyledSeasonalTabs = styled.div`
  p {
    margin-left: 10px;
  }
`;

export const SelectWrapper = styled.div`
  margin-bottom: 16px;
  text-align: left;
  .ant-select {
    min-width: fit-content;
    margin-top: 5px;
    margin-left: 10px;
  }
`;

export const CustomTabs = styled(CommonTabs)`
  .ant-tabs-nav-wrap {
    margin: 0 10px;
  }
  .ant-tabs-tab + .ant-tabs-tab {
    margin: 0 0 0 0;
  }
  .ant-tabs-tab {
    padding: 12px 24px;
  }
`;

export const LoadingWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
`;

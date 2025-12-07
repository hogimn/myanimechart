import styled from "styled-components";
import CommonButton from "../../../common/basic/CommonButton";
import CommonCol from "../../../common/basic/CommonCol";
import CommonSpin from "../../../common/basic/CommonSpin";

export const StyledModalButtons = styled.div`
  .ant-btn + .ant-btn {
    margin-left: 10px;
  }
`;

export const EpisodeSeenWrapper = styled.div`
  display: flex;
  align-items: center;
`;

export const PlusButton = styled(CommonButton)`
  margin-left: 8px;
  padding: 0 10px;
  font-size: 16px;
`;

export const StyledTitle = styled.h3`
  color: rgb(149, 195, 255);
`;

export const StyledCol = styled(CommonCol)`
  width: 150px;

  input {
    width: 50px;
    max-width: 50px;
  }
`;

export const StyledSpin = styled(CommonSpin)`
  display: flex;
  justify-content: center;
  align-items: center;
  height: ${(props) => props?.height || "100vh"};
`;

export const AnimeWrapper = styled(CommonCol)`
  display: flex;
  flex-direction: column;
  position: relative;

  .ant-col {
    max-width: 100%;
  }

  .ant-card-body {
    display: none;
  }

  @media (max-width: 768px) {
    .ant-card-cover {
      width: 160px;
    }
  }
`;

export const AnimeSubWrapper = styled.article`
  background-color: rgba(0, 0, 0, 0.25);
  border: rgba(131, 125, 125, 0.51) 1px solid;
  border-radius: 10px;
  margin: 10px;

  section + section {
    margin-top: 10px;
  }
`;

export const AnimeImageWrapper = styled.section`
  display: flex;
  margin-bottom: 8px;
`;

export const OverlayBox = styled.div`
  position: absolute;
  bottom: 10px;
  left: 10px;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 5px 10px;
  border-radius: 10px;
  font-size: 0.7rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;

  svg {
    margin-right: 5px;
  }
`;

export const ImageWrapper = styled.div`
  display: inline-block;
  cursor: pointer;
  border-top-left-radius: 9px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: relative;
`;

export const EditButton = styled.button`
  position: absolute;
  top: 15px;
  left: 15px;
  background-color: ${(props) =>
    props?.backgroundColor || "rgba(0, 0, 0, 0.7)"};
  border: none;
  border-radius: 50%;
  color: white;
  padding: 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 17px;

  &:hover {
    background-color: ${(props) =>
      props?.backgroundColorHover || "rgba(0, 0, 0, 1.0)"};
  }
`;

export const ModalContent = styled.div`
  padding: 20px;
`;

export const SelectWrapper = styled.div`
  display: flex;
  align-items: center;
  .ant-select {
    width: 115px;
  }
  margin-bottom: 10px;
`;

export const InputWrapper = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 10px;
`;

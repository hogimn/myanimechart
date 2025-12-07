import styled from "styled-components";

export const Container = styled.div`
  padding: 20px;

  .ant-pagination {
    margin-top: 10px;
  }
`;

export const StatusCountContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(85px, 1fr));
  gap: 10px;
  margin-bottom: 20px;
  background-color: #282c34;
  padding: 10px;
  border-radius: 8px;
`;

export const StatusCountItem = styled.div`
  text-align: center;
  color: #fff;
  font-size: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  background-color: ${({ active }) => (active ? "#3a3f4b" : "transparent")};
  border-radius: 6px;
  padding: 5px;
`;

export const StatusCountLabel = styled.span`
  color: ${({ status }) =>
    status === "COMPLETED"
      ? "rgb(86,228,157)"
      : status === "WAIT"
      ? "rgb(92,92,92)"
      : status === "FAILED"
      ? "rgb(230,100,111)"
      : status === "IN_PROGRESS"
      ? "rgb(121,148,224)"
      : "#fff"};
  font-weight: bold;
`;

export const CountValue = styled.div`
  font-size: 1.2rem;
  font-weight: bold;
  margin-top: 4px;
`;

export const LoaderContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50vh;
`;

export const List = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 5px;
`;

export const ListItem = styled.div`
  display: flex;
  background-color: #1e1e1e;
  padding: 10px;
  border-radius: 8px;
  align-items: center;
`;

export const AnimeInfo = styled.div`
  margin-left: 10px;
  display: flex;
  flex-direction: column;
`;

export const AnimeTitle = styled.div`
  color: #a7ccf1;
  font-size: 1rem;
  font-weight: bold;
  line-height: normal;
`;

export const StatusText = styled.div`
  margin: 5px 0;
  color: ${({ status }) =>
    status === "COMPLETED"
      ? "rgb(86,228,157)"
      : status === "WAIT"
      ? "rgb(92,92,92)"
      : status === "FAILED"
      ? "rgb(230,100,111)"
      : "rgb(121,148,224)"};
  font-weight: bold;
`;

export const TimeText = styled.div`
  color: #ccc;
  font-size: 0.8em;
`;

export const ImageWrapper = styled.div`
  display: flex;
  width: 80px;
  height: 120px;
  cursor: pointer;
`;

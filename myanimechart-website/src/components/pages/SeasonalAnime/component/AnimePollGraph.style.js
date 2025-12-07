import styled from "styled-components";

export const ScrollableContent = styled.div`
  max-height: 50vh;
  overflow-y: auto;
  padding-right: 0.5rem;
  font-size: 13px;
`;

export const StyledTotalVotes = styled.div`
  font-size: 0.9rem;
  font-weight: bold;
  color: rgb(149, 195, 255);
  text-align: left;
  margin-bottom: ${(props) => (props.marginBottom ? props.marginBottom : 0)};
`;

export const VoteList = styled.ul`
  margin: 0;
  padding: 0;
  list-style: none;
`;

export const VoteItem = styled.li`
  margin-bottom: 1rem;
`;

export const ProgressBarBackground = styled.div`
  width: 100%;
  background-color: rgba(81, 84, 102, 0.24);
  height: 1rem;
  margin-top: 0.25rem;
  border-radius: 0.375rem;
`;

export const ProgressBarFill = styled.div`
  height: 1rem;
  background-color: rgba(99, 154, 255, 1);
  border-radius: 0.375rem;
  width: ${(props) => props.width}%;
`;

export const EpisodeList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;
`;

export const EpisodeItem = styled.li`
  margin-bottom: 0.5rem;
  cursor: pointer;
  color: #9cc9ff;

  &:hover {
    text-decoration: underline;
  }
`;

import styled from "styled-components";

export const ArchiveContainer = styled.div`
  margin: 0 auto;
  padding: 40px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: rgb(17, 17, 17);
  border-radius: 16px;
`;

export const YearRow = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  flex-wrap: wrap;
  justify-content: center;
`;

export const YearLabel = styled.span`
  width: 100%;
  font-weight: bold;
  font-size: 18px;
  text-align: center;
  margin-bottom: 8px;
  color: lightgray;
`;

export const SeasonButton = styled.button`
  width: 150px;
  margin: 6px;
  padding: 8px 16px;
  border: none;
  border-radius: 12px;
  background-color: rgb(57, 82, 136);
  color: white;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s ease;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);

  &:hover {
    background-color: #b8dcff;
    transform: translateY(-2px);
  }

  &:active {
    transform: scale(0.98);
  }
`;

import styled from "styled-components";

export const ListBox = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${(props) =>
    props.category === "genre"
      ? "rgba(75, 74, 74, 0.22)"
      : "rgba(74, 92, 116, 0.22)"};
  padding: 3px;
  flex-wrap: wrap;

  span {
    background-color: ${(props) =>
      props.category === "genre"
        ? "rgba(122, 120, 120, 0.22)"
        : "rgba(151, 182, 218, 0.22)"};
    border-radius: 8px;
    padding: 1px 4px;
    margin: 2px;
    font-size: 0.6rem;
    color: rgba(255, 255, 255, 0.55);
  }
`;

export const Box = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(44, 44, 44, 0.22);
  font-size: 0.6rem;
  color: rgba(255, 255, 255, 0.55);

  span:not(:last-child) {
    border-right: 1px solid #353535;
    padding-right: 5px;
    margin-right: 5px;
    height: 15px;
    display: flex;
    align-items: center;
  }
`;

export const Dot = styled.div`
  display: inline-block;
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: ${(props) => props.color};
  margin-right: 5px;
  margin-left: 5px;
`;

export const DescriptionContainer = styled.div`
  flex: 1;
  background-color: rgba(0, 0, 0, 0.24);
  height: 300px;
  overflow-y: ${(props) => (props.expanded ? "auto" : "hidden")};
  position: relative;
`;

export const SeeMoreButton = styled.button`
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(19, 26, 39, 0.7);
  color: #ffffff;
  font-size: 0.8rem;
  border: none;
  padding: 5px;
  cursor: pointer;
  text-align: center;

  &:hover {
    background: rgba(4, 8, 43, 0.8);
  }
`;

export const HeaderContainer = styled.div`
  padding: 10px;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-content: center;
  align-items: center;
  justify-content: center;

  @media (max-width: 768px) {
    padding: 0 10px;
  }
`;

export const Title = styled.div`
  display: block;
  font-size: 1rem;
  font-weight: bold;
  color: #a7ccf1;
  line-height: 1;
`;

export const SubTitle = styled.div`
  display: block;
  font-size: 0.7rem;
  color: rgb(190, 183, 183);
  line-height: 1;
  text-overflow: ellipsis;
  text-align: center;
`;

export const TitleContainer = styled.span`
  padding: 15px 0;
  height: fit-content;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-overflow: ellipsis;
  text-align: center;
  overflow: hidden;

  ${Title} + ${SubTitle} {
    margin-top: 10px;

    @media (max-width: 768px) {
      margin-top: 7px;
    }
  }
  ${SubTitle} + ${SubTitle} {
    margin-top: 10px;

    @media (max-width: 768px) {
      margin-top: 7px;
    }
  }
`;

export const AnimeDetails = styled.div`
  padding: 10px;
  font-size: 0.8rem;

  & strong {
    font-weight: bold;
  }

  @media (min-width: 769px) {
    display: block !important;
  }
`;

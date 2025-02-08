import React, { useState } from "react";
import styled from "styled-components";
import {
  toDateLabel,
  toEpisodeLabel,
  toTypeLabel,
} from "../../../../util/strUtil";

const ListBox = styled.div`
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
    font-size: 0.7rem;
    color: rgba(255, 255, 255, 0.55);

    @media (max-width: 768px) {
      font-size: 0.6rem;
    }
  }
`;

const Box = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(44, 44, 44, 0.22);
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.55);

  @media (max-width: 768px) {
    font-size: 0.7rem;
  }

  span:not(:last-child) {
    border-right: 1px solid #353535;
    padding-right: 10px;
    margin-right: 10px;
    height: 15px;
    display: flex;
    align-items: center;
  }
`;

const Dot = styled.div`
  display: inline-block;
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: ${(props) => props.color};
  margin-right: 5px;
  margin-left: 5px;
`;

const DescriptionContainer = styled.div`
  flex: 2;
  background-color: rgba(0, 0, 0, 0.24);
  height: 350px;
  overflow-y: ${(props) => (props.expanded ? "auto" : "hidden")};
  position: relative;

  @media (max-width: 768px) {
    height: 200px;
  }
`;

const SeeMoreButton = styled.button`
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(19, 26, 39, 0.7);
  color: #ffffff;
  font-size: 0.9rem;
  border: none;
  padding: 5px;
  cursor: pointer;
  text-align: center;

  @media (max-width: 768px) {
    font-size: 0.7rem;
  }

  &:hover {
    background: rgba(4, 8, 43, 0.8);
  }
`;

const HeaderContainer = styled.div`
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

const Title = styled.div`
  display: block;
  font-size: 1.3rem;
  font-weight: bold;
  color: #a7ccf1;
  line-height: 1;

  @media (max-width: 768px) {
    font-size: 1rem;
  }
`;

const SubTitle = styled.div`
  display: block;
  font-size: 0.9rem;
  color: rgb(190, 183, 183);
  line-height: 1;
  text-overflow: ellipsis;
  text-align: center;

  @media (max-width: 768px) {
    font-size: 0.7rem;
  }
`;

const TitleContainer = styled.span`
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

const Link = styled.a`
  color: #1890ff;
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
`;

const AnimeDetails = styled.div`
  padding: 10px;
  font-size: 0.9rem;

  @media (max-width: 768px) {
    font-size: 0.8rem;
  }

  & strong {
    font-weight: bold;
  }

  @media (min-width: 769px) {
    display: block !important;
  }
`;

const DescriptionSection = ({ anime }) => {
  const [expanded, setExpanded] = useState(false);

  const toggleExpanded = () => {
    setExpanded(!expanded);
  };

  return (
    <DescriptionContainer expanded={expanded}>
      <HeaderContainer>
        <Link href={anime.link} target="_blank" rel="noopener noreferrer">
          <TitleContainer>
            <Title>{anime.title}</Title>
            <SubTitle>{anime.englishTitle}</SubTitle>
          </TitleContainer>
        </Link>
      </HeaderContainer>
      <Box>
        <Dot
          color={anime.airStatus === "finished_airing" ? "#fd7976" : "#00ff95"}
        />
        <span>{toDateLabel(anime.startDate)}</span>
        <span>{toEpisodeLabel(anime.episodes)} eps </span>
        <span>{toTypeLabel(anime.type)}</span>
      </Box>

      <ListBox category="genre">
        {anime.genre.map((genre, index) => (
          <span key={index}>{genre}</span>
        ))}
      </ListBox>

      <ListBox category="studio">
        {anime.studios.map((studio, index) => (
          <span key={index}>{studio}</span>
        ))}
      </ListBox>

      <AnimeDetails>{anime.synopsis}</AnimeDetails>

      {!expanded && (
        <SeeMoreButton onClick={toggleExpanded}>Enable Scrolling</SeeMoreButton>
      )}
    </DescriptionContainer>
  );
};

export default DescriptionSection;

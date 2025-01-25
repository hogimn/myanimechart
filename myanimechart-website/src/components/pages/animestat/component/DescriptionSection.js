import React from "react";
import styled from "styled-components";
import {
  toDateLabel,
  toEpisodeLabel,
  toTypeLabel,
} from "../../../../util/strUtil";
import { isMobile } from "react-device-detect";

const GenresBox = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(75, 74, 74, 0.22);
  padding: 3px;
  flex-wrap: wrap;

  .genre {
    background-color: #353535;
    border-radius: 8px;
    padding: 1px 4px;
    margin: 2px;
    font-size: 0.7rem;
    color: rgba(255, 255, 255, 0.55);
  }
`;

const Box = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(44, 44, 44, 0.22);
  padding: 3px;
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.55);

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
  overflow-y: ${isMobile ? "auto" : "hidden"};
  height: 350px;

  &:hover {
    overflow-y: auto;
  }

  @media (max-width: 769px) {
    height: 200px;
  }
`;

const HeaderContainer = styled.div`
  padding: 10px;
  background-color: rgba(0, 0, 0, 0);
  max-height: 87px;
  height: 87px;
  display: flex;
  align-content: center;
  align-items: center;
  justify-content: center;

  @media (max-width: 769px) {
    max-height: 70px;
    height: 70px;
  }
`;

const Title = styled.div`
  display: inline;
  font-size: 1.3rem;
  font-weight: bold;
  color: #a7ccf1;
  line-height: 1;

  @media (max-width: 769px) {
    font-size: 1.1rem;
  }
`;

const SubTitle = styled.div`
  font-size: 0.8rem;
  color: gray;
  display: -webkit-box;
  line-height: 1;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: center;
`;

const TitleContainer = styled.span`
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: center;

  ${Title} + ${SubTitle} {
    margin-top: 5px;
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

  & strong {
    font-weight: bold;
  }

  @media (min-width: 769px) {
    display: block !important;
  }
`;

const DescriptionSection = ({ anime }) => {
  return (
    <DescriptionContainer>
      <HeaderContainer>
        <Link href={anime.link} target="_blank" rel="noopener noreferrer">
          <TitleContainer>
            <Title>{anime.title}</Title>
          </TitleContainer>
          <SubTitle>{anime.englishTitle}</SubTitle>
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

      <GenresBox>
        {anime.genre.map((genre, index) => (
          <span key={index} className="genre">
            {genre}
          </span>
        ))}
      </GenresBox>

      <AnimeDetails>
        {anime.synopsis} <br /> <br />
        <strong>Japanese:</strong> {anime.japaneseTitle} <br />
        <strong>Studios:</strong> {anime.studios.join(", ")} <br />
      </AnimeDetails>
    </DescriptionContainer>
  );
};

export default DescriptionSection;

import { useEffect, useRef, useState } from "react";
import {
  toDateLabel,
  toEpisodeLabel,
  toSourceLabel,
  toTypeLabel,
} from "../../../../util/strUtil";
import TitleLink from "../../../common/link/TitleLink";
import {
  AnimeDetails,
  Box,
  DescriptionContainer,
  Dot,
  HeaderContainer,
  ListBox,
  SeeMoreButton,
  SubTitle,
  Title,
  TitleContainer,
} from "./DescriptionSection.style";

const DescriptionSection = ({ anime }) => {
  const [expanded, setExpanded] = useState(false);
  const [showScrollButton, setShowScrollButton] = useState(false);
  const containerRef = useRef(null);

  const checkOverflow = () => {
    requestAnimationFrame(() => {
      if (containerRef.current) {
        const containerHeight = containerRef.current.clientHeight;
        const totalContentHeight = Array.from(
          containerRef.current.children
        ).reduce((acc, child) => acc + child.offsetHeight, 0);

        setShowScrollButton(totalContentHeight > containerHeight);
      }
    });
  };

  useEffect(() => {
    checkOverflow();
  }, [anime, expanded]);

  const toggleExpanded = () => {
    setExpanded(!expanded);
  };

  return (
    <DescriptionContainer ref={containerRef} expanded={expanded}>
      <HeaderContainer>
        <TitleLink href={anime.link} target="_blank" rel="noopener noreferrer">
          <TitleContainer>
            <Title>{anime.title}</Title>
            <SubTitle>{anime.englishTitle}</SubTitle>
          </TitleContainer>
        </TitleLink>
      </HeaderContainer>
      <Box>
        <Dot
          color={anime.airStatus === "finished_airing" ? "#fd7976" : "#00ff95"}
        />
        <span>{toDateLabel(anime.startDate)}</span>
        <span>{toEpisodeLabel(anime.episodes)} eps </span>
        <span>{toTypeLabel(anime.type)}</span>
        <span>{toSourceLabel(anime.source)}</span>
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

      {showScrollButton && !expanded && (
        <SeeMoreButton onClick={toggleExpanded}>Enable Scrolling</SeeMoreButton>
      )}
    </DescriptionContainer>
  );
};

export default DescriptionSection;

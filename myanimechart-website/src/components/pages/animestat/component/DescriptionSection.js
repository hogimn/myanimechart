import React, {useState} from 'react';
import styled from 'styled-components';
import {
    capitalizeFirstLetter,
    toAirStatusLabel, toDateLabel,
    toEpisodeLabel,
    toScoreLabel,
    toTypeLabel
} from "../../../../util/strUtil";
import {FiChevronDown, FiChevronUp} from "react-icons/fi";

const Tag = styled.div`
    display: inline-block;
    color: ${(props) => (props.color)};
    padding: 3px;
    border: solid 1px rgba(238, 238, 238, 0.52);
    border-radius: 5px;
    width: fit-content;
`;

const Dot = styled.div`
    display: inline-block;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background-color: ${(props) => (props.color)};
    margin-left: 10px;
`;

const DescriptionContainer = styled.div`
    flex: 2;
    background-color: rgba(0, 0, 0, 0.2);
    padding: 10px;

    ${Tag} {
        margin-right: 5px;
        margin-bottom: 5px;
    }
`;

const TitleContainer = styled.span`
    display: inline;
    align-items: center;
`;

const Title = styled.div`
    display: inline;
    font-size: 1.3rem;
    font-weight: bold;
    color: white;

    @media (max-width: 769px) {
        font-size: 1.0rem;
    }
`;

const SubTitle = styled.div`
    font-size: 0.8rem;
    font-weight: bold;
    margin-bottom: 8px;
    color: gray;

    @media (max-width: 769px) {
        font-size: 0.6rem;
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
    @media (max-width: 768px) {
        margin-top: 5px;
    }

    font-size: 0.9rem;

    & strong {
        font-weight: bold;
    }

    @media (min-width: 769px) {
        display: block !important;
    }
`;

const ToggleButton = styled.div`
    display: inline-flex;
    align-items: center;
    cursor: pointer;
    color: #3e9ef6;
    font-size: 1rem;
    margin-top: 3px;

    & svg {
        margin-left: 5px;
    }

    @media (max-width: 768px) {
        display: inline-flex;
    }

    @media (min-width: 769px) {
        display: none;
    }
`;

const DescriptionSection = ({anime}) => {
    const [showDetails, setShowDetails] = useState(false);

    const handleToggleDetails = () => {
        setShowDetails(prevState => !prevState);
    };

    return (
        <DescriptionContainer>
            <Link href={anime.link} target="_blank" rel="noopener noreferrer">
                <TitleContainer>
                    <Title>{anime.title}</Title>
                    <Dot color={anime.airStatus === 'finished_airing' ? '#fd7976' : 'lightgreen'}/>
                </TitleContainer>
                <SubTitle>{anime.englishTitle}</SubTitle>
            </Link>
            <Tag>★{toScoreLabel(anime.score)}</Tag>
            <Tag>{capitalizeFirstLetter(anime.season)} {anime.year}</Tag>
            <Tag>{toTypeLabel(anime.type)}</Tag>
            <Tag>{toEpisodeLabel(anime.episodes)} Episodes</Tag> <br/>

            <strong>Japanese:</strong> {anime.japaneseTitle} <br/>
            <strong>Genres:</strong> {anime.genre.join(', ')} <br/>
            <strong>Studios:</strong> {anime.studios.join(', ')} <br/>

            <ToggleButton onClick={handleToggleDetails}>
                {showDetails ? 'Show Less' : 'More Info'}
                {showDetails ? <FiChevronUp/> : <FiChevronDown/>}
            </ToggleButton>

            <AnimeDetails style={{display: showDetails ? 'block' : 'none'}}>
                <strong>StartDate:</strong> {toDateLabel(anime.startDate)} <br/>
                <strong>EndDate:</strong> {toDateLabel(anime.endDate)} <br/>
                <strong>Members:</strong> {anime.members} <br/>
                <strong>Rank:</strong> {anime.rank} <br/>
                <strong>Popularity:</strong> {anime.popularity} <br/>
            </AnimeDetails>
        </DescriptionContainer>
    );
};

export default DescriptionSection;

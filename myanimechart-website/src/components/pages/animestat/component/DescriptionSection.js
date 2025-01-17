import React, {useState} from 'react';
import styled from 'styled-components';
import {
    capitalizeFirstLetter,
    toAirStatusLabel,
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

const DescriptionContainer = styled.div`
    flex: 2;
    background-color: rgba(0, 0, 0, 0.2);
    padding: 10px;

    ${Tag} {
        margin-right: 5px;
        margin-bottom: 5px;
    }
    
`;

const Title = styled.div`
    font-size: 1.3rem;
    font-weight: bold;
    margin-bottom: 8px;
    color: white;

    @media (max-width: 769px) {
        font-size: 1.1rem;
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
    margin-top: 8px;
    font-size: 0.9rem;

    & strong {
        font-weight: bold;
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

const FullDescription = styled.div`
    display: block;

    @media (min-width: 769px) {
        display: block;
    }

    @media (max-width: 768px) {
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
                <Title>{anime.title}</Title>
            </Link>
            <Tag color={'rgba(255,221,0,0.86)'}>★{toScoreLabel(anime.score)}</Tag>
            <Tag
                color={anime.status === 'finished_airing' ? '#fd7976' : 'lightgreen'}>{toAirStatusLabel(anime.airStatus)}</Tag>
            <Tag color={'rgba(255,189,222,0.76)'}>{capitalizeFirstLetter(anime.season)} {anime.year}</Tag>
            <Tag>{toTypeLabel(anime.type)}</Tag>
            <Tag>{toEpisodeLabel(anime.episodes)} Episodes</Tag> <br/>

            <strong>Genres:</strong> {anime.genre.join(', ')} <br/>
            <strong>Studios:</strong> {anime.studios.join(', ')} <br/>

            <ToggleButton onClick={handleToggleDetails}>
                {showDetails ? 'Show Less' : 'More Info'}
                {showDetails ? <FiChevronUp/> : <FiChevronDown/>}
            </ToggleButton>

            <FullDescription>
                <AnimeDetails>
                    <strong>Members:</strong> {anime.members} <br/>
                    <strong>Rank:</strong> {anime.rank} <br/>
                    <strong>Popularity:</strong> {anime.popularity} <br/>
                    <strong>Genres:</strong> {anime.genre.join(', ')} <br/>
                    <strong>Studios:</strong> {anime.studios.join(', ')} <br/>
                </AnimeDetails>
            </FullDescription>

            {showDetails && (
                <AnimeDetails>
                    <strong>Members:</strong> {anime.members} <br/>
                    <strong>Rank:</strong> {anime.rank} <br/>
                    <strong>Popularity:</strong> {anime.popularity} <br/>
                </AnimeDetails>
            )}
        </DescriptionContainer>
    );
};

export default DescriptionSection;

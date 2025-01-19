import React, {useEffect, useState} from 'react';
import styled from 'styled-components';
import {
    capitalizeFirstLetter,
    toDateLabel,
    toEpisodeLabel,
    toTypeLabel
} from "../../../../util/strUtil";
import {isMobile} from 'react-device-detect';
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
    overflow-y: auto;
    height: 350px;

    @media (max-width: 769px) {
        height: 200px;
    }

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
    font-size: 0.9rem;
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
    const [showDetails, setShowDetails] = useState(!isMobile);

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
            <Tag>{capitalizeFirstLetter(anime.season)} {anime.year}</Tag>
            <Tag>{toTypeLabel(anime.type)}</Tag>
            <Tag>{toEpisodeLabel(anime.episodes)} Episodes</Tag> <br/>

            <ToggleButton onClick={handleToggleDetails}>
                {showDetails ? 'Show Less' : 'More Info'}
                {showDetails ? <FiChevronUp/> : <FiChevronDown/>}
            </ToggleButton>

            {showDetails &&
                <AnimeDetails systyle={{display: showDetails ? 'block' : 'none'}}>
                    {anime.synopsis} <br/> <br/>
                    <strong>Japanese:</strong> {anime.japaneseTitle} <br/>
                    <strong>Genres:</strong> {anime.genre.join(', ')} <br/>
                    <strong>Studios:</strong> {anime.studios.join(', ')} <br/>
                    <strong>StartDate:</strong> {toDateLabel(anime.startDate)} <br/>
                    <strong>EndDate:</strong> {toDateLabel(anime.endDate)} <br/>
                </AnimeDetails>
            }
        </DescriptionContainer>
    );
};

export default DescriptionSection;

import React from 'react';
import styled from 'styled-components';
import {capitalizeFirstLetter, toAirStatusLabel, toTypeLabel} from "../../../../util/strUtil";

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
    }
`;

const Title = styled.div`
    font-size: 1.3rem;
    font-weight: bold;
    margin-bottom: 8px;
    color: white;
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

const DescriptionSection = ({anime}) => {
    return (
        <DescriptionContainer>
            <Link href={anime.link} target="_blank" rel="noopener noreferrer">
                <Title>{anime.title}</Title>
            </Link>
            <Tag color={anime.status === 'finished_airing' ? '#fd7976' : 'lightgreen'}>{toAirStatusLabel(anime.airStatus)}</Tag>
            <Tag>{capitalizeFirstLetter(anime.season)} {anime.year}</Tag>
            <Tag>{toTypeLabel(anime.type)}</Tag>
            <Tag>{anime.episodes} Episodes</Tag>
            <AnimeDetails>
                <strong>Score:</strong> {anime.score.toFixed(2)} <br/>
                <strong>Members:</strong> {anime.members} <br/>
                <strong>Rank:</strong> {anime.rank} <br/>
                <strong>Popularity:</strong> {anime.popularity} <br/>
                <strong>Genres:</strong> {anime.genre.join(', ')} <br/>
                <strong>Studios:</strong> {anime.studios.join(', ')} <br/>
            </AnimeDetails>
        </DescriptionContainer>
    );
};

export default DescriptionSection;

import React from 'react';
import styled from 'styled-components';

const Status = styled.span`
    color: ${(props) => (props.status === 'finished_airing' ? '#fd7976' : 'lightgreen')};
    padding: 3px;
    border: solid 1px rgba(238, 238, 238, 0.52);
    border-radius: 5px;
`;

const Season = styled.span`
    color: #ffc8e9;
    padding: 3px;
    border: solid 1px rgba(238, 238, 238, 0.52);
    border-radius: 5px;
`;

const DescriptionContainer = styled.div`
    flex: 2;
    background-color: rgba(0, 0, 0, 0.2);
    padding: 10px;
    
    ${Season} + ${Status} {
        margin-left: 5px;
    }
`;

const Title = styled.div`
    font-size: 1.5rem;
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
            <Season>{anime.season} {anime.year}</Season>
            <Status status={anime.airStatus}>{anime.airStatus}</Status> <br/>
            <AnimeDetails>
                <strong>Score:</strong> {anime.score.toFixed(2)} <br/>
                <strong>Members:</strong> {anime.members} <br/>
                <strong>Rank:</strong> {anime.rank} <br/>
                <strong>Popularity:</strong> {anime.popularity} <br/>
                <strong>Genres:</strong> {anime.genre.join(', ')} <br/>
                <strong>Studios:</strong> {anime.studios.join(', ')} <br/>
                <strong>Episodes:</strong> {anime.episodes} <br/>
                <strong>Type:</strong> {anime.type} <br/>
            </AnimeDetails>
        </DescriptionContainer>
    );
};

export default DescriptionSection;

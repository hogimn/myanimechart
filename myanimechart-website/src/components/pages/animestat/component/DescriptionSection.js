import React from 'react';
import styled from 'styled-components';

const DescriptionContainer = styled.div`
    flex: 2;
    background-color: rgba(0, 0, 0, 0.2);
    padding: 10px;
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
            <AnimeDetails>
                <strong>Score:</strong> {anime.score.toFixed(2)} <br/>
                <strong>Members:</strong> {anime.members} <br/>
                <strong>Rank:</strong> {anime.rank} <br/>
                <strong>Popularity:</strong> {anime.popularity} <br/>
                <strong>Genres:</strong> {anime.genre.join(', ')} <br/>
                <strong>Studios:</strong> {anime.studios.join(', ')} <br/>
                <strong>Episodes:</strong> {anime.episodes} <br/>
                <strong>Year:</strong> {anime.year} <br/>
                <strong>Season:</strong> {anime.season} <br />
                <strong>Type:</strong> {anime.type} <br/>
            </AnimeDetails>
        </DescriptionContainer>
    );
};

export default DescriptionSection;

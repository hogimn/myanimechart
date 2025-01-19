import React from 'react';
import styled from 'styled-components';
import {
    capitalizeFirstLetter,
    toDateLabel,
    toEpisodeLabel,
    toTypeLabel
} from "../../../../util/strUtil";

const Tag = styled.div`
    display: inline-block;
    color: ${(props) => (props.color)};
    padding: 3px;
    border: solid 1px rgba(238, 238, 238, 0.52);
    border-radius: 5px;
    width: fit-content;
    font-size: 0.7rem;
`;

const Dot = styled.div`
    display: inline-block;
    width: 7px;
    height: 7px;
    border-radius: 50%;
    background-color: ${(props) => (props.color)};
    margin-right: 5px;
    margin-left: 5px;
`;

const DescriptionContainer = styled.div`
    flex: 2;
    background-color: rgba(0, 0, 0, 0.24);
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

const HeaderContainer = styled.div`
    padding: 5px;
    background-color: rgba(0, 0, 0, 0.45);
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

const TitleContainer = styled.span`
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    text-align: center;
`;

const Title = styled.div`
    display: inline;
    font-size: 1.3rem;
    font-weight: bold;
    color: #a7ccf1;
    line-height: 1.0;

    @media (max-width: 769px) {
        font-size: 1.1rem;
    }
`;

const SubTitle = styled.div`
    font-size: 0.9rem;
    color: gray;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    text-align: center;

    @media (max-width: 769px) {
        font-size: 0.8rem;
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
    padding: 5px;
    font-size: 0.9rem;

    & strong {
        font-weight: bold;
    }

    @media (min-width: 769px) {
        display: block !important;
    }
`;

const DescriptionSection = ({anime}) => {
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
            <Dot color={anime.airStatus === 'finished_airing' ? '#fd7976' : '#00ff95'}/>
            <Tag>{capitalizeFirstLetter(anime.season)} {anime.year}</Tag>
            <Tag>{toTypeLabel(anime.type)}</Tag>
            <Tag>{toEpisodeLabel(anime.episodes)} Episodes</Tag> <br/>

            <AnimeDetails>
                {anime.synopsis} <br/> <br/>
                <strong>Japanese:</strong> {anime.japaneseTitle} <br/>
                <strong>Genres:</strong> {anime.genre.join(', ')} <br/>
                <strong>Studios:</strong> {anime.studios.join(', ')} <br/>
                <strong>StartDate:</strong> {toDateLabel(anime.startDate)} <br/>
                <strong>EndDate:</strong> {toDateLabel(anime.endDate)} <br/>
            </AnimeDetails>
        </DescriptionContainer>
    );
};

export default DescriptionSection;

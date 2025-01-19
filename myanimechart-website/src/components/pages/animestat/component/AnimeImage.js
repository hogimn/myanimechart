import React from 'react';
import styled from 'styled-components';
import useLazyLoad from "../../../hook/useLazyLoad";

const ImageContainer = styled.div`
    width: 220px;
    height: 100%;
    position: relative;

    @media (max-width: 768px) {
        width: 150px;
    }
`;

const StyledAnimeImage = styled.img`
    width: 100%;
    height: 100%;
    object-fit: cover;
`;

const AnimeImage = ({src, alt}) => {
    const [ref, isVisible] = useLazyLoad();

    return (
        <ImageContainer ref={ref}>
            {isVisible && <StyledAnimeImage src={src} alt={alt}/>}
        </ImageContainer>
    );
};

export default AnimeImage;
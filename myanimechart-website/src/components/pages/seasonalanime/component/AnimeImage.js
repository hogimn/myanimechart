import useLazyLoad from "../../../hook/useLazyLoad";
import { ImageContainer, StyledAnimeImage } from "./AnimeImage.style";

const AnimeImage = ({ src, alt }) => {
  const [ref, isVisible] = useLazyLoad();

  return (
    <ImageContainer ref={ref}>
      {isVisible && <StyledAnimeImage src={src} alt={alt} />}
    </ImageContainer>
  );
};

export default AnimeImage;

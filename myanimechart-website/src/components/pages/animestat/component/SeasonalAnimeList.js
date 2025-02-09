import { useEffect, useState } from "react";
import AnimeStatApi from "../../../api/animestat/AnimeStatApi";
import CommonRow from "../../../common/basic/CommonRow";
import CommonCol from "../../../common/basic/CommonCol";
import CommonAlert from "../../../common/basic/CommonAlert";
import CommonSpin from "../../../common/basic/CommonSpin";
import AnimeStatGraph from "./AnimeStatGraph";
import DescriptionSection from "./DescriptionSection";
import CommonPagination from "../../../common/basic/CommonPagination";
import styled from "styled-components";
import { toScoreLabel } from "../../../../util/strUtil";
import { FaStar, FaTrophy, FaUserFriends, FaVoteYea } from "react-icons/fa";
import { MdTrendingUp } from "react-icons/md";
import AnimeImage from "./AnimeImage";
import AnimePollGraph from "./AnimePollGraph";
import LazyGraphWrapper from "../../../common/wrapper/LazyGraphWrapper";
import CommonModal from "../../../common/basic/CommonModal";
import { isMobile } from "react-device-detect";

const StyledSpin = styled(CommonSpin)`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
`;

const AnimeStatWrapper = styled(CommonCol)`
  display: flex;
  flex-direction: column;

  .ant-col {
    max-width: 100%;
  }

  .ant-card-body {
    display: none;
  }

  @media (max-width: 768px) {
    .ant-card-cover {
      width: 160px;
    }
  }
`;

const AnimeStatSubWrapper = styled.article`
  background-color: rgba(0, 0, 0, 0.25);
  border: rgba(131, 125, 125, 0.51) 1px solid;
  border-radius: 10px;
  margin: 10px;

  section + section {
    margin-top: 10px;
  }
`;

const AnimeWrapper = styled.section`
  display: flex;
  margin-bottom: 8px;
`;

const OverlayBox = styled.div`
  position: absolute;
  bottom: 10px;
  left: 10px;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 5px 10px;
  border-radius: 10px;
  font-size: 0.7rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;

  svg {
    margin-right: 5px;
  }
`;

const ImageWrapper = styled.div`
  display: inline-block;
  cursor: pointer;
  border-top-left-radius: 9px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: relative;
`;

const SeasonalAnimeList = ({
  year,
  season,
  sortBy,
  filterBy,
  page,
  setPage,
  pageSize,
  animeList,
  selected,
}) => {
  const [animeStats, setAnimeStats] = useState([]);
  const [sortedAndFilteredStats, setSortedAndFilteredStats] = useState([]);
  const [currentAnimeStats, setCurrentAnimeStats] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedImage, setSelectedImage] = useState(null);

  const handleImageClick = (src) => {
    setSelectedImage(src);
  };

  const closeModal = () => {
    setSelectedImage(null);
  };

  useEffect(() => {
    if (!selected) {
      return;
    }

    const fetchData = async () => {
      setLoading(true);
      try {
        const data = await AnimeStatApi.fetchAnimeStats(year, season);
        setAnimeStats(data);
      } catch (err) {
        setError("Failed to fetch anime data");
      } finally {
        setLoading(false);
      }
    };

    if (year != null && season != null) {
      fetchData();
    } else if (animeList != null) {
      setAnimeStats(animeList);
    }
  }, [year, season, animeList, selected]);

  useEffect(() => {
    const sortAnimeStats = (data, criterion) => {
      return [...data].sort((a, b) => {
        if (criterion === "votes") {
          criterion = "scoringCount";
        }
        const aValue = a[criterion] ?? null;
        const bValue = b[criterion] ?? null;

        if (aValue === null && bValue !== null) return 1;
        if (bValue === null && aValue !== null) return -1;

        if (
          criterion === "score" ||
          criterion === "members" ||
          criterion === "scoringCount"
        ) {
          return bValue - aValue;
        } else if (criterion === "rank" || criterion === "popularity") {
          return aValue - bValue;
        } else if (criterion === "startDate") {
          return new Date(aValue) - new Date(bValue);
        }

        return 0;
      });
    };

    const filterAnimeStats = (data, filterBy) => {
      if (filterBy.type === "all" && filterBy.airStatus === "all") {
        return data;
      }

      return data.filter(
        (anime) =>
          (anime.type === filterBy.type || filterBy.type === "all") &&
          (anime.airStatus === filterBy.airStatus ||
            filterBy.airStatus === "all")
      );
    };

    let updatedAnimeStats = animeStats;
    if (sortBy != null) {
      updatedAnimeStats = sortAnimeStats(updatedAnimeStats, sortBy);
    }
    if (filterBy != null) {
      updatedAnimeStats = filterAnimeStats(updatedAnimeStats, filterBy);
    }

    setSortedAndFilteredStats(updatedAnimeStats);

    setPage(1);
  }, [animeStats, sortBy, filterBy, animeList, setPage]);

  useEffect(() => {
    const startIndex = (page - 1) * pageSize;
    setCurrentAnimeStats(
      sortedAndFilteredStats.slice(startIndex, startIndex + pageSize)
    );
  }, [page, pageSize, sortedAndFilteredStats]);

  const onPageChange = (page) => {
    setPage(page);
  };

  if (loading) {
    return <StyledSpin tip="Loading..." />;
  }

  if (error) {
    return <CommonAlert message={error} type="error" />;
  }

  if (!sortedAndFilteredStats.length) {
    return <p>No anime found</p>;
  }

  return (
    <>
      <CommonRow>
        {currentAnimeStats.map((anime) => (
          <AnimeStatWrapper
            lg={24}
            xl={12}
            xxl={8}
            key={`anime-card-${anime.id}`}
          >
            <AnimeStatSubWrapper>
              <AnimeWrapper>
                <ImageWrapper onClick={() => handleImageClick(anime.image)}>
                  <AnimeImage alt={anime.title} src={anime.image} />
                  <OverlayBox>
                    <span>
                      <FaStar title="Score" />
                      {toScoreLabel(anime.score)}
                    </span>
                    <span>
                      <FaVoteYea title="Votes" />
                      {anime.scoringCount}
                    </span>
                    <span>
                      <FaTrophy title="Rank" />
                      {anime.rank}
                    </span>
                    <span>
                      <FaUserFriends title="Members" />
                      {anime.members.toLocaleString()}
                    </span>
                    <span>
                      <MdTrendingUp title="Popularity" />
                      {anime.popularity}
                    </span>
                  </OverlayBox>
                </ImageWrapper>
                <DescriptionSection anime={anime} />
              </AnimeWrapper>

              <CommonCol
                key={`anime-graph-${anime.id}`}
                style={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "flex-start",
                }}
              >
                <LazyGraphWrapper>
                  <AnimeStatGraph
                    animeStats={anime.animeStats.slice(
                      10,
                      anime.animeStats.length - 1
                    )}
                    selectedLegend={"score"}
                  />
                </LazyGraphWrapper>
                <LazyGraphWrapper>
                  <AnimePollGraph polls={anime.polls} />
                </LazyGraphWrapper>
              </CommonCol>
            </AnimeStatSubWrapper>
          </AnimeStatWrapper>
        ))}
      </CommonRow>

      <CommonPagination
        current={page}
        pageSize={pageSize}
        total={sortedAndFilteredStats.length}
        onChange={onPageChange}
        style={{ marginTop: "16px", textAlign: "center" }}
        showSizeChanger={false}
      />

      <CommonModal
        open={!!selectedImage}
        onCancel={closeModal}
        footer={null}
        centered
        width={isMobile ? "90%" : "30%"}
      >
        <img
          src={selectedImage}
          alt="Anime"
          style={{
            width: "100%",
            height: "auto",
            maxHeight: "80vh",
            objectFit: "contain",
          }}
        />
      </CommonModal>
    </>
  );
};

export default SeasonalAnimeList;

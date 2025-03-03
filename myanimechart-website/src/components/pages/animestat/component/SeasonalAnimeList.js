import { useCallback, useEffect, useState } from "react";
import { EditOutlined, PlusOutlined } from "@ant-design/icons";
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
import { useUser } from "../../../common/context/UserContext";
import SecurityApi from "../../../api/animestat/SecurityApi";
import CommonInput from "../../../common/basic/CommonInput";
import CommonSelect from "../../../common/basic/CommonSelect";
import ModalButton from "../../../common/button/ModalButton";
import UserApi from "../../../api/animestat/UserApi";
import CommonButton from "../../../common/basic/CommonButton";

const statusOptions = [
  { value: "watching", label: "Watching" },
  { value: "completed", label: "Completed" },
  { value: "on_hold", label: "On Hold" },
  { value: "dropped", label: "Dropped" },
  { value: "plan_to_watch", label: "Plan to Watch" },
];

const scoreOptions = Array.from({ length: 10 }, (_, i) => ({
  value: i + 1,
  label: i + 1,
})).reverse();

const EpisodeSeenWrapper = styled.div`
  display: flex;
  align-items: center;
`;

const PlusButton = styled(CommonButton)`
  margin-left: 8px;
  padding: 0 10px;
  font-size: 16px;
`;

const StyledTitle = styled.h3`
  color: rgb(149, 195, 255);
`;

const StyledCol = styled(CommonCol)`
  width: 150px;

  input {
    width: 50px;
    max-width: 50px;
  }
`;

const StyledSpin = styled(CommonSpin)`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
`;

const AnimeStatWrapper = styled(CommonCol)`
  display: flex;
  flex-direction: column;
  position: relative;

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

const EditButton = styled.button`
  position: absolute;
  top: 15px;
  left: 15px;
  background-color: ${(props) =>
    props?.backgroundColor || "rgba(0, 0, 0, 0.7)"};
  border: none;
  border-radius: 50%;
  color: white;
  padding: 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 17px;

  &:hover {
    background-color: ${(props) =>
      props?.backgroundColorHover || "rgba(0, 0, 0, 1.0)"};
  }
`;

const ModalContent = styled.div`
  padding: 20px;
`;

const SelectWrapper = styled.div`
  display: flex;
  align-items: center;
  .ant-select {
    width: 115px;
  }
  margin-bottom: 10px;
`;

const InputWrapper = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 10px;
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
  const { user } = useUser();
  const [animeStats, setAnimeStats] = useState([]);
  const [sortedAndFilteredStats, setSortedAndFilteredStats] = useState([]);
  const [currentAnimeStats, setCurrentAnimeStats] = useState([]);
  const [animeStatsLoading, setAnimeStatsLoading] = useState(false);
  const [error, setError] = useState(null);

  const [selectedImage, setSelectedImage] = useState(null);
  const [selectedAnime, setSelectedAnime] = useState(null);
  const [selectedUserAnimeStatus, setSelectedUserAnimeStatus] = useState(null);

  const [showAnimeEdit, setShowAnimeEdit] = useState(false);
  const [userAnimeDict, setUserAnimeDict] = useState({});
  const [userAnimeLoading, setUserAnimeLoading] = useState(false);
  const [userAnimeUpdating, setUserAnimeUpdating] = useState(false);

  const handleImageClick = (src) => {
    setSelectedImage(src);
  };

  const closeModal = () => {
    setSelectedImage(null);
  };

  const openEditModal = async (anime) => {
    if (user == null) {
      SecurityApi.startOAuth2Flow();
      return;
    }

    setUserAnimeLoading(true);
    let userAnime = userAnimeDict[anime.id];
    if (userAnime == null) {
      userAnime = {
        animeId: anime.id,
        status: "Select",
        watchedEpisodes: 0,
        score: "Select",
      };
    }

    setSelectedAnime(anime);
    setSelectedUserAnimeStatus(userAnime);
    setShowAnimeEdit(true);
    setUserAnimeLoading(false);
  };

  const closeEditModal = () => {
    setShowAnimeEdit(false);
  };

  const refreshUserAnimeStatusDict = useCallback(async () => {
    if (user == null) {
      return;
    }

    const userAnimeDtos = await UserApi.getUserAnimeStatusListByYearAndSeason(
      year,
      season
    );

    const userAnimeDict = userAnimeDtos.reduce((acc, userAnimeDto) => {
      acc[userAnimeDto.animeId] = userAnimeDto;
      return acc;
    }, {});
    setUserAnimeDict(userAnimeDict);
  }, [user, year, season]);

  useEffect(() => {
    if (!selected) {
      return;
    }

    const fetchData = async () => {
      setAnimeStatsLoading(true);
      try {
        const animeStatsDto = await AnimeStatApi.fetchAnimeStats(year, season);
        setAnimeStats(animeStatsDto);
      } catch (err) {
        setError("Failed to fetch anime data");
      } finally {
        setAnimeStatsLoading(false);
      }
    };

    if (year != null && season != null) {
      fetchData();
    } else if (animeList != null) {
      setAnimeStats(animeList);
    }
  }, [year, season, animeList, selected]);

  useEffect(() => {
    const fetchData = async () => {
      await refreshUserAnimeStatusDict();
    };

    fetchData();
  }, [user, refreshUserAnimeStatusDict]);

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

  if (animeStatsLoading) {
    return <StyledSpin tip="Loading..." />;
  }

  if (error) {
    return <CommonAlert message={error} type="error" />;
  }

  if (!sortedAndFilteredStats.length) {
    return <p>No anime found</p>;
  }

  const makeEditButtonBackgroundColor = (animeId) => {
    const userAnimeStatus = userAnimeDict[animeId];
    if (userAnimeStatus == null) {
      return null;
    }

    switch (userAnimeStatus.status) {
      case "watching":
        return "rgb(65, 129, 105, 0.8)";
      case "completed":
        return "rgb(70, 91, 136, 0.8)";
      case "on_hold":
        return "rgb(150, 148, 72, 0.8)";
      case "dropped":
        return "rgb(136, 60, 60, 0.8)";
      case "plan_to_watch":
        return "rgb(78, 78, 78, 0.8)";
      default:
        return null;
    }
  };

  const makeEditButtonBackgroundColorHover = (animeId) => {
    const userAnimeStatus = userAnimeDict[animeId];
    if (userAnimeStatus == null) {
      return null;
    }

    switch (userAnimeStatus.status) {
      case "watching":
        return "rgb(65, 129, 105)";
      case "completed":
        return "rgb(70, 91, 136)";
      case "on_hold":
        return "rgb(150, 148, 72)";
      case "dropped":
        return "rgb(136, 60, 60)";
      case "plan_to_watch":
        return "rgb(78, 78, 78)";
      default:
        return null;
    }
  };

  return (
    <>
      <StyledSpin spinning={userAnimeLoading}>
        <CommonRow>
          {currentAnimeStats.map((anime) => (
            <AnimeStatWrapper
              sm={24}
              md={12}
              lg={12}
              xl={8}
              xxl={8}
              key={`anime-card-${anime.id}`}
            >
              <AnimeStatSubWrapper>
                <AnimeWrapper>
                  <ImageWrapper
                    onClick={() =>
                      handleImageClick(
                        anime.largeImage ? anime.largeImage : anime.image
                      )
                    }
                  >
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
                  <EditButton
                    backgroundColor={makeEditButtonBackgroundColor(anime.id)}
                    backgroundColorHover={makeEditButtonBackgroundColorHover(
                      anime.id
                    )}
                    onClick={() => openEditModal(anime)}
                  >
                    <EditOutlined />
                  </EditButton>
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
                        anime.animeStats.length
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
          open={showAnimeEdit}
          onCancel={closeEditModal}
          footer={null}
          centered
          title={"Edit Anime Status"}
        >
          <ModalContent>
            <StyledTitle>{selectedAnime?.title}</StyledTitle>
            <CommonRow>
              <SelectWrapper>
                <StyledCol>Status:</StyledCol>
                <StyledCol>
                  <CommonSelect
                    value={selectedUserAnimeStatus?.status}
                    options={statusOptions}
                    onChange={(value) =>
                      setSelectedUserAnimeStatus({
                        ...selectedUserAnimeStatus,
                        status: value,
                      })
                    }
                  />
                </StyledCol>
              </SelectWrapper>
            </CommonRow>
            <CommonRow>
              <InputWrapper>
                <StyledCol>Episode seen:</StyledCol>
                <StyledCol>
                  <EpisodeSeenWrapper>
                    <CommonInput
                      value={selectedUserAnimeStatus?.watchedEpisodes}
                      placeholder="Ep."
                      onChange={(event) =>
                        setSelectedUserAnimeStatus({
                          ...selectedUserAnimeStatus,
                          watchedEpisodes: event.target.value,
                        })
                      }
                    />
                    {"/ "}
                    {selectedAnime?.episodes}
                    <PlusButton
                      icon={<PlusOutlined />}
                      onClick={() => {
                        const newWatchedEpisodes = isNaN(
                          parseInt(selectedUserAnimeStatus?.watchedEpisodes)
                        )
                          ? 1
                          : parseInt(selectedUserAnimeStatus?.watchedEpisodes) +
                            1;
                        setSelectedUserAnimeStatus({
                          ...selectedUserAnimeStatus,
                          watchedEpisodes: newWatchedEpisodes.toString(),
                        });
                      }}
                    />
                  </EpisodeSeenWrapper>
                </StyledCol>
              </InputWrapper>
            </CommonRow>
            <CommonRow>
              <SelectWrapper>
                <StyledCol>Score:</StyledCol>
                <StyledCol>
                  <CommonSelect
                    value={selectedUserAnimeStatus?.score}
                    options={scoreOptions}
                    onChange={(value) =>
                      setSelectedUserAnimeStatus({
                        ...selectedUserAnimeStatus,
                        score: value,
                      })
                    }
                  />
                </StyledCol>
              </SelectWrapper>
            </CommonRow>
            <ModalButton
              onClick={async () => {
                setUserAnimeUpdating(true);
                await UserApi.updateUserAnimeStatus(selectedUserAnimeStatus);
                await refreshUserAnimeStatusDict();
                setUserAnimeUpdating(false);
                setShowAnimeEdit(false);
              }}
            >
              Update
              <StyledSpin spinning={userAnimeUpdating}></StyledSpin>
            </ModalButton>
          </ModalContent>
        </CommonModal>

        <CommonModal
          open={!!selectedImage}
          onCancel={closeModal}
          footer={null}
          centered
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
      </StyledSpin>
    </>
  );
};

export default SeasonalAnimeList;

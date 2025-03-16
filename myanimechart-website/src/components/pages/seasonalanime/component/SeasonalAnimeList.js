import { useCallback, useEffect, useState } from "react";
import { EditOutlined, PlusOutlined } from "@ant-design/icons";
import CommonRow from "../../../common/basic/CommonRow";
import CommonCol from "../../../common/basic/CommonCol";
import CommonAlert from "../../../common/basic/CommonAlert";
import CommonSpin from "../../../common/basic/CommonSpin";
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
import SecurityApi from "../../../api/anime/SecurityApi";
import CommonInput from "../../../common/basic/CommonInput";
import CommonSelect from "../../../common/basic/CommonSelect";
import ModalButton from "../../../common/button/ModalButton";
import UserApi from "../../../api/anime/UserApi";
import CommonButton from "../../../common/basic/CommonButton";
import AnimeApi from "../../../api/anime/AnimeApi";

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

const StyledModalButtons = styled.div`
  .ant-btn + .ant-btn {
    margin-left: 10px;
  }
`;

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
  height: ${(props) => props?.height || "100vh"};
`;

const AnimeWrapper = styled(CommonCol)`
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

const AnimeSubWrapper = styled.article`
  background-color: rgba(0, 0, 0, 0.25);
  border: rgba(131, 125, 125, 0.51) 1px solid;
  border-radius: 10px;
  margin: 10px;

  section + section {
    margin-top: 10px;
  }
`;

const AnimeImageWrapper = styled.section`
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
  const [animes, setAnimes] = useState([]);
  const [sortedAndFilteredAnimes, setSortedAndFilteredAnimes] = useState([]);
  const [currentPageAnimes, setCurrentPageAnimes] = useState([]);
  const [animesLoading, setAnimesLoading] = useState(false);
  const [error, setError] = useState(null);

  const [selectedImage, setSelectedImage] = useState(null);
  const [selectedAnime, setSelectedAnime] = useState(null);
  const [selectedUserAnimeStatus, setSelectedUserAnimeStatus] = useState(null);

  const [showUserAnimeStatusEdit, setShowUserAnimeStatusEdit] = useState(false);
  const [showUserAnimeStatusDelete, setShowUserAnimeStatusDelete] =
    useState(false);
  const [userAnimeDict, setUserAnimeDict] = useState({});
  const [userAnimeLoading, setUserAnimeLoading] = useState(false);
  const [userAnimeDeleting, setUserAnimeDeleting] = useState(false);
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
        score: 0,
      };
      setShowUserAnimeStatusDelete(false);
    } else {
      setShowUserAnimeStatusDelete(true);
    }

    setSelectedAnime(anime);
    setSelectedUserAnimeStatus(userAnime);
    setShowUserAnimeStatusEdit(true);
    setUserAnimeLoading(false);
  };

  const closeEditModal = () => {
    setShowUserAnimeStatusEdit(false);
  };

  const refreshUserAnimeStatusDict = useCallback(async () => {
    if (user == null) {
      return;
    }

    const userAnimes = await UserApi.findAllUserAnimeStatuses();

    if (userAnimes == null || Object.entries(userAnimes).length === 0) {
      setUserAnimeDict({});
      sessionStorage.setItem("userAnimes", "");
      return;
    }

    const userAnimeDict = userAnimes.reduce((acc, userAnime) => {
      acc[userAnime.animeId] = userAnime;
      return acc;
    }, {});

    setUserAnimeDict(userAnimeDict);
    sessionStorage.setItem("userAnimes", JSON.stringify(userAnimeDict));
  }, [user]);

  useEffect(() => {
    if (!selected) {
      return;
    }

    const fetchData = async () => {
      setAnimesLoading(true);
      try {
        const animes = await AnimeApi.findAnimesWithPollsByYearAndSeason(
          year,
          season
        );
        setAnimes(animes);
      } catch (err) {
        setError("Failed to fetch seasonal anime");
      } finally {
        setAnimesLoading(false);
      }
    };

    if (year != null && season != null) {
      fetchData();
    } else if (animeList != null) {
      setAnimes(animeList);
    }
  }, [year, season, animeList, selected]);

  useEffect(() => {
    const fetchData = async () => {
      await refreshUserAnimeStatusDict();
    };

    const userAnimes = sessionStorage.getItem("userAnimes");
    if (userAnimes) {
      setUserAnimeDict(JSON.parse(userAnimes));
    } else {
      fetchData();
    }
  }, [user, refreshUserAnimeStatusDict]);

  useEffect(() => {
    const sortAnimes = (data, criterion) => {
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

    const filterAnimes = (data, filterBy) => {
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

    let updatedAnimes = animes;
    if (sortBy != null) {
      updatedAnimes = sortAnimes(updatedAnimes, sortBy);
    }
    if (filterBy != null) {
      updatedAnimes = filterAnimes(updatedAnimes, filterBy);
    }

    setSortedAndFilteredAnimes(updatedAnimes);

    setPage(1);
  }, [animes, sortBy, filterBy, animeList, setPage]);

  useEffect(() => {
    const startIndex = (page - 1) * pageSize;
    setCurrentPageAnimes(
      sortedAndFilteredAnimes.slice(startIndex, startIndex + pageSize)
    );
  }, [page, pageSize, sortedAndFilteredAnimes]);

  const onPageChange = (page) => {
    setPage(page);
  };

  if (animesLoading) {
    return <StyledSpin tip="Loading..." />;
  }

  if (error) {
    return <CommonAlert message={error} type="error" />;
  }

  if (!sortedAndFilteredAnimes.length) {
    return <p>No anime found</p>;
  }

  const makeEditButtonBackgroundColor = (animeId) => {
    const userAnimeStatus = userAnimeDict[animeId];
    if (userAnimeStatus == null) {
      return null;
    }

    switch (userAnimeStatus.status) {
      case "watching":
        return "rgba(65, 129, 105, 0.8)";
      case "completed":
        return "rgba(70, 91, 136, 0.8)";
      case "on_hold":
        return "rgba(150, 148, 72, 0.8)";
      case "dropped":
        return "rgba(136, 60, 60, 0.8)";
      case "plan_to_watch":
        return "rgba(109, 109, 109, 0.8)";
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
        return "rgb(109, 109, 109)";
      default:
        return null;
    }
  };

  return (
    <>
      <StyledSpin spinning={userAnimeLoading}>
        <CommonRow>
          {currentPageAnimes.map((anime) => (
            <AnimeWrapper
              sm={24}
              md={12}
              lg={12}
              xl={8}
              xxl={8}
              key={`anime-card-${anime.id}`}
            >
              <AnimeSubWrapper>
                <AnimeImageWrapper>
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
                        {anime.scoringCount.toLocaleString("en-US")}
                      </span>
                      <span>
                        <FaTrophy title="Rank" />
                        {anime.rank.toLocaleString("en-US")}
                      </span>
                      <span>
                        <FaUserFriends title="Members" />
                        {anime.members.toLocaleString("en-US")}
                      </span>
                      <span>
                        <MdTrendingUp title="Popularity" />
                        {anime.popularity.toLocaleString("en-US")}
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
                </AnimeImageWrapper>

                <CommonCol
                  key={`anime-graph-${anime.id}`}
                  style={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "flex-start",
                  }}
                >
                  <LazyGraphWrapper>
                    <AnimePollGraph polls={anime.polls} />
                  </LazyGraphWrapper>
                </CommonCol>
              </AnimeSubWrapper>
            </AnimeWrapper>
          ))}
        </CommonRow>

        <CommonPagination
          current={page}
          pageSize={pageSize}
          total={sortedAndFilteredAnimes.length}
          onChange={onPageChange}
          style={{ marginTop: "16px", textAlign: "center" }}
          showSizeChanger={false}
        />

        <CommonModal
          open={showUserAnimeStatusEdit}
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
                    onChange={(value) => {
                      if (value === "completed") {
                        setSelectedUserAnimeStatus({
                          ...selectedUserAnimeStatus,
                          status: value,
                          watchedEpisodes: selectedAnime?.episodes,
                        });
                      } else {
                        setSelectedUserAnimeStatus({
                          ...selectedUserAnimeStatus,
                          status: value,
                        });
                      }
                    }}
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
                      onChange={(event) => {
                        const newWatchedEpisodes = event.target.value;
                        setSelectedUserAnimeStatus({
                          ...selectedUserAnimeStatus,
                          watchedEpisodes: newWatchedEpisodes,
                        });
                      }}
                    />
                    {"/ "}
                    {selectedAnime?.episodes}
                    <PlusButton
                      icon={<PlusOutlined />}
                      onClick={() => {
                        let newWatchedEpisodes = isNaN(
                          parseInt(selectedUserAnimeStatus?.watchedEpisodes)
                        )
                          ? 1
                          : parseInt(selectedUserAnimeStatus?.watchedEpisodes) +
                            1;

                        if (newWatchedEpisodes >= selectedAnime?.episodes) {
                          newWatchedEpisodes = selectedAnime?.episodes;
                        }

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
                    value={
                      selectedUserAnimeStatus?.score === 0
                        ? "Select"
                        : selectedUserAnimeStatus?.score
                    }
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
            <CommonRow>
              <StyledModalButtons>
                <ModalButton
                  onClick={async () => {
                    setUserAnimeUpdating(true);
                    await UserApi.updateUserAnimeStatus(
                      selectedUserAnimeStatus
                    );
                    await refreshUserAnimeStatusDict();
                    setUserAnimeUpdating(false);
                    setShowUserAnimeStatusEdit(false);
                  }}
                >
                  Update
                  <StyledSpin
                    spinning={userAnimeUpdating}
                    height={"100%"}
                  ></StyledSpin>
                </ModalButton>
                {showUserAnimeStatusDelete && (
                  <ModalButton
                    onClick={async () => {
                      setUserAnimeDeleting(true);
                      await UserApi.deleteUserAnimeStatus(
                        selectedUserAnimeStatus
                      );
                      await refreshUserAnimeStatusDict();
                      setUserAnimeDeleting(false);
                      setShowUserAnimeStatusEdit(false);
                    }}
                  >
                    Delete
                    <StyledSpin
                      spinning={userAnimeDeleting}
                      height={"100%"}
                    ></StyledSpin>
                  </ModalButton>
                )}
              </StyledModalButtons>
            </CommonRow>
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

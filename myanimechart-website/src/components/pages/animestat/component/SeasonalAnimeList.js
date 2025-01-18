import {useEffect, useState} from "react";
import AnimeStatApi from "../../../api/animestat/AnimeStatApi";
import CommonRow from "../../../common/basic/CommonRow";
import CommonCol from "../../../common/basic/CommonCol";
import CommonAlert from "../../../common/basic/CommonAlert";
import CommonSpin from "../../../common/basic/CommonSpin";
import AnimeStatsGraph from "./AnimeStatsGraph";
import DescriptionSection from "./DescriptionSection";
import CommonPagination from "../../../common/basic/CommonPagination";
import styled from "styled-components";
import {toScoreLabel} from "../../../../util/strUtil";
import {FaStar, FaTrophy, FaUserFriends} from "react-icons/fa";
import {MdTrendingUp} from "react-icons/md";

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
        max-width: 100%
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
    border: rgba(255, 254, 254, 0.62) 1px solid;
    border-radius: 5px;
    margin: 5px;
`;

const AnimeWrapper = styled.section`
    display: flex;
    margin-bottom: 8px;
`;

const GraphWrapper = styled.section`
    width: 100%;
    height: 350px;
    margin-bottom: 15px;

    canvas {
        touch-action: pan-y !important;
    }
`;

const OverlayBox = styled.div`
    position: absolute;
    bottom: 10px;
    left: 10px;
    background-color: rgba(0, 0, 0, 0.7);
    color: white;
    padding: 5px 10px;
    border-radius: 5px;
    font-size: 12px;
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
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    position: relative;
`;

const AnimeImage = styled.img`
    width: 220px;
    height: 100%;
    object-fit: cover;

    @media (max-width: 768px) {
        width: 150px;
    }
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
                               selected
                           }) => {
    const [animeStats, setAnimeStats] = useState([]);
    const [sortedAndFilteredStats, setSortedAndFilteredStats] = useState([]);
    const [currentAnimeStats, setCurrentAnimeStats] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

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
                const aValue = a[criterion] ?? null;
                const bValue = b[criterion] ?? null;

                if (aValue === null && bValue !== null) return 1;
                if (bValue === null && aValue !== null) return -1;

                if (criterion === "score" || criterion === "members" || criterion === "scoringCount") {
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

            return data.filter(anime =>
                (anime.type === filterBy.type || filterBy.type === "all") &&
                (anime.airStatus === filterBy.airStatus || filterBy.airStatus === "all"));
        };

        let updatedAnimeStats = animeStats;
        if (sortBy != null) {
            updatedAnimeStats = sortAnimeStats(updatedAnimeStats, sortBy);
        }
        if (filterBy != null) {
            updatedAnimeStats = filterAnimeStats(updatedAnimeStats, filterBy);
        }

        setSortedAndFilteredStats(updatedAnimeStats);
    }, [animeStats, sortBy, filterBy, animeList]);

    useEffect(() => {
        const startIndex = (page - 1) * pageSize;
        setCurrentAnimeStats(sortedAndFilteredStats.slice(startIndex, startIndex + pageSize));
    }, [page, pageSize, sortedAndFilteredStats]);

    const onPageChange = (page) => {
        setPage(page);
    };

    if (loading) {
        return <StyledSpin tip="Loading..."/>;
    }

    if (error) {
        return <CommonAlert message={error} type="error"/>;
    }

    if (!sortedAndFilteredStats.length) {
        return <p>No anime found</p>;
    }

    return (
        <>
            <CommonRow>
                {currentAnimeStats.map((anime) => (
                    <AnimeStatWrapper xs={24} md={12} lg={12} xl={8} key={`anime-card-${anime.id}`}>
                        <AnimeStatSubWrapper>
                            <AnimeWrapper>
                                <ImageWrapper>
                                    <AnimeImage alt={anime.title} src={anime.image}/>
                                    <OverlayBox>
                                        <span>
                                             <FaStar/>
                                            {toScoreLabel(anime.score)}
                                        </span>
                                        <span>
                                            <FaUserFriends/>
                                            {anime.members.toLocaleString()}
                                        </span>
                                        <span>
                                            <FaTrophy/>
                                            {anime.rank}
                                        </span>
                                        <span>
                                            <MdTrendingUp/>
                                            {anime.popularity}
                                        </span>
                                    </OverlayBox>
                                </ImageWrapper>
                                <DescriptionSection anime={anime}/>
                            </AnimeWrapper>

                            <CommonCol
                                xs={24} sm={24} md={12} key={`anime-graph-${anime.id}`}
                                style={{display: "flex", flexDirection: "column", alignItems: "flex-start"}}
                            >
                                <GraphWrapper>
                                    <AnimeStatsGraph animeStats={anime.animeStats} selectedLegend={sortBy}/>
                                </GraphWrapper>
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
                style={{marginTop: "16px", textAlign: "center"}}
                showSizeChanger={false}
            />
        </>
    );
};

export default SeasonalAnimeList;

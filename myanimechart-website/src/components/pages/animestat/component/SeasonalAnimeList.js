import {useEffect, useState} from "react";
import AnimeStatApi from "../../../api/animestat/AnimeStatApi";
import CommonRow from "../../../common/basic/CommonRow";
import CommonCol from "../../../common/basic/CommonCol";
import CommonAlert from "../../../common/basic/CommonAlert";
import CommonSpin from "../../../common/basic/CommonSpin";
import CommonCard from "../../../common/basic/CommonCard";
import AnimeStatsGraph from "./AnimeStatsGraph";
import DescriptionSection from "./DescriptionSection";
import CommonPagination from "../../../common/basic/CommonPagination";
import CommonSelect from "../../../common/basic/CommonSelect";
import styled from "styled-components";

const StyledSpin = styled(CommonSpin)`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
`;

const SelectWrapper = styled.div`
    margin-bottom: 16px;
    text-align: left;

    .ant-select {
        min-width: fit-content;
        margin-top: 5px;
        margin-left: 5px;
    }
`;

const AnimeStatWrapper = styled(CommonCol)`
    display: flex;
    flex-direction: column;

    .ant-col {
        max-width: 100%
    }

    .ant-card {
        height: 360px;
    }

    .ant-card-body {
        display: none;
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

const SeasonalAnimeList = ({
                               year,
                               season,
                               sortBy,
                               setSortBy,
                               filterBy,
                               setFilterBy,
                               page,
                               setPage,
                               pageSize,
                               animeList
                           }) => {
    const [animeStats, setAnimeStats] = useState(animeList != null ? animeList : []);
    const [sortedAndFilteredStats, setSortedAndFilteredStats] = useState([]);
    const [currentAnimeStats, setCurrentAnimeStats] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
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
        }
    }, [year, season, animeList]);

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
            <SelectWrapper>
                {filterBy && (
                    <>
                        <CommonSelect
                            value={`Type: ${filterBy.type}`}
                            onChange={(value) => setFilterBy({...filterBy, type: value})}
                        >
                            <CommonSelect.Option value="all">all</CommonSelect.Option>
                            <CommonSelect.Option value="tv">tv</CommonSelect.Option>
                            <CommonSelect.Option value="ona">ona</CommonSelect.Option>
                            <CommonSelect.Option value="movie">movie</CommonSelect.Option>
                            <CommonSelect.Option value="music">music</CommonSelect.Option>
                            <CommonSelect.Option value="pv">pv</CommonSelect.Option>
                            <CommonSelect.Option value="special">special</CommonSelect.Option>
                            <CommonSelect.Option value="tv_special">tv_special</CommonSelect.Option>
                        </CommonSelect>

                        <CommonSelect
                            value={`Air Status: ${filterBy.airStatus}`}
                            onChange={(value) => setFilterBy({...filterBy, airStatus: value})}
                        >
                            <CommonSelect.Option value="all">all</CommonSelect.Option>
                            <CommonSelect.Option value="currently_airing">currently_airing</CommonSelect.Option>
                            <CommonSelect.Option value="finished_airing">finished_airing</CommonSelect.Option>
                        </CommonSelect>
                    </>
                )}

                {sortBy && (
                    <CommonSelect
                        value={`Sort: ${sortBy.charAt(0).toUpperCase() + sortBy.slice(1)}`}
                        onChange={(value) => setSortBy(value)}
                    >
                        <CommonSelect.Option value="score">Score</CommonSelect.Option>
                        <CommonSelect.Option value="members">Members</CommonSelect.Option>
                        <CommonSelect.Option value="rank">Rank</CommonSelect.Option>
                        <CommonSelect.Option value="popularity">Popularity</CommonSelect.Option>
                        <CommonSelect.Option value="scoringCount">ScoringCount</CommonSelect.Option>
                    </CommonSelect>
                )}
            </SelectWrapper>

            <CommonRow gutter={[16, 16]}>
                {currentAnimeStats.map((anime) => (
                    <AnimeStatWrapper xs={24} lg={12} xl={8} key={`anime-card-${anime.id}`}>
                        <AnimeStatSubWrapper>
                            <AnimeWrapper>
                                <CommonCard hoverable cover={<img alt={anime.title} src={anime.image}/>}/>
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

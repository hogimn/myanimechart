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

const SelectWrapper = styled.div`
    margin-bottom: 16px;
    text-align: right;

    .ant-select {
        min-width: 117px;
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

const AnimeStatSubWrapper = styled.div`
    background-color: rgba(0, 0, 0, 0.25);
    border: rgba(255, 254, 254, 0.62) 1px solid;
    border-radius: 5px;
    margin: 5px;
`;

const AnimeWrapper = styled.div`
    display: flex;
    margin-bottom: 8px;
`;

const GraphWrapper = styled.div`
    width: 100%;
    height: 350px;
    margin-bottom: 15px;
`;

const SeasonalAnimeList = ({year, season, sortBy, setSortBy, page, setPage, pageSize}) => {
    const [animeStats, setAnimeStats] = useState([]);
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

        fetchData();
    }, [year, season]);

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

    const onPageChange = (page) => {
        setPage(page);
    };

    if (loading) {
        return <CommonSpin tip="Loading..."/>;
    }

    if (error) {
        return <CommonAlert message={error} type="error"/>;
    }

    if (!animeStats.length) {
        return <p>No anime found for this season</p>;
    }

    const sortedAnimeStats = sortAnimeStats(animeStats, sortBy);
    const startIndex = (page - 1) * pageSize;
    const currentAnimeStats = sortedAnimeStats.slice(startIndex, startIndex + pageSize);

    return (
        <>
            <SelectWrapper>
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
            </SelectWrapper>

            <CommonRow gutter={[16, 16]}>
                {currentAnimeStats.map((anime) => (
                    <>
                        <AnimeStatWrapper xs={24} sm={12} md={8} key={`anime-card-${anime.id}`}>
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
                    </>
                ))}
            </CommonRow>

            <CommonPagination
                current={page}
                pageSize={pageSize}
                total={animeStats.length}
                onChange={onPageChange}
                style={{marginTop: "16px", textAlign: "center"}}
                showSizeChanger={false}
            />
        </>
    );
};

export default SeasonalAnimeList;

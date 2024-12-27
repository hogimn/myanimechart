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

const SeasonalAnimeList = ({year, season, sortBy, setSortBy}) => {
    const [animeStats, setAnimeStats] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(10);

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

            if (criterion === "score" || criterion === "popularity" || criterion === "members") {
                return bValue - aValue;
            } else if (criterion === "rank") {
                return aValue - bValue;
            }

            return 0;
        });
    };

    const onPageChange = (page) => {
        setCurrentPage(page);
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
    const startIndex = (currentPage - 1) * itemsPerPage;
    const currentAnimeStats = sortedAnimeStats.slice(startIndex, startIndex + itemsPerPage);

    return (
        <>
            <div style={{marginBottom: "16px", textAlign: "right"}}>
                <CommonSelect
                    value={sortBy}
                    onChange={(value) => setSortBy(value)}
                    style={{width: 110}}
                >
                    <CommonSelect.Option value="score">Score</CommonSelect.Option>
                    <CommonSelect.Option value="members">Members</CommonSelect.Option>
                    <CommonSelect.Option value="rank">Rank</CommonSelect.Option>
                    <CommonSelect.Option value="popularity">Popularity</CommonSelect.Option>
                </CommonSelect>
            </div>

            <CommonRow gutter={[16, 16]}>
                {currentAnimeStats.map((anime) => (
                    <CommonCol
                        xs={16}
                        key={anime.id}
                        style={{display: "flex", flexDirection: "row", alignItems: "flex-start"}}
                    >
                        <div style={{flex: 1, marginRight: "16px"}}>
                            <CommonCard hoverable cover={<img alt={anime.title} src={anime.image}/>}/>
                        </div>

                        <DescriptionSection anime={anime}/>

                        <div style={{flex: 5}}>
                            <AnimeStatsGraph animeStats={anime.animeStats}/>
                        </div>
                    </CommonCol>
                ))}
            </CommonRow>

            <CommonPagination
                current={currentPage}
                pageSize={itemsPerPage}
                total={animeStats.length}
                onChange={onPageChange}
                style={{marginTop: "16px", textAlign: "center"}}
                showSizeChanger={false}
            />
        </>
    );
};

export default SeasonalAnimeList;

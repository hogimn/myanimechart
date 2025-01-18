import React, {useEffect, useState} from "react";
import CommonTabs from "../../../common/basic/CommonTabs";
import SeasonalAnimeList from "./SeasonalAnimeList";
import styled from "styled-components";
import {
    getCurrentSeason,
    getCurrentSeasonYear,
    getNextSeason,
    getNextSeasonYear,
    getPreviousSeason,
    getPreviousSeasonYear
} from "../../../../util/dateUtil";
import AnimeApi from "../../../api/animestat/AnimeApi";
import AnimeSearchBox from "./AnimeSearchBox";
import CommonSelect from "../../../common/basic/CommonSelect";
import {toAirStatusLabel, toTypeLabel} from "../../../../util/strUtil";

const SelectWrapper = styled.div`
    margin-bottom: 16px;
    text-align: left;

    .ant-select {
        min-width: fit-content;
        margin-top: 5px;
        margin-left: 5px;
    }
`;

const CustomTabs = styled(CommonTabs)`
    .ant-tabs-tab + .ant-tabs-tab {
        margin: 0 0 0 0;
    }

    .ant-tabs-tab {
        font-size: 17px;
        padding: 12px 24px;
    }
`;

const SeasonalTabs = () => {
    const [sortBy, setSortBy] = useState("score");
    const [filterBy, setFilterBy] = useState({type: "all", airStatus: "all"});
    const [activeTab, setActiveTab] = useState("2");
    const [page, setPage] = useState(1);
    const pageSize = 9;
    const [searchResults, setSearchResults] = useState([]);
    const [input, setInput] = useState("");

    const handleSearch = async (keyword) => {
        setInput(keyword);
        if (keyword == null || keyword.trim() === "") {
            setSearchResults([]);
            return;
        }

        const data = await AnimeApi.searchAnimeByKeyword(keyword);
        setSearchResults(data);
        setPage(1);
        return data;
    };

    const previousSeason = getPreviousSeason();
    const currentSeason = getCurrentSeason();
    const nextSeason = getNextSeason();

    const previousYear = getPreviousSeasonYear();
    const currentYear = getCurrentSeasonYear();
    const nextYear = getNextSeasonYear();

    const tabs = [
        {
            key: "1",
            label: `${previousSeason.charAt(0).toUpperCase() + previousSeason.slice(1)} ${previousYear}`,
            content: (
                <SeasonalAnimeList
                    year={previousYear}
                    season={previousSeason}
                    sortBy={sortBy}
                    filterBy={filterBy}
                    page={page}
                    setPage={setPage}
                    pageSize={pageSize}
                    selected={activeTab === "1"}
                />
            ),
        },
        {
            key: "2",
            label: `${currentSeason.charAt(0).toUpperCase() + currentSeason.slice(1)} ${currentYear}`,
            content: (
                <SeasonalAnimeList
                    year={currentYear}
                    season={currentSeason}
                    sortBy={sortBy}
                    filterBy={filterBy}
                    page={page}
                    setPage={setPage}
                    pageSize={pageSize}
                    selected={activeTab === "2"}
                />
            ),
        },
        {
            key: "3",
            label: `${nextSeason.charAt(0).toUpperCase() + nextSeason.slice(1)} ${nextYear}`,
            content: (
                <SeasonalAnimeList
                    year={nextYear}
                    season={nextSeason}
                    sortBy={sortBy}
                    filterBy={filterBy}
                    page={page}
                    setPage={setPage}
                    pageSize={pageSize}
                    selected={activeTab === "3"}
                />
            ),
        },
    ];

    const handleTabChange = (key) => {
        setActiveTab(key);
        setPage(1);
    };

    const goToTop = () => {
        window.scrollTo(0, 0);
    };

    useEffect(() => {
        goToTop();
    }, [page]);

    useEffect(() => {
        setPage(1);
        goToTop();
    }, [filterBy]);

    return (
        <>
            <AnimeSearchBox onSearch={handleSearch}/>
            <SelectWrapper>
                {filterBy && (
                    <>
                        <CommonSelect
                            value={`Type: ${toTypeLabel(filterBy.type)}`}
                            onChange={(value) => setFilterBy({...filterBy, type: value})}
                        >
                            <CommonSelect.Option value="all">ALL</CommonSelect.Option>
                            <CommonSelect.Option value="tv">TV</CommonSelect.Option>
                            <CommonSelect.Option value="ona">ONA</CommonSelect.Option>
                            <CommonSelect.Option value="movie">Movie</CommonSelect.Option>
                            <CommonSelect.Option value="music">Music</CommonSelect.Option>
                            <CommonSelect.Option value="pv">PV</CommonSelect.Option>
                            <CommonSelect.Option value="special">Special</CommonSelect.Option>
                            <CommonSelect.Option value="tv_special">TV Special</CommonSelect.Option>
                        </CommonSelect>

                        <CommonSelect
                            value={`Air Status: ${toAirStatusLabel(filterBy.airStatus)}`}
                            onChange={(value) => setFilterBy({...filterBy, airStatus: value})}
                        >
                            <CommonSelect.Option value="all">ALL</CommonSelect.Option>
                            <CommonSelect.Option value="currently_airing">Airing</CommonSelect.Option>
                            <CommonSelect.Option value="finished_airing">Ended</CommonSelect.Option>
                        </CommonSelect>
                    </>
                )}

                {sortBy && (
                    <CommonSelect
                        value={`Sort: ${sortBy.charAt(0).toUpperCase() + sortBy.slice(1)}`}
                        onChange={(value) => setSortBy(value)}
                    >
                        <CommonSelect.Option value="none">None</CommonSelect.Option>
                        <CommonSelect.Option value="score">Score</CommonSelect.Option>
                        <CommonSelect.Option value="members">Members</CommonSelect.Option>
                        <CommonSelect.Option value="rank">Rank</CommonSelect.Option>
                        <CommonSelect.Option value="popularity">Popularity</CommonSelect.Option>
                        <CommonSelect.Option value="scoringCount">ScoringCount</CommonSelect.Option>
                    </CommonSelect>
                )}
            </SelectWrapper>
            {input.length > 0 || searchResults.length > 0 ? (
                <SeasonalAnimeList
                    animeList={searchResults}
                    sortBy={sortBy}
                    filterBy={filterBy}
                    page={page}
                    setPage={setPage}
                    pageSize={pageSize}/>
            ) : (
                <CustomTabs
                    tabs={tabs}
                    defaultActiveKey="2"
                    activeKey={activeTab}
                    onChange={handleTabChange}
                />
            )}
        </>
    );
};

export default SeasonalTabs;

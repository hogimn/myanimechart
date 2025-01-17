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
import AnimeStatApi from "../../../api/animestat/AnimeApi";
import AnimeSearchBox from "./AnimeSearchBox";

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
    const pageSize = 12;
    const [searchResults, setSearchResults] = useState([]);

    const handleSearch = async (query) => {
        if (query == null || query.trim() === "") {
            setSearchResults([]);
            return;
        }

        const data = await AnimeStatApi.searchAnimeByTitleStartingWith(query);
        setSearchResults(data);
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
                    setSortBy={setSortBy}
                    filterBy={filterBy}
                    setFilterBy={setFilterBy}
                    page={page}
                    setPage={setPage}
                    pageSize={pageSize}
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
                    setSortBy={setSortBy}
                    filterBy={filterBy}
                    setFilterBy={setFilterBy}
                    page={page}
                    setPage={setPage}
                    pageSize={pageSize}
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
                    setSortBy={setSortBy}
                    filterBy={filterBy}
                    setFilterBy={setFilterBy}
                    page={page}
                    setPage={setPage}
                    pageSize={pageSize}
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
        <div>
            <AnimeSearchBox onSearch={handleSearch}/>
            {searchResults.length > 0 ? (
                <SeasonalAnimeList
                    animeList={searchResults}
                    sortBy={sortBy}
                    setSortBy={setSortBy}
                    filterBy={filterBy}
                    setFilterBy={setFilterBy}
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
        </div>
    );
};

export default SeasonalTabs;

import React, { useEffect, useState } from "react";
import CommonTabs from "../../../common/basic/CommonTabs";
import SeasonalAnimeList from "./SeasonalAnimeList";
import styled from "styled-components";
import {
  getCurrentSeason,
  getCurrentSeasonYear,
  getPreviousSeason,
  getPreviousSeasonYear,
  getPreviousSeasonFromSeason,
  getPreviousSeasonYearFromYearAndSeason,
  getNextSeasonFromSeason,
  getNextSeasonYearFromYearAndSeason,
} from "../../../../util/dateUtil";
import AnimeApi from "../../../api/anime/AnimeApi";
import AnimeSearchBox from "./AnimeSearchBox";
import CommonSelect from "../../../common/basic/CommonSelect";
import { toAirStatusLabel, toTypeLabel } from "../../../../util/strUtil";

const SelectWrapper = styled.div`
  margin-bottom: 16px;
  text-align: left;
  .ant-select {
    min-width: fit-content;
    margin-top: 5px;
    margin-left: 10px;
  }
`;

const CustomTabs = styled(CommonTabs)`
  .ant-tabs-nav-wrap {
    margin: 0 10px;
  }
  .ant-tabs-tab + .ant-tabs-tab {
    margin: 0 0 0 0;
  }
  .ant-tabs-tab {
    padding: 12px 24px;
  }
`;

const SeasonalTabs = () => {
  const [sortBy, setSortBy] = useState("score");
  const [filterBy, setFilterBy] = useState({ type: "all", airStatus: "all" });
  const [activeTab, setActiveTab] = useState("2");
  const [page, setPage] = useState(1);
  const [searchResults, setSearchResults] = useState([]);
  const [input, setInput] = useState("");

  let pageSize = 12;

  const handleSearch = async (keyword) => {
    setInput(keyword);
    if (!keyword.trim()) {
      setSearchResults([]);
      return;
    }
    const data = await AnimeApi.findAnimesWithPollsByKeyword(keyword);
    setSearchResults(data);
    setPage(1);
  };

  const [seasonData, setSeasonData] = useState([
    { season: getPreviousSeason(), year: getPreviousSeasonYear() },
    { season: getCurrentSeason(), year: getCurrentSeasonYear() },
  ]);

  const updateSeasons = (direction) => {
    if (direction === "prev") {
      console.log(seasonData);
      setSeasonData((prev) => [
        {
          season: getPreviousSeasonFromSeason(prev[0].season),
          year: getPreviousSeasonYearFromYearAndSeason(
            prev[0].year,
            prev[0].season
          ),
        },
        prev[0],
      ]);
    } else {
      setSeasonData((prev) => [
        prev[1],
        {
          season: getNextSeasonFromSeason(prev[1].season),
          year: getNextSeasonYearFromYearAndSeason(
            prev[1].year,
            prev[1].season
          ),
        },
      ]);
    }
  };

  const tabs = [
    {
      key: "prev",
      label: "...",
      content: null,
    },
    ...seasonData.map(({ season, year }, index) => ({
      key: `${index + 1}`,
      label: `${season.charAt(0).toUpperCase() + season.slice(1)} ${year}`,
      content: (
        <SeasonalAnimeList
          year={year}
          season={season}
          sortBy={sortBy}
          filterBy={filterBy}
          page={page}
          setPage={setPage}
          pageSize={pageSize}
          selected={activeTab === `${index + 1}`}
        />
      ),
    })),
    {
      key: "next",
      label: "...",
      content: null,
    },
  ];

  const handleTabChange = (key) => {
    if (key === "prev") {
      updateSeasons("prev");
      setPage(1);
    } else if (key === "next") {
      updateSeasons("next");
      setPage(1);
    } else {
      setActiveTab(key);
      setPage(1);
    }
  };

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  useEffect(() => {
    setPage(1);
    window.scrollTo(0, 0);
  }, [filterBy]);

  return (
    <>
      <AnimeSearchBox onSearch={handleSearch} />
      <SelectWrapper>
        <CommonSelect
          value={`Type: ${toTypeLabel(filterBy.type)}`}
          onChange={(value) => setFilterBy({ ...filterBy, type: value })}
        >
          <CommonSelect.Option value="all">All</CommonSelect.Option>
          <CommonSelect.Option value="tv">TV</CommonSelect.Option>
          <CommonSelect.Option value="ona">ONA</CommonSelect.Option>
          <CommonSelect.Option value="movie">Movie</CommonSelect.Option>
          <CommonSelect.Option value="music">Music</CommonSelect.Option>
          <CommonSelect.Option value="pv">PV</CommonSelect.Option>
          <CommonSelect.Option value="tv_special">
            TV Special
          </CommonSelect.Option>
        </CommonSelect>
        <CommonSelect
          value={`Air Status: ${toAirStatusLabel(filterBy.airStatus)}`}
          onChange={(value) => setFilterBy({ ...filterBy, airStatus: value })}
        >
          <CommonSelect.Option value="all">All</CommonSelect.Option>
          <CommonSelect.Option value="currently_airing">
            Airing
          </CommonSelect.Option>
          <CommonSelect.Option value="finished_airing">
            Ended
          </CommonSelect.Option>
        </CommonSelect>
        <CommonSelect
          value={`Sort: ${sortBy.charAt(0).toUpperCase() + sortBy.slice(1)}`}
          onChange={(value) => setSortBy(value)}
        >
          <CommonSelect.Option value="score">Score</CommonSelect.Option>
          <CommonSelect.Option value="votes">Votes</CommonSelect.Option>
          <CommonSelect.Option value="rank">Rank</CommonSelect.Option>
          <CommonSelect.Option value="members">Members</CommonSelect.Option>
          <CommonSelect.Option value="popularity">
            Popularity
          </CommonSelect.Option>
        </CommonSelect>
      </SelectWrapper>
      {input.length > 0 || searchResults.length > 0 ? (
        <SeasonalAnimeList
          animeList={searchResults}
          sortBy={sortBy}
          filterBy={filterBy}
          page={page}
          setPage={setPage}
          pageSize={pageSize}
          selected={true}
        />
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

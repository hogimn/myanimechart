import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
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
import CommonSpin from "../../../common/basic/CommonSpin";

const StyledSeasonalTabs = styled.div`
  p {
    margin-left: 10px;
  }
`;

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

const LoadingWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
`;

const LOCAL_STORAGE_KEY = "seasonalFilters";

const SeasonalTabs = ({ season, year }) => {
  const navigate = useNavigate();

  const [activeTab, setActiveTab] = useState("2");
  const [page, setPage] = useState(1);
  const [searchResults, setSearchResults] = useState([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);

  const [sortBy, setSortBy] = useState(() => {
    const saved = localStorage.getItem(LOCAL_STORAGE_KEY);
    if (saved) {
      try {
        return JSON.parse(saved).sortBy || "score";
      } catch {
        return "score";
      }
    }
    return "score";
  });

  const [filterBy, setFilterBy] = useState(() => {
    const saved = localStorage.getItem(LOCAL_STORAGE_KEY);
    if (saved) {
      try {
        return JSON.parse(saved).filterBy || { type: "tv", airStatus: "all" };
      } catch {
        return { type: "tv", airStatus: "all" };
      }
    }
    return { type: "tv", airStatus: "all" };
  });

  let pageSize = 12;

  const handleSearch = async (keyword) => {
    setInput(keyword);
    if (!keyword.trim()) {
      setSearchResults([]);
      return;
    }

    setLoading(true);
    const data = await AnimeApi.findAnimesWithPollsByKeyword(keyword);
    setSearchResults(data);
    setPage(1);
    setLoading(false);
  };

  const [seasonData, setSeasonData] = useState([
    { season: getPreviousSeason(), year: getPreviousSeasonYear() },
    { season: getCurrentSeason(), year: getCurrentSeasonYear() },
  ]);

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
    {
      key: "archive",
      label: "Archive",
      content: null,
    },
  ];

  const handleTabChange = (key) => {
    if (key === "prev") {
      const season = getPreviousSeasonFromSeason(seasonData[1].season);
      const year = getPreviousSeasonYearFromYearAndSeason(
        seasonData[1].year,
        seasonData[1].season
      );
      navigate(`/seasonal-anime?year=${year}&season=${season}`);
      setPage(1);
    } else if (key === "next") {
      const season = getNextSeasonFromSeason(seasonData[1].season);
      const year = getNextSeasonYearFromYearAndSeason(
        seasonData[1].year,
        seasonData[1].season
      );
      navigate(`/seasonal-anime?year=${year}&season=${season}`);
      setPage(1);
    } else if (key === "archive") {
      navigate("/season-archive");
    } else {
      setActiveTab(key);
      setPage(1);
    }
  };

  useEffect(() => {
    const data = {
      sortBy,
      filterBy,
    };
    console.log(data);
    localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(data));
  }, [sortBy, filterBy]);

  useEffect(() => {
    if (season != null && year != null) {
      setSeasonData([
        {
          season: getPreviousSeasonFromSeason(season),
          year: parseInt(getPreviousSeasonYearFromYearAndSeason(year, season)),
        },
        { season: season, year: parseInt(year) },
      ]);
    }
  }, [season, year]);

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  useEffect(() => {
    setPage(1);
    window.scrollTo(0, 0);
  }, [filterBy]);

  return (
    <StyledSeasonalTabs>
      <AnimeSearchBox onSearch={handleSearch} />
      <SelectWrapper>
        <CommonSelect
          value={`Type: ${toTypeLabel(filterBy.type)}`}
          onChange={(value) => setFilterBy({ ...filterBy, type: value })}
        >
          <CommonSelect.Option value="tv">TV</CommonSelect.Option>
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
      {loading ? (
        <LoadingWrapper>
          <CommonSpin />
        </LoadingWrapper>
      ) : input.length > 0 || searchResults.length > 0 ? (
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
    </StyledSeasonalTabs>
  );
};

export default SeasonalTabs;

import PageTemplate from "../../common/template/PageTemplate";
import styled from "styled-components";
import { useEffect, useState } from "react";
import { formatDate } from "../../../util/dateUtil";
import CommonSpin from "../../common/basic/CommonSpin";
import CollectorApi from "../../api/anime/CollectorApi";
import CommonAlert from "../../common/basic/CommonAlert";
import {
  capitalizeFirstLetter,
  toCollectionStatusLabel,
} from "../../../util/strUtil";
import CommonPagination from "../../common/basic/CommonPagination";
import CommonCollapse from "../../common/basic/CommonCollapse";
import AnimeImage from "../seasonalanime/component/AnimeImage";
import TitleLink from "../../common/link/TitleLink";

const { Panel } = CommonCollapse;

const PollCollectionStatus = () => {
  const [animeGroups, setAnimeGroups] = useState({});
  const [statusCounts, setStatusCounts] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState({});
  const [activePanel, setActivePanel] = useState(null);
  const [statusFilter, setStatusFilter] = useState(null);
  const [rawData, setRawData] = useState([]);

  const activateInProgressPanel = (data, groupedData) => {
    const inProgressAnime = data.find(({ status }) => status === "IN_PROGRESS");
    if (inProgressAnime) {
      const groupKey = `${inProgressAnime.animeDto.year}-${inProgressAnime.animeDto.season}`;
      if (groupedData[groupKey]) {
        setActivePanel([groupKey]);
        const page = Math.ceil(
          (groupedData[groupKey].findIndex(
            ({ animeDto }) => animeDto.id === inProgressAnime.animeDto.id
          ) +
            1) /
            5
        );
        setCurrentPage((prev) => ({
          ...prev,
          [groupKey]: page,
        }));
      }
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const data = await CollectorApi.findAllPollCollectionStatusWithAnime();
        setRawData(data);

        const groupedData = data.reduce(
          (acc, { animeDto, status, startedAt, finishedAt }) => {
            const key = `${animeDto.year}-${animeDto.season}`;
            if (!acc[key]) acc[key] = [];
            acc[key].push({ animeDto, status, startedAt, finishedAt });
            return acc;
          },
          {}
        );
        setAnimeGroups(groupedData);

        const initialPageState = Object.keys(groupedData).reduce((acc, key) => {
          acc[key] = 1;
          return acc;
        }, {});
        setCurrentPage(initialPageState);

        const counts = data.reduce((acc, { status }) => {
          acc[status] = (acc[status] || 0) + 1;
          return acc;
        }, {});
        setStatusCounts(counts);

        activateInProgressPanel(data, groupedData);
      } catch (err) {
        console.log(err);
        setError("Failed to fetch poll collection status");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handlePageChange = (page, groupKey) => {
    setCurrentPage((prev) => ({
      ...prev,
      [groupKey]: page,
    }));
  };

  const handleStatusFilter = (status) => {
    setStatusFilter((prev) => (prev === status ? null : status));
    if (status) {
      setActivePanel(null);
    } else {
      activateInProgressPanel(rawData, animeGroups);
    }
  };

  const getPaginatedFilteredItems = (groupKey, items) => {
    const startIndex = 0;
    let endIndex = startIndex + 5;
    return items.slice(startIndex, endIndex);
  };

  const getPaginatedItems = (groupKey, items) => {
    const startIndex = (currentPage[groupKey] - 1) * 5;
    let endIndex = startIndex + 5;
    return items.slice(startIndex, endIndex);
  };

  const totalCount = Object.values(statusCounts).reduce(
    (sum, count) => sum + count,
    0
  );

  return (
    <PageTemplate>
      {error && <CommonAlert message={error} type="error" />}
      <Container>
        <StatusCountContainer>
          <StatusCountItem
            onClick={() => handleStatusFilter(null)}
            active={statusFilter === null}
          >
            <StatusCountLabel>Total</StatusCountLabel>
            <CountValue>{totalCount}</CountValue>
          </StatusCountItem>
          <StatusCountItem
            onClick={() => handleStatusFilter("COMPLETED")}
            active={statusFilter === "COMPLETED"}
          >
            <StatusCountLabel status="COMPLETED">Completed</StatusCountLabel>
            <CountValue>{statusCounts["COMPLETED"] || 0}</CountValue>
          </StatusCountItem>
          <StatusCountItem
            onClick={() => handleStatusFilter("IN_PROGRESS")}
            active={statusFilter === "IN_PROGRESS"}
          >
            <StatusCountLabel status="IN_PROGRESS">InProgress</StatusCountLabel>
            <CountValue>{statusCounts["IN_PROGRESS"] || 0}</CountValue>
          </StatusCountItem>
          <StatusCountItem
            onClick={() => handleStatusFilter("FAILED")}
            active={statusFilter === "FAILED"}
          >
            <StatusCountLabel status="FAILED">Failed</StatusCountLabel>
            <CountValue>{statusCounts["FAILED"] || 0}</CountValue>
          </StatusCountItem>
          <StatusCountItem
            onClick={() => handleStatusFilter("WAIT")}
            active={statusFilter === "WAIT"}
          >
            <StatusCountLabel status="WAIT">Wait</StatusCountLabel>
            <CountValue>{statusCounts["WAIT"] || 0}</CountValue>
          </StatusCountItem>
        </StatusCountContainer>

        {loading ? (
          <LoaderContainer>
            <CommonSpin />
          </LoaderContainer>
        ) : (
          <CommonCollapse
            accordion
            defaultActiveKey={activePanel}
            activeKey={activePanel}
            onChange={(key) => setActivePanel(key)}
          >
            {Object.entries(animeGroups).map(([key, animeList]) => {
              const [year, season] = key.split("-");

              const filteredAnimeList = statusFilter
                ? animeList.filter(({ status }) => status === statusFilter)
                : animeList;

              if (filteredAnimeList.length === 0) {
                return null;
              }

              let paginatedItems;
              if (statusFilter) {
                paginatedItems = getPaginatedFilteredItems(
                  key,
                  filteredAnimeList
                );
              } else {
                paginatedItems = getPaginatedItems(key, filteredAnimeList);
              }

              return (
                <Panel
                  header={`${year} ${capitalizeFirstLetter(season)}`}
                  key={key}
                >
                  <List>
                    {paginatedItems.map(
                      ({ animeDto, status, startedAt, finishedAt }) => (
                        <ListItem key={animeDto.id}>
                          <ImageWrapper>
                            <AnimeImage
                              src={animeDto.image}
                              alt={animeDto.title}
                            />
                          </ImageWrapper>
                          <AnimeInfo>
                            <TitleLink
                              href={animeDto.link}
                              target="_blank"
                              rel="noopener noreferrer"
                            >
                              <AnimeTitle>{animeDto.title}</AnimeTitle>
                            </TitleLink>
                            <StatusText status={status}>
                              {toCollectionStatusLabel(status)}
                            </StatusText>
                            <TimeText>
                              Last Start: {formatDate(startedAt)}
                            </TimeText>
                            <TimeText>
                              Last End: {formatDate(finishedAt)}
                            </TimeText>
                          </AnimeInfo>
                        </ListItem>
                      )
                    )}
                  </List>
                  <CommonPagination
                    current={currentPage[key]}
                    pageSize={5}
                    total={filteredAnimeList.length}
                    onChange={(page) => handlePageChange(page, key)}
                    showSizeChanger={false}
                    hideOnSinglePage
                  />
                </Panel>
              );
            })}
          </CommonCollapse>
        )}
      </Container>
    </PageTemplate>
  );
};

// Styled Components

const Container = styled.div`
  padding: 20px;

  .ant-pagination {
    margin-top: 10px;
  }
`;

const StatusCountContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(85px, 1fr));
  gap: 10px;
  margin-bottom: 20px;
  background-color: #282c34;
  padding: 10px;
  border-radius: 8px;
`;

const StatusCountItem = styled.div`
  text-align: center;
  color: #fff;
  font-size: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  background-color: ${({ active }) => (active ? "#3a3f4b" : "transparent")};
  border-radius: 6px;
  padding: 5px;
`;

const StatusCountLabel = styled.span`
  color: ${({ status }) =>
    status === "COMPLETED"
      ? "rgb(86,228,157)"
      : status === "WAIT"
      ? "rgb(92,92,92)"
      : status === "FAILED"
      ? "rgb(230,100,111)"
      : status === "IN_PROGRESS"
      ? "rgb(121,148,224)"
      : "#fff"};
  font-weight: bold;
`;

const CountValue = styled.div`
  font-size: 1.2rem;
  font-weight: bold;
  margin-top: 4px;
`;

const LoaderContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50vh;
`;

const List = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 5px;
`;

const ListItem = styled.div`
  display: flex;
  background-color: #1e1e1e;
  padding: 10px;
  border-radius: 8px;
  align-items: center;
`;

const AnimeInfo = styled.div`
  margin-left: 10px;
  display: flex;
  flex-direction: column;
`;

const AnimeTitle = styled.div`
  color: #a7ccf1;
  font-size: 1rem;
  font-weight: bold;
  line-height: normal;
`;

const StatusText = styled.div`
  margin: 5px 0;
  color: ${({ status }) =>
    status === "COMPLETED"
      ? "rgb(86,228,157)"
      : status === "WAIT"
      ? "rgb(92,92,92)"
      : status === "FAILED"
      ? "rgb(230,100,111)"
      : "rgb(121,148,224)"};
  font-weight: bold;
`;

const TimeText = styled.div`
  color: #ccc;
  font-size: 0.8em;
`;

const ImageWrapper = styled.div`
  display: flex;
  width: 80px;
  height: 120px;
  cursor: pointer;
`;

export default PollCollectionStatus;

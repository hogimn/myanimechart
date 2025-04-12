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

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const data = await CollectorApi.findAllPollCollectionStatusWithAnime();
        // 그룹별 데이터 생성 (year-season)
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
        // 페이지 초기화 (각 그룹당 첫 페이지)
        const initialPageState = Object.keys(groupedData).reduce((acc, key) => {
          acc[key] = 1;
          return acc;
        }, {});
        setCurrentPage(initialPageState);

        // 각 status별 건수 계산
        const counts = data.reduce((acc, { status }) => {
          acc[status] = (acc[status] || 0) + 1;
          return acc;
        }, {});
        setStatusCounts(counts);

        // IN_PROGRESS 상태인 항목 기준으로 초기 패널/페이지 설정
        const inProgressAnime = data.find(
          ({ status }) => status === "IN_PROGRESS"
        );
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
      } catch (err) {
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

  const getPaginatedItems = (groupKey, items) => {
    const startIndex = (currentPage[groupKey] - 1) * 5;
    const endIndex = startIndex + 5;
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
          <StatusCountItem>
            <StatusCountLabel>Total</StatusCountLabel>
            <CountValue>{totalCount}</CountValue>
          </StatusCountItem>
          <StatusCountItem>
            <StatusCountLabel status="COMPLETED">Completed</StatusCountLabel>
            <CountValue>{statusCounts["COMPLETED"] || 0}</CountValue>
          </StatusCountItem>
          <StatusCountItem>
            <StatusCountLabel status="IN_PROGRESS">InProgress</StatusCountLabel>
            <CountValue>{statusCounts["IN_PROGRESS"] || 0}</CountValue>
          </StatusCountItem>
          <StatusCountItem>
            <StatusCountLabel status="FAILED">Failed</StatusCountLabel>
            <CountValue>{statusCounts["FAILED"] || 0}</CountValue>
          </StatusCountItem>
          <StatusCountItem>
            <StatusCountLabel status="WAIT">Wait</StatusCountLabel>
            <CountValue>{statusCounts["WAIT"] || 0}</CountValue>
          </StatusCountItem>
        </StatusCountContainer>

        {loading ? (
          <LoaderContainer>
            <CommonSpin />
          </LoaderContainer>
        ) : (
          <CommonCollapse accordion defaultActiveKey={activePanel}>
            {Object.entries(animeGroups).map(([key, animeList]) => {
              const [year, season] = key.split("-");
              const paginatedItems = getPaginatedItems(key, animeList);

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
                    total={animeList.length}
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

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

const { Panel } = CommonCollapse;

const PollCollectionStatus = () => {
  const [animeGroups, setAnimeGroups] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState({});

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const data = await CollectorApi.findAllPollCollectionStatusWithAnime();
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

  return (
    <PageTemplate>
      {error && <CommonAlert message={error} type="error" />}
      <Container>
        {loading ? (
          <LoaderContainer>
            <CommonSpin size="large" />
          </LoaderContainer>
        ) : (
          <CommonCollapse
            accordion
            defaultActiveKey={[Object.keys(animeGroups)[0]]}
          >
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
                        <ListItem key={animeDto.id} status={status}>
                          <ImageWrapper>
                            <AnimeImage
                              src={animeDto.image}
                              alt={animeDto.title}
                            />
                          </ImageWrapper>
                          <AnimeInfo>
                            <AnimeTitle>{animeDto.title}</AnimeTitle>
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
      ? "rgb(86, 228, 157)"
      : status === "WAIT"
      ? "rgb(92, 92, 92)"
      : status === "FAILED"
      ? "rgb(230, 100, 111)"
      : "rgb(121, 148, 224)"};
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

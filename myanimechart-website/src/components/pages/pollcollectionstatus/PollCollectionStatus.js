import PageTemplate from "../../common/template/PageTemplate";
import styled from "styled-components";
import { useEffect, useState } from "react";
import { formatDate } from "../../../util/dateUtil";
import CommonSpin from "../../common/basic/CommonSpin";
import CollectorApi from "../../api/anime/CollectorApi";
import CommonAlert from "../../common/basic/CommonAlert";

const PollCollectionStatus = () => {
  const [animeList, setAnimeList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const data = await CollectorApi.findAllPollCollectionStatusWithAnime();
        setAnimeList(data);
      } catch (err) {
        setError("Failed to fetch poll collection status");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  return (
    <PageTemplate>
      {error && <CommonAlert message={error} type="error" />}
      <Container>
        {loading ? (
          <LoaderContainer>
            <CommonSpin size="large" />
          </LoaderContainer>
        ) : (
          <List>
            {animeList.map(({ animeDto, status, startedAt, finishedAt }) => (
              <ListItem key={animeDto.id} status={status}>
                <AnimeImage src={animeDto.image} alt={animeDto.title} />
                <AnimeInfo>
                  <AnimeTitle>{animeDto.title}</AnimeTitle>
                  <StatusText status={status}>{status}</StatusText>
                  <TimeText>Last Start: {formatDate(startedAt)}</TimeText>
                  <TimeText>Last End: {formatDate(finishedAt)}</TimeText>
                </AnimeInfo>
              </ListItem>
            ))}
          </List>
        )}
      </Container>
    </PageTemplate>
  );
};

const Container = styled.div`
  padding: 20px;
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
`;

const ListItem = styled.div`
  display: flex;
  background-color: #1e1e1e;
  padding: 10px;
  border-radius: 8px;
  align-items: center;
`;

const AnimeImage = styled.img`
  width: 70px;
  height: 100px;
  border-radius: 4px;
`;

const AnimeInfo = styled.div`
  margin-left: 10px;
  display: flex;
  flex-direction: column;
`;

const AnimeTitle = styled.div`
  color: white;
  font-weight: bold;
`;

const StatusText = styled.div`
  margin: 5px 0;
  color: ${({ status }) =>
    status === "COMPLETED"
      ? "#4caf50"
      : status === "WAIT"
      ? "#9e9e9e"
      : status === "FAILED"
      ? "#f44336"
      : "#2196f3"};
  font-weight: bold;
`;

const TimeText = styled.div`
  color: #ccc;
  font-size: 0.8em;
`;

export default PollCollectionStatus;

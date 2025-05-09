import React, { useEffect, useRef, useState } from "react";
import { Bar } from "react-chartjs-2";
import { Chart as ChartJS } from "chart.js";
import CommonModal from "../../../common/basic/CommonModal";
import styled from "styled-components";
import ModalButton from "../../../common/button/ModalButton";
import { isMobile } from "react-device-detect";

const ScrollableContent = styled.div`
  max-height: 50vh;
  overflow-y: auto;
  padding-right: 0.5rem;
  font-size: 13px;
`;

const StyledTotalVotes = styled.div`
  font-size: 0.9rem;
  font-weight: bold;
  color: rgb(149, 195, 255);
  text-align: left;
  margin-bottom: ${(props) => (props.marginBottom ? props.marginBottom : 0)};
`;

const VoteList = styled.ul`
  margin: 0;
  padding: 0;
  list-style: none;
`;

const VoteItem = styled.li`
  margin-bottom: 1rem;
`;

const ProgressBarBackground = styled.div`
  width: 100%;
  background-color: rgba(81, 84, 102, 0.24);
  height: 1rem;
  margin-top: 0.25rem;
  border-radius: 0.375rem;
`;

const ProgressBarFill = styled.div`
  height: 1rem;
  background-color: rgba(99, 154, 255, 1);
  border-radius: 0.375rem;
  width: ${(props) => props.width}%;
`;

const EpisodeList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;
`;

const EpisodeItem = styled.li`
  margin-bottom: 0.5rem;
  cursor: pointer;
  color: #9cc9ff;

  &:hover {
    text-decoration: underline;
  }
`;

const zoomOptions = {
  zoom: {
    wheel: {
      enabled: true,
      modifierKey: "ctrl",
    },
    pinch: {
      enabled: true,
    },
    mode: "x",
  },
  pan: {
    enabled: !isMobile,
    mode: "x",
  },
};

const AnimePollGraph = ({ polls }) => {
  const chartRef = useRef(null);

  const [modalData, setModalData] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [showAllEpisodes, setShowAllEpisodes] = useState(false);

  const validPolls = polls ?? [];
  const pollOptions = [1, 2, 3, 4, 5];

  const episodes = Array.from(
    new Set(validPolls.map((poll) => poll.episode))
  ).sort((a, b) => a - b);

  const dataPerEpisode = episodes.map((episode) => {
    const pollsForEpisode = validPolls.filter(
      (poll) => poll.episode === episode
    );
    const totalVotes = pollsForEpisode.reduce(
      (sum, poll) => sum + poll.votes,
      0
    );

    return {
      episode,
      totalVotes,
      topicId: pollsForEpisode[0]?.topicId,
      optionVotes: pollOptions.map((optionId) => {
        const pollOption = pollsForEpisode.find(
          (poll) => poll.pollOptionId === optionId
        );
        return pollOption ? pollOption.votes : 0;
      }),
    };
  });

  const averageScores = dataPerEpisode.map((data) => {
    const weightedSum = data.optionVotes.reduce(
      (sum, votes, index) => sum + votes * (index + 1),
      0
    );
    return weightedSum / data.totalVotes;
  });

  const baseDatasetConfig = {
    tension: 0.3,
    borderRadius: 8,
    borderWidth: 1.5,
    pointRadius: 3,
    borderDash: [4.5, 4.5],
  };

  const chartData = {
    labels: episodes.map((ep) => `Ep.${ep}`),
    datasets: [
      {
        label: "Avg. Score",
        type: "line",
        data: averageScores,
        borderColor: "rgba(183, 221, 247, 0.9)",
        pointBackgroundColor: "rgba(255, 255, 255, 0)",
        yAxisID: "y1",
        ...baseDatasetConfig,
      },
      ...pollOptions.map((option, index) => ({
        label: `★${option}`,
        data: dataPerEpisode.map((data) => data.optionVotes[index]),
        borderColor: [
          "rgba(163, 48, 80, 1)",
          "rgba(139, 63, 124, 1)",
          "rgba(114, 83, 166, 1)",
          "rgba(88, 120, 198, 1)",
          "rgba(74, 144, 226, 1)",
        ][index],
        backgroundColor: [
          "rgba(163, 48, 80, 0.25)",
          "rgba(139, 63, 124, 0.25)",
          "rgba(114, 83, 166, 0.25)",
          "rgba(88, 120, 198, 0.25)",
          "rgba(74, 144, 226, 0.25)",
        ][index],
        stack: "Stack 0",
        ...baseDatasetConfig,
      })),
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    interaction: {
      mode: "index",
      intersect: false,
    },
    plugins: {
      tooltip: {
        animation: {
          duration: 0,
        },
        interaction: {
          mode: "index",
          intersect: false,
        },
        position: "poll",
        backgroundColor: "rgba(0, 0, 0, 0.9)",
        itemSort: (a, b) => {
          return b.datasetIndex - a.datasetIndex;
        },
        titleFont: {
          size: 10,
        },
        bodyFont: {
          size: 10,
        },
        callbacks: {
          labelColor: (tooltipItem, chart) => {
            const datasetIndex = tooltipItem.datasetIndex;
            const borderColor = chartData.datasets[datasetIndex].borderColor;
            return {
              borderColor: borderColor,
              backgroundColor: borderColor,
            };
          },
          label: (context) => {
            const datasetLabel = context.dataset.label || "";
            const value = context.raw;

            if (datasetLabel.includes("Avg.")) {
              return `${datasetLabel}: ${value.toFixed(2)}`;
            }

            const episodeIndex = context.dataIndex;
            const optionIndex = pollOptions.indexOf(
              parseInt(datasetLabel.replace("★", "").replace(".0", ""))
            );

            if (optionIndex !== -1) {
              const totalVotes = dataPerEpisode[episodeIndex].totalVotes;
              const votes = value;
              const percentage = ((votes / totalVotes) * 100).toFixed(1);
              return `${datasetLabel}: ${votes} votes (${percentage}%)`;
            }

            return `${datasetLabel}: ${value} votes`;
          },
        },
      },
      legend: {
        position: "top",
        labels: {
          color: "#ffffff",
          boxHeight: 7,
          usePointStyle: true,
          generateLabels: (chart) => {
            const labels =
              ChartJS.defaults.plugins.legend.labels.generateLabels(chart);
            return labels.reverse().map((label) => {
              label.fillStyle = label.strokeStyle;
              if (label.hidden) {
                label.fontColor = "rgba(255, 255, 255, 0.4)";
                label.lineWidth = 0;
              } else {
                label.fontColor = "rgba(255, 255, 255, 0.8)";
                label.lineWidth = 5;
              }
              return label;
            });
          },
        },
      },
      zoom: zoomOptions,
    },
    scales: {
      x: {
        stacked: true,
        ticks: {
          color: "rgba(192, 192, 192, 0.57)",
          autoSkipPadding: 10,
          maxRotation: 0,
          minRotation: 0,
        },
      },
      y: {
        position: "right",
        stacked: true,
        ticks: {
          color: "rgba(192, 192, 192, 0.57)",
        },
      },
      y1: {
        position: "left",
        beginAtZero: false,
        ticks: {
          color: "rgba(192, 192, 192, 0.57)",
          callback: (value) => value.toFixed(2),
        },
      },
    },
    animation: {
      duration: 600,
    },
    onClick: (event, elements) => {
      if (elements.length > 0) {
        const element = elements[0];
        const episodeIndex = element.index;
        const data = dataPerEpisode[episodeIndex];

        setModalData({
          episode: data.episode,
          totalVotes: data.totalVotes,
          averageScore: averageScores[episodeIndex],
          votesBreakdown: pollOptions.map((option, index) => {
            const votes = data.optionVotes[index];
            const percentage = ((votes / data.totalVotes) * 100).toFixed(1);
            return { option, votes, percentage };
          }),
          topicId: data.topicId,
        });
        setIsModalOpen(true);
        openEpisodeModal(data.episode);
      }
    },
  };

  const openEpisodeModal = (episode) => {
    const data = dataPerEpisode.find((d) => d.episode === episode);
    const index = episodes.indexOf(episode);

    setModalData({
      episode: data.episode,
      totalVotes: data.totalVotes,
      averageScore: averageScores[index],
      votesBreakdown: pollOptions.map((option, i) => {
        const votes = data.optionVotes[i];
        const percentage = ((votes / data.totalVotes) * 100).toFixed(1);
        return { option, votes, percentage };
      }),
      topicId: data.topicId,
    });
    setIsModalOpen(true);
    setShowAllEpisodes(false);
  };

  useEffect(() => {
    const handleTouchEnd = (event) => {
      if (event.target.tagName.toLowerCase() !== "canvas") {
        chartRef.current.canvas.dispatchEvent(new Event("mouseout"));
      }
    };

    document.addEventListener("touchend", handleTouchEnd);
    return () => {
      document.removeEventListener("touchend", handleTouchEnd);
    };
  }, []);

  return (
    <>
      <Bar ref={chartRef} options={options} data={chartData} />

      {isModalOpen && modalData && (
        <CommonModal
          title={
            showAllEpisodes ? "All Episodes" : `Episode ${modalData.episode}`
          }
          open={isModalOpen}
          onCancel={() => setIsModalOpen(false)}
          footer={null}
          centered
        >
          <ScrollableContent>
            {showAllEpisodes ? (
              <EpisodeList>
                {episodes.map((ep) => (
                  <EpisodeItem key={ep} onClick={() => openEpisodeModal(ep)}>
                    Episode {ep}
                  </EpisodeItem>
                ))}
              </EpisodeList>
            ) : (
              <>
                <StyledTotalVotes>
                  Average Score: {modalData.averageScore.toFixed(2)}
                </StyledTotalVotes>
                <StyledTotalVotes marginBottom={"1rem"}>
                  Total Votes: {modalData.totalVotes}
                </StyledTotalVotes>
                <VoteList>
                  {[...modalData.votesBreakdown]
                    .reverse()
                    .map(({ option, votes, percentage }) => (
                      <VoteItem key={option}>
                        ★{option}: {votes} votes ({percentage}%)
                        <ProgressBarBackground>
                          <ProgressBarFill width={percentage} />
                        </ProgressBarBackground>
                      </VoteItem>
                    ))}
                </VoteList>
                <ModalButton
                  onClick={() =>
                    window.open(
                      `https://myanimelist.net/forum/?topicid=${modalData.topicId}`,
                      "_blank"
                    )
                  }
                >
                  Go to Discussion
                </ModalButton>
                <div
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    marginTop: "1rem",
                    gap: "0.5rem",
                  }}
                >
                  <ModalButton
                    onClick={() => {
                      const currentIdx = episodes.indexOf(modalData.episode);
                      if (currentIdx > 0) {
                        openEpisodeModal(episodes[currentIdx - 1]);
                      }
                    }}
                    disabled={modalData.episode === episodes[0]}
                  >
                    Prev
                  </ModalButton>
                  <ModalButton onClick={() => setShowAllEpisodes(true)}>
                    All Episodes
                  </ModalButton>
                  <ModalButton
                    onClick={() => {
                      const currentIdx = episodes.indexOf(modalData.episode);
                      if (currentIdx < episodes.length - 1) {
                        openEpisodeModal(episodes[currentIdx + 1]);
                      }
                    }}
                    disabled={
                      modalData.episode === episodes[episodes.length - 1]
                    }
                  >
                    Next
                  </ModalButton>
                </div>
              </>
            )}
          </ScrollableContent>
        </CommonModal>
      )}
    </>
  );
};

export default AnimePollGraph;

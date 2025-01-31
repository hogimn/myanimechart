import React, { useState } from "react";
import { Bar } from "react-chartjs-2";
import {
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LinearScale,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  BarElement,
} from "chart.js";
import zoomPlugin from "chartjs-plugin-zoom";
import { isMobile } from "react-device-detect";
import CommonModal from "../../../common/basic/CommonModal";
import styled from "styled-components";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend,
  zoomPlugin
);

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

const StyledTotalVotes = styled.div`
  margin-bottom: 1rem;
  font-size: 1.2rem;
  font-weight: bold;
  color: #3b82f6;
  text-align: center;
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
  background-color: #e2e8f0;
  height: 1rem;
  margin-top: 0.25rem;
  border-radius: 0.375rem;
`;

const ProgressBarFill = styled.div`
  height: 1rem;
  background-color: #3b82f6;
  border-radius: 0.375rem;
  width: ${(props) => props.width}%;
`;

const StyledButton = styled.button`
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 0.375rem;
  cursor: pointer;
  &:hover {
    background-color: #2563eb;
  }
`;

const AnimePollGraph = ({ polls }) => {
  const [modalData, setModalData] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const episodes = Array.from(new Set(polls.map((poll) => poll.episode))).sort(
    (a, b) => a - b
  );

  const pollOptions = [1, 2, 3, 4, 5];

  const dataPerEpisode = episodes.map((episode) => {
    const pollsForEpisode = polls.filter((poll) => poll.episode === episode);
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
    tension: 0.1,
    pointRadius: 4,
    pointHitRadius: 5,
    borderRadius: 8,
    borderWith: 2,
    borderDash: [8, 4],
  };

  const chartData = {
    labels: episodes.map((ep) => `ep ${ep}`),
    datasets: [
      {
        label: "Avg. Score",
        type: "line",
        data: averageScores,
        borderColor: "rgba(38, 194, 229, 0.4)",
        backgroundColor: "rgba(38, 194, 229, 0.4)",
        pointBackgroundColor: "rgba(38, 194, 229, 1)",
        yAxisID: "y1",
        ...baseDatasetConfig,
      },
      ...pollOptions.map((option, index) => ({
        label: `★${option}`,
        data: dataPerEpisode.map((data) => data.optionVotes[index]),
        backgroundColor: `rgba(${(index + 1) * 50}, ${
          (index + 1) * 100
        }, 255, 0.4)`,
        pointBackgroundColor: `rgba(${(index + 1) * 50}, ${
          (index + 1) * 100
        }, 255, 1)`,
        stack: "Stack 0",
        ...baseDatasetConfig,
      })),
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      tooltip: {
        callbacks: {
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
          boxHeight: 9,
          usePointStyle: true,
          generateLabels: (chart) => {
            const labels =
              ChartJS.defaults.plugins.legend.labels.generateLabels(chart);
            return labels.map((label) => {
              if (label.hidden) {
                label.fontColor = "rgba(255, 255, 255, 0.2)";
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
        title: {
          display: false,
          text: "Episode",
          color: "#ffffff",
        },
        ticks: {
          color: "#ffffff",
        },
      },
      y: {
        position: "right",
        stacked: true,
        title: {
          display: false,
          text: "Votes",
          color: "#ffffff",
        },
        ticks: {
          color: "#ffffff",
        },
      },
      y1: {
        position: "left",
        title: {
          display: false,
          text: "Avg. Score",
          color: "#ffffff",
        },
        ticks: {
          color: "#ffffff",
        },
        beginAtZero: false,
        grid: {
          drawOnChartArea: false,
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
          votesBreakdown: pollOptions.map((option, index) => {
            const votes = data.optionVotes[index];
            const percentage = ((votes / data.totalVotes) * 100).toFixed(1);
            return { option, votes, percentage };
          }),
          topicId: data.topicId,
        });
        setIsModalOpen(true);
      }
    },
  };

  return (
    <>
      <Bar options={options} data={chartData} />

      {isModalOpen && modalData && (
        <CommonModal
          title={`Episode ${modalData.episode}`}
          open={isModalOpen}
          onCancel={() => setIsModalOpen(false)}
          footer={null}
          centered
        >
          <StyledTotalVotes>
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
          <StyledButton
            onClick={() =>
              window.open(
                `https://myanimelist.net/forum/?topicid=${modalData.topicId}`,
                "_blank"
              )
            }
          >
            Go to Discussion
          </StyledButton>
        </CommonModal>
      )}
    </>
  );
};

export default AnimePollGraph;

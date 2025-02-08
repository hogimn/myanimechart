import React, { useRef, useState } from "react";
import { Bar } from "react-chartjs-2";
import { Chart as ChartJS } from "chart.js";
import CommonModal from "../../../common/basic/CommonModal";
import styled from "styled-components";
import StyledZoomButton from "../../../common/button/ZoomButton";

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
  const chartRef = useRef(null);

  const [modalData, setModalData] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [zoomEnabled, setZoomEnabled] = useState(false);

  const handleZoomToggle = () => {
    setZoomEnabled((prev) => !prev);
  };

  const zoomOptions = {
    zoom: {
      wheel: {
        enabled: zoomEnabled,
      },
      pinch: {
        enabled: zoomEnabled,
      },
      mode: "x",
    },
    pan: {
      enabled: zoomEnabled,
      mode: "x",
    },
  };

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
    tension: 0.3,
    pointRadius: 3.0,
    pointHoverRadius: 8,
    pointHitRadius: 6,
    borderRadius: 7,
    borderWidth: 2,
    borderDash: [4, 2],
  };

  const chartData = {
    labels: episodes.map((ep) => `ep${ep}`),
    datasets: [
      {
        label: "Avg. Score",
        type: "line",
        data: averageScores,
        borderColor: "rgb(174, 209, 241)",
        backgroundColor: "rgba(118, 165, 211, 0.2)",
        pointBackgroundColor: "rgb(92, 111, 131)",
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
        pointBackgroundColor: [
          "rgba(163, 48, 80, 1)",
          "rgba(139, 63, 124, 1)",
          "rgba(114, 83, 166, 1)",
          "rgba(88, 120, 198, 1)",
          "rgba(74, 144, 226, 1)",
        ][index],
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
        yAlign: "bottom",
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
            return labels.reverse().map((label) => {
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
        ticks: {
          color: "rgba(192, 192, 192, 0.57)",
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
      <StyledZoomButton
        zoomEnabled={zoomEnabled}
        top={"260px"}
        onClick={handleZoomToggle}
      />
      <Bar ref={chartRef} options={options} data={chartData} />

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

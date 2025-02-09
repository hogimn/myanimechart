import React, { useRef, useState } from "react";
import { Bar } from "react-chartjs-2";
import { Chart as ChartJS } from "chart.js";
import StyledZoomButton from "../../../common/button/ZoomButton";

const AnimePollGraph = ({ polls }) => {
  const chartRef = useRef(null);
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
    pointHoverRadius: 6,
    pointHitRadius: 8,
    borderRadius: 8,
    borderWidth: 1.5,
    borderDash: [5, 2],
    pointRadius: 3,
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
        mode: "index",
        intersect: false,
        yAlign: "bottom",
        itemSort: (a, b) => {
          return b.datasetIndex - a.datasetIndex;
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
        window.open(
          `https://myanimelist.net/forum/?topicid=${data.topicId}`,
          "_blank"
        );
      }
    },
  };

  return (
    <>
      <StyledZoomButton
        zoomEnabled={zoomEnabled}
        top={"270px"}
        onClick={handleZoomToggle}
      />
      <Bar ref={chartRef} options={options} data={chartData} />
    </>
  );
};

export default AnimePollGraph;

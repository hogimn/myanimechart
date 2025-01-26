import React from "react";
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

const AnimePollGraph = ({ polls }) => {
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
    pointRadius: 0,
    pointHoverRadius: 5,
    pointHitRadius: 5,
  };

  const chartData = {
    labels: episodes.map((ep) => `ep ${ep}`),
    datasets: [
      {
        label: "Avg. Score",
        type: "line",
        data: averageScores,
        borderColor: "rgb(104,255,242)",
        backgroundColor: "rgb(104,255,242)",
        pointBackgroundColor: "rgb(104,255,242)",
        yAxisID: "y1",
        ...baseDatasetConfig,
      },
      {
        label: `★1.0`,
        data: dataPerEpisode.map((data) => data.optionVotes[0]),
        borderColor: "rgb(145, 118, 163)",
        backgroundColor: "rgb(145, 118, 163)",
        pointBackgroundColor: "rgb(145, 118, 163)",
        stack: "Stack 0",
        ...baseDatasetConfig,
      },
      {
        label: `★2.0`,
        data: dataPerEpisode.map((data) => data.optionVotes[1]),
        borderColor: "rgb(58, 53, 104)",
        backgroundColor: "rgb(58, 53, 104)",
        pointBackgroundColor: "rgb(58, 53, 104)",
        stack: "Stack 0",
        ...baseDatasetConfig,
      },
      {
        label: `★3.0`,
        data: dataPerEpisode.map((data) => data.optionVotes[2]),
        borderColor: "rgb(116, 110, 194)",
        backgroundColor: "rgb(116, 110, 194)",
        pointBackgroundColor: "rgb(116, 110, 194)",
        stack: "Stack 0",
        ...baseDatasetConfig,
      },
      {
        label: `★4.0`,
        data: dataPerEpisode.map((data) => data.optionVotes[3]),
        borderColor: "rgb(158, 165, 228)",
        backgroundColor: "rgb(158, 165, 228)",
        pointBackgroundColor: "rgb(158, 165, 228)",
        stack: "Stack 0",
        ...baseDatasetConfig,
      },
      {
        label: `★5.0`,
        data: dataPerEpisode.map((data) => data.optionVotes[4]),
        borderColor: "rgb(205, 208, 250)",
        backgroundColor: "rgb(205, 208, 250)",
        pointBackgroundColor: "rgb(205, 208, 250)",
        stack: "Stack 0",
        ...baseDatasetConfig,
      },
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

            if (datasetLabel.includes("Average")) {
              return `${datasetLabel}: ${value.toFixed(2)}`;
            }

            const episodeIndex = context.dataIndex;
            const optionIndex = pollOptions.indexOf(
              parseInt(datasetLabel.replace("☆", "").replace(".0", ""))
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
                label.fontColor = "#c9e0ff";
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
  };

  return <Bar options={options} data={chartData} />;
};

export default AnimePollGraph;

import React from "react";
import { Bar } from "react-chartjs-2";
import {
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LinearScale,
  LineElement,
  PointElement,
  TimeScale,
  Title,
  Tooltip,
  BarElement,
} from "chart.js";
import zoomPlugin from "chartjs-plugin-zoom";
import "chartjs-adapter-date-fns";
import { isMobile } from "react-device-detect";

const plugin = {
  id: "increase-legend-spacing",
  beforeInit(chart) {
    const originalFit = chart.legend.fit;

    chart.legend.fit = function fit() {
      originalFit.bind(chart.legend)();
      this.height += 20;
    };
  },
};

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  zoomPlugin,
  TimeScale,
  plugin,
  BarElement,
  CategoryScale
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
      optionPercentages: pollOptions.map((optionId) => {
        const pollOption = pollsForEpisode.find(
          (poll) => poll.pollOptionId === optionId
        );
        const votes = pollOption ? pollOption.votes : 0;
        return totalVotes > 0 ? ((votes / totalVotes) * 100).toFixed(1) : "0.0";
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

  const chartData = {
    labels: episodes.map((ep) => `ep ${ep}`),
    datasets: [
      ...pollOptions.map((optionId, index) => ({
        label: `☆${optionId}.0`,
        data: dataPerEpisode.map((data) => data.optionVotes[index]),
        backgroundColor: `rgba(${50 + index * 40}, ${
          100 + index * 30
        }, 255, 0.6)`,
      })),
      {
        label: "Average Score",
        type: "line",
        data: averageScores,
        borderColor: "rgb(104,255,242)",
        backgroundColor: "rgb(104,255,242)",
        pointBackgroundColor: "rgb(104,255,242)",
        tension: 0.4,
        yAxisID: "y1",
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
              const percentage =
                dataPerEpisode[episodeIndex].optionPercentages[optionIndex];
              return `${datasetLabel}: ${value} votes (${percentage}%)`;
            }

            return `${datasetLabel}: ${value} votes`;
          },
        },
      },
      legend: {
        position: "top",
        labels: {
          usePointStyle: true,
        },
      },
      zoom: zoomOptions,
    },
    scales: {
      x: {
        title: {
          display: false,
          text: "Episode",
        },
      },
      y: {
        title: {
          display: false,
          text: "Votes",
        },
        beginAtZero: true,
      },
      y1: {
        position: "right",
        title: {
          display: false,
          text: "Average Score",
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

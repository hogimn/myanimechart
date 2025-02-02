import React, { useEffect, useRef, useState } from "react";
import { Line } from "react-chartjs-2";
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
} from "chart.js";
import zoomPlugin from "chartjs-plugin-zoom";
import "chartjs-adapter-date-fns";
import { parseISO } from "date-fns";
import CommonButton from "../../../common/basic/CommonButton";
import { isMobile } from "react-device-detect";
import styled from "styled-components";
import { MdRestore } from "react-icons/md";

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
  plugin
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

const StyledResetButton = styled(CommonButton)`
  background-color: rgba(0, 0, 0, 0.6);
  position: absolute;
  top: -48px;
  left: 180px;
  padding: 7px;
  margin: 0;
  border-radius: 8px;
  color: #fff;

  &:hover {
    background-color: rgba(0, 0, 0, 0.8);
  }

  @media (max-width: 768px) {
    left: 112px;
  }
`;

const AnimeStatGraph = ({ animeStats, selectedLegend }) => {
  const [activeLegend, setActiveLegend] = useState("");
  const chartRef = useRef(null);

  useEffect(() => {
    if (selectedLegend === "startDate") {
      const val = localStorage.getItem("selectedLegend");
      setActiveLegend(val);
    } else {
      setActiveLegend(selectedLegend);
      localStorage.setItem("selectedLegend", selectedLegend);
    }
  }, [selectedLegend]);

  const handleResetZoom = () => {
    if (chartRef.current) {
      chartRef.current.resetZoom();
    }
  };

  const baseDatasetConfig = {
    tension: 0.5,
    pointRadius: 0,
    pointHoverRadius: 4,
    pointHitRadius: 8,
    borderRadius: 8,
    borderWidth: 1.5,
    borderDash: [],
  };

  const chartData = {
    labels: animeStats.map((stat) => parseISO(stat.recordedAt)),
    datasets: [
      {
        label: "Score",
        data: animeStats.map((stat) => stat.score),
        borderColor: "#6EA8FE",
        backgroundColor: "rgba(110, 168, 254, 0.15)",
        pointBackgroundColor: "#6EA8FE",
        hidden: activeLegend && activeLegend !== "score",
        ...baseDatasetConfig,
      },
      {
        label: "Votes",
        data: animeStats.map((stat) => stat.scoringCount),
        borderColor: "#4C79FF",
        backgroundColor: "rgba(76, 121, 255, 0.15)",
        pointBackgroundColor: "#4C79FF",
        hidden: activeLegend && activeLegend.toLowerCase() !== "votes",
        ...baseDatasetConfig,
      },
      {
        label: "Rank",
        data: animeStats.map((stat) => stat.rank),
        borderColor: "#6D5DFF",
        backgroundColor: "rgba(109, 93, 255, 0.15)",
        pointBackgroundColor: "#6D5DFF",
        hidden: activeLegend && activeLegend !== "rank",
        ...baseDatasetConfig,
      },
      {
        label: "Members",
        data: animeStats.map((stat) => stat.members),
        borderColor: "#B95EFF",
        backgroundColor: "rgba(185, 94, 255, 0.15)",
        pointBackgroundColor: "#B95EFF",
        hidden: activeLegend && activeLegend !== "members",
        ...baseDatasetConfig,
      },
      {
        label: "Popularity",
        data: animeStats.map((stat) => stat.popularity),
        borderColor: "#FF6584",
        backgroundColor: "rgba(255, 101, 132, 0.15)",
        pointBackgroundColor: "#FF6584",
        hidden: activeLegend && activeLegend !== "popularity",
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
          label: (context) => `${context.dataset.label}: ${context.raw}`,
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
        onClick: (e, legendItem) => {
          const clickedLegend = legendItem.text.toLowerCase();
          setActiveLegend(clickedLegend);
        },
      },
      zoom: zoomOptions,
    },
    scales: {
      x: {
        type: "time",
        time: {
          unit: "day",
        },
        ticks: {
          color: "#ffffff",
          callback: function (value, index) {
            return "";
          },
        },
      },
      y: {
        ticks: {
          color: "#ffffff",
        },
      },
    },
    animation: {
      duration: 0,
    },
  };

  return (
    <>
      <StyledResetButton onClick={handleResetZoom}>
        <MdRestore />
      </StyledResetButton>
      <Line ref={chartRef} data={chartData} options={options} />
    </>
  );
};

export default AnimeStatGraph;

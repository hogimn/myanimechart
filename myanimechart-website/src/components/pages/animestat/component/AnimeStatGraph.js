import React, { useEffect, useRef, useState } from "react";
import { Line } from "react-chartjs-2";
import { Chart as ChartJS } from "chart.js";
import "chartjs-adapter-date-fns";
import { format, parseISO } from "date-fns";
import ZoomButton from "../../../common/button/ZoomButton";
import { createGradient } from "../../../../util/chartUtil";

const AnimeStatGraph = ({ animeStats, selectedLegend }) => {
  const chartRef = useRef(null);
  const [activeLegend, setActiveLegend] = useState("");
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

  useEffect(() => {
    if (selectedLegend === "startDate") {
      const val = localStorage.getItem("selectedLegend");
      setActiveLegend(val);
    } else {
      setActiveLegend(selectedLegend);
      localStorage.setItem("selectedLegend", selectedLegend);
    }
  }, [selectedLegend]);

  const baseDatasetConfig = {
    tension: 0.3,
    pointRadius: 0,
    pointHoverRadius: 6,
    pointHitRadius: 8,
    borderRadius: 8,
    borderWidth: 1.5,
    borderDash: [],
    fill: true,
  };

  const chartData = {
    labels: animeStats.map((stat) => parseISO(stat.recordedAt)),
    datasets: [
      {
        label: "Score",
        data: animeStats.map((stat) => stat.score),
        borderColor: "#6EA8FE",
        backgroundColor: (ctx) => {
          const { chartArea } = ctx.chart;
          if (!chartArea) return;
          const ctxRef = ctx.chart.ctx;
          return createGradient(
            ctxRef,
            chartArea,
            "rgba(110, 168, 254, 0.5)",
            "rgba(110, 168, 254, 0.0)"
          );
        },
        pointBackgroundColor: "#6EA8FE",
        hidden: activeLegend && activeLegend !== "score",
        ...baseDatasetConfig,
      },
      {
        label: "Votes",
        data: animeStats.map((stat) => stat.scoringCount),
        borderColor: "#4C79FF",
        backgroundColor: (ctx) => {
          const { chartArea } = ctx.chart;
          if (!chartArea) return;
          const ctxRef = ctx.chart.ctx;
          return createGradient(
            ctxRef,
            chartArea,
            "rgba(76, 121, 255, 0.5)",
            "rgba(76, 121, 255, 0.0)"
          );
        },
        pointBackgroundColor: "#4C79FF",
        hidden: activeLegend && activeLegend.toLowerCase() !== "votes",
        ...baseDatasetConfig,
      },
      {
        label: "Rank",
        data: animeStats.map((stat) => stat.rank),
        borderColor: "#6D5DFF",
        backgroundColor: (ctx) => {
          const { chartArea } = ctx.chart;
          if (!chartArea) return;
          const ctxRef = ctx.chart.ctx;
          return createGradient(
            ctxRef,
            chartArea,
            "rgba(109, 93, 255, 0.5)",
            "rgba(109, 93, 255, 0.0)"
          );
        },
        pointBackgroundColor: "#6D5DFF",
        hidden: activeLegend && activeLegend !== "rank",
        ...baseDatasetConfig,
      },
      {
        label: "Members",
        data: animeStats.map((stat) => stat.members),
        borderColor: "#B95EFF",
        backgroundColor: (ctx) => {
          const { chartArea } = ctx.chart;
          if (!chartArea) return;
          const ctxRef = ctx.chart.ctx;
          return createGradient(
            ctxRef,
            chartArea,
            "rgba(185, 94, 255, 0.5)",
            "rgba(185, 94, 255, 0.0)"
          );
        },
        pointBackgroundColor: "#B95EFF",
        hidden: activeLegend && activeLegend !== "members",
        ...baseDatasetConfig,
      },
      {
        label: "Popularity",
        data: animeStats.map((stat) => stat.popularity),
        borderColor: "#FF6584",
        backgroundColor: (ctx) => {
          const { chartArea } = ctx.chart;
          if (!chartArea) return;
          const ctxRef = ctx.chart.ctx;
          return createGradient(
            ctxRef,
            chartArea,
            "rgba(255, 101, 132, 0.5)",
            "rgba(255, 101, 132, 0.0)"
          );
        },
        pointBackgroundColor: "#FF6584",
        hidden: activeLegend && activeLegend !== "popularity",
        ...baseDatasetConfig,
      },
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
        position: "stat",
        callbacks: {
          label: (context) => {
            if (context.dataset.label.includes("Score")) {
              return `${context.dataset.label}: ${context.raw.toFixed(2)}`;
            } else {
              return `${context.dataset.label}: ${context.raw}`;
            }
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
            return labels.map((label) => {
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
          color: "rgba(192, 192, 192, 0.57)",
          stepSize: 7,
          callback: function (value, index) {
            return format(new Date(value), "MM/dd");
          },
        },
      },
      y: {
        ticks: {
          color: "rgba(192, 192, 192, 0.57)",
          callback: function (value, index, values) {
            const datasetIndex = this.chart.data.datasets.findIndex(
              (dataset) => dataset.label === "Score"
            );

            if (
              datasetIndex !== -1 &&
              this.chart.isDatasetVisible(datasetIndex)
            ) {
              return value.toFixed(2);
            }
            return value;
          },
        },
        grid: {
          color: "rgba(255, 255, 255, 0.2)",
          lineWidth: 1,
        },
      },
    },
    animation: {
      duration: 0,
    },
    elements: {
      line: {
        borderDash: [5, 5],
      },
    },
  };

  return (
    <>
      <ZoomButton
        zoomEnabled={zoomEnabled}
        top={"33px"}
        onClick={handleZoomToggle}
      />
      <Line ref={chartRef} data={chartData} options={options} />
    </>
  );
};

export default AnimeStatGraph;

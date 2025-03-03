import {
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  TimeScale,
  BarElement,
  Chart,
  Filler,
} from "chart.js";
import zoomPlugin from "chartjs-plugin-zoom";

const increaseLegendSpacing = {
  id: "increase-legend-spacing",
  beforeInit(chart) {
    const originalFit = chart.legend.fit;

    chart.legend.fit = function fit() {
      originalFit.bind(chart.legend)();
      this.height += 50;
    };
  },
};

const verticalHoverLine = {
  id: "verticalHoverLine",
  afterDatasetsDraw(chart, args, plugins) {
    const {
      ctx,
      tooltip,
      chartArea: { top, bottom },
    } = chart;

    if (tooltip._active.length > 0) {
      const tooltipX = tooltip._active[0].element.x;
      const tooltipY = tooltip._active[0].element.y;
      const datasetIndex = tooltip._active[0].datasetIndex;
      const dataset = chart.data.datasets[datasetIndex];

      ctx.save();
      ctx.beginPath();
      ctx.lineWidth = 1;
      ctx.strokeStyle = "rgba(173, 173, 173, 0.95)";
      ctx.setLineDash([2, 2]);
      ctx.moveTo(tooltipX, top);
      ctx.lineTo(tooltipX, bottom);
      ctx.stroke();
      ctx.closePath();

      ctx.beginPath();
      ctx.arc(tooltipX, tooltipY, 4, 0, Math.PI * 2);
      ctx.fillStyle = dataset.borderColor;
      ctx.fill();
      ctx.closePath();
    }
  },
};

export const registerCharts = () => {
  Chart.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    zoomPlugin,
    TimeScale,
    BarElement,
    Filler,
    increaseLegendSpacing,
    verticalHoverLine
  );

  Tooltip.positioners.stat = (elements, position) => {
    return {
      x: position.x,
      y: 110,
    };
  };

  Tooltip.positioners.poll = (elements, position) => {
    return {
      x: position.x,
      y: 120,
    };
  };

  Chart.defaults.font.size = 11;
};

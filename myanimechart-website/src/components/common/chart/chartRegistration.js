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
  LineController,
} from "chart.js";
import zoomPlugin from "chartjs-plugin-zoom";

const increaseLegendSpacing = {
  id: "increase-legend-spacing",
  beforeInit(chart) {
    const originalFit = chart.legend.fit;

    chart.legend.fit = function fit() {
      originalFit.bind(chart.legend)();
      this.height += 0;
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
      ctx.setLineDash([]);
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
    verticalHoverLine,
    LineController
  );

  Tooltip.positioners.poll = (elements, position) => {
    const chartWidth = elements[0]?.element?.$context?.chart?.width || 0;

    const offsetX = 100;
    const isLeftSide = position.x < chartWidth / 2;

    return {
      x: isLeftSide ? position.x + offsetX : position.x - offsetX,
      y: 0,
    };
  };

  Chart.defaults.font.size = 11;
};

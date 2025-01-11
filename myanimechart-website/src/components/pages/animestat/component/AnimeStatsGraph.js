import React, {useState, useRef, useEffect} from 'react';
import {Line} from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    Chart,
    TimeScale,
} from 'chart.js';
import zoomPlugin from "chartjs-plugin-zoom";
import 'chartjs-adapter-date-fns';
import {parseISO} from 'date-fns';
import CommonButton from "../../../common/basic/CommonButton";
import {isMobile} from 'react-device-detect';
import styled from "styled-components";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, zoomPlugin, TimeScale);

const zoomOptions = {
    zoom: {
        wheel: {
            enabled: true,
            modifierKey: 'ctrl',
        },
        pinch: {
            enabled: true
        },
        mode: 'x',
    },
    pan: {
        enabled: !isMobile,
        mode: 'x',
    },
};

const ResetButtonWrapper = styled(CommonButton)`
    position: absolute;
    top: -7px;
    right: 10px;
    z-index: 10;
    padding-left: 10px;
    padding-right: 10px;
`;

const AnimeStatsGraph = ({animeStats, selectedLegend}) => {
    const [activeLegend, setActiveLegend] = useState(selectedLegend);
    const chartRef = useRef(null); // Ref to access the chart instance

    useEffect(() => {
        setActiveLegend(selectedLegend);
    }, [selectedLegend]);

    const handleResetZoom = () => {
        if (chartRef.current) {
            chartRef.current.resetZoom();
        }
    };

    const chartData = {
        labels: animeStats.map(stat => parseISO(stat.recordedAt)),
        datasets: [
            {
                label: 'Score',
                data: animeStats.map(stat => stat.score),
                borderColor: 'rgba(75,192,192,1)',
                backgroundColor: 'rgba(75,192,192,1)',
                tension: 0.1,
                hidden: activeLegend && activeLegend !== 'score',
                borderWidth: 0.3,
                pointBackgroundColor: 'rgba(75,192,192,1)',
                pointRadius: 2,
            },
            {
                label: 'Members',
                data: animeStats.map(stat => stat.members),
                fill: false,
                borderColor: 'rgba(255,99,132,1)',
                backgroundColor: 'rgba(255,99,132,1)',
                tension: 0.1,
                hidden: activeLegend && activeLegend !== 'members',
                borderWidth: 0.3,
                pointBackgroundColor: 'rgba(255,99,132,1)',
                pointRadius: 2,
            },
            {
                label: 'Popularity',
                data: animeStats.map(stat => stat.popularity),
                fill: false,
                borderColor: 'rgba(153,102,255,1)',
                backgroundColor: 'rgba(153,102,255,1)',
                tension: 0.1,
                hidden: activeLegend && activeLegend !== 'popularity',
                borderWidth: 0.3,
                pointBackgroundColor: 'rgba(153,102,255,1)',
                pointRadius: 2,
            },
            {
                label: 'Rank',
                data: animeStats.map(stat => stat.rank),
                fill: false,
                borderColor: 'rgba(255,159,64,1)',
                backgroundColor: 'rgba(255,159,64,1)',
                tension: 0.1,
                hidden: activeLegend && activeLegend !== 'rank',
                borderWidth: 0.3,
                pointBackgroundColor: 'rgba(255,159,64,1)',
                pointRadius: 2,
            },
            {
                label: 'ScoringCount',
                data: animeStats.map(stat => stat.scoringCount),
                fill: false,
                borderColor: 'rgba(255,205,86,1)',
                backgroundColor: 'rgba(255,205,86,1)',
                tension: 0.1,
                hidden: activeLegend && activeLegend.toLowerCase() !== 'scoringcount',
                borderWidth: 0.3,
                pointBackgroundColor: 'rgba(255,205,86,1)',
                pointRadius: 2,
            },
        ],
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        hover: {
            mode: 'nearest',
            intersect: false,
        },
        plugins: {
            tooltip: {
                mode: 'nearest',
                intersect: false,
                callbacks: {
                    label: context => `${context.dataset.label}: ${context.raw}`,
                },
            },
            legend: {
                labels: {
                    color: '#ffffff',
                    generateLabels: chart => {
                        const labels = Chart.defaults.plugins.legend.labels.generateLabels(chart);
                        return labels.map(label => {
                            const datasetIndex = chart.data.datasets
                                .findIndex(dataset => dataset.label === label.text);
                            const dataset = chart.data.datasets[datasetIndex];
                            if (dataset.hidden) {
                                label.fontColor = 'gray';
                            } else {
                                label.fontColor = 'white';
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
                type: 'time',
                time: {
                    unit: 'day',
                },
                ticks: {
                    color: '#ffffff',
                    callback: function (value, index) {
                        return '';
                    },
                },
            },
            y: {
                ticks: {
                    color: '#ffffff',
                },
            },
        },
        animation: {
            duration: 0,
        },
    };

    useEffect(() => {
        const resizeChart = () => {
            if (chartRef.current) {
                const chart = chartRef.current;
                const width = chart.canvas.clientWidth;
                const height = chart.canvas.clientHeight;

                chart.canvas.width = width * window.devicePixelRatio;
                chart.canvas.height = height * window.devicePixelRatio;
                chart.canvas.style.width = `${width}px`;
                chart.canvas.style.height = `${height}px`;

                chart.resize();
            }
        };

        window.addEventListener('resize', resizeChart);

        return () => {
            window.removeEventListener('resize', resizeChart);
        };
    }, []);

    return (
        <>
            <Line ref={chartRef} data={chartData} options={options}/>
            <ResetButtonWrapper
                onClick={handleResetZoom}
            >
                ↻
            </ResetButtonWrapper>
        </>
    );
};

export default AnimeStatsGraph;

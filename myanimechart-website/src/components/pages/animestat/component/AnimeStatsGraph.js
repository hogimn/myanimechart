import React, {useEffect, useRef, useState} from 'react';
import {Line} from 'react-chartjs-2';
import {
    CategoryScale,
    Chart as ChartJS,
    Chart,
    Legend,
    LinearScale,
    LineElement,
    PointElement,
    TimeScale,
    Title,
    Tooltip,
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
            enabled: true,
        },
        mode: 'x',
    },
    pan: {
        enabled: !isMobile,
        mode: 'x',
    },
};

const StyledResetButton = styled(CommonButton)`
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

    const baseDatasetConfig = {
        tension: 0.1,
        borderWidth: 0.3,
        pointRadius: 2,
        pointHoverRadius: 5,
        pointHitRadius: 50,
    };

    const chartData = {
        labels: animeStats.map(stat => parseISO(stat.recordedAt)),
        datasets: [
            {
                label: 'Score',
                data: animeStats.map(stat => stat.score),
                borderColor: 'rgba(75,192,192,1)',
                backgroundColor: 'rgba(75,192,192,1)',
                pointBackgroundColor: 'rgba(75,192,192,1)',
                hidden: activeLegend && activeLegend !== 'score',
                ...baseDatasetConfig
            },
            {
                label: 'Members',
                data: animeStats.map(stat => stat.members),
                borderColor: 'rgba(255,99,132,1)',
                backgroundColor: 'rgba(255,99,132,1)',
                pointBackgroundColor: 'rgba(255,99,132,1)',
                hidden: activeLegend && activeLegend !== 'members',
                ...baseDatasetConfig
            },
            {
                label: 'Popularity',
                data: animeStats.map(stat => stat.popularity),
                borderColor: 'rgba(153,102,255,1)',
                backgroundColor: 'rgba(153,102,255,1)',
                pointBackgroundColor: 'rgba(153,102,255,1)',
                hidden: activeLegend && activeLegend !== 'popularity',
                ...baseDatasetConfig
            },
            {
                label: 'Rank',
                data: animeStats.map(stat => stat.rank),
                borderColor: 'rgba(255,159,64,1)',
                backgroundColor: 'rgba(255,159,64,1)',
                pointBackgroundColor: 'rgba(255,159,64,1)',
                hidden: activeLegend && activeLegend !== 'rank',
                ...baseDatasetConfig
            },
            {
                label: 'ScoringCount',
                data: animeStats.map(stat => stat.scoringCount),
                borderColor: 'rgba(255,205,86,1)',
                backgroundColor: 'rgba(255,205,86,1)',
                pointBackgroundColor: 'rgba(255,205,86,1)',
                hidden: activeLegend && activeLegend.toLowerCase() !== 'scoringcount',
                ...baseDatasetConfig
            },
        ],
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            tooltip: {
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
            <StyledResetButton
                onClick={handleResetZoom}
            >
                ↻
            </StyledResetButton>
        </>
    );
};

export default AnimeStatsGraph;

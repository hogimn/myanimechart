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
    const [activeLegend, setActiveLegend] = useState('');
    const chartRef = useRef(null);

    useEffect(() => {
        if (selectedLegend === 'startDate') {
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
                borderColor: 'rgb(104,255,242)',
                backgroundColor: 'rgb(104,255,242)',
                pointBackgroundColor: 'rgb(104,255,242)',
                hidden: activeLegend && activeLegend !== 'score',
                ...baseDatasetConfig
            },
            {
                label: 'ScoringCount',
                data: animeStats.map(stat => stat.scoringCount),
                borderColor: 'rgb(94,183,255)',
                backgroundColor: 'rgb(94,183,255)',
                pointBackgroundColor: 'rgb(94,183,255)',
                hidden: activeLegend && activeLegend.toLowerCase() !== 'scoringcount',
                ...baseDatasetConfig
            },
            {
                label: 'Members',
                data: animeStats.map(stat => stat.members),
                borderColor: 'rgb(126,109,246)',
                backgroundColor: 'rgb(126,109,246)',
                pointBackgroundColor: 'rgb(126,109,246)',
                hidden: activeLegend && activeLegend !== 'members',
                ...baseDatasetConfig
            },
            {
                label: 'Popularity',
                data: animeStats.map(stat => stat.popularity),
                borderColor: 'rgb(255,118,237)',
                backgroundColor: 'rgb(255,118,237)',
                pointBackgroundColor: 'rgb(255,118,237)',
                hidden: activeLegend && activeLegend !== 'popularity',
                ...baseDatasetConfig
            },
            {
                label: 'Rank',
                data: animeStats.map(stat => stat.rank),
                borderColor: 'rgb(253,116,116)',
                backgroundColor: 'rgb(253,116,116)',
                pointBackgroundColor: 'rgb(253,116,116)',
                hidden: activeLegend && activeLegend !== 'rank',
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

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
import {MdRestore} from "react-icons/md";

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
    background-color: rgba(0, 0, 0, 0.7);
    position: absolute;
    top: -48px;
    left: 180px;
    padding: 7px;
    margin: 0;

    @media (max-width: 768px) {
        left: 112px;
    }
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
        pointRadius: 0,
        borderWidth: 2.0,
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
                    usePointStyle: true,
                    generateLabels: chart => {
                        const labels = Chart.defaults.plugins.legend.labels.generateLabels(chart);
                        return labels.map(label => {
                            if (label.hidden) {
                                label.fontColor = 'rgba(255, 255, 255, 0.2)';
                                label.lineWidth = 0;
                            } else {
                                label.fontColor = '#c9e0ff';
                                label.lineWidth = 7;
                                label.text = " " + label.text;
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

    return (
        <>
            <StyledResetButton
                onClick={handleResetZoom}
            >
                <MdRestore/>
            </StyledResetButton>
            <Line ref={chartRef} data={chartData} options={options}/>
        </>
    );
};

export default AnimeStatsGraph;

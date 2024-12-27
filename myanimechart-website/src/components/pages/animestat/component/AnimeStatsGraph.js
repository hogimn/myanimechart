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
    Legend
} from 'chart.js';
import zoomPlugin from "chartjs-plugin-zoom";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, zoomPlugin);

const zoomOptions = {};

const AnimeStatsGraph = ({animeStats}) => {
    const [activeLegend, setActiveLegend] = useState('Score');
    const chartRef = useRef(null); // Ref to access the chart instance

    const chartData = {
        labels: animeStats.map(stat => new Date(stat.recordedAt).toLocaleString()),
        datasets: [
            {
                label: 'Score',
                data: animeStats.map(stat => stat.score),
                fill: false,
                borderColor: 'rgba(75,192,192,1)',
                tension: 0.1,
                hidden: activeLegend && activeLegend !== 'Score',
            },
            {
                label: 'Members',
                data: animeStats.map(stat => stat.members),
                fill: false,
                borderColor: 'rgba(255,99,132,1)',
                tension: 0.1,
                hidden: activeLegend && activeLegend !== 'Members',
            },
            {
                label: 'Popularity',
                data: animeStats.map(stat => stat.popularity),
                fill: false,
                borderColor: 'rgba(153,102,255,1)',
                tension: 0.1,
                hidden: activeLegend && activeLegend !== 'Popularity',
            },
            {
                label: 'Rank',
                data: animeStats.map(stat => stat.rank),
                fill: false,
                borderColor: 'rgba(255,159,64,1)',
                tension: 0.1,
                hidden: activeLegend && activeLegend !== 'Rank',
            },
            {
                label: 'Scoring Count',
                data: animeStats.map(stat => stat.scoringCount),
                fill: false,
                borderColor: 'rgba(255,205,86,1)',
                tension: 0.1,
                hidden: activeLegend && activeLegend !== 'Scoring Count',
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
                labels: {
                    color: 'rgba(255,255,255,0.39)',
                },
                onClick: (e, legendItem) => {
                    setActiveLegend(activeLegend === legendItem.text ? null : legendItem.text);
                },
            },
            zoom: zoomOptions,
        },
        scales: {
            x: {
                ticks: {
                    color: '#ffffff',
                    callback: function (value, index) {
                        const showEveryNth = 5;
                        // return index % showEveryNth === 0 ? this.getLabelForValue(value) : '';
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

    return <Line ref={chartRef} data={chartData} options={options}/>;
};

export default AnimeStatsGraph;

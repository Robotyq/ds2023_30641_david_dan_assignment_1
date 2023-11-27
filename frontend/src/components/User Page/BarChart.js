import React from 'react';
import {BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Title, Tooltip,} from 'chart.js';
import {Bar} from 'react-chartjs-2';
// import faker from 'faker';

ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
);

const BarChart = ({data}) => {
    const measures = data.map(entry => entry.measure);
    const hours = data.map(entry => entry.hour);

    const options = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Day consumption Bar Chart',
            },
        },
    };


    const dataset = {
        labels: hours,
        datasets: [
            {
                label: 'Consumption in kWh',
                data: measures,
                backgroundColor: 'rgba(0,102,255,0.66)',
            },
        ],
    };

    return (
        <div>
            <Bar options={options} data={dataset}/>
        </div>
    );
}
export default BarChart;

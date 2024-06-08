function drawGraph(){
    var ctx2 = $('#' + graphName).get(0).getContext("2d");
    myChart3 = new Chart(ctx2, {
        type: "line",
        data: {
            labels: labels,
            datasets: 
            [
                {
                    label: "Nhiệt độ (°C)",
                    data: temperatures,
                    borderColor: "#b90b0b",
                    backgroundColor: "#b90b0b",
                    tension: 0.4,
                    yAxisID: "y1"
                },
                {
                    label: "Độ ẩm (%)",
                    data: humidities,
                    borderColor: "#134a5a",
                    backgroundColor: "#134a5a",
                    tension: 0.4,
                    yAxisID: "y2"
                },
            ]
            },
        options: {
            responsive: true,
            scales: {
                y1: {
                    type: 'linear',
                    display: true,
                    position: 'left',
                },
                y2: {
                    type: 'linear',
                    display: true,
                    position: 'right',

                    // grid line settings
                    grid: {
                        drawOnChartArea: false, // only want the grid lines for one axis to show up
                    },
                },
            }
        }
    });

    var ctx3 = $('#' + graphName2).get(0).getContext("2d");
    myChart2 = new Chart(ctx3, {
        type: "line",
        data: {
            labels: labels,
            datasets:
                [
                    {
                        label: "Nồng độ cồn",
                        data: mq3,
                        borderColor: "#b90b0b",
                        backgroundColor: "#b90b0b",
                        tension: 0.4,
                        yAxisID: "y1"
                    },
                    {
                        label: "Khí gas",
                        data: mq4,
                        borderColor: "#134a5a",
                        backgroundColor: "#134a5a",
                        tension: 0.4,
                        yAxisID: "y2"
                    },
                ]
        },
        options: {
            responsive: true,
            scales: {
                y1: {
                    type: 'linear',
                    display: true,
                    position: 'left',
                },
                y2: {
                    type: 'linear',
                    display: true,
                    position: 'right',

                    // grid line settings
                    grid: {
                        drawOnChartArea: false, // only want the grid lines for one axis to show up
                    },
                },
            }
        }
    });
}
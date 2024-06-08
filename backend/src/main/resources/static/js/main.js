const temperatures = [0, 0, 0, 0, 0, 0, 0];
const humidities = [0, 0, 0, 0, 0, 0, 0];
const mq3 = [0, 0, 0, 0, 0, 0, 0];
const mq4 = [0, 0, 0, 0, 0, 0, 0];
const labels = ['loading', 'loading', 'loading', 'loading', 'loading', 'loading', 'loading'];
const graphName = "graph"
const graphName2 = "graph2"
let ledStatus = [0,0]
var myChart2;
var myChart3;
var doBuiGraph;
drawGraph();
var pendingLed1 = false
var pendingLed2 = false
var pendingLed3 = false

getAllData();

function fetchData(callback) {
    $.ajax({
        url: '/data',
        type: 'GET',
        contentType: 'application/json',
        dataType: 'json',
        success: function (result) {
            callback(result); // Gọi callback và truyền dữ liệu về
        },
        error: function (error) {
            console.log(error);
            callback(null); // Gọi callback với giá trị null để xác định lỗi
        }
    });
}

async function getAllData() {  // xử lí dữ liệu nhận được
    fetchData(function (data) {
        if (data != null) {
            console.log(data)
            for (var i = 0; i < data.length; i++) {
                temperatures[i] = data[i].temperature;
                humidities[i] = data[i].humidity;
                mq3[i] = data[i].mq3;
                labels[i] = data[i].time;
                mq4[i] = data[i].mq4;
            }

            updateFrontend()
        }
    });
}

var socket = new SockJS("/ws");
var stompClient = Stomp.over(socket);

stompClient.connect({}, function (data){
    console.log("Connected: " +data)

    stompClient.subscribe("/topic/data-sensor", async function(message){
        console.log('Received data: ' + message.body);
        var data = JSON.parse(message.body);
        console.log(data);

        temperatures.shift()
        temperatures.push(data.temperature)

        humidities.shift()
        humidities.push(data.humidity)

        mq3.shift()
        mq3.push(data.mq3)

        mq4.shift()
        mq4.push(data.mq4)

        labels.shift()
        labels.push(data.time)

        updateFrontend()
    })
})


function updateFrontend(){
    getTemperature(temperatures[6]);
    getHumidity(humidities[6]);
    getMq3Status(mq3[6]);
    getMq4Status(mq4[6]);
    myChart2.update();
    myChart3.update();
}
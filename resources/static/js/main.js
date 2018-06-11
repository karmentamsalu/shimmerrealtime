var firstChild = true;
var startTime;
var dpsX = [];
var dpsY = [];
var dpsZ = [];
var dpsRuut = [];
var signals = [];

var chart;
var currentSteps = 0;

var lenX = 0;
var lenY = 0;
var lenZ = 0;

var updateInterval = 100;
var frequency = 50.33;
var displaySize = 2013;
var xAxisInterval = displaySize/frequency;

var dataX = {type: "line",
             dataPoints: dpsX,
             name: "X telg",
             showInLegend: true};
var dataY = {type: "line",
             dataPoints: dpsY,
             name: "Y telg",
             showInLegend: true};
var dataZ = {type: "line",
             dataPoints: dpsZ,
             name: "Z telg",
             showInLegend: true};
var dataRuut = {type: "line",
             dataPoints: dpsRuut,
             name: "Ruutkesmine",
             showInLegend: true};

// Initialize Firebase kiirendusanduriapp@gmail.com
  var config = {
    apiKey: "AIzaSyBKz5CZ-nRo1reqiElEnw-78vl5o6OAeBs",
    authDomain: "kiirendusandurisync-a8a7c.firebaseapp.com",
    databaseURL: "https://kiirendusandurisync-a8a7c.firebaseio.com",
    projectId: "kiirendusandurisync-a8a7c",
    storageBucket: "kiirendusandurisync-a8a7c.appspot.com",
    messagingSenderId: "545262520259"
  };
  firebase.initializeApp(config);

var ref = firebase.database().ref();

ref.on("child_added", function(snapshot, prevChildKey) {
    var newData = String(snapshot.val());

    var newDataArray = newData.split(";");
    var x = newDataArray[0];
    var y = newDataArray[1];
    var z = newDataArray[2]
    var timestamp = newDataArray[3];

    checkTimeInterval(timestamp);
    //sendToJava(x, y, z, timestamp);
    console.log("X: " + x + " Y: " + y + " Z: " + z + " time: " + timestamp);
    dpsX.push({x: Number(timestamp), y: Number(x)});
    dpsY.push({x: Number(timestamp), y: Number(y)});
    dpsZ.push({x: Number(timestamp), y: Number(z)});
    dpsRuut.push({x: Number(timestamp), y: Math.sqrt(Math.pow(Number(x), 2) + Math.pow(Number(y), 2) + Math.pow(Number(z), 2))});

    spliceExtraSignals();

});

function spliceExtraSignals() {
    console.log("splice: " + xAxisInterval);
    if (dpsX.length > 0 && Number(dpsX[dpsX.length-1].x) - Number(dpsX[0].x) > xAxisInterval)
        {
            var deleteUntil = 0;
            var firstElement = Number(dpsX[dpsX.length-1].x) - xAxisInterval;
            for (i = 0; i < dpsX.length; i++) {
                if (dpsX[i].x < firstElement) {
                    deleteUntil = dpsX[i].x;
                }
            }
            console.log("Delete until: " + deleteUntil);
            dpsX.splice(0, deleteUntil);
            dpsY.splice(0, deleteUntil);
            dpsZ.splice(0, deleteUntil);
            dpsRuut.splice(0, deleteUntil);
            console.log("first: " + dpsX[0].x);
            //console.log("Splice X: " + dpsX.length + " Y " + dpsY.length + " Z " + dpsZ.length);
        }
}

function checkTimeInterval(newTime) {
    if (dpsX.length > 0) {
        var lastTime = dpsX[dpsX.length - 1].x;

        if (newTime - Number(lastTime) >= 2){
            var timestamp = (newTime - Number(lastTime))/2;
            dpsX.push({x: Number(timestamp), y: null});
            dpsY.push({x: Number(timestamp), y: null});
            dpsZ.push({x: Number(timestamp), y: null});
            dpsRuut.push({x: Number(timestamp), y: null});
        }
    }
}

window.onload = function () {
    displayChart();
}

function displayChart() {
    console.log("Start chartjs");
    chart = new CanvasJS.Chart("chartContainer",{
        zoomEnabled: true,
        animationEnabled: true,
        title: {
            text: "Signaal Shimmer'i kiirendusandurilt"
        },
        axisX: {
            title: "Aeg, (s)",
            suffix: " s"
        },
        axisY: {
            title: "Kiirendus, (m/s^2)",
            includeZero: true,
            lineThickness: 1,
            viewportMinimum: -20,
            viewportMaximum: 30
        },
        legend:{
            cursor: "pointer",
            fontSize: 16
        },
        toolTip:{
            shared: true
        },
        data: [dataX, dataY, dataZ]
        });
    console.log("render");
    chart.render();


    var updateChart = function () {
        chart.render();
        ajaxGet();
    };
    setInterval(function(){updateChart()}, updateInterval);

}
function openNav() {
    document.getElementById("myNav").style.display = "block";
}

function closeNav() {
    document.getElementById("myNav").style.display = "none";
}

function restartSteps() {
    console.log("Restart");
    ajaxPost();
}

function confirm() {
    var time = document.getElementById("time").value;
    var freq = document.getElementById("freq").value;
    var checkboxes = document.querySelectorAll('.axis:checked'), values = [];
    Array.prototype.forEach.call(checkboxes, function(el) {
        values.push(el.value);
    });

    displaySize = Math.round(time*freq);
    xAxisInterval = displaySize/freq;

    console.log("time: " + time + " freq: " + freq + " cb: " + values);
    console.log("interval: " + xAxisInterval);
    spliceExtraSignals();
    var newDataset = [];
    for (i = 0; i < values.length; i++) {
        if (values[i] === "X") {
            newDataset.push(dataX);
        } else if (values[i] === "Y") {
            newDataset.push(dataY);
        } else if (values[i] === "Z") {
            newDataset.push(dataZ);
        } else if (values[i] === "ruut") {
            newDataset.push(dataRuut);
        }
    }
    chart.set("data", newDataset);
    chart.render();
    closeNav();
}

function ajaxGet(){
    $.ajax({
        type : "GET",
        url : window.location + "steps",
        success: function(result){
            if(result.status === "Done"){
                var newSteps = result.steps;
                if (newSteps != currentSteps) {
                    var steps = "<p id='stepsHeader'>Current steps: </p><p id='steps'>" + newSteps + "</p>";
                    $("#stepCount").html(steps);
                    currentSteps = newSteps;
                }
            }else{
                console.log("Fail: ", result);
            }
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function ajaxPost() {
    var post = {
        toZero: 0
    }

    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : window.location + "nullSteps",
        data : JSON.stringify(post),
        dataType : 'json',
        success : function(result) {
            if(result.status === "Done"){
                console.log("Successful " + result.status);
            }else{
                console.log("Else wrong???? " + result.status);
            }

        },
        error : function(e) {
            console.log("SIGNAL: " + JSON.stringify(post));
            console.log("ERROR: ", e);
        }
    });
}


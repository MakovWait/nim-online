$(window).bind('beforeunload', function(){

    //save info somewhere
    //disconnect();
    return 'are you sure you want to leave?';
});

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

var id;

function connect() {
    var socket = new SockJS('/secured/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        id = stompClient.subscribe('/topic/**', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    }, function (error) {
        console.log(error)
    });
}

function connect1() {
    id = stompClient.subscribe('/ass', function (greeting) {
        showGreeting(JSON.parse(greeting.body).content);
    });
}

function disconnect() {
    if (stompClient !== null) {
        id.unsubscribe();
        //stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var i = $("#name").val();
    stompClient.send("/app/hello/"+i, {}, JSON.stringify("hello"));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#connect1" ).click(function() { connect1(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});


$(document).ready(function () {

    var isConnected = false;
    var gamesTopicSub = null;

    $body = $("body");

    localStorage.clear();

    var links;

    var timerId = null;
    var isFindingGame = false;

    if ($("a[id=account]").text() === 'guest') {
        $("#logoutButton").addClass("btn_hide");
    } else {
        $("#logoutButton").removeClass("btn_hide");
    }

    $("a[id=account]").click(function (event) {
        event.preventDefault();

        if ($(this).text() === 'guest') {
            window.location.replace("/login");
        } else {
            window.location.replace("/personal");
        }
    });

    $(".logo").click(function (event) {
        event.preventDefault();
        if (window.location.pathname !== "/lobby") {
            window.location.replace("/lobby");
        }
    });

    $(".find-game-button").click(function () {

        $.ajax({
            timeout: 1000,
            url: '/room',
            success: function () {
                window.location.replace("/room");
            },
            error: function(request, status, error) {
                var statusCode = request.status;
                if (statusCode === 401) {
                    window.location.replace("/login");
                }
            }
        })
    });

    $('#logoutButton').on('click', function (event) {
        if (!isFindingGame) {
            event.preventDefault();
            $.ajax({
                data: {},
                timeout: 1000,
                type: 'POST',
                url: '/logout',
                success: function () {
                    sessionStorage.clear();
                    window.location = '/';
                }
            })
        }
    });

    var pointsNum = 0;

    function getGame() {

        var findGameAlert = $(".find-game-alert");

        if (pointsNum < 3) {
            findGameAlert.append(".");
            pointsNum++;
        } else {
            findGameAlert.text(findGameAlert.text().substring(0, findGameAlert.text().length - 3));
            pointsNum = 0;
        }

        console.log(pointsNum);

        $.ajax({
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            url: links.find(link => link.rel === 'game').href,
            success: function (data) {
                if (data != null) {
                    clearInterval(timerId);
                    sessionStorage.setItem("game", JSON.stringify(data));
                    window.location.replace("/room");
                }
            }
        })
    }


    fillPlayersTable();

    function fillPlayersTable() {
        $.ajax({
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            url: "/users",
            success: function (users) {
                var playersTable = $('#players tr');
                playersTable.not(':first').not(':last').remove();
                var html = '';

                users.sort(function (a, b) {
                    return a.score - b.score;
                }).reverse();

                for (var i = 0; i < users.length; i++)
                    html += '<tr>' +
                        '<td>' + (i + 1) + "." + '</td>' +
                        '<td>' + users[i].username + '</td>' +
                        '<td>' + users[i].score + '</td>' +
                        '</tr>';

                playersTable.first().after(html);
            }
        });
    }

    function sleep(milliseconds) {
        var start = new Date().getTime();
        for (var i = 0; i < 1e7; i++) {
            if ((new Date().getTime() - start) > milliseconds) {
                break;
            }
        }
    }
});


// $(document).ajaxStart(function () {
//     if (!overlay_loader.hasClass("overlay-loader_selected")) {
//         overlay_loader.addClass("overlay-loader_selected");
//     }
// });
//
// $(document).ajaxStop(function () {
//     if (overlay_loader.hasClass("overlay-loader_selected")) {
//         overlay_loader.removeClass("overlay-loader_selected");
//     }
// });

// $('.player-id').append(data.id);
// if (!overlay_loader.hasClass("overlay-loader_selected")) {
//     overlay_loader.addClass("overlay-loader_selected");
// }
//
// timerId = setInterval(getGame, 1000);
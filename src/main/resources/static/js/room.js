$(document).ready(function () {
    $body = $("body");

    var playerName = $("span[id=account]").text();
    var overlay_loader = $(".overlay-loader");
    var game_board = $(".game-board");
    var isEnemyTurn = true;
    var turn = $("#turn");
    var gameId = null;
    var time = 10;
    var i = setInterval(timer, 1000);

    var stompClient = null;

    startFindingGame();

    function hideAllMsg() {
        $("#timer-msg").hide();
        $("#ur-turn-msg").hide();
        $("#enemy-turn-msg").hide();
        $("#win-msg").hide();
        $("#loose-msg").hide();
        $('#toLobby').hide();
    }

    function showEndGameMsg(isWin) {
        if (isWin) {
            $('#win-msg').show();
        } else {
            $('#loose-msg').show();
        }

        $('#toLobby').show();
    }

    function showPlayerTurnMsg() {
        $("#ur-turn-msg").show();
    }

    function showEnemyTurnMsg() {
        $("#enemy-turn-msg").show();
    }

    $.ajax({
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        url: "questions",
        success: function (question) {
            $("p[class=question]").text(question.questionContent);

            $('ul[class=options] li').each(function (i) {
                $(this).find("div[class=radio__text]").text(question.options[i].optionContent);
            });
        }
    });

    function timer() {
        time--;
        $('.time').text(time);
        if (time === 0) {
            clearInterval(i);
        }
    }

    $(".logo").click(function (event) {
        event.preventDefault();
        if (window.location.pathname != "/lobby") {
            window.location.replace("/lobby");
        }
    });

    function startFindingGame() {
        if (stompClient == null || !stompClient.connected) {
            var socket = new SockJS('/secured/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                //console.log('Connected: ' + frame);
                gamesTopicSub = subToGames();
            }, function (error) {
                window.location.replace("/login")
            });
        } else {
            gamesTopicSub = subToGames();
        }
    }

    function subToGames() {
        let subscription = null;
        if (stompClient != null) {
            subscription = stompClient.subscribe('/user/secured/topic/games', function (response) {
                    if (overlay_loader.hasClass("overlay-loader_selected")) {
                        overlay_loader.removeClass("overlay-loader_selected");
                    }

                    if (!game_board.hasClass("game-board_selected")) {
                        game_board.addClass("game-board_selected");
                    }

                    $('div[id=buttons]').removeClass("wrapper_selected");

                    var game = JSON.parse(response.body);
                    gameId = game.id;

                    var board = game.lastTurn.gameBoard.board;

                    var table = document.getElementById("turn");

                    console.log(game);

                    hideAllMsg();

                    console.log(playerName);

                    if (game.winner != null) {
                        showEndGameMsg(game.winner.username === playerName);
                    } else if (playerName === game.lastTurn.currentPlayer.username) {
                        isEnemyTurn = false;
                        showPlayerTurnMsg();
                    } else {
                        isEnemyTurn = true;
                        showEnemyTurnMsg();
                    }

                    clearTable(table);
                    updateTableWithSocketResponse(table, board);
                    updateTableEvents();
                }
            );

            if (!overlay_loader.hasClass("overlay-loader_selected")) {
                overlay_loader.addClass("overlay-loader_selected");
            }

            $('div[id=buttons]').addClass("wrapper_selected");

        }
        return subscription;
    }

    $(".stop-finding-game-button").click(function () {
        gamesTopicSub.unsubscribe();

        if (overlay_loader.hasClass("overlay-loader_selected")) {
            overlay_loader.removeClass("overlay-loader_selected");
        }

        $('div[id=buttons]').removeClass("wrapper_selected");

        isFindingGame = false;
        window.location.replace("/lobby");
        stompClient.disconnect();
    });

    $("#toLobby").click(function () {
        window.location.replace("/lobby");
    });

    function clearTable(table) {
        for (var i = table.rows.length - 1; i >= 0; i--) {
            table.deleteRow(i);
        }
    }

    function updateTableWithSocketResponse(table, response) {
        for (var i = 0; i < response.length; i++) {
            var row = table.insertRow(i);

            for (var j = 0; j < response[i]; j++) {
                row.insertCell(j);
            }
        }
    }

    class PlayerTurn {
        constructor(gameId, row, col) {
            this.gameId = gameId;
            this.row = row;
            this.col = col;
        }
    }

    function updateTableEvents() {
        var turnTd = $('#turn td');

        turnTd.hover(function (event) {
            event.preventDefault();

            var tr = $(this).parent();
            var col = tr.children().index($(this));

            tr.find('td').each(function () {
                if (tr.children().index($(this)) >= col) {
                    $(this).addClass("selected");
                }
            });

        });

        turnTd.mouseout(function (event) {
            event.preventDefault();

            var tr = $(this).parent();
            var col = tr.children().index($(this));

            tr.find('td').each(function () {
                if (tr.children().index($(this)) >= col) {
                    $(this).removeClass("selected");
                }
            });
        });

        turnTd.click(function (event) {
            event.preventDefault();

            var tr = $(this).parent();
            var col = tr.children().index($(this));
            var row = $(this).parent().parent().children().index(tr);
            console.log(row);
            console.log(col);

            if (!isEnemyTurn) {
                var playerTurn = new PlayerTurn(gameId, row, col);
                stompClient.send("/app/turns/", {}, JSON.stringify(playerTurn));
            }
        });
    }

})
;





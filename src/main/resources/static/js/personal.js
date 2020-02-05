$(document).ready(function () {

    var currentPlayerName = $("a[id=account]").text();

    $(".logo").click(function (event) {
        event.preventDefault();
        if (window.location.pathname !== "/lobby") {
            window.location.replace("/lobby");
        }
    });

    $.ajax({
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        url: "/games?userName=" + currentPlayerName,
        success: function (games) {
            var gamesTable = $('#games tr');
            gamesTable.not(':first').not(':last').remove();
            var html = '';

            games.sort(function (a, b) {
                return a.id - b.id;
            }).reverse();

            for (var i = 0; i < games.length; i++) {
                var rw = parseRowValue(games[i]);

                var tr = '<tr class="tr_red">';

                if (rw.isWin) {
                    tr = '<tr class="tr_green">'
                }

                html += tr +
                    '<td>' + rw.enemyName + '</td>' +
                    '<td>' + getResultText(rw.isWin) + '</td>' +
                    '</tr>';
            }

            gamesTable.first().after(html);
        }
    });

    class RowValue {
        constructor(enemyName, isWin) {
            this.enemyName = enemyName;
            this.isWin = isWin;
        }
    }

    function parseRowValue(game) {
        var rw = new RowValue("", false);
        for (var i = 0; i < game.players.length; i++) {
            if (game.players[i].user.username === currentPlayerName) {
                rw.isWin = game.players[i].winner;
            } else {
                rw.enemyName = game.players[i].user.username;
            }
        }

        return rw;
    }

    function getResultText(isWin) {
        if (isWin) {
            return "Победа";
        } else {
            return "Поражение";
        }
    }
});
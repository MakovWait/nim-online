jQuery(document).ready(function ($) {
    $('#loginform').submit(function (event) {
        event.preventDefault();

        var data = 'username=' + $('#username').val() + '&password=' + $('#password').val();

        $.ajax({
            data: data,
            timeout: 1000,
            type: 'POST',
            url: '/login',
            success: function (data) {
                window.location.replace("/lobby");
            }
        })
    });

    $(".logo").click(function (event) {
        event.preventDefault();
        window.location.replace("/lobby");
    });

    if ($("span[id=account]").text() !== 'guest') {
        window.location.replace("/lobby");
    }
});

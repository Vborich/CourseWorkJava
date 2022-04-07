$("#uploadAvatar").on('click', function (ev) {
    $('.my-image').croppie('result', {
        type: 'base64',
        size: 'viewport'
    }).then(function (image) {
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/users/'+ userId +'/uploadAvatar',
            data: { 'image': image },
            success: function (data) {
                if (data == "error") {
                    $("#uploadAvatar").attr("disabled", true);
                    var boxZone = $('.box-body');
                    boxZone.empty();
                    boxZone.append('<div class="border-0 bg-white" style="margin-top:15px;"><span style="color:red;">Произошла ошибка загрузки картинки</span></div>');
                }
                else window.location.href = 'http://localhost:8080/users/' + userId;
            },
            error: function (data) {
                window.location.href = 'http://localhost:8080/users/' + userId;
            }
        });
    });
});

function checkImage(file) {
    let type = file.type;
    if (type.indexOf("jpeg") != -1 || type.indexOf("png") != -1)
        return true;
    return false;
}
function readFile(input) {
    if (input.files && input.files[0]) {
        $("#uploadAvatar").attr("disabled", true);
        var boxZone = $(input).parent().parent().find('.preview-zone').find('.box').find('.box-body');
        if (checkImage(input.files[0])) {
            var reader = new FileReader();
            reader.onload = function (e) {
                boxZone.empty();
                boxZone.append('<div class="border-0 bg-white" style="margin-top:15px;"><img class="my-image" height="250" width="250" src="' + e.target.result + '" /></div>');
                $("#uploadAvatar").attr("disabled", false);
                showCropAvatar();
            };
            reader.readAsDataURL(input.files[0]);
        }
        else {
            boxZone.empty();
            boxZone.append('<div class="border-0 bg-white" style="margin-top:15px;"><span style="color:red;">Файл должен быть изображением с раширением .png или .jpeg</span></div>');
        }
    }
}
function showCropAvatar() {
    $(function () {
        $('.my-image').croppie({
            viewport: {
                width: 150,
                height: 150,
                type: 'circle'
            },
            boundary: {
                width: 250,
                height: 250,
            },
        });
    });
}
$(".dropzone").change(function () {
    readFile(this);
});
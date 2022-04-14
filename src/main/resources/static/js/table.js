    $(document).ready(function () {
    $("#table").DataTable({
        responsive: true,
        "order": [],
        "columnDefs": [
            { "width": "12%", "targets": -1, 'bSortable': false, "searchable": false } ],
        "language": {
            "emptyTable": "Данные отсутствуют в таблице",
            "info": "Показаны записи с _START_ по _END_ из _TOTAL_",
            "infoEmpty": "Показано с 0 по 0 из 0 записей",
            "infoFiltered": "(отфильтровано из _MAX_ записей)",
            "lengthMenu": "Показать записи _MENU_",
            "loadingRecords": "Загружается ...",
            "processing": "Обработка...",
            "search": "Поиск:",
            "zeroRecords": "Нет записей",
            "paginate": {
                "first": "Первый",
                "last":  "Последний",
                "next": "Cледующий",
                "previous": "Предыдущий"
            }
        }
    });
});
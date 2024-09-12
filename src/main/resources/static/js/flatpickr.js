// flatpickr-setup.js

document.addEventListener("DOMContentLoaded", function() {
    flatpickr("#startTime", {
        dateFormat: "Y-m-d",
        altInput: true,
        altFormat: "F j, Y",
        allowInput: true,
        defaultDate: "today",
        disableMobile: true,
        onOpen: function(selectedDates, dateStr, instance) {
            setTimeout(function() {
                const calendar = instance.calendarContainer;
                const input = instance._input;
                const inputRect = input.getBoundingClientRect();

                // 입력 필드의 오른쪽에 위치하도록 달력 위치 조정
                calendar.style.left = (inputRect.right + window.scrollX + 15) + "px"; // 오른쪽에 10px 간격 추가
                calendar.style.top = (inputRect.top + window.scrollY) + "px"; // 입력 필드의 수직 위치에 맞춤
            }, 10);
        }
    });

    flatpickr("#deadLine", {
        dateFormat: "Y-m-d",
        altInput: true,
        altFormat: "F j, Y",
        allowInput: true,
        disableMobile: true,
        onOpen: function(selectedDates, dateStr, instance) {
            setTimeout(function() {
                const calendar = instance.calendarContainer;
                const input = instance._input;
                const inputRect = input.getBoundingClientRect();

                // 입력 필드의 오른쪽에 위치하도록 달력 위치 조정
                calendar.style.left = (inputRect.right + window.scrollX + 15) + "px"; // 오른쪽에 10px 간격 추가
                calendar.style.top = (inputRect.top + window.scrollY) + "px"; // 입력 필드의 수직 위치에 맞춤
            }, 10);
        }
    });
});
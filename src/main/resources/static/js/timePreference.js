// Script for handling time preference selection
const timeOptions = document.querySelectorAll('.time-option');
const preferredTimesInput = document.getElementById('preferenceTime');
let preferredTimes = [];

timeOptions.forEach(option => {
    option.addEventListener('click', () => {
        const time = option.getAttribute('data-time');
        if (preferredTimes.includes(time)) {
            preferredTimes = preferredTimes.filter(t => t !== time);
            option.classList.remove('selected');
        } else {
            preferredTimes.push(time);
            option.classList.add('selected');
        }
        preferredTimesInput.value = preferredTimes.join(',');
    });
});

// Script for handling rest time preference selection
const restOptions = document.querySelectorAll('.rest-option');
const restTimeInput = document.getElementById('restTime');

restOptions.forEach(option => {
    option.addEventListener('click', () => {
        const time = option.getAttribute('data-time');

        // 모든 옵션에서 'selected' 클래스를 제거하고, 선택된 항목에만 추가
        restOptions.forEach(opt => opt.classList.remove('selected'));
        option.classList.add('selected');

        // 선택한 시간을 hidden input 필드에 저장
        restTimeInput.value = time;
    });
});
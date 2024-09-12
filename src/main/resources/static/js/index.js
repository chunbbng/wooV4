//index.js

// 우선순위 캔버스 드로잉 스크립트
const canvas = document.getElementById('priorityCanvas');
const ctx = canvas.getContext('2d');
let dragging = false;
let point = { x: 20, y: 380 };

function draw() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = 'black';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    // 축 그리기
    ctx.strokeStyle = 'white';
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(20, 0);
    ctx.lineTo(20, 400);
    ctx.moveTo(0, 380);
    ctx.lineTo(400, 380);
    ctx.stroke();

    // 화살표 그리기
    ctx.beginPath();
    ctx.moveTo(20, 0);
    ctx.lineTo(15, 10);
    ctx.moveTo(20, 0);
    ctx.lineTo(25, 10);
    ctx.moveTo(400, 380);
    ctx.lineTo(390, 375);
    ctx.moveTo(400, 380);
    ctx.lineTo(390, 385);
    ctx.stroke();

    // 격자선 및 눈금 그리기
    ctx.strokeStyle = 'gray';
    ctx.lineWidth = 1;

    for (let i = 1; i <= 10; i++) {
        let y = 380 - i * 35; // Y축 눈금 간격
        let x = 20 + i * 35; // X축 눈금 간격

        // 가로선
        ctx.beginPath();
        ctx.moveTo(20, y);
        ctx.lineTo(400, y);
        ctx.stroke();

        // 세로선
        ctx.beginPath();
        ctx.moveTo(x, 0);
        ctx.lineTo(x, 380);
        ctx.stroke();

        // y축 눈금
        ctx.beginPath();
        ctx.moveTo(15, y);
        ctx.lineTo(20, y);
        ctx.stroke();

        // x축 눈금
        ctx.beginPath();
        ctx.moveTo(x, 380);
        ctx.lineTo(x, 385);
        ctx.stroke();
    }

    // 레이블 그리기
    ctx.fillStyle = 'white';
    ctx.font = '16px Arial';
    ctx.fillText('긴급도', 30, 25);
    ctx.fillText('중요도', 350, 365);

    // 포인트 그리기
    ctx.beginPath();
    ctx.arc(point.x, point.y, 13, 0, Math.PI * 2);
    ctx.fillStyle = 'red';
    ctx.fill();
}

// 초기 캔버스 렌더링
draw();

canvas.addEventListener('mousedown', (e) => {
    const rect = canvas.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;
    if (Math.sqrt((x - point.x) ** 2 + (y - point.y) ** 2) < 10) {
        dragging = true;
    }
});

canvas.addEventListener('mousemove', (e) => {
    if (dragging) {
        const rect = canvas.getBoundingClientRect();
        point.x = e.clientX - rect.left;
        point.y = e.clientY - rect.top;
        point.x = Math.max(20, Math.min(point.x, 380)); // 캔버스 내로 포인트 제한
        point.y = Math.max(20, Math.min(point.y, 380)); // 캔버스 내로 포인트 제한
        draw();
    }
});

canvas.addEventListener('mouseup', () => {
    dragging = false;
    // 우선순위 값을 계산 (1 to 10)
    const importance = Math.round((point.x - 20) / 36) + 1;
    const urgency = Math.round((380 - point.y) / 36) + 1;
    // 숨겨진 입력 필드에 계산된 중요도와 긴급도 설정
    document.getElementById('importance').value = importance;
    document.getElementById('urgency').value = urgency;

});

document.addEventListener("DOMContentLoaded", function () {
    var gif = document.getElementById("myGif");
    var fallbackImage = document.getElementById("fallbackImage");

    // 페이지 로드 시 1회 자동 재생
    gif.style.display = "block";
    fallbackImage.style.display = "none";

    // 자동 재생 후 GIF 숨기기 (자동 반복 방지) 및 대체 이미지 표시
    setTimeout(function () {
        gif.style.display = "none";
        fallbackImage.style.display = "block"; // 대체 이미지 표시
    }, 3000); // GIF의 재생 시간에 맞춰 조정

    // 마우스를 갖다 댔을 때 재생
    document.querySelector('.animation-container').addEventListener("mouseenter", function () {
        gif.style.display = "block";
        fallbackImage.style.display = "none"; // 대체 이미지 숨기기
    });

    // 마우스를 떼었을 때 GIF 숨기기 및 대체 이미지 표시
    document.querySelector('.animation-container').addEventListener("mouseleave", function () {
        gif.style.display = "none";
        fallbackImage.style.display = "block"; // 대체 이미지 표시
    });
});

document.querySelectorAll('.schedule-box').forEach(box => {
    box.addEventListener('mouseenter', () => {
        console.log('Mouse entered');  // 로그 확인
        box.classList.add('blink');
    });

    box.addEventListener('mouseleave', () => {
        console.log('Mouse left');  // 로그 확인
        box.classList.remove('blink');
    });
});



draw();
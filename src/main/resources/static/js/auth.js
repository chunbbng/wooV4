document.addEventListener("DOMContentLoaded", function () {
    const toggleLoginButton = document.getElementById("toggleLogin");
    const loginBanner = document.getElementById("loginBanner");
    const loginButton = document.getElementById("loginButton");
    const signupButton = document.getElementById("signupButton");
    const headerLogoutButton = document.getElementById("headerLogout");
    const authForm = document.getElementById("authForm");
    const loginFields = document.getElementById("login-fields");
    const signupFields = document.getElementById("signup-fields");
    const messageDiv = document.getElementById("message");

    if (toggleLoginButton) {
        toggleLoginButton.addEventListener("click", function () {
            if (loginBanner && (loginBanner.style.display === "none" || loginBanner.style.display === "")) {
                loginBanner.style.display = "block";
            } else if (loginBanner) {
                loginBanner.style.display = "none";
            }
        });
    }

    if (loginButton) {
        loginButton.addEventListener("click", function () {
            const formData = new FormData(authForm);
            fetch('/auth/login', {
                method: 'POST',
                body: formData
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        messageDiv.innerHTML = data.message + "님 환영합니다!";
                        loginFields.style.display = "none";
                        signupFields.style.display = "none";
                        loginButton.style.display = "none";
                        signupButton.style.display = "none";
                        toggleLoginButton.textContent = "로그아웃"; // 로그인 버튼을 로그아웃 버튼으로 변경
                        if (headerLogoutButton) headerLogoutButton.style.display = "block"; // 로그아웃 버튼 표시
                    } else {
                        messageDiv.innerHTML = "로그인 실패: " + data.error;
                    }
                })
                .catch(error => {
                    messageDiv.innerHTML = "오류 발생: " + error.message;
                });
        });
    }

    if (signupButton) {
        signupButton.addEventListener("click", function () {
            if (signupFields.style.display === "none") {
                signupFields.style.display = "block";
                loginButton.textContent = "회원가입 확인";
            } else {
                const formData = new FormData(authForm);
                fetch('/auth/signup', {
                    method: 'POST',
                    body: formData
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            messageDiv.innerHTML = data.message + "님 환영합니다!";
                            loginFields.style.display = "none";
                            signupFields.style.display = "none";
                            loginButton.style.display = "none";
                            signupButton.style.display = "none";
                            toggleLoginButton.textContent = "로그아웃"; // 로그인 버튼을 로그아웃 버튼으로 변경
                            if (headerLogoutButton) headerLogoutButton.style.display = "block"; // 로그아웃 버튼 표시
                        } else {
                            messageDiv.innerHTML = "회원가입 실패: " + data.error;
                        }
                    })
                    .catch(error => {
                        messageDiv.innerHTML = "오류 발생: " + error.message;
                    });
            }
        });
    }

    if (headerLogoutButton) {
        headerLogoutButton.addEventListener("click", function () {
            fetch('/auth/logout', { method: 'POST' })
                .then(() => {
                    location.reload(); // 페이지 리로드하여 초기 상태로 복구
                })
                .catch(error => {
                    messageDiv.innerHTML = "오류 발생: " + error.message;
                });
        });
    }

    if (toggleLoginButton) {
        toggleLoginButton.addEventListener("click", function () {
            if (toggleLoginButton.textContent === "로그아웃") {
                if (headerLogoutButton) headerLogoutButton.click(); // 로그아웃 기능 실행
            }
        });
    }
});

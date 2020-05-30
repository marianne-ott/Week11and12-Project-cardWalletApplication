const showHide = document.getElementById("showHide");
showHide.addEventListener('click', function show(event) {
    event.preventDefault();
    const password = document.getElementById("password");
    const eye = document.getElementById("eye");
    if (password.type == 'text') {
        eye.classList.add("fa-eye");
        eye.classList.remove("fa-eye-slash");
        password.type = 'password';
    }
    else {
        eye.classList.add("fa-eye-slash");
        eye.classList.remove("fa-eye");
        password.type = 'text';
    }
});
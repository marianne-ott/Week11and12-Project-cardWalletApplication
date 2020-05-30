const email = document.getElementById("email");
const submitElement = document.querySelector('input[type="submit"]');
submitElement.addEventListener('click', function check(event) {
    if(!email.value) {
        event.preventDefault();
        const warningElement = document.getElementById("warningMessage");
        warningElement.innerText = "*Required";
    }
});
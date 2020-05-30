const checkboxElement = document.querySelector('input[type="checkbox"]');
const submitElement = document.querySelector('input[type="submit"]');
submitElement.addEventListener('click', function check(event){
    if(!checkboxElement.checked) {
        event.preventDefault();
        const warningElement = document.getElementById("warningMessage");
        warningElement.innerText="*Please agree to our terms of service.";
    }
});
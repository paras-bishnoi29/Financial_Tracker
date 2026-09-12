
const button = document.getElementById("signupBtn");

button.addEventListener("click", function () {
    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    
    const user = {
        username: username,
        email: email,
        password: password
    }
    fetch("/user/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    })
    .then(Response => Response.text())
    .then(data => {
        console.log(data);
        if(data === "Signup succesfull"){
            alert("Signup Succesfull, Login Now!"); 
            window.location.href = "/login.html";
            clearForm();
        }
        else{
            alert(data);
        }
    })
})

function clearForm() {

    document.getElementById("username").value = "";
    document.getElementById("email").value = "";
    document.getElementById("password").value = "";

}
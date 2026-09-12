const loginToggle = document.getElementById("loginToggle");
const signupToggle = document.getElementById("signupToggle");

const loginForm = document.getElementById("loginForm");
const signupForm = document.getElementById("signupForm");

signupForm.style.display = "block";
loginForm.style.display = "none";

signupToggle.classList.add("active");
loginToggle.classList.remove("active");

loginToggle.addEventListener("click", () => {
    loginForm.style.display = "block";
    signupForm.style.display = "none";

    loginToggle.classList.add("active");
    signupToggle.classList.remove("active");
});

signupToggle.addEventListener("click", () => {
    loginForm.style.display = "none";
    signupForm.style.display = "block";

    signupToggle.classList.add("active");
    loginToggle.classList.remove("active");

});


const button = document.getElementById("signupBtn");

button.addEventListener("click", function () {
    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("signupPassword").value;

    const user = {
        username: username,
        email: email,
        password: password
    }

    if (username.trim() === "" || email.trim() === "" || password.trim() === "") {
        alert("Fill all the fields");
        return;
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
            if (data === "Signup succesfull") {
                alert("Signup Succesfull, Login Now!");
                loginForm.style.display = "block";
                signupForm.style.display = "none";

                clearForm();
            }
            else {
                alert(data);
            }
        })
})

function clearForm() {

    document.getElementById("username").value = "";
    document.getElementById("email").value = "";
    document.getElementById("password").value = "";

}

const buttonLogin = document.getElementById("loginBtn");

buttonLogin.addEventListener("click", function () {
    const usermail = document.getElementById("usermail").value;
    const password = document.getElementById("password").value;

    if (usermail.trim() === "" || password.trim() === "") {
        alert("Fill all fields");
        return
    }

    const loginRequest = {
        usermail: usermail,
        password: password
    }

    fetch("/user/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(loginRequest)
    })
        .then(Response => Response.text())
        .then(data => {
            console.log(data);
            if (data === "Login Succesfull") {
                window.location.href = "/dashboard.html";
            }
            else {
                alert(data);
            }
        })

})
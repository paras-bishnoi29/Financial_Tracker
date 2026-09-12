const button = document.getElementById("loginBtn");

button.addEventListener("click", function(){
    const usermail = document.getElementById("usermail").value;
    const password = document.getElementById("password").value;

    if(usermail.trim()==="" || password.trim()===""){
        alert("Fill all fields");
        return
    }

    const loginRequest = {
        usermail : usermail,
        password : password
    }

    fetch("/user/login", {
        method: "POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify(loginRequest)
    })
    .then(Response => Response.text())
    .then(data =>{
        console.log(data);
        if(data === "Login Succesfull"){
            window.location.href= "/dashboard.html";
        }
        else{
            alert(data);
        }
    })

})
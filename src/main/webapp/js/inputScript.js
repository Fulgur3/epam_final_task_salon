function togglePasswordVisibility() {
    var x = document.getElementById("passwordField");
    if (x.type === "password") {
        x.type = "text";
    } else {
        x.type = "password";
    }
}

function toggleNewPasswordVisibility() {
    var x = document.getElementById("newPasswordField");
    if (x.type === "password") {
        x.type = "text";
    } else {
        x.type = "password";
    }
}
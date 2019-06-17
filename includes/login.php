<?php

include_once 'db.php';
include_once 'functions.php';

sec_session_start();

if(isset($_POST['password'], $_POST['email'])) {

    $password = mysqli_escape_string($mysqli, $_POST['password']);
    $email = mysqli_escape_string($mysqli, $_POST['email']);

    if(login($email, $password, $mysqli)) {
        echo "1";
    } else {
        echo "Ungültige Zugangsdaten";
    }
}
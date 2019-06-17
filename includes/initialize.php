<?php

include_once 'db.php';
include_once 'functions.php';


sec_session_start();


if(!isset($_SESSION['admin'])) {
    echo "0";
    exit();
}

if($mysqli->query("SELECT COUNT(*) as numb FROM users")->fetch_assoc()['numb'] > 0) {
    echo "0";
    exit();
}



if(isset($_POST['pass'], $_POST['email'])) {

    $password = mysqli_escape_string($mysqli, $_POST['pass']);
    $email = mysqli_escape_string($mysqli, $_POST['email']);

    if(strlen($password != 128)) {
        exitMessage("Etwas ist schief gegangen, versuchen Sie es erneut");
    }

    if(!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        exitMessage("Bitte nutzen Sie eine gültige E-Mail Adresse");
    }

    $salt = hash('sha512', uniqid(openssl_random_pseudo_bytes(16), TRUE));
    $password = hash('sha512', $password.$salt);
    $qr = "INSERT INTO users (email, password, salt) values ('$email', '$password', '$salt')";

    if($mysqli->query($qr)) {
        echo "1";
    }
}
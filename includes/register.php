<?php

include_once 'db.php';
include_once 'functions.php';

if(isset($_POST['email'], $_POST['pw1'], $_POST['pw2'], $_POST['firstname'],$_POST['anrede'], $_POST['lastname'], $_POST['city'], $_POST['street'], $_POST['plz'], $_POST['pot'], $_POST['addition'], $_POST['telefon'])) {


    //Hole alle Daten, welche per Ajax hierher geschickt wurden

    $email = mysqli_escape_string($mysqli, $_POST['email']);
    $firstname = mysqli_escape_string($mysqli, $_POST['firstname']);
    $lastname = mysqli_escape_string($mysqli, $_POST['lastname']);
    $city = mysqli_escape_string($mysqli, $_POST['city']);
    $plz = mysqli_escape_string($mysqli, $_POST['plz']);
    $street = mysqli_escape_string($mysqli, $_POST['street']);
    $pw1 = mysqli_escape_string($mysqli, $_POST['pw1']);
    $pw2 = mysqli_escape_string($mysqli, $_POST['pw2']);
    $anrede = mysqli_escape_string($mysqli, $_POST['anrede']);
    $addition = mysqli_escape_string($mysqli, $_POST['addition']);

    $password = $pw1;


    //Honeypot um mögliche Botanmeldungen auszuschließen
    if(!empty($_POST['pot'])) {
        exitMessage("No entry for you");
    }

    //Überprüfe ob Email das richtige Format hat
    if(!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        exitMessage("Bitte nutzen Sie eine gültige E-Mail street");
    }

    if($mysqli->query("SELECT * FROM users WHERE email = '$email'")->num_rows > 0) {
        exitMessage("Diese E-Mail Adresse ist bereits mit einem Konto verknüpft.");
    }

    if(checkPassword($password) == "0") {
        exitMessage("Das Passwort muss mindestens 8 Zeichen lang sein und einen Großbuchstaben sowie eine Zahl enthalten");
    }

    if(empty($firstname) || empty($lastname) || empty($city) || empty($plz) || empty($street)) {
        exitMessage("Bitte füllen Sie alle Felder aus");
    }

    $random_salt = hash('sha512', uniqid(openssl_random_pseudo_bytes(16), TRUE));
    $shapass = hash('sha512', $password);

    $saltedpass = hash('sha512', $shapass.$random_salt);

    $roleid = 2;
    if(isset($_POST['inputCompanyname'])) {
        $companyname = mysqli_escape_string($mysqli, $_POST['inputCompanyname']);
        $roleid = 3;
        $qr = "INSERT INTO users (password, salt, vorname, nachname, adresse, ort, plz, email, roleid, company, addition, approved, regdate) values ('$saltedpass', '$random_salt', '$firstname', '$lastname', '$street', '$city', '$plz', '$email', '$roleid', '$companyname', '$addition', 0, NOW())";
    } else{
        $objectinfo = 'Objektadresse: '.mysqli_escape_string($mysqli, $_POST['objectstrasse']).' '.mysqli_escape_string($mysqli, $_POST['objectnr']).' '.mysqli_escape_string($mysqli, $_POST['objectplz']).' '.mysqli_escape_string($mysqli, $_POST['objectcity']).' Zusatz: '.mysqli_escape_string($mysqli, $_POST['objectaddition']);

        $qr = "INSERT INTO users (password, salt, vorname, nachname, adresse, ort, plz, email, roleid, addition, approved, regdate, objectinfo) VALUES ('$saltedpass', '$random_salt', '$firstname', '$lastname', '$street', '$city', '$plz', '$email', '$roleid', '$addition', 0, NOW(), '$objectinfo')";
    }


    if($mysqli->query($qr)) {
        echo "1";
    } else {
        exitMessage("Es ist ein Fehler aufgetreten.");
    }

    $mysqli->close();
}


?>
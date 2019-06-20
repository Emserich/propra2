<?php
/**
 * Das sind die Login-Angaben für die Datenbank
 */  
define("HOST", "localhost");     // Der Host mit dem wir uns verbinden
define("USER", "root");    // Der Datenbank-Benutzername
define("PASSWORD", '');    // Das Datenbank-Passwort
define("DATABASE", "propra1");    // Der Datenbankname

define("SECURE", TRUE);

$mysqli = new mysqli(HOST, USER, PASSWORD, DATABASE);

$mysqli->set_charset("utf8");

function query($qr) {
    return $mysqli->query($qr);
}
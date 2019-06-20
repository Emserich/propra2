<?php

function exitMessage($message) {
    echo $message;
    exit();
}

function generateRandomString($length = 10) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
}

function getUserRoleName($id, $mysqli) {
    return getRoleName($mysqli->query("SELECT roleid FROM users WHERE id = '$id'")->fetch_assoc()['roleid']);
}

function getRoleName($roleid) {
    if($roleid == 1) {
        return 'Admin';
    } else if ($roleid == 2) {
        return 'Verwalter';
    } else if ($roleid == 3) {
        return 'Vermieter';
    } else if ($roleid == 4) {
        return 'Mieter';
    } else {
        return 'Eigentümer';
    }
}

function enToGer($date) {
    return $date{8}.$date{9}.'.'.$date{5}.$date{6}.'.'.$date{0}.$date{1}.$date{2}.$date{3};
}

function checkPassword($password) {


        if(strlen($password) < 8) {
            return "0";
        }
    
        if(!preg_match('/[0-9]/',$password)) {
            return "0";
        }
    
        if(!preg_match('/[A-Z]/', $password)) {
            return "0";
        }

        return "1";
}

function sec_session_start() {

    $session_name = 'sec_session_id';   // vergib einen Sessionnamen
    $secure = false;
    // Damit wird verhindert, dass JavaScript auf die session id zugreifen kann. 
    $httponly = true;

    // Zwingt die Sessions nur Cookies zu benutzen.

    if (ini_set('session.use_only_cookies', 1) === FALSE) {
    	header("Location: ../error.php?err=Could not initiate a safe session (ini_set)");
    	exit();
    }

    // Holt Cookie-Parameter.
    $cookieParams = session_get_cookie_params();
    session_set_cookie_params($cookieParams["lifetime"],$cookieParams["path"],$cookieParams["domain"],$secure,$httponly);

    // Setzt den Session-Name zu oben angegebenem.
    session_name($session_name);
    session_start();            // Startet die PHP-Sitzung 
    session_regenerate_id();    // Erneuert die Session, löscht die alte. 

  }




function login($email, $password, $mysqli)
{
    // Das Benutzen vorbereiteter Statements verhindert SQL-Injektion.
    if ($stmt = $mysqli->prepare("SELECT id, password, salt, roleid, vorname, nachname FROM users WHERE email = ? LIMIT 1")) {

        $stmt->bind_param('s', $email);  // Bind "$email" to parameter.
        $stmt->execute();    // Führe die vorbereitete Anfrage aus.
        $stmt->store_result();

        // hole Variablen von result.
        $stmt->bind_result($user_id, $db_password, $salt, $roleid, $vorname, $nachname);
        $stmt->fetch();

        // hash das Passwort mit dem eindeutigen salt.
        $password = hash('sha512', $password . $salt);
        
        if ($stmt->num_rows == 1) {
            // Wenn es den Benutzer gibt, dann wird überprüft ob das Konto
            // blockiert ist durch zu viele Login-Versuche 
            if (checkbrute($user_id, $mysqli) == true) {
                // Konto ist blockiert 
                return false;
            } else {
                // Überprüfe, ob das Passwort in der Datenbank mit dem vom
                // Benutzer angegebenen übereinstimmt.
                if ($db_password == $password) {

                    // Passwort ist korrekt!
                    // Hole den user-agent string des Benutzers.
                    $user_browser = $_SERVER['HTTP_USER_AGENT'];

                    // XSS-Schutz, denn eventuell wird der Wert gedruckt
                    $user_id = preg_replace("/[^0-9]+/", "", $user_id);
                    $_SESSION['user_id'] = $user_id;
                    $_SESSION['username'] = $vorname.' '.$nachname;
                    $_SESSION['email'] = $email;
                    $_SESSION['login_string'] = hash('sha512',$password . $user_browser);
                    $_SESSION['roleid'] = $roleid;

                    session_write_close();

                    // Login erfolgreich.
                    return true;
                } else {

                    // Passwort ist nicht korrekt
                    // Der Versuch wird in der Datenbank gespeichert
                    $now = time();
                    $mysqli->query("INSERT INTO login_attempts(user_id, time) VALUES ('$user_id', '$now')");

                    return false;
                }
            }
        } else {

            //Es gibt keinen Benutzer.
            return false;
        }
    }
}



function checkbrute($user_id, $mysqli)
{
    // Hole den aktuellen Zeitstempel 
    $now = time();

    // Alle Login-Versuche der letzten zwei Stunden werden gezählt.
    $valid_attempts = $now - (2 * 60 * 60);
    if ($stmt = $mysqli->prepare("SELECT time FROM login_attempts WHERE user_id = ? AND time > '$valid_attempts'")) {
        $stmt->bind_param('i', $user_id);

        // Führe die vorbereitet Abfrage aus. 
        $stmt->execute();
        $stmt->store_result();

        // Wenn es mehr als 5 fehlgeschlagene Versuche gab 
        if ($stmt->num_rows > 5) {
            return true;
        } else {
            return false;
        }
    }
}



function login_check($mysqli)
{
    // Überprüfe, ob alle Session-Variablen gesetzt sind 
    if (isset($_SESSION['email'], $_SESSION['roleid'], $_SESSION['login_string'], $_SESSION['user_id'])) {

        $user_id = $_SESSION['user_id'];
        $login_string = $_SESSION['login_string'];

        // Hole den user-agent string des Benutzers.
        $user_browser = $_SERVER['HTTP_USER_AGENT'];

        if ($stmt = $mysqli->prepare("SELECT password FROM users WHERE id = ? LIMIT 1")) {

            // Bind "$user_id" zum Parameter. 
            $stmt->bind_param('i', $user_id);
            $stmt->execute();   // Execute the prepared query.
            $stmt->store_result();



            if ($stmt->num_rows == 1) {
                // Wenn es den Benutzer gibt, hole die Variablen von result.
                $stmt->bind_result($password);
                $stmt->fetch();
                $login_check = hash('sha512', $password . $user_browser);

                if ($login_check == $login_string) {
                    // Eingeloggt!!!! 
                    return true;
                } else {
                    // Nicht eingeloggt
                    return false;
                }
            } else {
                // Nicht eingeloggt
                return false;
            }
        } else {
            // Nicht eingeloggt
            return false;
        }
    } else {
        // Nicht eingeloggt
        return false;
    }
}

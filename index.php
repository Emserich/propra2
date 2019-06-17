<?php

include_once 'includes/db.php';
    include_once 'includes/functions.php';

    sec_session_start();
    
    if($mysqli->query("SELECT * FROM users")->num_rows == 0) {
      header('Location: admin.php');
      exit();
    }
    $title = "Anmelden";
    include_once("header.php");
?>

  <body class="text-center">

    <div class="form-signin">
      <img src="images/logo.png" class="mb-4" width="72" height="72" alt="">
      <h1 class="h3 mb-4 font-weight-normal">Immoboss Pro</h1>
      <label for="inputEmail" class="sr-only">E-Mail-Adresse</label>
      <input type="email" id="inputEmail" class="form-control" placeholder="E-Mail-Adresse" required autofocus>
      <label for="inputPassword" class="sr-only">Passwort</label>
      <input type="password" id="inputPassword" class="form-control" placeholder="Passwort" required>

      <div class="my-4">
        <a href="register.php">Als Eigentümer registrieren?</a>
      </div>

      <button class="btn btn-lg btn-primary btn-block" onclick="login()">Anmelden</button>
      
      <div class="alert alert-danger" id="errortext" role="alert" style="display: none">
      </div>
    </div>

    <footer class="footerfixed text-muted">&copy; ProPra'19 Gruppe 3</footer>
  </body>


  <script src="js/sha512.min.js"></script>
  <script src="js/jquery-3.4.1.min.js"></script>

  <script src="js/popper.min.js"></script>
  <script src="js/bootstrap.min.js"></script>

  <script>

    $('input').on('keypress', (event)=> {
      if(event.which === 13){
        $('.btn').click();
      }
    });

    function errorMessage(text) {
      $('#errortext').show(100);
      $('#errortext').html(text);
    }

    function login() {

      let password = $('#inputPassword').val(),
          email = $('#inputEmail').val();

      if (!email || !password) {
        errorMessage("Bitte geben Sie eine E-Mail und das zugehörige Passwort an");
        return;
      }

      $.post('includes/login.php',{email:email, password:sha512(password)}).done((data) => {
          if(data == "1") {
            window.location.href = './frontend';
          } else {
            errorMessage(data);
          }
      }).fail((data) => console.log(data));
    }
  </script>

</html>
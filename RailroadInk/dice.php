<!doctype html>
<html lang="de">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Markowski, Maryschok, Tchudjo, Steffens, Zöller">

    <title>Würfel</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="libs/css/bootstrap.min.css">
    <link href="css/bs_overwrite.css" rel="stylesheet"> 


    <link rel="stylesheet" href="css/dice_style.css">

  </head>

  <body>
    <div class="container">

      <div class="row my-5">
        <div class="col-md-12">


          <div class="dice">
            <ol class="die-list even-roll" data-roll="1" id="die-1">
              <li class="die-item" data-side="1">
                <img src="images/dice_1.png">
              </li>
              <li class="die-item" data-side="2">
                <img src="images/dice_2.png">
              </li>
              <li class="die-item" data-side="3">
                <img src="images/dice_3.png">
              </li>
              <li class="die-item" data-side="4">
                <img src="images/dice_4.png">
              </li>
              <li class="die-item" data-side="5">
                <img src="images/dice_5.png">
              </li>
              <li class="die-item" data-side="6">
                <img src="images/dice_6.png">
              </li>
            </ol>
            <ol class="die-list odd-roll" data-roll="1" id="die-2">
              <li class="die-item" data-side="1">
                <img src="images/dice_7.png">
              </li>
              <li class="die-item" data-side="2">
                <img src="images/dice_9.png">
              </li>
              <li class="die-item" data-side="3">
                <img src="images/dice_9.png">
              </li>
              <li class="die-item" data-side="4">
                <img src="images/dice_9.png">
              </li>
              <li class="die-item" data-side="5">
                <img src="images/dice_9.png">
              </li>
              <li class="die-item" data-side="6">
                <img src="images/dice_7.png">
              </li>
            </ol>
          </div>
          <button class="btn btn-primary" type="button" id="roll-button">Komm ficken</button>

          
        </div>
      </div>

    </div><!-- /container -->


    <footer class="container text-muted mt-5">
      <hr>
      &copy; ProPra'19 Gruppe 3
    </footer>

  </body>


  <?php
    include_once ("footer_bs.php");
  ?>

  <script src="js/dice.js"></script>



</html>
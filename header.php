<?php

  include_once 'includes/db.php';
  include_once 'includes/functions.php';

?>

<!doctype html>
<html lang="de">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Markowski, Maryschok, Tchudjo, Steffens, Zöller">

    <title>
      <?=$title?>  
    </title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="libs_frontend/css/bootstrap.min.css">
    <link href="css/bs_overwrite.css" rel="stylesheet"> <!-- Änderungen am Bootstrap-Code durch Überschreiben in separater Datei  -->

    <?php   
        if (isset($cssFile)) { //$cssFile ist z.B. "index", dann wird index_style.css eingebunden
          echo '<link rel="stylesheet" href="css/'.$cssFile.'_style.css">';
        }
        else {
          echo '';
        }
    ?>

  </head>
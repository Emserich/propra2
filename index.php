<?php

  $cssFile = "index";
  $title = "Railroad Ink";
  include_once("header.php");

?>

  <body>
    <div class="container">

      <div class="row"><!-- Übersichten -->
        <div class="col-md-12">

          <div class="row">
            <div class="col-md-12">Spezialstrecken</div>
          </div>

          <div class="row" style="border: 1px dotted black;">
            <div class="col-md-12">Würfelübersicht</div>
          </div>

          <div class="row" style="border: 1px dotted black;">
            <div class="col-md-12">Wertungszeile</div>
          </div>

          <div class="row" style="border: 1px dotted black;">
            <div class="col-md-12">Punkteübersicht</div>
          </div>          

        </div>
      </div>


      <div class="row row_board"><!-- 7x7-Felder -->
        <div class="col-md-12 col_board">

          <?php $fieldnr=1 ?>

          <div class="row fieldrow">
            <div class="col-md-12">
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12">
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12">
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12">
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12">
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12">
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12">
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
              <div class="field" id="field_<?php echo($fieldnr) ?>">
                <?php echo ($fieldnr++) ?>
              </div>
            </div>
          </div>
          
        </div>
      </div>

    </div><!-- /container -->


    <footer class="container text-muted">
      <hr>
      &copy; ProPra'19 Gruppe 3
    </footer>

  </body>


  <?php
    include_once ("footer_bs.php");
  ?>

  <script src="js/index.js"></script>



</html>
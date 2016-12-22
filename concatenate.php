<?php
/**
 * concatenate.php
 * Concatenates the Netflix Prize traning_set files into one single file. 
 * Input arguments: [1] : Directory of traning_set files
 *                  [2] : Path to output file. If file does not exist, 
 *                        the script will create a new one.
 * The script formats the concatenated in following:
 * [item id],[user id],[rating]
 * [item id],[user id],[rating]
 * ...
 * Date is an optional variable, dependent on use (i.e, Temporal dynamics).
 */

$time_start = microtime(true);

$dirWithMovies = $_SERVER["argv"][1];
$outFile = $_SERVER["argv"][2];

is_dir($dirWithMovies) 
  or die($dirWithMovies . " is not a directory.\n");

$dh = opendir($dirWithMovies)
  or die("Error opening directory: " . $dirWithMovies);

$ofile = fopen($outFile, "w");

while (($file = readdir($dh)) != FALSE) {
  $file = $dirWithMovies . "/" . $file;
  $fc = file($file);

  $itemID = array_shift($fc);
  $itemID= rtrim($itemID);
  $itemID = rtrim($itemID,":");
  
  foreach ($fc as $line) {
    $pieces = explode(',', $line);
    $userID = $pieces[0];
    $rating = $pieces[1];
    //$date = $pieces[2];

    $outLine =  $itemID . ','  . $userID . ',' . $rating .  "\n";
    fwrite($ofile, $outLine) 
      or die("Error writing to file " . $outFile . "\n");
  }

}

closedir($dh);

$time_end = microtime(true);
$time = $time_end - $time_start;
echo "Runtime: ".round($time,2)." seconds\n";
?>
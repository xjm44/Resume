<!--

  // learnt from https://www.youtube.com/watch?v=HOcFFJr2YdE
  // echo "aaa";
    // $host = "";
    // $db = mysqli_connect($host,"root","","database.db");
    // if($db->connect_error){
    //   die("Connection failed: " . $db->connect_error);
    // }
    // // $db  = mysqli_connect($host,"","","database.db")
    //     // $result = $db->query("SELECT * FROM Users where user_id=5");
    //     // while ($row = $result->fetchArray()){
    //     //   echo $row['first_name'];
    //     // }
    //     // echo $result->fetchArray()['first_name'];
    //
    //     $result = array();
    //     $message = $_POST['message'];
    //     $sender = $_POST['sender'];
    //     $receiver = $_POST['receiver'];
    //
    //     $sql = "INSERT INTO 'message' (`text`,`sender`,`receiver`)VALUES ('".$message."','".$sender."','".$receiver."')";
    //     $result['status'] = $db->query($sql);
    //
    //     //print msg
    //
    //     $start = isset($_GET['start']) ? intval($_GET['start']):0;
    //     $items = $db->query("SELECT * FROM `message` WHERE `id`>" . $start);
    //     while($row = $items->fetch_assoc()){
    //       $result['items'][] = $row;
    //     }
    //
    //     $db->close();
    //     header('Access-Control-Allow-Origin: *');
    //     header('Content-Type: application/json');

        // echo json_encode($result);
    // } else {
    //     echo "Connection to database failed!\n";
    // }
 -->




 <?php
echo(123)
 $con = mysql_connect("localhost","","","../database.db");

 if (!$con)

   {

   die('Could not connect: ' . mysql_error());

   }







 $sql="INSERT INTO message (text,sender, receiver)

 VALUES

 ('$_POST[message]','2','3')";



 if (!mysql_query($sql,$con))

   {

   die('Error: ' . mysql_error());

   }

 echo "1 record added";



 mysql_close($con)

 ?>

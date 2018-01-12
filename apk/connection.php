<?php 
	$array = [];
	if(!isset($_SERVER['PHP_AUTH_USER'])){
		echo json_encode($array);
	} else {
		$user = $_SERVER['PHP_AUTH_USER'];
		$password = $_SERVER['PHP_AUTH_PW'];
	}

	if ($_SERVER['REQUEST_METHOD'] === 'POST') {
		// The request is using the POST method
		$con = mysqli_connect($_SERVER['HTTP_HOST'],$user,$password,"meteo");
		if (mysqli_connect_errno() != 0)
		{
			$code = mysqli_connect_errno();
			echo json_encode(http_response_code($code));
		} else {
			$datetime = $_POST["datetime"];
			$dev_id = $_POST["dev_id"];
			$sql= "SELECT * FROM `dust` WHERE `datetime` > '$datetime' AND `dev_id` = $dev_id ";
			$result = mysqli_query($con ,$sql);
				
			while ($row = mysqli_fetch_assoc($result)) {
				$array[] = $row;
			}
				
			header('Content-Type:Application/json');
			echo json_encode($array);
			mysqli_free_result($result);
			mysqli_close($con);
		}
	}
 ?>
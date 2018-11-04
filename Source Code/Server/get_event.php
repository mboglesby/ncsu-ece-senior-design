<?php
	$mysql_hostname = 'mysql-people.eos.ncsu.edu:3306';
	$mysql_database_name = 'Pctsui';
	$php_admin_username = 'PctsuiA';
	$php_admin_password = 'p-!l4l@0rClJbCtm';
	
	// Connect to database
	$db_connection = mysql_connect($mysql_hostname, $php_admin_username, $php_admin_password);
	if (!$db_connection)
	{
		die("Error connecting to database server '$mysql_hostname': " . mysql_error() . "");
	}
	$db_connection_select = mysql_select_db($mysql_database_name);
	if (!db_connection_select)
	{
		die("Error connecting to database '$mysql_database_name': " . mysql_error() . "");
	}
	
	// Get JSON data (mac_address, user_name) from mobile app
	$json = $_SERVER['HTTP_JSON'];
	$json_data = json_decode($json);
	$user_name = $json_data->user_name;
	
	// Initialize variables
	$json_array = array();
	
	// Get today's date
	date_default_timezone_set('EST');
	$date = new DateTime();
	$date_string = $date->format("Y-m-d");
	
	// Check to see if an event has been generated for today
	$sql = "SELECT event_id FROM events WHERE date='$date_string' LIMIT 1";
	$res = mysql_query($sql);
	// If an event has not been generated for today, generate one
	if (!($row = mysql_fetch_array($res)))
	{
		$event_zone = rand(21, 24);
		$num_teammates = rand(2, 5);
		$bonus_score = rand(50, 500);
		
		$sql = "INSERT INTO events (date, event_zone, num_teammates, bonus_score)
				VALUES ('$date_string', $event_zone, $num_teammates, $bonus_score)";
		mysql_query($sql);
	}

	// Get today's event(s)
	$sql = "SELECT event_zone, num_teammates, bonus_score FROM events WHERE date='$date_string'";
	$res = mysql_query($sql);
	$i = 1;
	while($row = mysql_fetch_array($res))
	{
		// Find player's team
		$sql = "SELECT team_name FROM players WHERE user_name='$user_name' LIMIT 1";
		$res = mysql_query($sql);
		$row2 = mysql_fetch_array($res);
		$team_name = $row2[0];
		
		// Find out whether or not team has completed event
		$completed = false;
		if ($team_name != "none")
		{
			$sql = "SELECT event_completed_date FROM teams WHERE team_name='$team_name' LIMIT 1";
			$res = mysql_query($sql);
			$row3 = mysql_fetch_array($res);
			if ($row3[0] == $date_string)
			{
				$completed = true;
			}
		}
				
		// Add event data to json array
		$event_name = "$row[1] teammates at Zone $row[0]";
		$event_array = array('event_name'=>$event_name, 'bonus_score'=>$row[2], 'completed'=>$completed);
		$json_array[$i] = $event_array;
		$i++;
	}
	
	// return json array to mobile app
	header('Content-type: application/json');
	echo json_encode($json_array);

?>
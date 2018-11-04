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
	$mac_address = strtoupper($json_data->mac_address);
	$user_name = $json_data->user_name;
	
	//---- DISTANCE/ZONE HANDLING CODE ----
	
	// Initialize variables
	$current_zone_id;
	$lat;
	$lon;
	$previous_zone_id;
	$previous_user_distance;
	$team_name;
	$user_id;
	$previous_team_distance;
	$latest_user_distance;
	$new_team_distance;
	
	// Get current_zone_id from mac_address
	$sql = "SELECT zone_id, lat, lon FROM zones WHERE mac_address='$mac_address' LIMIT 1";
	$res = mysql_query($sql);
	if ($row = mysql_fetch_row($res))
	{
		$current_zone_id = intval($row[0]);
		$lat = $row[1];
		$lon = $row[2];
	}
	else
	{
		header('Content-type: application/json');
		$json_array = array('error'=>"MAC Address not found.");
		echo json_encode($json_array);
		die;
	}
	
	// Get previous_zone_id, previous_user_distance, team_name from user_name
	$sql = "SELECT previous_zone_id, total_distance, team_name, user_id FROM players WHERE user_name='$user_name' LIMIT 1";
	$res = mysql_query($sql);
	if ($row = mysql_fetch_row($res))
	{
		$previous_zone_id = intval($row[0]);
		$previous_user_distance = intval($row[1]);
		$team_name = $row[2];
		$user_id = $row[3];
	}
	else
	{
		header('Content-type: application/json');
		$json_array = array('error'=>"Player not found.");
		echo json_encode($json_array);
		die;
	}
	
	// If the user is on a team, get previous_team_distance from team_name
	if ($team_name != "none")
	{
		$sql = "SELECT total_distance FROM teams WHERE team_name='$team_name' LIMIT 1";
		$res = mysql_query($sql);
		if ($row = mysql_fetch_row($res))
		{
			$previous_team_distance = intval($row[0]);
		}
		else
		{
			header('Content-type: application/json');
			$json_array = array('error'=>"Team not found.");
			echo json_encode($json_array);
			die;
		}
	}
	
	// If the user has changed zones, get distance from last zone to current zone
	if (($previous_zone_id != $current_zone_id) && ($previous_zone_id != 0))
	{
		$distance_description;
		if ($previous_zone_id < $current_zone_id)
		{
			$distance_description = "$previous_zone_id" . "to" . "$current_zone_id";
		}
		else
		{
			$distance_description = "$current_zone_id" . "to" . "$previous_zone_id";
		}
		$sql = "SELECT distance FROM distances WHERE description='$distance_description' LIMIT 1";
		$res = mysql_query($sql);
		if ($row = mysql_fetch_row($res))
		{
			$latest_user_distance = intval($row[0]);
		}
		else
		{
			header('Content-type: application/json');
			$json_array = array('error'=>"Distance not found.");
			echo json_encode($json_array);
			die;
		}
	}
	// If the user hasn't changed zones, latest_user_distance = 0
	else
	{
		$latest_user_distance = 0;
	}
	
	// Calculate new user distance
	$new_user_distance = $previous_user_distance + $latest_user_distance;
	
	if ($team_name != "none")
	{
		// Calculate new team distance
		$new_team_distance = $previous_team_distance + $latest_user_distance;
	}
	
	// Get today's date/time
	date_default_timezone_set('EST');
	$date = new DateTime();
	$date_string = $date->format("Y-m-d");
	$date_time_string = $date->format("Y-m-d H:i");
	
	// Update previous_zone_datetime
	$sql = "UPDATE players SET previous_zone_datetime='$date_time_string' WHERE user_name='$user_name'";
	mysql_query($sql);
		
	// Update previous_zone_id
	$sql = "UPDATE players SET previous_zone_id=$current_zone_id WHERE user_name='$user_name'";
	mysql_query($sql);
	
	//---- EVENT GENERATION CODE ----
	
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
	
	//---- EVENT HANDLING CODE ----
	// (currently set up to generate and manage one event per day)
	
	// Initialize variables
	$event_completed_date = $date_string;
	
	// If the user is on a team, fetch event_completed_date
	if ($team_name != "none")
	{
		$sql = "SELECT event_completed_date FROM teams WHERE team_name='$team_name' LIMIT 1";
		$res = mysql_query($sql);
		$row = mysql_fetch_array($res);
		$event_completed_date = $row[0];
	}
	
	// If the user's team has not completed the event, check to see if the requisite number of players are in the event zone
	if ($event_completed_date != $date_string)
	{
		// Retrieve event information
		$sql = "SELECT event_zone, num_teammates, bonus_score FROM events WHERE date='$date_string' LIMIT 1";
		$res = mysql_query($sql);
		$row = mysql_fetch_array($res);
		$event_zone = intval($row[0]);
		$num_teammates = intval($row[1]);
		$bonus_score = intval($row[2]);
		if ($event_zone == $current_zone_id)
		{
			$sql = "SELECT previous_zone_id, previous_zone_datetime FROM players WHERE team_name='$team_name'";
			$res = mysql_query($sql);
			$count = 0;
			// Check to see if requisite number of players are in event zone
			while($row2 = mysql_fetch_array($res))
			{
				$teammate_zone_id = intval($row2[0]);
				$teammate_zone_datetime = $row2[1];
				if (($teammate_zone_id == $current_zone_id) && ($teammate_zone_datetime == $date_time_string))
				{
					$count++;
				}
			}
			// If team has completed event, award bonus points
			if ($count >= $num_teammates)
			{
				$new_team_distance = $new_team_distance + $bonus_score;
				$sql = "UPDATE teams SET event_completed_date='$date_string' WHERE team_name='$team_name'";
				mysql_query($sql);
			}
		}
	}
	
	// Update user distance
	$sql = "UPDATE players SET total_distance=$new_user_distance WHERE user_name='$user_name'";
	mysql_query($sql);
	
	if ($team_name != "none")
	{
		// Update team distance
		$sql = "UPDATE teams SET total_distance=$new_team_distance WHERE team_name='$team_name'";
		mysql_query($sql);
	}
	
	//---- ACTIVITY LOGGING CODE ----
	
	$sql = "INSERT INTO activity_log (user_id, zone_id, timestamp)
			VALUES ($user_id, $current_zone_id, '$date_time_string')";
	mysql_query($sql);
	
	//---- JSON RETURN CODE ----
	
	header('Content-type: application/json');
	$json_array = array('current_zone_id'=>$current_zone_id, 'new_total_distance'=>$new_user_distance);
	echo json_encode($json_array);
	
?>
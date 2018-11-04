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
	
	// Get JSON data (team_name) from mobile app
	$json = $_SERVER['HTTP_JSON'];
	$json_data = json_decode($json);
	$user_name = $json_data->user_name;
	$team_name = $json_data->team_name;
	$join = $json_data->join;
	
	// Initialize variables
	$success = null;
	$error = null;
	
	// Check to see if team exists
	$sql = "SELECT team_id FROM teams WHERE team_name='$team_name'";
	$res = mysql_query($sql);
	// if team exists, return error
	if ($row = mysql_fetch_row($res))
	{
    	$error = "Team '$team_name' already exists.";
    	$join = false;
    }
	// if team doesn't exist, create team
    else
    {
		$sql = "INSERT INTO teams (team_name, total_distance, event_completed_date) 
    			VALUES ('$team_name', 0, 'never')";
    	if (mysql_query($sql))
    	{
    		$success = "Team '$team_name' created.";
    	}
    	else
    	{
    		$error = "Error creating team.";
    		$join = false;
    	}
    }
    
    // if user wants to join team, have user join team
    if ($join)
    {
    	// Check to see if user exists
		$sql = "SELECT user_id FROM players WHERE user_name='$user_name'";
		$res = mysql_query($sql);
		// if the user exists, switch his/her team
		if ($row = mysql_fetch_row($res))
		{
			$sql = "UPDATE players SET team_name='$team_name' WHERE user_name='$user_name'";
    		if (mysql_query($sql))
    		{
    			$success = $success . " User '$user_name' is now on team '$team_name'.";
    		}
    		else
    		{
    			$error = "Error joining team.";
    		}
   		}
		// If user doesn't exist, return error
		else
		{
			$error = "Error: user '$user_name' does not exist.";
    	}
    }
    
    // Return messages to mobile app
	header('Content-type: application/json');
	$json_array = array('success'=>$success, 'error'=>$error);
	echo json_encode($json_array);
?>
    
   
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
	
	// Get JSON data (user_name, team_name) from mobile app
	$json = $_SERVER['HTTP_JSON'];
	$json_data = json_decode($json);
	$user_name = $json_data->user_name;
	
	// Initialize variables
	$success = null;
	$error = null;
	
	// Check to see if user exists
	$sql = "SELECT user_id FROM players WHERE user_name='$user_name'";
	$res = mysql_query($sql);
	// If user exists, return error
	if ($row = mysql_fetch_row($res))
	{
		$error = "Username '$user_name' is already taken.";
	}
	// If user doesn't exist, create user
	else
	{
		$sql = "INSERT INTO players (user_name, team_name, previous_zone_id, previous_zone_datetime, total_distance) 
    			VALUES ('$user_name', 'none', 0, 'empty', 0)";
    	if (mysql_query($sql))
    	{
    		$success = "User '$user_name' created.";
    	}
    	else
    	{
    		$error = "Error creating user.";
    	}
    }
    
    // Return messages to mobile app
	header('Content-type: application/json');
	$json_array = array('success'=>$success, 'error'=>$error);
	echo json_encode($json_array);
?>
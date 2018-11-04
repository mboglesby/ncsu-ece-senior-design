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
	
	// Get JSON data (user_name) from mobile app
	$json = $_SERVER['HTTP_JSON'];
	$json_data = json_decode($json);
	$user_name = $json_data->user_name;
	
	// Initialize variables
	$success = null;
	$error = null;
	$team_name = null;
	$total_distance = null;
	
	// Check to see if user exists
	$sql = "SELECT team_name, total_distance FROM players WHERE user_name='$user_name'";
	$res = mysql_query($sql);
	// if the user exists, get team_name and total_distance
	if ($row = mysql_fetch_row($res))
	{
		$success = "User '$user_name' is now logged in.";
		$team_name = $row[0];
		$total_distance = intval($row[1]);
   	}
	// If user doesn't exist, return error
	else
	{
		$error = "Error: user '$user_name' does not exist.";
    }
    
    // Return messages to mobile app
	header('Content-type: application/json');
	$json_array = array('success'=>$success, 'team_name'=>$team_name, 'total_distance'=>$total_distance, 'error'=>$error);
	echo json_encode($json_array);
?>
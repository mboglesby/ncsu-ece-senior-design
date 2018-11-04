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
	
	// Get today's date
	date_default_timezone_set('EST');
	$date = new DateTime();
	$date_time_string = $date->format("Y-m-d H:i");
	
	// Get user_id
	$sql = "SELECT user_id FROM players WHERE user_name='$user_name' LIMIT 1";
	$res = mysql_query($sql);
	$row = mysql_fetch_row($res);
	$user_id = $row[0];

	// Update log
	$sql = "INSERT INTO messages (user_id, timestamp)
			VALUES ($user_id, '$date_time_string')";
	mysql_query($sql);

?>
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
	
	// Initialize variables
	$json_array = array();

	//$sql = "SELECT team_name, total_distance FROM teams ORDER BY total_distance DESC LIMIT 10";
	$sql = "SELECT team_name, total_distance FROM teams ORDER BY total_distance DESC";
	$res = mysql_query($sql);
	$i = 1;
	//while(($row = mysql_fetch_array($res)) && ($i<=10))
	while($row = mysql_fetch_array($res))
	{
		$team_array = array('team_name'=>$row[0], 'distance'=>$row[1]);
		$json_array[$i] = $team_array;
		$i++;
	}
	
	if (sizeof($json_array) == 0)
	{
		echo "{}";
	}
	else
	{
		header('Content-type: application/json');
		echo json_encode($json_array);
	}
?>
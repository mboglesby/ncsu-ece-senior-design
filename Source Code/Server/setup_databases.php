<html>
<head>
</head>
<body>
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

	// Drop all tables
	// From http://www.ebrueggeman.com/blog/drop-all-tables-in-mysql
	// (inherited from Andrew Williams)
	$sql = "SHOW TABLES FROM $mysql_database_name";
	if($result = mysql_query($sql)){
  		/* add table name to array */
  		while($row = mysql_fetch_row($result)){
    		$found_tables[]=$row[0];
  	}}else{
  		die("Error, could not list tables. MySQL Error: " . mysql_error());
	}
	/* loop through and drop each table */
	foreach($found_tables as $table_name){
  		$sql = "DROP TABLE $mysql_database_name.$table_name";
  		if($result = mysql_query($sql)){
    		echo "Table '$table_name' deleted successfully.<br />";
  		}  else{
    		echo "Error deleting '$table_name'. MySQL Error: " . mysql_error() . "";
  		}
	}
	
	// Re-init players table
	$sql = "CREATE TABLE players 
			(
				user_id INT(11) NOT NULL AUTO_INCREMENT, 
				user_name VARCHAR(30) NOT NULL, 
				team_name VARCHAR(30) NOT NULL, 
				previous_zone_id INT(11) NOT NULL, 
				previous_zone_datetime VARCHAR(30) NOT NULL,
				total_distance INT(30) NOT NULL,
				PRIMARY KEY (user_id)
			) 
			ENGINE=MyISAM";
	mysql_query($sql) || die("Error creating table: " . mysql_error() . "");
	echo "Created table 'players'<br />";
	
	// Re-init zones table
	$sql = "CREATE TABLE zones 
			(
				entry_id INT(11) NOT NULL AUTO_INCREMENT, 
				zone_id INT(11) NOT NULL, 
				zone_description VARCHAR(30), 
				mac_address VARCHAR(30) NOT NULL, 
				lat VARCHAR(30) NOT NULL, 
				lon VARCHAR(30) NOT NULL, 
				PRIMARY KEY (entry_id)
			) 
			ENGINE=MyISAM";
	mysql_query($sql) || die("Error creating table: " . mysql_error() . "");
	echo "Created table 'zones'<br />";
	
	// Re-init distances table
	$sql = "CREATE TABLE distances 
			(
				distance_id INT(11) NOT NULL AUTO_INCREMENT, 
				description VARCHAR(30) NOT NULL, 
				distance INT(11) NOT NULL, 
				PRIMARY KEY (distance_id)
			) 
			ENGINE=MyISAM";
	mysql_query($sql) || die("Error creating table: " . mysql_error() . "");
	echo "Created table 'distances'<br />";
	
	// Re-init teams table
	$sql = "CREATE TABLE teams 
			(
				team_id INT(11) NOT NULL AUTO_INCREMENT, 
				team_name VARCHAR(30) NOT NULL, 
				total_distance INT(30) NOT NULL, 
				event_completed_date VARCHAR(30) NOT NULL, 
				PRIMARY KEY (team_id)
			) 
			ENGINE=MyISAM";
	mysql_query($sql) || die("Error creating table: " . mysql_error() . "");
	echo "Created table 'teams'<br />";
	
	// Re-init events table
	$sql = "CREATE TABLE events 
			(
				event_id INT(11) NOT NULL AUTO_INCREMENT, 
				date VARCHAR(30) NOT NULL, 
				event_zone INT(11) NOT NULL, 
				num_teammates INT(11) NOT NULL, 
				bonus_score INT(30) NOT NULL, 
				PRIMARY KEY (event_id)
			) 
			ENGINE=MyISAM";
	mysql_query($sql) || die("Error creating table: " . mysql_error() . "");
	echo "Created table 'teams'<br />";
	
	// Re-init messages table
	$sql = "CREATE TABLE messages 
			(
				event_id INT(11) NOT NULL AUTO_INCREMENT, 
				user_id INT(11) NOT NULL, 
				timestamp VARCHAR(30) NOT NULL, 
				PRIMARY KEY (event_id)
			) 
			ENGINE=MyISAM";
	mysql_query($sql) || die("Error creating table: " . mysql_error() . "");
	echo "Created table 'messages'<br />";
	
	// Re-init activity_log table
	$sql = "CREATE TABLE activity_log 
			(
				event_id INT(11) NOT NULL AUTO_INCREMENT, 
				user_id INT(11) NOT NULL,  
				zone_id INT(11) NOT NULL, 
				timestamp VARCHAR(30) NOT NULL, 
				PRIMARY KEY (event_id)
			) 
			ENGINE=MyISAM";
	mysql_query($sql) || die("Error creating table: " . mysql_error() . "");
	echo "Created table 'activity_log'<br />";
	
?>
</body>
</html>
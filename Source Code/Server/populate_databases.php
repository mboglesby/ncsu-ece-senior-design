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
	
	// Adding zones
    
    // Format:
    // Insert the following into zones table:
    // 	zone_id - integer value unique to each zone
    //	zone_description (optional) - string description of zone
    //	mac_address - mac address string
    //	lat - lat string
    //	lon - lon string
	
	// EB3 Computer Lab
	echo "Adding data for EB3 Computer Lab<br />";
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (21, 'EB3 Computer Lab', 'D8:C7:C8:38:31:00', '35.77129', '-78.673643')";
    mysql_query($sql) || die("Error adding data for EB3 Computer Lab");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (21, 'EB3 Computer Lab', 'D8:C7:C8:38:31:01', '35.77129', '-78.673643')";
    mysql_query($sql) || die("Error adding data for EB3 Computer Lab");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (21, 'EB3 Computer Lab', 'D8:C7:C8:38:31:11', '35.77129', '-78.673643')";
    mysql_query($sql) || die("Error adding data for EB3 Computer Lab");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (21, 'EB3 Computer Lab', 'D8:C7:C8:38:31:12', '35.77129', '-78.673643')";
    mysql_query($sql) || die("Error adding data for EB3 Computer Lab");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (21, 'EB3 Computer Lab', 'D8:C7:C8:38:59:80', '35.77129', '-78.673643')";
    mysql_query($sql) || die("Error adding data for EB3 Computer Lab");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (21, 'EB3 Computer Lab', 'D8:C7:C8:38:59:81', '35.77129', '-78.673643')";
    mysql_query($sql) || die("Error adding data for EB3 Computer Lab");
	
	// EB2 Archway
	echo "Adding data for EB2 Archway<br />";
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (22, 'EB2 Archway', 'D8:C7:C8:38:29:A0', '35.771906', '-78.67389')";
    mysql_query($sql) || die("Error adding data for EB2 Archway");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (22, 'EB2 Archway', 'D8:C7:C8:38:29:A1', '35.771906', '-78.67389')";
    mysql_query($sql) || die("Error adding data for EB2 Archway");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (22, 'EB2 Archway', 'D8:C7:C8:38:67:20', '35.771906', '-78.67389')";
    mysql_query($sql) || die("Error adding data for EB2 Archway");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (22, 'EB2 Archway', 'D8:C7:C8:38:67:21', '35.771906', '-78.67389')";
    mysql_query($sql) || die("Error adding data for EB2 Archway");
	
	// EB1 Lobby
	echo "Adding data for EB1 Lobby<br />";
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:38:91:80', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:38:91:80', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:3E:7A:20', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:3E:7A:21', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:38:8C:20', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:38:8C:21', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:38:66:60', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:38:66:61', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:38:8C:31', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:38:8C:32', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:38:91:91', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:38:91:92', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:3E:7A:31', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (23, 'EB1 Lobby', 'D8:C7:C8:3E:7A:32', '35.771625', '-78.675054')";
    mysql_query($sql) || die("Error adding data for EB1 Lobby");
	
	// Innovation Cafe
	echo "Adding data for Innovation Cafe<br />";
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (24, 'Innovation Cafe', 'D8:C7:C8:3D:F3:00', '35.773606', '-78.673376')";
    mysql_query($sql) || die("Error adding data for iCafe");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (24, 'Innovation Cafe', 'D8:C7:C8:3D:F3:01', '35.773606', '-78.673376')";
    mysql_query($sql) || die("Error adding data for iCafe");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (24, 'Innovation Cafe', 'D8:C7:C8:3D:F3:11', '35.773606', '-78.673376')";
    mysql_query($sql) || die("Error adding data for iCafe");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (24, 'Innovation Cafe', 'D8:C7:C8:3D:F3:12', '35.773606', '-78.673376')";
    mysql_query($sql) || die("Error adding data for iCafe");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (24, 'Innovation Cafe', 'D8:C7:C8:3D:F4:60', '35.773606', '-78.673376')";
    mysql_query($sql) || die("Error adding data for iCafe");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (24, 'Innovation Cafe', 'D8:C7:C8:3D:F4:61', '35.773606', '-78.673376')";
    mysql_query($sql) || die("Error adding data for iCafe");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (24, 'Innovation Cafe', 'D8:C7:C8:3D:E1:40', '35.773606', '-78.673376')";
    mysql_query($sql) || die("Error adding data for iCafe");
	$sql = "INSERT INTO zones (zone_id, zone_description, mac_address, lat, lon) 
    		VALUES (24, 'Innovation Cafe', 'D8:C7:C8:3D:E1:41', '35.773606', '-78.673376')";
    mysql_query($sql) || die("Error adding data for iCafe");
	
	// CentMesh zones:
	
	// AP1
	echo "Adding data for AP1<br />";
	// ath0
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (1, '06:02:6F:A7:3E:D8', '35.768437', '-78.678036')";
    mysql_query($sql) || die("Error adding data for AP1-ath0");
	// ath1
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (1, '06:02:6F:A7:3E:C8', '35.768437', '-78.678036')";
    mysql_query($sql) || die("Error adding data for AP1-ath1");
	// ath2
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (1, '06:02:6F:A7:3E:DA', '35.768437', '-78.678036')";
    mysql_query($sql) || die("Error adding data for AP1-ath2");
	// ath3
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (1, '06:02:6F:A7:3E:D1', '35.768437', '-78.678036')";
    mysql_query($sql) || die("Error adding data for AP1-ath3");
	
	// AP4
	echo "Adding data for AP4<br />";
	// ath0
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (4, '06:02:6F:A7:3E:BD', '35.771659', '-78.67756')";
    mysql_query($sql) || die("Error adding data for AP4-ath0");
	// ath1
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (4, '06:02:6F:A7:3E:BB', '35.771659', '-78.67756')";
    mysql_query($sql) || die("Error adding data for AP4-ath1");
	// ath2
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (4, '06:02:6F:A7:3E:B8', '35.771659', '-78.67756')";
    mysql_query($sql) || die("Error adding data for AP4-ath2");
	// ath3
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (4, '06:02:6F:A7:3E:C1', '35.771659', '-78.67756')";
    mysql_query($sql) || die("Error adding data for AP4-ath3");
	
	// AP5
	echo "Adding data for AP5<br />";
	// ath0
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (5, '06:02:6F:A0:50:FD', '35.772434', '-78.677454')";
    mysql_query($sql) || die("Error adding data for AP5-ath0");
	// ath1
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (5, '06:02:6F:A0:50:F9', '35.772434', '-78.677454')";
    mysql_query($sql) || die("Error adding data for AP5-ath1");
	// ath2
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (5, '06:02:6F:A0:52:29', '35.772434', '-78.677454')";
    mysql_query($sql) || die("Error adding data for AP5-ath2");
	// ath3
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (5, '06:02:6F:A0:50:FE', '35.772434', '-78.677454')";
    mysql_query($sql) || die("Error adding data for AP5-ath3");
	
	// AP6
	echo "Adding data for AP6<br />";
	// ath0
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (6, '00:02:6F:A7:3E:E4', '35.773494', '-78.677183')";
    mysql_query($sql) || die("Error adding data for AP6-ath0");
	// ath1
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (6, '00:02:6F:A0:51:DF', '35.773494', '-78.677183')";
    mysql_query($sql) || die("Error adding data for AP6-ath1");
	// ath2
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (6, '00:02:6F:A0:51:E5', '35.773494', '-78.677183')";
    mysql_query($sql) || die("Error adding data for AP6-ath2");
	// ath3
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (6, '00:02:6F:A0:51:E4', '35.773494', '-78.677183')";
    mysql_query($sql) || die("Error adding data for AP6-ath3");
	
	// AP9
	echo "Adding data for AP9<br />";
	// ath0
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (9, '06:02:6F:A7:3E:BC', '35.771051', '-78.676465')";
    mysql_query($sql) || die("Error adding data for AP9-ath0");
	// ath1
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (9, '06:02:6F:A0:50:F8', '35.771051', '-78.676465')";
    mysql_query($sql) || die("Error adding data for AP9-ath1");
	// ath2
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (9, '06:02:6F:A7:3E:D4', '35.771051', '-78.676465')";
    mysql_query($sql) || die("Error adding data for AP9-ath2");
	// ath3
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (9, '06:02:6F:A7:3E:BE', '35.771051', '-78.676465')";
    mysql_query($sql) || die("Error adding data for AP9-ath3");
	
	// AP14
	echo "Adding data for AP14<br />";
	// ath0
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (14, '06:02:6F:A0:51:DA', '35.770006', '-78.676564')";
    mysql_query($sql) || die("Error adding data for AP14-ath0");
	// ath1
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (14, '06:02:6F:A0:51:DB', '35.770006', '-78.676564')";
    mysql_query($sql) || die("Error adding data for AP14-ath1");
	// ath2
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (14, '06:02:6F:A0:51:D7', '35.770006', '-78.676564')";
    mysql_query($sql) || die("Error adding data for AP14-ath2");
	// ath3
	$sql = "INSERT INTO zones (zone_id, mac_address, lat, lon) 
    		VALUES (14, '06:02:6F:A0:51:D6', '35.770006', '-78.676564')";
    mysql_query($sql) || die("Error adding data for AP14-ath3");
    
    // Add distances
    echo "Adding distance data<br />";
    
    // Format:
    // Insert the following into distances table:
    // 	description - in the form "<LOWER_ZONE_ID>to<HIGHER_ZONE_ID>"
    //	distance - an integer value
    
    // 21to22
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('21to22', 72)";
    mysql_query($sql) || die("Error adding data for distance 21to22");
    // 22to23
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('22to23', 110)";
    mysql_query($sql) || die("Error adding data for distance 22to23");
    // 23to24
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('23to24', 267)";
    mysql_query($sql) || die("Error adding data for distance 23to24");
    // 21to23
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('21to23', 133)";
    mysql_query($sql) || die("Error adding data for distance 21to23");
    // 21to24
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('21to24', 259)";
    mysql_query($sql) || die("Error adding data for distance 21to24");
    // 22to24
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('22to24', 195)";
    mysql_query($sql) || die("Error adding data for distance 22to24");
    
    // CentMesh zone distances:
    
    // 0to1
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('0to1', 0)";
    mysql_query($sql) || die("Error adding data for distance 0to1");
    // 0to4
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('0to4', 0)";
    mysql_query($sql) || die("Error adding data for distance 0to4");
    // 0to5
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('0to5', 0)";
    mysql_query($sql) || die("Error adding data for distance 0to5");
    // 0to6
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('0to6', 0)";
    mysql_query($sql) || die("Error adding data for distance 0to6");
    // 0to9
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('0to9', 0)";
    mysql_query($sql) || die("Error adding data for distance 0to9");
    // 0to14
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('0to14', 0)";
    mysql_query($sql) || die("Error adding data for distance 0to14");
    // 1to4
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('1to4', 360)";
    mysql_query($sql) || die("Error adding data for distance 1to4");
    // 1to5
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('1to5', 447)";
    mysql_query($sql) || die("Error adding data for distance 1to5");
    // 1to6
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('1to6', 567)";
    mysql_query($sql) || die("Error adding data for distance 1to6");
    // 1to9
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('1to9', 323)";
    mysql_query($sql) || die("Error adding data for distance 1to9");
    // 1to14
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('1to14', 219)";
    mysql_query($sql) || die("Error adding data for distance 1to14");
    // 4to1
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('4to1', 360)";
    mysql_query($sql) || die("Error adding data for distance 4to1");
    // 4to5
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('4to5', 86)";
    mysql_query($sql) || die("Error adding data for distance 4to5");
    // 4to6
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('4to6', 206)";
    mysql_query($sql) || die("Error adding data for distance 4to6");
    // 4to9
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('4to9', 119)";
    mysql_query($sql) || die("Error adding data for distance 4to9");
    // 4to14
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('4to14', 204)";
    mysql_query($sql) || die("Error adding data for distance 4to14");
    // 5to1
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('5to1', 447)";
    mysql_query($sql) || die("Error adding data for distance 5to1");
    // 5to4
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('5to4', 86)";
    mysql_query($sql) || die("Error adding data for distance 5to4");
    // 5to6
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('5to6', 120)";
    mysql_query($sql) || die("Error adding data for distance 5to6");
    // 5to9
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('5to9', 177)";
    mysql_query($sql) || die("Error adding data for distance 5to9");
    // 5to14
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('5to14', 281)";
    mysql_query($sql) || die("Error adding data for distance 5to14");
    // 6to1
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('6to1', 567)";
    mysql_query($sql) || die("Error adding data for distance 6to1");
    // 6to4
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('6to4', 206)";
    mysql_query($sql) || die("Error adding data for distance 6to4");
    // 6to5
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('6to5', 120)";
    mysql_query($sql) || die("Error adding data for distance 6to5");
    // 6to9
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('6to9', 279)";
    mysql_query($sql) || die("Error adding data for distance 6to9");
    // 6to14
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('6to14', 391)";
    mysql_query($sql) || die("Error adding data for distance 6to14");
    // 9to1
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('9to1', 323)";
    mysql_query($sql) || die("Error adding data for distance 9to1");
    // 9to4
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('9to4', 119)";
    mysql_query($sql) || die("Error adding data for distance 9to4");
    // 9to5
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('9to5', 177)";
    mysql_query($sql) || die("Error adding data for distance 9to5");
    // 9to6
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('9to6', 279)";
    mysql_query($sql) || die("Error adding data for distance 9to6");
    // 9to14
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('9to14', 116)";
    mysql_query($sql) || die("Error adding data for distance 9to14");
    // 14to1
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('14to1', 219)";
    mysql_query($sql) || die("Error adding data for distance 14to1");
    // 14to4
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('14to4', 204)";
    mysql_query($sql) || die("Error adding data for distance 14to4");
    // 14to5
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('14to5', 281)";
    mysql_query($sql) || die("Error adding data for distance 14to5");
    // 14to6
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('14to6', 391)";
    mysql_query($sql) || die("Error adding data for distance 14to6");
    // 14to9
	$sql = "INSERT INTO distances (description, distance) 
    		VALUES ('14to9', 116)";
    mysql_query($sql) || die("Error adding data for distance 14to9");
	
?>
</body>
</html>
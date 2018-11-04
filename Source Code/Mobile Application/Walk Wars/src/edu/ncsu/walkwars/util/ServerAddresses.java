package edu.ncsu.walkwars.util;

public class ServerAddresses
{

    public static final String baseDomainURL        = "http://people.engr.ncsu.edu/ctsui/";
    
    public static final String getZoneURL           = baseDomainURL +  "get_zone.php";
    public static final String getEventBoardURL     = baseDomainURL + "get_event.php";
    public static final String getLeaderBoardURL    = baseDomainURL + "get_leaderboard.php";


    public static final String registerNewUserURL = baseDomainURL + "create_user.php";
    public static final String createNewTeamURL   = baseDomainURL + "create_team.php";
    public static final String loginUserURL       = baseDomainURL + "login_user.php";
    public static final String switchTeamURL      = baseDomainURL + "switch_teams.php";
    
    public static final String logMessageURL      = baseDomainURL + "log_message.php";
    
    
    public ServerAddresses()
    {
        // TODO Auto-generated constructor stub
    }

}

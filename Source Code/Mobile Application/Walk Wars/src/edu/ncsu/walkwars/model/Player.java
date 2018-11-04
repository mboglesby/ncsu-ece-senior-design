package edu.ncsu.walkwars.model;

public class Player
{

    private String playerName = "";
    private String teamName = "";
    private Double score = 0d;

    public Player()
    {
        // TODO Auto-generated constructor stub
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public String getTeamName()
    {
        return teamName;
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    public Double getScore()
    {
        return score;
    }

    public void setScore(Double score)
    {
        this.score = score;
    }

    public void addScore(Double additionScore)
    {
        this.score += additionScore;
    }
}

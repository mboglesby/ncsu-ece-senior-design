package edu.ncsu.walkwars.model;

import java.util.ArrayList;

public class Team
{

    private String teamName = "";
    private Double score = 0d;
    private Double totalDistance = 0d;
    private ArrayList<String> teamMembers = new ArrayList<String>();

    public Team()
    {
        // Empty constructor
    }

    public Team(String teamName)
    {
        this.setTeamName(teamName);
    }

    public Team(String teamName, Double score)
    {
        this.setTeamName(teamName);
        this.setScore(score);
    }

    public Team(String teamName, Double score, Double totalDistance)
    {
        this.setTeamName(teamName);
        this.setScore(score);
        this.setTotalDistance(totalDistance);
    }

    public Team(String teamName, Double score, Double totalDistance, ArrayList<String> teamMembers)
    {
        this.setTeamName(teamName);
        this.setScore(score);
        this.setTotalDistance(totalDistance);
        this.setTeamMembersList(teamMembers);
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

    public Double getTotalDistance()
    {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance)
    {
        this.totalDistance = totalDistance;
    }

    public void addDistance(Double distance)
    {
        this.totalDistance += distance;
    }

    public void addTeamMember(String memberName)
    {
        this.teamMembers.add(memberName);
    }

    public ArrayList<String> getTeamMembersList()
    {
        return teamMembers;
    }

    public void setTeamMembersList(ArrayList<String> teamMembers)
    {
        this.teamMembers = teamMembers;
    }

}

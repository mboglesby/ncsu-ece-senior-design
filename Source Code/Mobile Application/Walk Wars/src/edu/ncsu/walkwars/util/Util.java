package edu.ncsu.walkwars.util;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import edu.ncsu.walkwars.activity.EventsActivity;
import edu.ncsu.walkwars.activity.MainApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class Util
{
    SharedPreferences prefs = null;
    private Context context = null;
    private String PREF = "my_preference";
    private String USERNAME = "user_name";
    private String TEAMNAME = "team_name";
    private String AUTOUPDATE = "auto_update";

    public static final String DEBUG = "DEBUG";
    private static final String SCORE = "SCORE";

    /**
     * Constructor
     * 
     * @param context
     */
    public Util(Context context)
    {
        this.context = context;
        prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        //application = (MainApplication) Util.this.getApplication();
    }

    /**
     * Method to determine the first launch of this app
     */
    public boolean detectFirstLaunch()
    {
        if (prefs.getBoolean("firstrun", true))
        {
            prefs.edit().putBoolean("firstrun", false).commit();
            return true;
        }
        else
        {
            return false;
        }
    }

    public void turnOnAutoUpdate()
    {
        setAutoUpdate(true);
    }

    public void turnOffAutoUpdate()
    {
        setAutoUpdate(false);
    }

    private void setAutoUpdate(boolean value)
    {
        SharedPreferences userDetails = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        Editor edit = userDetails.edit();
        edit.putBoolean(AUTOUPDATE, value);
        edit.commit();
        Log.d("DEBUG", "Setting the autoupdate boolean: " + userDetails.getBoolean(AUTOUPDATE, true));
    }

    public boolean autoUpdateIsOn()
    {
        SharedPreferences userDetails = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return userDetails.getBoolean(AUTOUPDATE, true);
    }

    public Float getPlayerScore()
    {
        SharedPreferences userDetails = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return userDetails.getFloat(SCORE, 0);
    }

    public void storePlayerScore(Float score)
    {
        SharedPreferences userDetails = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        Editor edit = userDetails.edit();
        edit.putFloat(SCORE, score);
        edit.commit();
    }

    public void storePlayerName(String playerName)
    {
        SharedPreferences userDetails = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        Editor edit = userDetails.edit();
        edit.putString(USERNAME, playerName);
        edit.commit();
        Log.d("DEBUG", "Storing user name: " + userDetails.getString(USERNAME, null));
    }

    public void storeTeamName(String teamName)
    {
        SharedPreferences userDetails = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        Editor edit = userDetails.edit();
        edit.putString(TEAMNAME, teamName);
        edit.commit();
        Log.d("DEBUG", "Storing team name: " + userDetails.getString(TEAMNAME, null));
    }

    public String getPlayerName()
    {
        SharedPreferences userDetails = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return userDetails.getString(USERNAME, null);
    }

    public String getTeamName()
    {
        SharedPreferences userDetails = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return userDetails.getString(TEAMNAME, null);
    }

    public static String getTimeStamp()
    {
        String delegate = "hh:mm:ss aaa";
        String timeString = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
        return timeString.toUpperCase();
    }

    public boolean isLoggedIn()
    {
        String userName = getPlayerName();
        if (userName != null )
        {
            if (userName.length() != 0){
                return true;
            }
            return false;
        }
        else
        {
            return false;
        }
    }

    public static void popToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static JSONObject convertStringToJSONObject(String response)
    {
        JSONObject json = null;
        try
        {
            json = new JSONObject(response);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.d(Util.DEBUG, "Error parsing JSON");
            return null;
        }
        return json;
        /*
         * BufferedReader reader; try { reader = new BufferedReader(new
         * InputStreamReader(response.getEntity().getContent(), "UTF-8"));
         * StringBuilder builder = new StringBuilder(); for (String line = null;
         * (line = reader.readLine()) != null;) {
         * builder.append(line).append("\n"); } JSONTokener tokener = new
         * JSONTokener(builder.toString()); JSONArray finalResult = new
         * JSONArray(tokener); Log.d("DEBUG", finalResult.toString()); return
         * finalResult; } catch (UnsupportedEncodingException e) { // TODO
         * Auto-generated catch block e.printStackTrace(); } catch
         * (IllegalStateException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
         * catch block e.printStackTrace(); } catch (JSONException e) { // TODO
         * Auto-generated catch block e.printStackTrace(); } return null;
         */
    }

}

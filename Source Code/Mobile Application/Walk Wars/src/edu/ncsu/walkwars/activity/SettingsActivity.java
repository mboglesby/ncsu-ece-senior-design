package edu.ncsu.walkwars.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.ncsu.walkwars.R;
import edu.ncsu.walkwars.logic.PostJSONDataAsyncTask;
import edu.ncsu.walkwars.util.ServerAddresses;
import edu.ncsu.walkwars.util.Util;

public class SettingsActivity extends Activity
{
    private ListView settingsListView;
    //private MainApplication application;
    private Context context;
    
    // Write Log File: http://stackoverflow.com/questions/6175002/write-android-logcat-data-to-a-file
    
    private Util util = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // setContentView(R.layout.activity_settings_new);
        context = this;
        util = new Util(context);

        setContentView(R.layout.activity_settings);
        settingsListView = (ListView) findViewById(R.id.settingsListView);
        //application = (MainApplication) SettingsActivity.this.getApplication();

        // Set up list of options
        String[] values = new String[] { 
                "Register new user", 
                "Create new team", 
                "Log in as existing user",
                "Join a team"};
                //"Save log to file"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        
        // Set list view
        settingsListView.setAdapter(adapter);
        settingsListView.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                { 
                    // Register new user
                    createNewUserDialog().show();
                }
                else if (position == 1)
                {
                    // Register new team
                    if(!util.isLoggedIn()){
                        Toast.makeText(context, "Currently not logged in. First create a new user or log in", Toast.LENGTH_LONG).show();
                        return;
                    }
                    // If user is logged in, allow them to create a new team
                    createNewTeamDialog().show();
                }
                else if (position == 2)
                {
                    // Log in user
                    loginUserDialog().show();
                }
                else if (position == 3)
                {
                    if(!util.isLoggedIn()){
                        Toast.makeText(context, "Currently not logged in. First create a new user or log in", Toast.LENGTH_LONG).show();
                        return;
                    }
                    // Join a team
                    joinTeamDialog().show();
                }
                else if (position == 4){
                    try {
                        Process process = Runtime.getRuntime().exec("logcat -d");
                        BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                        StringBuilder log=new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                          log.append(line);
                        }
                        appendStringToFile("MyLog.txt", log.toString());
                        Log.d(Util.DEBUG, log.toString());
                        /*
                        TextView tv = (TextView)findViewById(R.id.textView1);
                        tv.setText(log.toString());
                          */
                      } 
                    catch (IOException e) {
                    }
                }
            }
        });
        
        // Update the logged in information
        updatePlayerInfo();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        updatePlayerInfo();
    }

    
    private void updatePlayerInfo(){
        TextView userNametextView = (TextView)findViewById(R.id.userNameTextView);
        TextView teamNametextView = (TextView)findViewById(R.id.teamNameTextView);
        TextView scoretextView = (TextView)findViewById(R.id.scoreTextView);
        
        String playerName = util.getPlayerName();
        String teamName = util.getTeamName();
        String score = String.valueOf(util.getPlayerScore());

        Log.d(Util.DEBUG, "In update textview: score: " + score);
        scoretextView.setText(String.valueOf(score));
  
        if (playerName != null)
        {
            userNametextView.setText(playerName);
        }
        
        if (teamName != null)
        {
            teamNametextView.setText(teamName);
            //info = info + " on team \"" + teamName + "\"";
        }
        else 
        {
            teamNametextView.setText("None");
        }
    }
    
    /**
     * Create a new user dialog
     * @return
     */
    private AlertDialog createNewUserDialog()
    {
        // Inflate a custom layout to an AlertDialog
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog_create_new_user, null);

        // Get the two text fields
        final EditText usernameEditText = (EditText) textEntryView.findViewById(R.id.createUserEditText);

        // Add "space key listener" to remove spaces automatically
        usernameEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result))
                {
                    usernameEditText.setText(result);
                    usernameEditText.setSelection(result.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });

        // Build the create new user dialog
        return new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Dialog))
                .setView(textEntryView)
                .setTitle("Create New User")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        // Do nothing on cancel
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        final String username = usernameEditText.getText().toString().trim();
                        
                        if(username.length() == 0){
                            return;
                        }
                        Log.d("DEBUG", "Creating new user name: " + username);

                        JSONObject userAndTeamNameJSON = new JSONObject();
                        try
                        {
                            userAndTeamNameJSON.put("user_name", username);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        new PostJSONDataAsyncTask(context, userAndTeamNameJSON, ServerAddresses.registerNewUserURL, true)
                        {
                            // Override the onPostExecute to do whatever you want
                            @Override
                            protected void onPostExecute(String response)
                            {
                                super.onPostExecute(response);

                                if (response != null)
                                {
                                    JSONObject jsonObject = Util.convertStringToJSONObject(response);
                                    String message = "";
                                    
                                    try
                                    {
                                        // No error if "error" is null
                                        if(jsonObject.isNull("error")){
                                            message = jsonObject.getString("success");
                                            
                                            // Log in here
                                            util.storePlayerName(username);
                                            util.storeTeamName(null);
                                            util.storePlayerScore(0f);
                                            
                                            // Update the text view at the bottom here
                                            updatePlayerInfo();
                                        }
                                        else
                                        {
                                            message = jsonObject.getString("error");
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(context, "Error connecting to server", Toast.LENGTH_LONG).show();
                                }

                            }
                        }.execute();
                    }
                })
                .create();
    }
    

    /**
     * Create a new team dialog
     * @return
     */
    private AlertDialog createNewTeamDialog()
    {
        // Inflate a custom layout to an AlertDialog
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog_create_new_team, null);

        // Get the two text fields
        final EditText usernameEditText = (EditText) textEntryView.findViewById(R.id.createTeamEditText);
        final CheckBox joinTeamCheckBox = (CheckBox) textEntryView.findViewById(R.id.joinTeamCheckBox);

        // Add "space key listener" to remove spaces automatically
        usernameEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result))
                {
                    usernameEditText.setText(result);
                    usernameEditText.setSelection(result.length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });

        // Build the create new user dialog
        return new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Dialog))
                .setView(textEntryView)
                .setTitle("Create New Team")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        // Do nothing on cancel
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        final boolean join = joinTeamCheckBox.isChecked();
                        final String teamName = usernameEditText.getText().toString().trim();
                        
                        if(teamName.length() == 0){
                            return;
                        }
                        Log.d("DEBUG", "Creating new team name: " + teamName);

                        JSONObject json = new JSONObject();
                        try
                        {
                            json.put("user_name", util.getPlayerName());
                            json.put("team_name", teamName);
                            json.put("join", join);
                            Log.d(Util.DEBUG, json.toString());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        new PostJSONDataAsyncTask(context, json, ServerAddresses.createNewTeamURL, true)
                        {
                            // Override the onPostExecute to do whatever you want
                            @Override
                            protected void onPostExecute(String response)
                            {
                                super.onPostExecute(response);

                                if (response != null)
                                {
                                    JSONObject jsonObject = Util.convertStringToJSONObject(response);
                                    String message = "";
                                    
                                    try
                                    {
                                        // No error if "error" is null  
                                        if(jsonObject.isNull("error")){
                                            message = jsonObject.getString("success");
                                            
                                            // Update team name
                                            if(join){
                                                util.storeTeamName(teamName);
                                            }
                                            
                                            // Update the text view at the bottom here
                                            updatePlayerInfo();
                                        }
                                        else
                                        {
                                            message = jsonObject.getString("error");
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(context, "Error connecting to server", Toast.LENGTH_LONG).show();
                                }

                            }
                        }.execute();
                    }
                })
                .create();
    }

    
    /**
     * Create a new team dialog
     * @return
     */
    private AlertDialog loginUserDialog()
    {
        // Inflate a custom layout to an AlertDialog
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog_login_user, null);

        // Get the two text fields
        final EditText usernameEditText = (EditText) textEntryView.findViewById(R.id.loginUserEditText);
        
        // Add "space key listener" to remove spaces automatically
        usernameEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result))
                {
                    usernameEditText.setText(result);
                    usernameEditText.setSelection(result.length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });

        // Build the create new user dialog
        return new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Dialog))
                .setView(textEntryView)
                .setTitle("Login")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        // Do nothing on cancel
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        final String userName = usernameEditText.getText().toString().trim();
                        
                        if(userName.length() == 0){
                            return;
                        }
                        Log.d("DEBUG", "Logging in as another user: " + userName);

                        JSONObject json = new JSONObject();
                        try
                        {
                            json.put("user_name", userName);
                            Log.d(Util.DEBUG, json.toString());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        new PostJSONDataAsyncTask(context, json, ServerAddresses.loginUserURL, true)
                        {
                            // Override the onPostExecute to do whatever you want
                            @Override
                            protected void onPostExecute(String response)
                            {
                                super.onPostExecute(response);

                                if (response != null)
                                {
                                    JSONObject jsonObject = Util.convertStringToJSONObject(response);
                                    String message = "";
                                    
                                    try
                                    {
                                        // No error if "error" is null  
                                        if(jsonObject.isNull("error")){
                                            message = jsonObject.getString("success");
                                            util.storePlayerName(userName);

                                            // Store team name if not null
                                            if(jsonObject.isNull("team_name")){
                                                util.storeTeamName(null);
                                            }
                                            else
                                            {
                                                String newTeamName = jsonObject.getString("team_name");
                                                util.storeTeamName(newTeamName);
                                            }
                                            
                                            // Store the user's score
                                            int totalDistance = jsonObject.getInt("total_distance");
                                            Log.d(Util.DEBUG, String.valueOf(totalDistance));
                                            util.storePlayerScore((float) totalDistance);

                                            // Update the text view at the bottom here
                                            updatePlayerInfo();
                                        }
                                        else
                                        {
                                            message = jsonObject.getString("error");
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(context, "Error connecting to server", Toast.LENGTH_LONG).show();
                                }

                            }
                        }.execute();
                    }
                })
                .create();
    }
    
    
    /**
     * Create a new team dialog
     * @return
     */
    private AlertDialog joinTeamDialog()
    {
        // Inflate a custom layout to an AlertDialog
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog_join_team, null);

        // Get the two text fields
        final EditText teamNameEditText = (EditText) textEntryView.findViewById(R.id.joinTeamEditText);

        // Add "space key listener" to remove spaces automatically
        teamNameEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result))
                {
                    teamNameEditText.setText(result);
                    teamNameEditText.setSelection(result.length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });

        // Build the create new user dialog
        return new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Dialog))
                .setView(textEntryView)
                .setTitle("Join a Team")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        // Do nothing on cancel
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        final String teamName = teamNameEditText.getText().toString().trim();
                        
                        if(teamName.length() == 0){
                            return;
                        }
                        Log.d("DEBUG", "Joining a team with name: " + teamName);

                        JSONObject json = new JSONObject();
                        try
                        {
                            json.put("user_name", util.getPlayerName());
                            json.put("team_name", teamName);
                            Log.d(Util.DEBUG, json.toString());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        new PostJSONDataAsyncTask(context, json, ServerAddresses.switchTeamURL, true)
                        {
                            // Override the onPostExecute to do whatever you want
                            @Override
                            protected void onPostExecute(String response)
                            {
                                super.onPostExecute(response);

                                if (response != null)
                                {
                                    JSONObject jsonObject = Util.convertStringToJSONObject(response);
                                    String message = "";
                                    
                                    try
                                    {
                                        // No error if "error" is null  
                                        if(jsonObject.isNull("error")){
                                            message = jsonObject.getString("success");
                                            
                                            // Set the team name; 
                                            util.storeTeamName(teamName);
                                            
                                            // Update the text view at the bottom here
                                            updatePlayerInfo();
                                        }
                                        else
                                        {
                                            message = jsonObject.getString("error");
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(context, "Error connecting to server", Toast.LENGTH_LONG).show();
                                }

                            }
                        }.execute();
                    }
                })
                .create();
    }
    
    
    
    
    
    // ==================================================================
    // Helper function to append a line to the end of a file
    // ==================================================================
    private void appendStringToFile (String fileName, String lineToAppend) {
        FileWriter out;
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, fileName);
        try
        {
            out = new FileWriter(file, true);
            out.write(lineToAppend + "\n");
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    /*

    private String getTextFromEditText (View view) {
        final EditText edTxt = (EditText)view;
        return edTxt.getText().toString().trim();
    }
    
    public void registerNewUserClick (View view) {
        // Toast.makeText(getBaseContext(), "Register New User", Toast.LENGTH_LONG).show();
        
        String newUserNameString = getTextFromEditText((EditText) findViewById(R.id.registerNewUserEditText));
        
        JSONObject newUsernameJSON = new JSONObject();
        try
        {
            newUsernameJSON.put("user_name", newUserNameString);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        // Perform the async task
        new PostJSONDataAsyncTask(context, newUsernameJSON, registerNewUserURL, true)
        {
            // Override the onPostExecute to do whatever you want
            @Override
            protected void onPostExecute(String response)
            {
                if (this.dialog.isShowing()) {this.dialog.dismiss();} 
                // Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                if (response != null)
                {
                    // Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    JSONObject jsonObject = Util.convertHTTPStringReponseToJSONObject(response);
                    String returnedMessage = "";
                    // Log.d("SettingsActivity", jsonObject.toString());
                    
                    try
                    {
                        returnedMessage = jsonObject.getString("message").toString();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, returnedMessage, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, "Error connecting to server", Toast.LENGTH_LONG).show();
                }

            }
        }.execute();
        
    }
    
    public void createNewTeamClick (View view) {
        // Toast.makeText(getBaseContext(), "Create New Team", Toast.LENGTH_LONG).show();

        String newTeamNameString = getTextFromEditText((EditText) findViewById(R.id.createNewTeamEditText));
        
        JSONObject newTeamNameJSON = new JSONObject();
        try
        {
            newTeamNameJSON.put("team_name", newTeamNameString);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        // Perform the async task
        new PostJSONDataAsyncTask(context, newTeamNameJSON, createNewTeamURL, true)
        {
            // Override the onPostExecute to do whatever you want
            @Override
            protected void onPostExecute(String response)
            {
                if (this.dialog.isShowing()) {this.dialog.dismiss();} 
                // Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                if (response != null)
                {
                    // Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    JSONObject jsonObject = Util.convertHTTPStringReponseToJSONObject(response);
                    String returnedMessage = "";
                    // Log.d("SettingsActivity", jsonObject.toString());
                    
                    try
                    {
                        returnedMessage = jsonObject.getString("message").toString();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, returnedMessage, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, "Error connecting to server", Toast.LENGTH_LONG).show();
                }

            }
        }.execute();
        
    }
    


    public void loginAsUserClick (View view) { 
        // Toast.makeText(getBaseContext(), "Log in as User", Toast.LENGTH_LONG).show();
        
        String userNameString = getTextFromEditText((EditText) findViewById(R.id.loginAsUserEditText));
        
        JSONObject usernameJSON = new JSONObject();
        try
        {
            usernameJSON.put("user_name", userNameString);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        // Perform the async task
        new PostJSONDataAsyncTask(context, usernameJSON, loginUserURL, true)
        {
            // Override the onPostExecute to do whatever you want
            @Override
            protected void onPostExecute(String response)
            {
                if (this.dialog.isShowing()) {this.dialog.dismiss();} 
                // Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                if (response != null)
                {
                    // Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    JSONObject jsonObject = Util.convertHTTPStringReponseToJSONObject(response);
                    String returnedMessage = "";
                    // Log.d("SettingsActivity", jsonObject.toString());
                    
                    try
                    {
                        returnedMessage = jsonObject.getString("message").toString();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, returnedMessage, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, "Error connecting to server", Toast.LENGTH_LONG).show();
                }

            }
        }.execute();
        
    }
    


    public void joinTeamClick (View view) {
        // Toast.makeText(getBaseContext(), "Switch to Another Existing Team", Toast.LENGTH_LONG).show();
        
        String teamNameString = getTextFromEditText((EditText) findViewById(R.id.joinTeamEditText));
        
        JSONObject usernameJSON = new JSONObject();
        try
        {
            usernameJSON.put("team_name", teamNameString);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        // Perform the async task
        new PostJSONDataAsyncTask(context, usernameJSON, switchTeamURL, true)
        {
            // Override the onPostExecute to do whatever you want
            @Override
            protected void onPostExecute(String response)
            {
                if (this.dialog.isShowing()) {this.dialog.dismiss();} 
                // Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                if (response != null)
                {
                    // Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    JSONObject jsonObject = Util.convertHTTPStringReponseToJSONObject(response);
                    String returnedMessage = "";
                    // Log.d("SettingsActivity", jsonObject.toString());
                    
                    try
                    {
                        returnedMessage = jsonObject.getString("message").toString();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, returnedMessage, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, "Error connecting to server", Toast.LENGTH_LONG).show();
                }

            }
        }.execute();
        
    }
    */
    
}

package barqsoft.footballscores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
    public static int selected_match_id;
    public static int current_fragment = 2;
    public static String LOG_TAG = "MainActivity";
    private final String save_tag = "Save Test";
    private PagerFragment my_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "Reached MainActivity onCreate");
        if (savedInstanceState == null) {
            my_main = new PagerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, my_main)
                    .commit();
        }
        checkFirstRun();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_about :
                Intent start_about = new Intent(this, AboutActivity.class);
                startActivity(start_about);
                return true;
            case R.id.action_help :
                showHelpDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            showHelpDialog();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public void showHelpDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.help_title)
                .setMessage(R.string.help_description)
                .setPositiveButton(R.string.help_dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(save_tag, "will save");
        Log.v(save_tag, "fragment: " + String.valueOf(my_main.mPagerHandler.getCurrentItem()));
        Log.v(save_tag, "selected id: " + selected_match_id);
        outState.putInt("Pager_Current", my_main.mPagerHandler.getCurrentItem());
        outState.putInt("Selected_match", selected_match_id);
        getSupportFragmentManager().putFragment(outState, "my_main", my_main);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(save_tag, "will retrive");
        Log.v(save_tag, "fragment: " + String.valueOf(savedInstanceState.getInt("Pager_Current")));
        Log.v(save_tag, "selected id: " + savedInstanceState.getInt("Selected_match"));
        current_fragment = savedInstanceState.getInt("Pager_Current");
        selected_match_id = savedInstanceState.getInt("Selected_match");
        my_main = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, "my_main");
        super.onRestoreInstanceState(savedInstanceState);
    }
}

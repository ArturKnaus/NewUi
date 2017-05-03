package org.catroid.catrobat.newui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.catroid.catrobat.newui.data.LookInfo;
import org.catroid.catrobat.newui.data.ProjectItem;
import org.catroid.catrobat.newui.data.SoundInfo;
import org.catroid.catrobat.newui.data.Sprite;
import org.catroid.catrobat.newui.data.serialization.ProjectSerializer;
import org.catroid.catrobat.newui.ui.ProjectActivity;
import org.catroid.catrobat.newui.ui.SpriteActivity;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainMenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // TODO remove callback, just for debugging purposes
    public void onListViewClick(View view) {

        ProjectItem projectItem = new ProjectItem(0, null, "Test Project");

        Sprite spriteOne = new Sprite("Sprite 1");
        Sprite spriteTwo = new Sprite("Sprite w");

        LookInfo luckyLook = new LookInfo("luckyLook", null);
        LookInfo lookyLooky = new LookInfo("lookyLooky", null);

        SoundInfo soundInfo = new SoundInfo("Sound", null);


        spriteOne.addLook(luckyLook);
        spriteOne.addLook(lookyLooky);
        spriteOne.addSound(soundInfo);

        projectItem.addSprite(spriteOne);

        ProjectSerializer projectSerializer = new ProjectSerializer();

        String serializedProject = projectSerializer.serializeProject(projectItem);

        Log.d(TAG, serializedProject);
    }

    public void onRecycleViewClick(View view) {
        Intent intent = new Intent(this, SpriteActivity.class);
        startActivity(intent);
    }

    public void onGridViewClick(View view) {
        Intent intent = new Intent(this, ProjectActivity.class);
        startActivity(intent);
    }
}

package org.catroid.catrobat.newui.ui.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Toast;

import org.catroid.catrobat.newui.R;
import org.catroid.catrobat.newui.data.Constants;
import org.catroid.catrobat.newui.data.Project;

import org.catroid.catrobat.newui.db.brigde.ProjectBridge;
import org.catroid.catrobat.newui.ui.adapter.OnSwipeTouchListener;
import org.catroid.catrobat.newui.ui.adapter.ProjectAdapter;
import org.catroid.catrobat.newui.ui.adapter.WebViewManager;
import org.catroid.catrobat.newui.ui.fragment.BaseRecyclerListFragment;
import org.catroid.catrobat.newui.ui.fragment.BaseRecyclerListFragmentDelegate;
import org.catroid.catrobat.newui.ui.fragment.ProjectListFragment;

import java.util.Date;

import static android.view.View.GONE;

public class ProjectActivity extends AppCompatActivity implements BaseRecyclerListFragmentDelegate<Project> {

    private static final String TAG = ProjectActivity.class.getSimpleName();
    private WebView mWebView;
    private OnSwipeTouchListener onSwipeTouchListener;
    private ProjectListFragment mProjectListFragment;

    private View mBottomSheet;
    private BottomSheetBehavior mBottomSheetBehavior;
    private BottomNavigationView mBottomNavigationView;
    private int mLastTabPosition;

    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.project_activity_view);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Constants.PROJECT_IMAGE_SIZE = getSizeForGridViewImages();

        requestAndroidPermissions();

        setupWebView();
        setupProjectListFragment();
        setupFAB();

        setupBottomSheet();
        setupBottomSheetNavigation();
        selectBottomTab(1);
    }


    private void setupBottomSheet() {
        mBottomSheet = (View) findViewById(R.id.bottom_sheet);
        mBottomSheet.setVisibility(View.INVISIBLE);

        mBottomSheetBehavior = (BottomSheetBehavior) BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(true);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    }

    private void selectBottomTab(int tabPosition) {
        mBottomNavigationView.getMenu().getItem(tabPosition).setChecked(true);
        mLastTabPosition = tabPosition;
    }

    private void setupBottomSheetNavigation() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                        int newSelectedBottomTab = mapMenuItemToPosition(item);
                        if (newSelectedBottomTab == mLastTabPosition) {
                            return true;
                        }

                        ProjectAdapter.ProjectScope newProjectScope = getScopeForMenuItem(item);

                        if (newSelectedBottomTab > mLastTabPosition) {
                            mProjectListFragment.animateSlideRightToScope(newProjectScope);
                        } else {
                            mProjectListFragment.animateSlideLeftToScope(newProjectScope);
                        }

                        mLastTabPosition = newSelectedBottomTab;

                        return true;
                    }
                });
    }

    private ProjectAdapter.ProjectScope getScopeForMenuItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_action_favorites:
                return ProjectAdapter.ProjectScope.FAVORITES;
            case R.id.bottom_action_recent:
                return ProjectAdapter.ProjectScope.RECENT;
            case R.id.bottom_action_all:
            default:
                return ProjectAdapter.ProjectScope.ALL;
        }
    }

    private int mapMenuItemToPosition(@NonNull MenuItem item) {
        int position;
        switch (item.getItemId()) {
            case R.id.bottom_action_favorites:
                position = 0;
                break;
            case R.id.bottom_action_all:
                position = 1;
                break;
            case R.id.bottom_action_recent:
                position = 2;
                break;
            default:
                position = 1;
                break;
        }

        return position;
    }


    private void setupProjectListFragment() {
        mProjectListFragment = (ProjectListFragment) getSupportFragmentManager().findFragmentById(R.id.project_fragment);
        mProjectListFragment.setBaseRecyclerListFragmentDelegate(this);
        mProjectListFragment.setProjectActivity(this);
    }

    private void setupFAB() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddProjectClicked();
            }
        });
    }

    private void onAddProjectClicked() {
        mProjectListFragment.onAddButtonClicked();
    }

    private void setupWebView() {
        mWebView = (WebView) findViewById(R.id.webview);
        try {
            if (!(WebViewManager.loadFromURL(mWebView, Constants.PROJECT_NEWS_URL, this))) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            } else {
                DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

                mWebView.getLayoutParams().height = mDisplayMetrics.heightPixels / 4;


            }
        } catch (Exception exception) {
            mWebView.setVisibility(GONE);
            mWebView.getLayoutParams().height = 0;
            Toast.makeText(this, "NOT connected !!!", Toast.LENGTH_LONG).show();
        }

        setSwipeThresholdForWebView();
        onSwipeTouchListener = createOnSwipeListener();
        mWebView.setOnTouchListener(onSwipeTouchListener);
    }


    private void requestAndroidPermissions() {
        ActivityCompat.requestPermissions(ProjectActivity.this, new String[]
                {Manifest.permission.INTERNET}, 1);
        ActivityCompat.requestPermissions(ProjectActivity.this, new String[]
                {Manifest.permission.ACCESS_WIFI_STATE}, 1);
        ActivityCompat.requestPermissions(ProjectActivity.this, new String[]
                {Manifest.permission.ACCESS_NETWORK_STATE}, 1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private int getSizeForGridViewImages() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        float dp = (mDisplayMetrics.widthPixels * 1.5f) / density;

        return (int) dp;
    }

    private void setSwipeThresholdForWebView() {

        try {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

            double windowWidth = mDisplayMetrics.widthPixels * 0.5;

            Constants.SWIPE_THRESHOLD = (int) windowWidth;
            Constants.SWIPE_VELOCITY_THRESHOLD = (int) windowWidth;
        } catch (Exception ex) {
            Log.wtf("DISPLAY METRICS ERROR", ex.getMessage());
            Constants.SWIPE_THRESHOLD = 300;
            Constants.SWIPE_VELOCITY_THRESHOLD = 300;
        }
    }

    private OnSwipeTouchListener createOnSwipeListener() {
        OnSwipeTouchListener touchListener = new OnSwipeTouchListener() {

            @Override
            public void onSwipeRight() {
                Animation outAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.out_animation);
                mWebView.setVisibility(GONE);
            }

            @Override
            public void onSwipeLeft() {
                Animation outAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.out_animation);
                mWebView.setVisibility(GONE);
            }

            @Override
            public void onClick() {
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse(Constants.PROJECT_FULL_NEWS_URL));
                startActivity(httpIntent);
            }
        };

        return touchListener;
    }

    @Override
    public void onItemClicked(BaseRecyclerListFragment<Project> fragment, Project project) {
        Intent scenesActivityIntent = new Intent(this, SceneActivity.class);

        scenesActivityIntent.putExtra(SceneActivity.PROJECT_ID_KEY, project.getId());
        scenesActivityIntent.putExtra(SceneActivity.PROJECT_NAME_KEY, project.getName());

        updateProjectLastAccess(project);

        startActivity(scenesActivityIntent);
    }

    private void updateProjectLastAccess(Project project) {
        ProjectBridge bridge = new ProjectBridge(this);

        project.setLastAccess(new Date());

        bridge.update(project);
    }

    public void onProjectListFragmentScrolled(ProjectListFragment fragment, int dx, int dy) {
        if(dy > Constants.SCROLL_ACTION_MIN_THRESHOLD) {
            if (mWebView.getVisibility() != View.GONE) {
                mWebView.setVisibility(GONE);
            }
            mBottomNavigationView.setVisibility(View.INVISIBLE);

            ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(mFab, "translationY", getResources().getDimension(R.dimen.bottom_navigation_height));
            mObjectAnimator.setDuration(300);
            mObjectAnimator.start();

        } else if(dy < (-1) * Constants.SCROLL_ACTION_MIN_THRESHOLD) {
            ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(mFab, "translationY", (-0.05f) * getResources().getDimension(R.dimen.bottom_navigation_height));
            mObjectAnimator.setDuration(300);
            mObjectAnimator.start();

            mBottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}

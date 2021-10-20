package phucdv.android.magicnote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import phucdv.android.magicnote.noteinterface.ShareComponents;
import phucdv.android.magicnote.util.Constants;

public class MagicNoteActivity extends AppCompatActivity implements ShareComponents {

    public static final String ACTION_NEW_NOTE = "phucdv.action.ACTION_NEW_NOTE";

    private AppBarConfiguration mAppBarConfiguration;
    private NavController mNavController;
    private FloatingActionButton mFab;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private BottomAppBar mBottomAppBar;
    private TextView mTitleBottomAppBar;
    private AnimatedVectorDrawable mAnimatedVectorDrawable;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_note);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mBottomAppBar = findViewById(R.id.bottomAppBar);
        mTitleBottomAppBar = mBottomAppBar.findViewById(R.id.bottom_bar_title);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_processing, R.id.nav_archive, R.id.nav_recycle_bin)
                .setDrawerLayout(mDrawer)
                .build();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);

        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                mBottomAppBar.performShow();
            }
        });
        mFab = findViewById(R.id.fab);
        mAnimatedVectorDrawable = (AnimatedVectorDrawable) mFab.getDrawable();

        Intent intent = getIntent();
        if(intent != null && intent.getAction() != null && intent.getAction().equals(ACTION_NEW_NOTE)){
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.ARG_PARENT_ID,
                    intent.getLongExtra(Constants.ARG_PARENT_ID, Constants.UNKNOW_PARENT_ID));
            navigate(R.id.action_global_editNoteFragment, bundle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public DrawerLayout getDrawerLayout() {
        return mDrawer;
    }

    public FloatingActionButton getFloatingActionButton(){
        return mFab;
    }

    public NavController getNavController(){
        return mNavController;
    }

    public BottomAppBar getBottomAppBar(){
        return mBottomAppBar;
    }

    public TextView getBottomAppBarTitle(){
        return mTitleBottomAppBar;
    }

    @Override
    public AnimatedVectorDrawable getAnimatedVectorDrawable() {
        return mAnimatedVectorDrawable;
    }

    @Override
    public void setFabDrawable(int drawableId){
        mAnimatedVectorDrawable = (AnimatedVectorDrawable) mFab.getDrawable();
        mAnimatedVectorDrawable.registerAnimationCallback(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                mAnimatedVectorDrawable = (AnimatedVectorDrawable) getDrawable(drawableId);
                mFab.setImageDrawable(mAnimatedVectorDrawable);
            }
        });
        mAnimatedVectorDrawable.start();
    }

    @Override
    public void navigate(int target, Bundle bundle) {
        mNavController.navigate(target, bundle);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }
}
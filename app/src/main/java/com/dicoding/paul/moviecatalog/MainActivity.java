package com.dicoding.paul.moviecatalog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.dicoding.paul.moviecatalog.alarmmanager.AlarmReceiver;
import com.dicoding.paul.moviecatalog.nowplayingfragment.NowPlayingFragment;
import com.dicoding.paul.moviecatalog.upcomingfragment.UpcomingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.paul.moviecatalog.SearchActivity.EXTRA_MOVIE;
import static com.dicoding.paul.moviecatalog.alarmmanager.AlarmReceiver.TYPE_DAILY_CONTENTS;
import static com.dicoding.paul.moviecatalog.alarmmanager.AlarmReceiver.TYPE_REMINDER;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SearchView searchView;
    private String movie;
    private final static String SHARED_KEY ="shared_preferences";
    private final static String DAILY = "daily_contents";
    private final static String NOW_PLAYING = "now_playing";

    private AlarmReceiver alarmReceiver;

    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tb_my_toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout_main) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view_main) NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);

        initiateReminders();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        searchView.setQuery("", false);
        searchView.clearFocus();
    }

    @Override
    public void onBackPressed() {
        ButterKnife.bind(this);

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            searchView.clearFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movie = searchView.getQuery().toString();
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_MOVIE, movie);

                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("movie", bundle);
                startActivity(intent);

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new NowPlayingFragment(), getResources().getString(R.string.now_playing));
        adapter.addFragment(new UpcomingFragment(), getResources().getString(R.string.upcoming));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ButterKnife.bind(this);

        int id = item.getItemId();

        if (id == R.id.home) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (id == R.id.search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);

        } else if (id == R.id.favourite) {
            Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
            startActivity(intent);

        } else if (id == R.id.switch_daily) {
            //We will use initiateReminders() to manipulate this menuItem
            return false;

        } else if (id == R.id.switch_now_playing) {
            //We will use initiateReminders() to manipulate this menuItem
            return false;

        } else if (id == R.id.settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

    //We will use this method to manipulate switch button instead of using switch preference/settings activity
    //Using switch preference to manipulate only 2 menu items is such an overkill
    //It will also leave blank space in navigation view
    private void initiateReminders() {
        alarmReceiver = new AlarmReceiver();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_KEY, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        SwitchCompat switchDaily = (SwitchCompat) navigationView.getMenu().
                findItem(R.id.switch_daily).getActionView();

        switchDaily.setChecked(sharedPreferences.getBoolean(DAILY, false));

        switchDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alarmReceiver.setReminders(getApplicationContext(), TYPE_DAILY_CONTENTS);
                    editor.putBoolean(DAILY, true).apply();
                } else {
                    alarmReceiver.cancelAlarm(getApplicationContext(), TYPE_DAILY_CONTENTS);
                    editor.putBoolean(DAILY, false).apply();
                }
            }
        });

        SwitchCompat switchNowPlaying = (SwitchCompat) navigationView.getMenu().
                findItem(R.id.switch_now_playing).getActionView();

        switchNowPlaying.setChecked(sharedPreferences.getBoolean(NOW_PLAYING, false));

        switchNowPlaying.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alarmReceiver.setReminders(getApplicationContext(), TYPE_REMINDER);
                    editor.putBoolean(NOW_PLAYING, true).apply();
                } else {
                    alarmReceiver.cancelAlarm(getApplicationContext(), TYPE_REMINDER);
                    editor.putBoolean(NOW_PLAYING, false).apply();
                }
            }
        });
    }
}

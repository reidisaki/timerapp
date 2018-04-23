package kalei.com.timerapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kalei.com.timerapp.views.TimerItemAdapter;

/**
 * Created by risaki on 9/24/17.
 */

public class MainActivity extends TimerBaseActivity {

    @BindView (R.id.fab)
    FloatingActionButton fab;

    @BindView (R.id.main_recycler_view)
    RecyclerView recyclerView;
    @BindView (R.id.toolbar)
    Toolbar toolbar;
    //todo: research how the hell to use drawerlayout
    @BindView (R.id.drawer_layout) DrawerLayout mDrawerLayout;
    TimerItemAdapter adapter;
    ArrayList<TimerItem> timerItemList;
    int id = -1; // this needs to be the id you are passing from the item you select

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent i = new Intent(MainActivity.this, TimerActivityDetail.class);
                i.putExtra(TimerActivityDetail.ID_BUNDLE_NAME, id);
                startActivity(i);
            }
        });

        String jsonListString = PrefManager.getListOfItems(this);
        timerItemList = new Gson().fromJson(jsonListString, new TypeToken<List<TimerItem>>() {
        }.getType());
        adapter = new TimerItemAdapter(this);
        adapter.setItems(timerItemList);
        LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.common_open_on_phone,
                R.string.common_open_on_phone);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);

        View actionBarDropDownView = getLayoutInflater().inflate(R.layout.actionbar_dropdown, null, false);
        Spinner actionBarSpinner = actionBarDropDownView.findViewById(R.id.category_spinner);

        String[] categories = getResources().getStringArray(R.array.category_string_array);
        List<String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, categories);
        arrayList.add(1, "All");

        ArrayAdapter<String> dropDownCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);

        dropDownCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionBarSpinner.setAdapter(dropDownCategoryAdapter);
        actionBarSpinner.setDropDownWidth(500);
        //todo: can you use images with dropdown menus?
        actionBarSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {

                adapter.setItems(timerItemList);
                switch (i) {
                    case 1:
                        //show all
                        adapter.updateList(timerItemList);
                        break;
                    case 2:
                        //birthday
                        adapter.updateList(filteredList("Birthday"));
                        break;
                    case 3:
                        //health
                        adapter.updateList(filteredList("Health"));
                        break;
                    case 4:
                        //house
                        adapter.updateList(filteredList("House"));
                        break;
                    case 5:
                        //medical
                        adapter.updateList(filteredList("medical"));
                        break;
                    case 6:
                        //pet
                        adapter.updateList(filteredList("Pet"));
                        break;
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) {

            }
        });
        getSupportActionBar().setCustomView(actionBarDropDownView);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        mDrawerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(MainActivity.this, "Drawer is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        mDrawerLayout.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerSlide(final View drawerView, final float slideOffset) {
            }

            @Override
            public void onDrawerOpened(final View drawerView) {

                Toast.makeText(MainActivity.this, "Drawer is open", Toast.LENGTH_SHORT).show();
//                getToolbar().setNavigationIcon(R.drawable.ic_action_back_white);
            }

            @Override
            public void onDrawerClosed(final View drawerView) {
//                getToolbar().setNavigationIcon(getHamburgerDrawable());
                Toast.makeText(MainActivity.this, "Drawer is closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerStateChanged(final int newState) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.spinner);

        return true;
    }

    public ArrayList<TimerItem> filteredList(String categoryName) {
        ArrayList<TimerItem> filteredList = new ArrayList<>();
        if (adapter.getItems() != null) {
            for (TimerItem t : adapter.getItems()) {
                if (t.getCategory().toLowerCase().equals(categoryName.toLowerCase())) {
                    filteredList.add(t);
                }
            }
        }
        return filteredList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String jsonListString = PrefManager.getListOfItems(this);
        ArrayList<TimerItem> timerItemList = new Gson().fromJson(jsonListString, new TypeToken<List<TimerItem>>() {
        }.getType());
        adapter.setItems(timerItemList);
        adapter.notifyDataSetChanged();
    }
}

package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppUsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView usersListView;
    private ArrayList<String> usersList;
    private ArrayAdapter adapter;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_users);

        usersListView = findViewById(R.id.listViewId);
        usersList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usersList);

        // Getting SwipeContainerLayout
        swipeLayout = findViewById(R.id.swipeRefresher);

        usersListView.setOnItemClickListener(this);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                parseQuery.whereNotContainedIn("username", usersList);
                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (objects.size() > 0 && e == null){
                            for (ParseUser user : objects){
                                usersList.add(user.getUsername());
                            }
                            adapter.notifyDataSetChanged();
                            usersListView.setAdapter(adapter);
                        }
                    }
                });

                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 4000); // Delay in millis
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout){
            FancyToast.makeText(WhatsAppUsersActivity.this, ParseUser.getCurrentUser().getUsername() + " logged out successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                ParseUser.logOut();
                finish();
                switchToLogin();
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchToLogin(){
        Intent intent = new Intent(WhatsAppUsersActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(WhatsAppUsersActivity.this, WhatsAppChatActivity.class);
        intent.putExtra("selectedUser", usersList.get(position));
        startActivity(intent);
    }
}
package info.rayrojas.avispa;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Credentials;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;


import java.util.ArrayList;

import info.rayrojas.avispa.activities.CredentialsActivity;
import info.rayrojas.avispa.adapters.NotifyAdapter;
import info.rayrojas.avispa.conf.Settings;
import info.rayrojas.avispa.models.Channel;
import info.rayrojas.avispa.models.Credential;
import info.rayrojas.avispa.models.Event;
import info.rayrojas.avispa.models.Notify;
import info.rayrojas.avispa.notifiers.NotificationView;
import info.rayrojas.avispa.services.SpyService;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ServiceConnection, SpyService.CallBack {

    EditText editText;
    ListView listViewNotifications;
    ArrayList<Notify> items;
    NotifyAdapter itemsAdapter;
    SpyService serviceCommunitcation;
    Boolean wasBinded = false;
    Notify list;
    Boolean myFirstTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRows();
                Snackbar.make(view, "Refrescando", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        list = new Notify();

        listViewNotifications = (ListView) findViewById(R.id.listNotifications);

        items = list.getAll(this);

        itemsAdapter =
                new NotifyAdapter(this, items);

        listViewNotifications.setAdapter(itemsAdapter);

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listViewNotifications),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                itemsAdapter.remove(position);
                                updateRows();
                            }
                        });

        listViewNotifications.setOnTouchListener(touchListener);
        listViewNotifications.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listViewNotifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Intent currentIntent = new Intent(MainActivity.this, NotificationView.class);
                    Notify currentNotify = itemsAdapter.getOneByPosition(position);
                    if ( currentNotify == null ) {
                        Toast.makeText(MainActivity.this, "Error en la selección.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    currentIntent.putExtra("notifyId", currentNotify.getId());
                    startActivity(currentIntent);
                }
            }
        });

    }

    //            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (touchListener.existPendingDismisses()) {
//                    touchListener.undoPendingDismiss();
//                } else {
//                    Toast.makeText(MainActivity.this, "Position " + position, Toast.LENGTH_SHORT).show();
//                }
//            }

    public void doStartService() {
        Intent mServiceIntent = new Intent(this, SpyService.class);
        if (!isMyServiceRunning(SpyService.class)) {
            startService(mServiceIntent);
        }
        wasBinded = true;
        bindService(mServiceIntent, this, Context.BIND_AUTO_CREATE);
    }
    public void reBind() {
        Intent mServiceIntent = new Intent(this, SpyService.class);
        wasBinded = true;
        bindService(mServiceIntent, this, Context.BIND_AUTO_CREATE);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.v ("bichito", true+"<<");
                return true;
            }
        }
        return false;
    }

    public void restartService() {
        Snackbar.make(getWindow().getDecorView(), "Parando servicio.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        doStopService();
        Snackbar.make(getWindow().getDecorView(), "Reiniciando servicio.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        doStartService();
    }

    public void doStopService() {
//        if (!isMyServiceRunning(SpyService.class)) {
//            return;
//        }
        if (serviceCommunitcation != null && wasBinded) {
            serviceCommunitcation.setCallBack(null);
            unbindService(this);
            wasBinded = false;
        }
        stopService(new Intent(this, SpyService.class));
    }

    public void doAfterMessage(String channelName, String eventName, final String data) {
        Log.v("bichito", data);
        editText.setText(data);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        View v = findViewById(R.id.action_restart);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if ( id == R.id.action_restart ) {
            restartService();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setup) {
            Intent o = new Intent(this, CredentialsActivity.class);
            startActivity(o);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateRows() {
        items.clear();
        items.addAll(list.getAll(this));
        itemsAdapter.notifyDataSetChanged();

    }

    public boolean setupCredentials() {
        Channel _currentChannel = new Channel();
        _currentChannel.getActive(this);
        if ( _currentChannel.isNonDefined() ) {
            return false;
        }

        Event _currentEvent = new Event();
        _currentEvent.getActive(this);
        if ( _currentEvent.isNonDefined() ) {
            return false;
        }

        Credential _currentCredential = new Credential();
        _currentCredential.getActive(this);

        if (  _currentCredential.isNonDefined()) {
            return false;
        }

        Settings.CLIENT_CHANNEL_INFO = _currentChannel.getName();
        Settings.CLIENT_EVENT_INFO = _currentEvent.getName();
        Settings.CLIENT_TOKEN = _currentCredential.getToken();
        Settings.CLIENT_PROVIDER = _currentCredential.getClient();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("bichito", "gonna pause");
// unbindService if activity is in onPause() state
        if (serviceCommunitcation != null) {
            serviceCommunitcation.setCallBack(null);
            unbindService(this);
            wasBinded = false;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("bichito", "gonna stop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("bichito", "gonna close");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("bichito", "on resume");
        if (setupCredentials()) {
            restartService();
//            if ( myFirstTime ) {
//                myFirstTime = false;
//                restartService();
//            } else {
//                Intent intent = getIntent();ok
//                String action = intent.getStringExtra("action");
//                if ( action != null && action == "refresh" ) {
//                    restartService();
//                } else {
//                    reBind();
//                }
//            }
        } else {
            Snackbar.make(getWindow().getDecorView(), "No existe datos definidos.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        //doStartService();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        serviceCommunitcation = ((SpyService.MyBinder)service).getService();
        serviceCommunitcation.setCallBack(this);
        wasBinded = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onBindingDied(ComponentName name) {

    }

    @Override
    public void onNullBinding(ComponentName name) {

    }

    @Override
    public void onOperationProgress(int progress) {

    }

    @Override
    public void onOperationCompleted() {
        updateRows();
    }
}

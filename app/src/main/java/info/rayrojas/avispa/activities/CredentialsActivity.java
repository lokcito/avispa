package info.rayrojas.avispa.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import info.rayrojas.avispa.MainActivity;
import info.rayrojas.avispa.R;
import info.rayrojas.avispa.activities.ui.main.PageViewModel;
import info.rayrojas.avispa.activities.ui.main.PlaceholderFragment;
import info.rayrojas.avispa.activities.ui.main.SectionsPagerAdapter;
import info.rayrojas.avispa.models.Channel;
import info.rayrojas.avispa.models.Credential;
import info.rayrojas.avispa.models.Event;

public class CredentialsActivity extends AppCompatActivity {

    PageViewModel pageViewModel;
    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;


    public Fragment getFragmentById(int position) {
        return sectionsPagerAdapter.getItem(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton goBack = findViewById(R.id.back);

        /*
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                PlaceholderFragment o = (PlaceholderFragment)getFragmentById(position);
                o.updateRows();
                if ( position == 2 ) {


                }
                Log.v("bichito", "changed");
                // Check if this is the page you want.
            }
        });*/

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent o = new Intent(CredentialsActivity.this, MainActivity.class);
                o.putExtra("action", "refresh");
                startActivity(o);
            }});
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                final int ipage = CredentialsActivity.this.getCurrentPage();
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(CredentialsActivity.this);
                final View promptsView;
                if ( ipage == 0 ) {
                    promptsView = li.inflate(R.layout.credential_input_dialog, null);
                } else {
                    promptsView = li.inflate(R.layout.event_input_dialog, null);
                }



                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        CredentialsActivity.this);

                alertDialogBuilder.setView(promptsView);


                if ( ipage == 0 ) {
                    final Switch switchPusher = (Switch) promptsView
                            .findViewById(R.id.switchPusher);

                    final Switch switchAbly = (Switch) promptsView
                            .findViewById(R.id.switchAbly);

                    switchPusher.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchAbly.setChecked(false);
                        }
                    });

                    switchAbly.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchPusher.setChecked(false);
                        }
                    });
                }
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text

                                        if ( ipage == 0 ) {
                                            Switch switchPusher = (Switch) promptsView
                                                    .findViewById(R.id.switchPusher);
                                        String client = switchPusher.isChecked() ? "pusher" : "ably";
                                            setOnDatabase(client, userInput.getText().toString());
                                        } else {
                                            setOnDatabase("", userInput.getText().toString());
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                //                sectionsPagerAdapter.
                //pageViewModel = sectionsPagerAdapter.getPageViewModel();
            }
        });
    }
    public int getCurrentPage() {
        return viewPager.getCurrentItem();
    }

    public void refreshDatabase() {
        int ipage = CredentialsActivity.this.getCurrentPage();
        PlaceholderFragment fragment = (PlaceholderFragment)SectionsPagerAdapter.currentFragments[ipage];
        fragment.updateRows();
    }

    public void setOnDatabase(String client, String _text) {
        int ipage = CredentialsActivity.this.getCurrentPage();
        if ( ipage == 0 ) {
            Credential o = new Credential();
            o.setClient(client);
            o.setToken(_text);
            o.setLocal(CredentialsActivity.this);
            PlaceholderFragment fragment = (PlaceholderFragment)SectionsPagerAdapter.currentFragments[ipage];
            fragment.updateRows();
        } else if ( ipage == 1 ) {
            Event o = new Event();
            o.setName(_text);
            o.setLocal(CredentialsActivity.this);
            PlaceholderFragment fragment = (PlaceholderFragment)SectionsPagerAdapter.currentFragments[ipage];
            fragment.updateRows();
        } else if ( ipage == 2 ) {
            Channel o = new Channel();
            o.setName(_text);
            o.setLocal(CredentialsActivity.this);
            PlaceholderFragment fragment = (PlaceholderFragment)SectionsPagerAdapter.currentFragments[ipage];
            fragment.updateRows();
        }
    }

}
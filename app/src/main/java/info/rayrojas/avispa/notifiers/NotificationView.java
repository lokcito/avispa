package info.rayrojas.avispa.notifiers;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.app.NotificationManager;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import info.rayrojas.avispa.MainActivity;
import info.rayrojas.avispa.R;
import info.rayrojas.avispa.models.Notify;

public class NotificationView extends Activity {
    int notifyId;
    TextView _title;
    TextView _client;
    TextView _event;
    TextView _channel;

    TextView _message;
    TextView _extra;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationview);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Dismiss Notification
        notificationmanager.cancel(0);

        // Retrive the data from MainActivity.java
        Intent i = getIntent();
        try {
            i.getIntExtra("notifyId", notifyId);
        } catch (Exception ex){

        }
        Notify o = new Notify();
        o.setId(notifyId);
        Notify oo = o.getOne(this);
        if ( oo == null ) {
            return;
        }
        draw(oo);

    }
    public void draw(Notify currentNotify) {
        _title = (TextView) findViewById(R.id.txtTitle);
        _client = (TextView) findViewById(R.id.txtClient);
        _event = (TextView) findViewById(R.id.txtEvent);
        _channel = (TextView) findViewById(R.id.txtChannel);
        _message = (TextView) findViewById(R.id.message);
        _extra = (TextView) findViewById(R.id.extra);

        _title.setText(String.format("Titulo: %s", currentNotify.getTitle()));
        _client.setText(String.format("Cliente: %s", currentNotify.getToken()));
        _channel.setText(String.format("Canal: %s", currentNotify.getChannel()));
        _event.setText(String.format("Evento: %s", currentNotify.getEvent()));

        _message.setText(String.format(currentNotify.getMessage()));
        _message.setEnabled(false);
        _extra.setText(String.format(currentNotify.getExtra()));
        _extra.setEnabled(false);
        Button btnList = (Button)findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(NotificationView.this, MainActivity.class);
                startActivity(o);

            }
        });

    }
}
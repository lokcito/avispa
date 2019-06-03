package info.rayrojas.avispa.activities.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.ArrayList;

import info.rayrojas.avispa.MainActivity;
import info.rayrojas.avispa.R;
import info.rayrojas.avispa.adapters.ChannelAdapter;
import info.rayrojas.avispa.adapters.CredentialAdapter;
import info.rayrojas.avispa.adapters.EventAdapter;
import info.rayrojas.avispa.adapters.NotifyAdapter;
import info.rayrojas.avispa.models.Channel;
import info.rayrojas.avispa.models.Credential;
import info.rayrojas.avispa.models.Event;
import info.rayrojas.avispa.models.Notify;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    Context _context;
    Credential credentialList;
    Event eventList;
    Channel channelList;
    EventAdapter eventAdapter;
    ChannelAdapter channelAdapter;
    CredentialAdapter credentialAdapter;
    ArrayList<Credential> credentialItems;
    ArrayList<Event> eventItems;
    ArrayList<Channel> channelItems;
    ListView listViewCredentials;
    int POSITION;

    public PageViewModel getPageViewModel() {
        return this.pageViewModel;
    }

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        fragment.POSITION = index;
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        _context = this.getContext();
        View root = inflater.inflate(R.layout.fragment_credentials_list, container, false);

//        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.v("bichito", s);
//                textView.setText(s);
            }
        });

        if ( this.POSITION == 1 ) {
            credentialsAdapter(root);
        } else if ( this.POSITION == 2 ) {
            eventsAdapter(root);
        } else if ( this.POSITION == 3 ) {
            channelsAdapter(root);
        }

        return root;
    }

    public void channelsAdapter(View root) {
        channelList = new Channel();

        listViewCredentials = (ListView) root.findViewById(R.id.listCredentials);

        channelItems = channelList.getAll(_context);

        channelAdapter =
                new ChannelAdapter(_context, channelItems);

        listViewCredentials.setAdapter(channelAdapter);

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listViewCredentials),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                channelAdapter.remove(position);
                                updateRows();
                            }
                        });

        listViewCredentials.setOnTouchListener(touchListener);
        listViewCredentials.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listViewCredentials.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(_context, "Position " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void eventsAdapter(View root) {
        eventList = new Event();

        listViewCredentials = (ListView) root.findViewById(R.id.listCredentials);

        eventItems = eventList.getAll(_context);

        eventAdapter =
                new EventAdapter(_context, eventItems);

        listViewCredentials.setAdapter(eventAdapter);

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listViewCredentials),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                eventAdapter.remove(position);
                                updateRows();
                            }
                        });

        listViewCredentials.setOnTouchListener(touchListener);
        listViewCredentials.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listViewCredentials.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(_context, "Position " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void credentialsAdapter(View root) {
        credentialList = new Credential();

        listViewCredentials = (ListView) root.findViewById(R.id.listCredentials);

        credentialItems = credentialList.getAll(_context);

        credentialAdapter =
                new CredentialAdapter(_context, credentialItems);

        listViewCredentials.setAdapter(credentialAdapter);

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listViewCredentials),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                credentialAdapter.remove(position);
                                updateRows();
                            }
                        });

        listViewCredentials.setOnTouchListener(touchListener);
        listViewCredentials.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listViewCredentials.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(_context, "Position " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void updateRows() {
        if ( this.POSITION == 1 ) {
            credentialItems.clear();
            credentialItems.addAll(credentialList.getAll(_context));
            Toast.makeText(_context, "n rows: " + credentialItems.size(), Toast.LENGTH_SHORT).show();
            credentialAdapter.notifyDataSetChanged();
        } else if ( this.POSITION == 2 ) {
            eventItems.clear();
            eventItems.addAll(eventList.getAll(_context));
            Toast.makeText(_context, "n rows: " + eventItems.size(), Toast.LENGTH_SHORT).show();
            eventAdapter.notifyDataSetChanged();
        } else if ( this.POSITION == 3 ) {
            channelItems.clear();
            channelItems.addAll(channelList.getAll(_context));
            Toast.makeText(_context, "n rows: " + channelItems.size(), Toast.LENGTH_SHORT).show();
            channelAdapter.notifyDataSetChanged();
        }

    }
}
package info.rayrojas.avispa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import info.rayrojas.avispa.R;
import info.rayrojas.avispa.models.Channel;
import info.rayrojas.avispa.models.Credential;
import info.rayrojas.avispa.models.Notify;

public class ChannelAdapter extends ArrayAdapter<Channel> {
    Context context;
    ArrayList<Channel> items;

    private class ViewHolder {
        TextView id;
        TextView name;

        private ViewHolder() {
        }
    }

    public void remove(int position) {
        Channel o = this.items.get(position);
        if ( o != null ) {
            o.unsetLocal(this.context);
        }
    }

    public ChannelAdapter(Context context, ArrayList<Channel> _items) {
        super(context, 0, _items);
        this.items = _items;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ChannelAdapter.ViewHolder holder;
        final Channel rowItem = (Channel) getItem(position);
        LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_notify_item, null);
            holder = new ChannelAdapter.ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.more = (Button) convertView.findViewById(R.id.more);
            convertView.setTag(holder);
        } else {
            holder = (ChannelAdapter.ViewHolder) convertView.getTag();
        }


        holder.name.setText(rowItem.getName());


//        holder.more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                MenuActivity activity = (MenuActivity) context;
////                activity.productListFragmentSelectProduct(position);
//
//            }
//        });


//        holder.image.setImageBitmap(rowItem.getSmallBitMap());
        return convertView;
    }
}

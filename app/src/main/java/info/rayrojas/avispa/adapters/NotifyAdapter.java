package info.rayrojas.avispa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import info.rayrojas.avispa.R;
import info.rayrojas.avispa.models.Notify;

public class NotifyAdapter extends ArrayAdapter<Notify> {
    Context context;

    private class ViewHolder {
        TextView id;
        TextView title;
        TextView message;
        TextView extra;
        Button more;

        private ViewHolder() {
        }
    }

    public NotifyAdapter(Context context, List<Notify> items) {
        super(context, 0, items);
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Notify rowItem = (Notify) getItem(position);
        LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_notify_item, null);
            holder = new ViewHolder();

            holder.id = (TextView) convertView.findViewById(R.id._id);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.extra = (TextView) convertView.findViewById(R.id.extra);
            holder.more = (Button) convertView.findViewById(R.id.more);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.id.setText(rowItem.getId() + "");
        holder.message.setText(rowItem.getMessage());
        holder.extra.setText(rowItem.getExtra());
        holder.title.setText(rowItem.getTitle());


        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MenuActivity activity = (MenuActivity) context;
//                activity.productListFragmentSelectProduct(position);

            }
        });


//        holder.image.setImageBitmap(rowItem.getSmallBitMap());
        return convertView;
    }
}


package info.rayrojas.avispa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import info.rayrojas.avispa.R;
import info.rayrojas.avispa.models.Credential;
import info.rayrojas.avispa.models.Notify;

public class CredentialAdapter extends ArrayAdapter<Credential> {
    Context context;
    ArrayList<Credential> items;

    private class ViewHolder {
        TextView id;
        TextView token;

        private ViewHolder() {
        }
    }

    public void remove(int position) {
        Credential o = this.items.get(position);
        if ( o != null ) {
            o.unsetLocal(this.context);
        }
    }

    public CredentialAdapter(Context context, ArrayList<Credential> _items) {
        super(context, 0, _items);
        this.items = _items;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        CredentialAdapter.ViewHolder holder;
        final Credential rowItem = (Credential) getItem(position);
        LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_credential_item, null);
            holder = new CredentialAdapter.ViewHolder();

            //holder.id = (TextView) convertView.findViewById(R.id._id);
            holder.token = (TextView) convertView.findViewById(R.id.token);
//            holder.more = (Button) convertView.findViewById(R.id.more);
            convertView.setTag(holder);
        } else {
            holder = (CredentialAdapter.ViewHolder) convertView.getTag();
        }

//        holder.id.setText(rowItem.getId() + "");
        holder.token.setText(rowItem.getToken());


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

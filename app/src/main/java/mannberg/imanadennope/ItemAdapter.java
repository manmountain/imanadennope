package mannberg.imanadennope;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by mannb on 8/21/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>  {

    private List<Item> items;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView username;
        public TextView itemtext;
        public TextView location;
        public TextView time;
        public TextView votes;
        public ImageView usericon;
        public ImageView bar;
        public ImageView dots;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            username = (TextView) v.findViewById(R.id.username);
            itemtext = (TextView) v.findViewById(R.id.itemtext);
            location = (TextView) v.findViewById(R.id.locationtext);
            time = (TextView) v.findViewById(R.id.timetext);
            votes = (TextView) v.findViewById(R.id.votes);
            usericon = (ImageView) v.findViewById(R.id.usericon);
            bar = (ImageView) v.findViewById(R.id.bar);
            dots = (ImageView) v.findViewById(R.id.dots);

        }
    }

    public ItemAdapter() {
        String text = "Om man jobbar extra på ett sågverk och bara jobbar natt (04-10) kan man dra in 100000 efter skatt?";
        items = new ArrayList<Item>();
        items.add(new Item("OG", text, "Hometown", 0, 0, 0, true));

        for(int i = 0; i < 10; i++) {
            addItem();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Item item = items.get(position);
        holder.username.setText(item.username);
        holder.location.setText(item.location);
        holder.time.setText(""+item.time+"min");
        holder.itemtext.setText(item.itemtext);
        holder.votes.setText(""+item.votes);
        final int options;
        if (position == 0) {
            holder.usericon.setImageResource(R.drawable.crown);
            holder.bar.setVisibility(View.VISIBLE);
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                options = R.array.dialog_options2;
            } else {
                options = R.array.dialog_options;
            }
        } else {
            holder.usericon.setImageResource(R.drawable.cat);
            holder.bar.setVisibility(View.GONE);
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                options = R.array.dialog_options_response2;
            } else {
                options = R.array.dialog_options_response;
            }
        }

        holder.dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setItems(options ,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                    }});
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                final AlertDialog dialog = builder.create();
                View v = new View(dialog.getContext());
                v.setMinimumHeight(Math.round(1*(dialog.getContext().getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT)));
                dialog.getListView().addFooterView(v);
                dialog.getListView().setFooterDividersEnabled(true);
                dialog.show();
                dialog.getListView().requestFocus();
            }
        });
    }

    public void addItem() {
        int m = items.size();
        for(int i = 0; i < m; i++) {
            Item o = items.get(i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                o.time += ThreadLocalRandom.current().nextInt(1, 3 + 1);
                o.votes += ThreadLocalRandom.current().nextInt(2, 9 + 1);
            } else {
                o.time++;
                o.votes++;
            }
        }
        Item last = items.get(m - 1);
        if (last != null) {
            String text = "I månaden? Nope.";
            int thispos = last.pos + 1;
            items.add(new Item("" + thispos, text, "Near", 0, 0, thispos, false));
        }
    }
}

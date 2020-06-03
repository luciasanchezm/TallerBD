package com.tallerbd.bichos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class bichoAdapter extends RecyclerView.Adapter<bichoAdapter.MyViewHolder> {
    private ArrayList<Bicho> mDataset;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public bichoAdapter(ArrayList<Bicho> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public bichoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v, context);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bindItems(mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mDataset == null)
            return 0;
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        Context context;
        // each data item is just a string in this case
        public View itemView;
        public MyViewHolder(View v, final Context context) {
            super(v);
            itemView = v;
            this.context = context;
        }

        public void bindItems(final Bicho bicho) {
            TextView textView = itemView.findViewById(R.id.itemText);
            textView.setText(bicho.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ConnectionSQL.getSelectedBicho() == bicho) {
                        v.setBackgroundColor(context.getResources().getColor(R.color.White));
                        ConnectionSQL.setSelectedBicho(null);
                    }
                    else if(ConnectionSQL.getSelectedBicho() == null) {
                        v.setBackgroundColor(context.getResources().getColor(R.color.PrimaryColor));
                        ConnectionSQL.setSelectedBicho(bicho);
                    }
                }
            });
        }
    }
}

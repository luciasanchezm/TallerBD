package com.tallerbd.bichos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class usuarioAdapter extends RecyclerView.Adapter<usuarioAdapter.MyViewHolder> {
    private ArrayList<Usuario> mDataset;
    private Context context;
    private int step;

    // Provide a suitable constructor (depends on the kind of dataset)
    public usuarioAdapter(ArrayList<Usuario> myDataset, Context context, int step) {
        mDataset = myDataset;
        this.context = context;
        this.step = step;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public usuarioAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v, context, step);
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
        public View itemView;
        public int step;

        public MyViewHolder(View v, final Context context, int step) {
            super(v);
            itemView = v;
            this.context = context;
            this.step = step;
        }

        public void bindItems(final Usuario usuario) {
            TextView textView = itemView.findViewById(R.id.itemText);
            textView.setText(usuario.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ConnectionSQL.getSelectedUsuario(step) == usuario) {
                        v.setBackgroundColor(context.getResources().getColor(R.color.White));
                        ConnectionSQL.setSelectedUsuario(null, step);
                    }
                    else if(ConnectionSQL.getSelectedUsuario(step) == null) {
                        v.setBackgroundColor(context.getResources().getColor(R.color.PrimaryColor));
                        ConnectionSQL.setSelectedUsuario(usuario, step);
                    }
                }
            });
        }
    }
}

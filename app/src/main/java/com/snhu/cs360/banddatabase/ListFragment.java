package com.snhu.cs360.banddatabase;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListFragment extends Fragment implements DetailsFragment.BandListener {

    private BandAdapter adapter;

    @Override
    public void handleEdited(Band original, Band edited) {
        adapter.editBand(original, edited);
    }

    @Override
    public void handleDeleted(Band deleted) {
        adapter.deleteBand(deleted);
    }

    // For the activity to implement
    public interface OnBandSelectedListener {
        void onBandSelected(int bandId);
    }

    // Reference to the activity
    private OnBandSelectedListener mListener;

    public ListFragment(){
        DetailsFragment.register(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBandSelectedListener) {
            mListener = (OnBandSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBandSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.band_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Send bands to recycler view
        adapter = new BandAdapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.floating_action_button);

        fab.setOnClickListener(l -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Add Band");

            View editView = inflater.inflate(R.layout.band_details, null);
            builder.setView(editView);

            EditText txtDescription = editView.findViewById(R.id.txtDescription);
            EditText txtName = editView.findViewById(R.id.txtName);

            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                String name = txtName.getText().toString();
                String description = txtDescription.getText().toString();
                Band band = new Band(name, description);
                long result = BandsDatabase.getInstance(getContext()).addBand(band);

                if (result == -1) {
                    Toast.makeText(getContext(), "Band name already exists", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.addBand(new Band(result, band));
                }
            });

            builder.setNegativeButton("Cancel", null);
            builder.create();

            builder.show();
        });

        return view;
    }

    private class BandHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Band mBand;

        private final TextView mNameTextView;

        public BandHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_band, parent, false));
            itemView.setOnClickListener(this);
            mNameTextView = itemView.findViewById(R.id.lblBandName);
        }

        public void bind(Band band) {
            mBand = band;
            mNameTextView.setText(mBand.getName());
        }

        @Override
        public void onClick(View view) {
            // Tell ListActivity what band was clicked
            mListener.onBandSelected((int)mBand.getId());
        }
    }

    private class BandAdapter extends RecyclerView.Adapter<BandHolder> {

        private final List<Band> mBands;

        public BandAdapter() {
            mBands = BandsDatabase.getInstance(getContext()).getBands();
        }

        @Override
        public BandHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BandHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(BandHolder holder, int position) {
            Band band = mBands.get(position);
            holder.bind(band);
        }

        @Override
        public int getItemCount() {
            return mBands.size();
        }

        public void addBand(Band band){
            mBands.add(band);
            notifyItemInserted(mBands.size() - 1);
        }

        public void editBand(Band original, Band edited){
            int index = mBands.indexOf(original);

            mBands.add(index, edited);
            mBands.remove(original);
            notifyItemChanged(index);
        }

        public void deleteBand(Band band){
            int index = mBands.indexOf(band);
            mBands.remove(band);
            notifyItemRemoved(index);
        }
    }


}
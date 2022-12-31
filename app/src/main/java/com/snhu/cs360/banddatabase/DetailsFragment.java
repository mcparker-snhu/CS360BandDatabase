package com.snhu.cs360.banddatabase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {

    private static final List<BandListener> listeners = new ArrayList<>();

    public interface BandListener {
        void handleEdited(Band original, Band edited);
        void handleDeleted(Band deleted);
    }

    private Band mBand;

    public static DetailsFragment newInstance(int bandId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt("bandId", bandId);
        fragment.setArguments(args);
        return fragment;
    }

    public static void register(BandListener listener){
        listeners.add(listener);
    }

    public static void remove(BandListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the band ID from the intent that started DetailsActivity
        int bandId = 1;
        if (getArguments() != null) {
            bandId = getArguments().getInt("bandId");
        }

        mBand = BandsDatabase.getInstance(getContext()).getBand(bandId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        TextView nameTextView = (TextView) view.findViewById(R.id.lblBandName);
        nameTextView.setText(mBand.getName());

        TextView descriptionTextView = (TextView) view.findViewById(R.id.bandDescription);
        descriptionTextView.setText(mBand.getDescription());

        Button btnEdit = view.findViewById(R.id.btnEdit);

        btnEdit.setOnClickListener(l -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Edit Band Details");

            View editView = inflater.inflate(R.layout.band_details, null);
            builder.setView(editView);

            EditText txtDescription = editView.findViewById(R.id.txtDescription);
            txtDescription.setText(mBand.getDescription());
            EditText txtName = editView.findViewById(R.id.txtName);
            txtName.setText(mBand.getName());

            builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                String name = txtName.getText().toString();
                String description = txtDescription.getText().toString();
                Band edited = new Band(mBand.getId(), name, description);

                boolean isEdited = BandsDatabase.getInstance(getContext()).editBand(mBand.getId(), edited);

                if (!isEdited){
                    Toast.makeText(getContext(), "Error editing band", Toast.LENGTH_SHORT).show();
                } else {
                    listeners.forEach(listener -> listener.handleEdited(mBand, edited));
                    mBand = edited;
                    nameTextView.setText(edited.getName());
                    descriptionTextView.setText(edited.getDescription());

                }
            });

            builder.setNegativeButton("Cancel", null);
            builder.create();

            builder.show();
        });

        Button btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(l -> {
            boolean isDeleted = BandsDatabase.getInstance(getContext()).deleteBand(mBand.getId());

            if (!isDeleted){
                Toast.makeText(getContext(), "Error deleting band", Toast.LENGTH_SHORT).show();
            } else {
                listeners.forEach(listener -> listener.handleDeleted(mBand));
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
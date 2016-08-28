package com.phacsin.entreprenia.fragments;


import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phacsin.entreprenia.R;

import me.drakeet.materialdialog.MaterialDialog;


/**
 * Created by Bineesh P Babu on 19-08-2016.
 */
public class Talks  extends android.support.v4.app.Fragment {
    protected CardView cardView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_talks, container, false);
        cardView = (CardView) view.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog mMaterialDialog = new MaterialDialog(getContext());
                mMaterialDialog.setContentView(R.layout.fragment_event_details);
                mMaterialDialog.setCanceledOnTouchOutside(true);
                mMaterialDialog.show();
            }
        });
        return view;
    }
}

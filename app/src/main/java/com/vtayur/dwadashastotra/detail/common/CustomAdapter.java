package com.vtayur.dwadashastotra.detail.common;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.vtayur.dwadashastotra.R;
import com.vtayur.dwadashastotra.data.DataProvider;
import com.vtayur.dwadashastotra.data.DwadashaStotraMenu;
import com.vtayur.dwadashastotra.data.Language;

import java.util.List;
import java.util.Random;

public class CustomAdapter extends RecyclerView.Adapter {
    private final Random mRandom;
    private final List<Integer> mBackgroundColors;
    List<String> personNames;
    Activity context;

    public CustomAdapter(Activity context, List personNames) {
        this.context = context;
        this.personNames = personNames;
        mRandom = new Random();
        mBackgroundColors = DataProvider.getBackgroundColorList();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sample, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        // set the data in items
        final String pName = personNames.get(position);
        vh.name.setText(pName);
        // implement setOnClickListener event on item view.
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, pName, Toast.LENGTH_SHORT).show();

                String langSelected = context.getApplicationContext().getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, "");
                DwadashaStotraMenu.getEnum(pName).execute(context, pName, position, Language.getLanguageEnum(langSelected));
            }
        });

        vh.setBackgroundResource(mBackgroundColors.get(position % mBackgroundColors.size()));
    }

    @Override
    public int getItemCount() {
        return personNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void setBackgroundResource(Integer integer) {
            name.setBackgroundResource(integer);
        }
    }
}
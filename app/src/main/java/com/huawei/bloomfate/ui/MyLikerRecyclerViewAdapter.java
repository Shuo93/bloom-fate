package com.huawei.bloomfate.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huawei.bloomfate.R;
import com.huawei.bloomfate.ui.LikerFragment.OnListFragmentInteractionListener;
import com.huawei.bloomfate.ui.dummy.DummyContent.DummyItem;
import com.huawei.bloomfate.util.DateUtil;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLikerRecyclerViewAdapter extends RecyclerView.Adapter<MyLikerRecyclerViewAdapter.ViewHolder> {

    private final List<LikerFragment.LikerItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyLikerRecyclerViewAdapter(List<LikerFragment.LikerItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_liker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.inflate();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem.getUserId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameTv;
        public final TextView timeTv;
        public LikerFragment.LikerItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTv = (TextView) view.findViewById(R.id.name_tv);
            timeTv = (TextView) view.findViewById(R.id.time_tv);
        }

        void inflate() {
            nameTv.setText(mItem.getName());
            timeTv.setText(DateUtil.convertDateTime(mItem.getTime()));
        }
    }
}

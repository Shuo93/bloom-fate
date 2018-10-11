package com.huawei.bloomfate.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.bloomfate.R;
import com.huawei.bloomfate.model.PersonBasic;
import com.huawei.bloomfate.ui.PersonFragment.OnListFragmentInteractionListener;
import com.huawei.bloomfate.ui.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPersonRecyclerViewAdapter extends RecyclerView.Adapter<MyPersonRecyclerViewAdapter.ViewHolder> {

    private final List<PersonBasic> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPersonRecyclerViewAdapter(List<PersonBasic> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_person, parent, false);
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
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView nameTv;
        final TextView sexTv;
        final TextView locationTv;
        final TextView ageTv;
        final TextView signatureTv;
        final ImageView photoImage;
        PersonBasic mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTv = (TextView) view.findViewById(R.id.name_tv);
            sexTv = (TextView) view.findViewById(R.id.sex_tv);
            locationTv = view.findViewById(R.id.location_tv);
            ageTv = view.findViewById(R.id.age_tv);
            signatureTv = view.findViewById(R.id.signature_tv);
            photoImage = view.findViewById(R.id.photo_image);
        }

        void inflate() {
            nameTv.setText(mItem.getName());
            sexTv.setText(mItem.getSex());
            locationTv.setText(mItem.getLocation());
            ageTv.setText(mItem.getAge());
            signatureTv.setText(mItem.getIntroduction());
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}

package com.huawei.bloomfate.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.bloomfate.R;
import com.huawei.bloomfate.ui.PermissionFragment.OnListFragmentInteractionListener;
import com.huawei.bloomfate.ui.dummy.DummyContent.DummyItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.PortUnreachableException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPermissionRecyclerViewAdapter extends RecyclerView.Adapter<MyPermissionRecyclerViewAdapter.ViewHolder> {

    private final List<JSONObject> mValues;
    private final OnListFragmentInteractionListener mListener;

    private PermissionFragment.Type type;

    public MyPermissionRecyclerViewAdapter(List<JSONObject> items, OnListFragmentInteractionListener listener, PermissionFragment.Type type) {
        mValues = items;
        mListener = listener;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_permission, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        try {
            holder.inflate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
//        public final TextView mIdView;
//        public final TextView mContentView;
        final ImageView photoImage;
        final TextView nameTv;
        final TextView statusTv;
        final TextView typeTv;
        final TextView encryptedContentTv;
        final TextView sendTimeTv;
        JSONObject mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            photoImage =  view.findViewById(R.id.photo_image);
            nameTv =  view.findViewById(R.id.name_tv);
            statusTv =  view.findViewById(R.id.status_tv);
            typeTv =  view.findViewById(R.id.type_tv);
            encryptedContentTv =  view.findViewById(R.id.encrypted_content_tv);
            sendTimeTv =  view.findViewById(R.id.send_time_tv);

//            mIdView = (TextView) view.findViewById(R.id.item_number);
//            mContentView = (TextView) view.findViewById(R.id.content);
        }

        void inflate() throws JSONException {
            if (type == PermissionFragment.Type.SEND) {
                nameTv.setText(mItem.getString("receiver_name"));
            } else if (type == PermissionFragment.Type.RECEIVE) {
                nameTv.setText(mItem.getString("sender_name"));
            }
            String status = mItem.getString("status");
            statusTv.setText(convertStatus(status));
            try {
                sendTimeTv.setText(convertDateTime(mItem.getString("date_time")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        private String convertDateTime(String timestamp) throws ParseException {
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            java.util.Date date = format.parse(timestamp);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(date);
        }

        private String convertStatus(String status) {
            switch (status) {
                case "pending":
                    return "待处理";
                case "reject":
                    return "已拒绝";
                case "approve":
                    return "已同意";
                default:
                    return "未知状态";
            }
        }


//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}

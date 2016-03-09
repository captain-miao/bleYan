package com.github.captain_miao.android.bluetoothletutorial.fragment;


import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.captain_miao.android.bluetoothletutorial.R;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetLogView {
    private BleLogAdapter mAdapter;
    private BottomSheetDialog mDialog;
    /**
     * @param context
     */
    public BottomSheetLogView(Context context, List<String> logInfoList) {
        mDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_log_recycler_view, null);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bottom_sheet_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new BleLogAdapter(logInfoList);
        recyclerView.setAdapter(mAdapter);

        mDialog.setContentView(view);
        mDialog.show();
    }

    public static BottomSheetLogView show(Context context, List<String> logInfoList) {
        return new BottomSheetLogView(context, logInfoList);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.list_item_text_view);
        }
    }

    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }
    public void show() {
        if(mDialog != null) {
            mDialog.show();
        }
    }

    public void appendLog(String log) {
        mAdapter.appendData(log);
    }

    private static class BleLogAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<String> logInfoList;

        public BleLogAdapter(List<String> logInfoList) {
            this.logInfoList = logInfoList == null ? new ArrayList<String>() : logInfoList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.log_list_item, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(logInfoList.get(position));
        }

        @Override
        public int getItemCount() {
            return logInfoList.size();
        }


        public void appendData(String log) {
            logInfoList.add(log);
            notifyItemInserted(logInfoList.size() - 1);
        }
    }
}

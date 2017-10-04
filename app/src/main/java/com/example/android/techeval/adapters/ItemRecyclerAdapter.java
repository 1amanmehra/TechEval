package com.example.android.techeval.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.techeval.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Recycler Adapter for the RecyclerView in Point 1
 */
public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<String> mItemList;
    private int mItemWidth;

    private OnItemClickListener mItemOnClickListener;

    public ItemRecyclerAdapter(Context context, int numOfItems, int itemWidth) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemList = new ArrayList<>();
        for (int i = 0; i < numOfItems; i++) {
            mItemList.add(context.getString(R.string.format_point_one_item, i + 1));
        }
        mItemWidth = itemWidth;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_list, parent, false);
        itemView.getLayoutParams().width = mItemWidth;
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mItemList.get(position), mItemOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setItemOnClickListener(OnItemClickListener clickListener) {
        mItemOnClickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mItemView;
        TextView mItemNameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mItemNameTv = (TextView) itemView.findViewById(R.id.tvItemName);
        }

        public void bind(final String itemName, final OnItemClickListener clickListener) {
            mItemNameTv.setText(itemName);
            if (clickListener != null) {
                mItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onClick(view, getAdapterPosition(), itemName);
                    }
                });
            }
        }
    }

    /**
     * Interface definition for a callback to be invoked when an item in the RecyclerView has been clicked.
     */
    public interface OnItemClickListener {
        void onClick(View view, int position, String itemName);
    }
}

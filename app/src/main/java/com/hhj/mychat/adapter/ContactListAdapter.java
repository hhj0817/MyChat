package com.hhj.mychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hhj.mychat.model.ContactItem;
import com.hhj.mychat.widget.ContactItemView;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactListItemViewHolder> {
    public static final String TAG = "ContactListAdapter";

    private Context mContext;
    private List<ContactItem> mContactItems;

    private OnItemClickListener mOnItemClickListener;

    public ContactListAdapter(Context context, List<ContactItem> items) {
        mContext = context;
        mContactItems = items;
    }

    /**
     * 创建viewholder
     * @param parent
     * @param viewType item的类型
     * @return
     */
    @Override
    public ContactListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactListItemViewHolder(new ContactItemView(mContext));
    }

    /**
     * 根据position对应位置数据去刷新holder hold住的view
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ContactListItemViewHolder holder, final int position) {
        holder.mContactItemView.bindView(mContactItems.get(position));
        //设置点击和长按事件
        holder.mContactItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(mContactItems.get(position).getUserName());
                }
            }
        });

        holder.mContactItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onLongClick(mContactItems.get(position).getUserName());
                }
                return false;
            }
        });
    }

    /**
     * 返回item个数
     * @return
     */
    @Override
    public int getItemCount() {
        return mContactItems.size();
    }

    public class ContactListItemViewHolder extends RecyclerView.ViewHolder {

        private ContactItemView mContactItemView;

        public ContactListItemViewHolder(ContactItemView itemView) {
            super(itemView);
            mContactItemView = itemView;
        }
    }

    public interface OnItemClickListener {

        void onClick(String userName);

        void onLongClick(String userName);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

}

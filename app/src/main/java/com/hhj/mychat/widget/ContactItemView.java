package com.hhj.mychat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhj.mychat.R;
import com.hhj.mychat.model.ContactItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactItemView extends RelativeLayout {
    public static final String TAG = "ContactItemView";
    @BindView(R.id.first_letter)
    TextView mFirstLetter;
    @BindView(R.id.user_name)
    TextView mUserName;

    public ContactItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public ContactItemView(Context context) {
        this(context, null);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_contact_list_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(ContactItem contactItem) {
        mUserName.setText(contactItem.getUserName());
        if (contactItem.showFirstLetter) {
            mFirstLetter.setVisibility(VISIBLE);
            mFirstLetter.setText(contactItem.getFirstLetter());
        } else {
            mFirstLetter.setVisibility(GONE);
        }
    }
}

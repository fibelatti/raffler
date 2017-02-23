package com.fibelatti.raffler.presentation.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.GroupItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteAdapter
        extends RecyclerView.Adapter<VoteAdapter.GroupItemViewHolder> {

    private Context context;
    private List<GroupItem> groupItems;

    public class GroupItemViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.radio_button)
        public RadioButton radioButton;
        @BindView(R.id.name)
        public TextView itemName;

        public GroupItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public VoteAdapter(Context context) {
        this.context = context;
        this.groupItems = new ArrayList<>();
    }

    public void setGroupItems(List<GroupItem> groupItems) {
        this.groupItems.clear();
        this.groupItems.addAll(groupItems);

        notifyDataSetChanged();
    }

    private Context getContext() {
        return context;
    }

    @Override
    public GroupItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_vote, parent, false);

        return new GroupItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupItemViewHolder holder, int position) {
        GroupItem groupItem = groupItems.get(position);

        holder.itemName.setText(groupItem.getName());
        holder.radioButton.setSelected(groupItem.getSelected());
    }

    @Override
    public int getItemCount() {
        return groupItems.size();
    }
}

package com.fibelatti.raffler.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.GroupItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupAdapter
        extends RecyclerView.Adapter<GroupAdapter.GroupItemViewHolder> {

    private List<GroupItem> groupItems;

    public class GroupItemViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        public TextView itemName;
        @BindView(R.id.chk_selected)
        public CheckBox isSelected;

        public GroupItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public GroupAdapter() {
        this.groupItems = new ArrayList<>();
    }

    public void setGroupItems(List<GroupItem> groupItems) {
        this.groupItems.clear();
        this.groupItems.addAll(groupItems);

        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public GroupItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_group_item, parent, false);

        return new GroupItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupItemViewHolder holder, int position) {
        GroupItem groupItem = groupItems.get(position);

        holder.itemName.setText(groupItem.getName());
        holder.isSelected.setChecked(groupItem.getSelected());
    }

    @Override
    public int getItemCount() {
        return groupItems.size();
    }
}

package com.fibelatti.raffler.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.GroupItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupAdapter
        extends RecyclerView.Adapter<GroupAdapter.GroupItemViewHolder> {

    private Context context;
    private List<GroupItem> groupItems;
    private Set<GroupItem> selectedItems = new HashSet<>();

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

    public GroupAdapter(Context context, List<GroupItem> groupItems) {
        this.context = context;
        this.groupItems = groupItems;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public GroupItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_group_item, parent, false);

        return new GroupItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupItemViewHolder holder, int position) {
        GroupItem groupItem = groupItems.get(position);

        holder.itemName.setText(groupItem.getName());
        holder.isSelected.setChecked(selectedItems.contains(groupItem));
    }

    @Override
    public int getItemCount() {
        return groupItems.size();
    }

    public void toggleSelected(int position) {
        GroupItem item = groupItems.get(position);
        boolean isChecked = getSelectedItems().contains(item);

        if (isChecked) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }

        notifyDataSetChanged();
    }

    public void clearSelectedItems() {
        selectedItems.clear();
    }

    public Set<GroupItem> getSelectedItems() {
        return selectedItems;
    }

    public int getSelectedItemsCount() {
        return selectedItems.size();
    }

    public void refreshSelectedItems() {
        selectedItems.clear();
        selectedItems.addAll(groupItems);
    }

    public void checkAllItems() {
        selectedItems.addAll(groupItems);
        notifyDataSetChanged();
    }

    public void uncheckAllItems() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void deleteCheckedItems() {
        for (GroupItem groupItem : selectedItems) {
            groupItems.remove(groupItem);
        }
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void deleteAllItems() {
        groupItems.clear();
        selectedItems.clear();
        notifyDataSetChanged();
    }
}

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
import com.fibelatti.raffler.views.extensions.RecyclerTouchListener;

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

        holder.setGroupItem(groupItem);
        holder.setIsSelected(selectedItems.contains(groupItem));
    }

    @Override
    public int getItemCount() {
        return groupItems.size();
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

    public class GroupItemViewHolder
            extends RecyclerView.ViewHolder
            implements RecyclerTouchListener.OnItemClickListener {
        @BindView(R.id.name)
        public TextView itemName;
        @BindView(R.id.chk_selected)
        public CheckBox isSelected;

        private GroupItem groupItem;

        public GroupItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setGroupItem(GroupItem groupItem) {
            this.groupItem = groupItem;
            itemName.setText(groupItem.getName());
        }

        public void setIsSelected(boolean checked) {
            isSelected.setChecked(checked);
        }

        @Override
        public void onItemTouch(View view, int position) {
            GroupItem item = groupItems.get(position);
            boolean isChecked = getSelectedItems().contains(item);

            if (isChecked) {
                selectedItems.add(item);
            } else {
                selectedItems.remove(item);
            }

            notifyItemChanged(position);
        }
    }
}

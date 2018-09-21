package com.fibelatti.raffler.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter
        extends RecyclerView.Adapter<MainAdapter.GroupViewHolder> {

    private List<Group> groupList, filterList;

    public class GroupViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        public TextView name;
        @BindView(R.id.item_count)
        public TextView itemCount;

        public GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public MainAdapter() {
        this.groupList = new ArrayList<>();
        this.filterList = new ArrayList<>();
    }

    public void setGroups(List<Group> groupList) {
        this.groupList.clear();
        this.groupList.addAll(groupList);

        this.filterList.clear();
        this.filterList.addAll(groupList);

        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_group, parent, false);

        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = filterList.get(position);

        holder.name.setText(group.getName());
        holder.itemCount.setText(holder.itemView.getResources().getQuantityString(R.plurals.main_hint_group_items_quantity,
                group.getItemsCount(), group.getItemsCount()));
    }

    @Override
    public int getItemCount() {
        return (filterList != null ? filterList.size() : 0);
    }

    public void filter(final String text) {
        filterList.clear();
        if (TextUtils.isEmpty(text)) {
            filterList.addAll(groupList);
        } else {
            for (Group item : groupList) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    filterList.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }
}

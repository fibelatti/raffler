package com.fibelatti.raffler.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class CombinationGroupSelectionAdapter
        extends RecyclerView.Adapter<CombinationGroupSelectionAdapter.GroupViewHolder> {

    private List<Group> groupList;

    public class GroupViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        public TextView name;

        public GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public CombinationGroupSelectionAdapter() {
        this.groupList = new ArrayList<>();
    }

    public void setGroups(List<Group> groupList) {
        this.groupList.clear();
        this.groupList.addAll(groupList);

        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_combination_group, parent, false);

        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupList.get(position);

        holder.name.setText(group.getName());
    }

    @Override
    public int getItemCount() {
        return (groupList != null ? groupList.size() : 0);
    }
}
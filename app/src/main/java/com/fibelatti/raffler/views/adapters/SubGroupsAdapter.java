package com.fibelatti.raffler.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubGroupsAdapter
        extends RecyclerView.Adapter<SubGroupsAdapter.GroupViewHolder> {

    private List<Group> subgroupsList;

    public class GroupViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        public TextView name;
        @BindView(R.id.items)
        public TextView items;

        public GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public SubGroupsAdapter(List<Group> subgroupsList) {
        this.subgroupsList = subgroupsList;
    }

    @Override
    @NonNull
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_sub_groups, parent, false);

        return new GroupViewHolder(itemView);
    }

    @Override
    @NonNull
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        Group group = subgroupsList.get(position);
        StringBuilder sb = new StringBuilder();

        for (GroupItem item : group.getItems()) {
            if (!sb.toString().isEmpty()) sb.append("\n");
            sb.append(item.getName());
        }

        holder.name.setText(group.getName());
        holder.items.setText(sb.toString());
    }

    @Override
    public int getItemCount() {
        return subgroupsList.size();
    }
}

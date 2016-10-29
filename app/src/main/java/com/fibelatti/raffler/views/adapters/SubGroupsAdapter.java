package com.fibelatti.raffler.views.adapters;

import android.content.Context;
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

    private Context context;
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

    public SubGroupsAdapter(Context context, List<Group> subgroupsList) {
        this.context = context;
        this.subgroupsList = subgroupsList;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_sub_groups, parent, false);

        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        Group group = subgroupsList.get(position);
        StringBuilder sb = new StringBuilder();

        for (GroupItem item : group.getItems()) {
            sb.append("\n");
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

package com.fibelatti.raffler.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.GroupViewHolder> {

    private Context context;
    private List<Group> groupList;

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        public TextView name;
        @BindView(R.id.item_count)
        public TextView itemCount;

        public GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public MainAdapter(Context context, List<Group> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_group, parent, false);

        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        Group group = groupList.get(position);

        holder.name.setText(group.getName());
        holder.itemCount.setText(getContext().getResources().getQuantityString(R.plurals.hint_group_items_quantity,
                group.getItemCount(), group.getItemCount()));
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}

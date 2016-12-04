package com.fibelatti.raffler.views.adapters;

import android.app.Activity;
import android.content.Context;
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

    private Context context;
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

    public MainAdapter(Context context) {
        this.context = context;
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
        Group group = filterList.get(position);

        holder.name.setText(group.getName());
        holder.itemCount.setText(getContext().getResources().getQuantityString(R.plurals.main_hint_group_items_quantity,
                group.getItemsCount(), group.getItemsCount()));
    }

    @Override
    public int getItemCount() {
        return (filterList != null ? filterList.size() : 0);
    }

    public void filter(final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }
}

package com.fibelatti.raffler.presentation.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fibelatti.raffler.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomWinnersAdapter
        extends RecyclerView.Adapter<RandomWinnersAdapter.GroupViewHolder> {

    private Context context;
    private List<String> winnersList;

    public class GroupViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.number)
        public TextView number;
        @BindView(R.id.name)
        public TextView name;

        public GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public RandomWinnersAdapter(Context context, List<String> winnersList) {
        this.context = context;
        this.winnersList = winnersList;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_random_winners, parent, false);

        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        String winner = winnersList.get(position);

        holder.number.setText(context.getString(R.string.random_winners_msg_result, position + 1));
        holder.name.setText(winner);
    }

    @Override
    public int getItemCount() {
        return winnersList.size();
    }
}

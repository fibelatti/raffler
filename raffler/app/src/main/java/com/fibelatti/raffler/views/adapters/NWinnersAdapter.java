package com.fibelatti.raffler.views.adapters;

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

public class NWinnersAdapter extends RecyclerView.Adapter<NWinnersAdapter.GroupViewHolder> {

    private Context context;
    private List<String> winnersList;

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        public TextView name;

        public GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public NWinnersAdapter(Context context, List<String> winnersList) {
        this.context = context;
        this.winnersList = winnersList;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_n_winners, parent, false);

        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        String winner = winnersList.get(position);

        holder.name.setText(context.getString(R.string.nwinners_msg_result, winner));
    }

    @Override
    public int getItemCount() {
        return winnersList.size();
    }
}

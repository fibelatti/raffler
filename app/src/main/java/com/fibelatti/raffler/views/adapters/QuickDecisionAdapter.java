package com.fibelatti.raffler.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.QuickDecision;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuickDecisionAdapter
        extends RecyclerView.Adapter<QuickDecisionAdapter.QuickDecisionViewHolder> {

    private List<QuickDecision> quickDecisions;

    public class QuickDecisionViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.button_quick_decision)
        public Button quickDecisionButton;

        public QuickDecisionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public QuickDecisionAdapter() {
        this.quickDecisions = new ArrayList<>();
    }

    public void setQuickDecisions(List<QuickDecision> quickDecisions) {
        this.quickDecisions.clear();
        this.quickDecisions.addAll(quickDecisions);

        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public QuickDecisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_quick_decision, parent, false);

        return new QuickDecisionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuickDecisionViewHolder holder, int position) {
        int positionInList = position % quickDecisions.size();

        QuickDecision quickDecision = quickDecisions.get(positionInList);

        holder.quickDecisionButton.setText(quickDecision.getName());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}

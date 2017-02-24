package com.fibelatti.raffler.presentation.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.QuickDecision;
import com.fibelatti.raffler.presentation.presenters.MainPresenterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuickDecisionAdapter
        extends RecyclerView.Adapter<QuickDecisionAdapter.QuickDecisionViewHolder> {

    private Context context;
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

    public QuickDecisionAdapter(Context context) {
        this.context = context;
        this.quickDecisions = new ArrayList<>();
    }

    public void setQuickDecisions(List<QuickDecision> quickDecisions) {
        this.quickDecisions.clear();
        this.quickDecisions.addAll(quickDecisions);

        notifyDataSetChanged();

        ((MainPresenterView) context).snapQuickDecisions();
    }

    private Context getContext() {
        return context;
    }

    @Override
    public QuickDecisionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_quick_decision, parent, false);

        return new QuickDecisionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuickDecisionViewHolder holder, int position) {
        int positionInList = position % quickDecisions.size();

        QuickDecision quickDecision = quickDecisions.get(positionInList);

        holder.quickDecisionButton.setText(quickDecision.getName());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public void handleItemClick(int position) {
        int positionInList = position % quickDecisions.size();
        ((MainPresenterView) context).goToQuickDecision(quickDecisions.get(positionInList));
    }
}

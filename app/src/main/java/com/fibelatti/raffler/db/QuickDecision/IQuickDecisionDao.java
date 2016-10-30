package com.fibelatti.raffler.db.QuickDecision;

import com.fibelatti.raffler.models.QuickDecision;

import java.util.List;

public interface IQuickDecisionDao {
    QuickDecision fetchQuickDecisionById(long quickDecisionId);

    List<QuickDecision> getQuickDecisions();

    List<QuickDecision> getEnabledQuickDecisions();

    boolean toggleQuickDecisionEnabled(QuickDecision quickDecision);
}

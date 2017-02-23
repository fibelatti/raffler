package com.fibelatti.raffler.db.QuickDecision;

import com.fibelatti.raffler.models.QuickDecision;

import java.util.List;

public interface QuickDecisionDao {
    QuickDecision fetchQuickDecisionById(long quickDecisionId);

    List<QuickDecision> fetchAllQuickDecisions();
}

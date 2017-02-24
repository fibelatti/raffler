package com.fibelatti.raffler.presentation.presenters.impl;

import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.QuickDecision;
import com.fibelatti.raffler.presentation.presenters.MainPresenter;
import com.fibelatti.raffler.presentation.presenters.MainPresenterView;

import java.util.List;

public class MainPresenterImpl
        implements MainPresenter {

    private MainPresenterView view;

    private MainPresenterImpl(MainPresenterView view) {
        this.view = view;
    }

    public static MainPresenterImpl createPresenter(MainPresenterView view) {
        return new MainPresenterImpl(view);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean isUserLoggedIn() {
        return false;
    }

    @Override
    public List<Group> getGroups() {
        return Database.groupDao.fetchAllGroups();
    }

    @Override
    public List<QuickDecision> getQuickDecisions() {
        return Database.quickDecisionDao.fetchAllQuickDecisions();
    }
}

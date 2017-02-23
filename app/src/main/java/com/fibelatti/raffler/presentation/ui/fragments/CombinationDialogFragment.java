package com.fibelatti.raffler.presentation.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.db.Database;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;
import com.fibelatti.raffler.presentation.ui.adapters.CombinationGroupSelectionAdapter;
import com.fibelatti.raffler.presentation.ui.extensions.DividerItemDecoration;
import com.fibelatti.raffler.presentation.ui.extensions.RecyclerTouchListener;
import com.fibelatti.raffler.presentation.ui.listeners.CombinationListener;
import com.fibelatti.raffler.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CombinationDialogFragment
        extends DialogFragment {
    public static final String TAG = CombinationDialogFragment.class.getSimpleName();

    private static final String COMBINATION_TYPE = "Combination Type";
    private static final int COMBINATION_TYPE_GROUP = 0;
    private static final int COMBINATION_TYPE_LIST = 1;
    private static final String COMBINATION_INSTANCE_SELECTED_TAB = "Selected Tab";
    private static final String COMBINATION_INSTANCE_TYPED_ITEMS = "Typed Items";

    private Context context;
    private CombinationListener listener;
    private CombinationGroupSelectionAdapter adapter;
    private List<Group> groupList;

    //region layout bindings
    @BindView(R.id.tab_host)
    TabHost tabHost;
    @BindView(R.id.recycler_view_existing_group)
    RecyclerView recyclerView;
    @BindView(R.id.input_layout_items)
    TextInputLayout inputItemsLayout;
    @BindView(R.id.input_items)
    EditText inputItems;
    @BindView(R.id.button_finish_typing)
    Button buttonFinish;
    //endregion

    public CombinationDialogFragment() {
    }

    public static CombinationDialogFragment newInstanceWithGroup(Group group) {
        return setUpBundle(COMBINATION_TYPE_GROUP, group);
    }

    public static CombinationDialogFragment newInstanceWithList(Group group) {
        return setUpBundle(COMBINATION_TYPE_LIST, group);
    }

    private static CombinationDialogFragment setUpBundle(int type, Group group) {
        CombinationDialogFragment f = new CombinationDialogFragment();

        Bundle args = new Bundle();
        args.putInt(COMBINATION_TYPE, type);
        args.putParcelable(Constants.INTENT_EXTRA_GROUP, group);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.context = getActivity();
        this.adapter = new CombinationGroupSelectionAdapter(context);
        this.groupList = new ArrayList<>();

        View view = View.inflate(getContext(), R.layout.dialog_combination_selection, null);
        ButterKnife.bind(this, view);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.combination_dialog_title))
                .setNegativeButton(R.string.group_form_dialog_hint_cancel, null)
                .create();

        dialog.setOnShowListener(dialog1 -> {
            Button buttonNegative = ((AlertDialog) dialog1).getButton(DialogInterface.BUTTON_NEGATIVE);
            if (buttonNegative != null)
                buttonNegative.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
        });

        setUpTabHost();
        setUpRecyclerView();
        fetchData();

        if (savedInstanceState != null) {
            tabHost.setCurrentTab(savedInstanceState.getInt(COMBINATION_INSTANCE_SELECTED_TAB));
            inputItems.setText(savedInstanceState.getString(COMBINATION_INSTANCE_TYPED_ITEMS));
        } else {
            tabHost.setCurrentTab(getArguments().getInt(COMBINATION_TYPE));
            if (getArguments().getInt(COMBINATION_TYPE) == COMBINATION_TYPE_LIST) {
                inputItems.setText(TextUtils.join(", ", ((Group) getArguments().getParcelable(Constants.INTENT_EXTRA_GROUP)).getItemNames()));
            }
        }

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CombinationListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COMBINATION_INSTANCE_SELECTED_TAB, tabHost.getCurrentTab());
        outState.putString(COMBINATION_INSTANCE_TYPED_ITEMS, inputItems.getText().toString());
    }

    private void setUpTabHost() {
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec specOne = tabHost.newTabSpec(getString(R.string.combination_dialog_tab_one_title));
        specOne.setContent(R.id.tab_layout_one);
        specOne.setIndicator(getString(R.string.combination_dialog_tab_one_title));
        tabHost.addTab(specOne);

        //Tab 2
        TabHost.TabSpec specTwo = tabHost.newTabSpec(getString(R.string.combination_dialog_tab_two_title));
        specTwo.setContent(R.id.tab_layout_two);
        specTwo.setIndicator(getString(R.string.combination_dialog_tab_two_title));
        tabHost.addTab(specTwo);
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener.Builder(context)
                .setOnItemTouchListener((view, position) -> {
                    listener.selectGroupCallback(groupList.get(position));
                    dismiss();
                })
                .build());
    }

    private void fetchData() {
        if (!groupList.isEmpty()) groupList.clear();
        groupList.addAll(Database.groupDao.fetchAllGroups());
        adapter.setGroups(groupList);
    }

    private boolean validateForm() {
        return validateInput();
    }

    private boolean validateInput() {
        if (StringUtils.isNullOrEmpty(inputItems.getText().toString())) {
            inputItemsLayout.setError(getString(R.string.combination_dialog_form_msg_validate_input));
            requestFocus(inputItems);
            return false;
        } else {
            inputItemsLayout.setError(null);
            inputItemsLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @OnClick(R.id.button_finish_typing)
    public void finishTypingList() {
        if (validateForm()) {
            List<String> items = Arrays.asList(inputItems.getText().toString().split("\\s*,\\s*"));
            Group group = new Group.Builder().build();

            for (String name : items) {
                group.addItem(new GroupItem.Builder().setName(name).build());
            }

            listener.selectGroupCallback(group);
            dismiss();
        }
    }
}

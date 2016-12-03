package com.fibelatti.raffler.views.fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.R;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.itangqi.waveloadingview.WaveLoadingView;

public class SecretVotingResultsFragment
        extends Fragment {
    public static final String TAG = SecretVotingResultsFragment.class.getSimpleName();

    private Resources resources;
    private Group group;
    private LinkedHashMap<GroupItem, Integer> votesMap;
    private int totalVotes;

    //region layout bindings
    @BindView(R.id.tv_header_text)
    TextView headerText;

    @BindView(R.id.cv_first_place)
    CardView cardFirstPlace;
    @BindView(R.id.divider_first_place)
    View dividerFirstPlace;
    @BindView(R.id.tv_first_place_header)
    TextView textFirstPlaceHeader;
    @BindView(R.id.graph_view_first_place)
    WaveLoadingView graphFirstPlace;
    @BindView(R.id.tv_first_place)
    TextView textFirstPlace;

    @BindView(R.id.cv_second_place)
    CardView cardSecondPlace;
    @BindView(R.id.divider_second_place)
    View dividerSecondPlace;
    @BindView(R.id.tv_second_place_header)
    TextView textSecondPlaceHeader;
    @BindView(R.id.graph_view_second_place)
    WaveLoadingView graphSecondPlace;
    @BindView(R.id.tv_second_place)
    TextView textSecondPlace;

    @BindView(R.id.cv_third_place)
    CardView cardThirdPlace;
    @BindView(R.id.divider_third_place)
    View dividerThirdPlace;
    @BindView(R.id.tv_third_place_header)
    TextView textThirdPlaceHeader;
    @BindView(R.id.graph_view_third_place)
    WaveLoadingView graphThirdPlace;
    @BindView(R.id.tv_third_place)
    TextView textThirdPlace;
    //endregion

    public SecretVotingResultsFragment() {
    }

    public static SecretVotingResultsFragment newInstance(Group group, LinkedHashMap<GroupItem, Integer> votesMap) {
        SecretVotingResultsFragment f = new SecretVotingResultsFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.INTENT_EXTRA_GROUP, group);
        args.putSerializable(Constants.INTENT_EXTRA_VOTES_MAP, votesMap);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_secret_voting_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        resources = getResources();

        if (savedInstanceState != null) {
            this.group = savedInstanceState.getParcelable(Constants.INTENT_EXTRA_GROUP);
            this.votesMap = (LinkedHashMap<GroupItem, Integer>) savedInstanceState.getSerializable(Constants.INTENT_EXTRA_VOTES_MAP);
        } else {
            this.group = getArguments().getParcelable(Constants.INTENT_EXTRA_GROUP);
            this.votesMap = (LinkedHashMap<GroupItem, Integer>) getArguments().getSerializable(Constants.INTENT_EXTRA_VOTES_MAP);
        }

        this.votesMap = sortByValue(votesMap, true);
        setUpData();

        setUpLayout();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteOpaque));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.INTENT_EXTRA_GROUP, group);
        outState.putSerializable(Constants.INTENT_EXTRA_VOTES_MAP, votesMap);
    }

    @OnClick(R.id.button_back)
    public void navigateBack() {
        getActivity().finish();
    }

    private void setUpLayout() {
        final boolean isThirdPlaceVisible = cardThirdPlace.getVisibility() == View.VISIBLE;

        ViewTreeObserver firstObserver = cardFirstPlace.getViewTreeObserver();
        firstObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) graphFirstPlace.getLayoutParams();
                int measure = Math.min(
                        cardFirstPlace.getHeight()
                                - getResources().getDimensionPixelSize(R.dimen.padding_small) * 2
                                - getResources().getDimensionPixelSize(R.dimen.margin_small)
                                - textFirstPlaceHeader.getHeight()
                                - dividerFirstPlace.getHeight() - getResources().getDimensionPixelSize(R.dimen.margin_small)
                                - textFirstPlace.getHeight() - getResources().getDimensionPixelSize(R.dimen.margin_regular),
                        cardFirstPlace.getWidth()
                                - getResources().getDimensionPixelSize(R.dimen.padding_small) * 2
                                - getResources().getDimensionPixelSize(R.dimen.margin_small) * 2);

                params.height = measure;
                params.width = measure;

                graphFirstPlace.setLayoutParams(params);

                cardFirstPlace.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        ViewTreeObserver secondObserver = cardSecondPlace.getViewTreeObserver();
        secondObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) graphSecondPlace.getLayoutParams();
                int measure = Math.min(
                        cardSecondPlace.getHeight()
                                - getResources().getDimensionPixelSize(R.dimen.padding_small) * 2
                                - getResources().getDimensionPixelSize(R.dimen.margin_small)
                                - textSecondPlaceHeader.getHeight()
                                - dividerSecondPlace.getHeight() - getResources().getDimensionPixelSize(R.dimen.margin_small)
                                - textSecondPlace.getHeight() - getResources().getDimensionPixelSize(R.dimen.margin_regular),
                        cardSecondPlace.getWidth()
                                - getResources().getDimensionPixelSize(R.dimen.padding_small) * 2
                                - getResources().getDimensionPixelSize(R.dimen.margin_small) * 2
                                - (isThirdPlaceVisible ? getResources().getDimensionPixelSize(R.dimen.margin_small) / 2 : 0));

                params.height = measure;
                params.width = measure;

                graphSecondPlace.setLayoutParams(params);

                cardSecondPlace.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        ViewTreeObserver thirdObserver = cardThirdPlace.getViewTreeObserver();
        thirdObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) graphThirdPlace.getLayoutParams();
                int measure = Math.min(
                        cardThirdPlace.getHeight()
                                - getResources().getDimensionPixelSize(R.dimen.padding_small) * 2
                                - getResources().getDimensionPixelSize(R.dimen.margin_small)
                                - textThirdPlaceHeader.getHeight()
                                - dividerThirdPlace.getHeight() - getResources().getDimensionPixelSize(R.dimen.margin_small)
                                - textThirdPlace.getHeight() - getResources().getDimensionPixelSize(R.dimen.margin_regular),
                        cardThirdPlace.getWidth()
                                - getResources().getDimensionPixelSize(R.dimen.padding_small) * 2
                                - getResources().getDimensionPixelSize(R.dimen.margin_small) * 2
                                - getResources().getDimensionPixelSize(R.dimen.margin_small) / 2);

                params.height = measure;
                params.width = measure;

                graphThirdPlace.setLayoutParams(params);

                cardThirdPlace.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void setUpData() {
        if (votesMap != null) {
            for (int current : votesMap.values()) {
                totalVotes += current;
            }

            this.headerText.setText(getString(R.string.secret_voting_results_header_text, group.getName()));

            int quantity;
            int percent;
            GroupItem item;

            // First place
            quantity = (new ArrayList<>(votesMap.values())).get(0);

            percent = (int) ((quantity / (float) totalVotes) * 100);
            item = (new ArrayList<>(votesMap.keySet())).get(0);

            graphFirstPlace.setProgressValue(percent);
            graphFirstPlace.setCenterTitle(getString(R.string.secret_voting_results_graph_text_percent, percent));
            graphFirstPlace.setBottomTitle(resources.getQuantityString(R.plurals.secret_voting_results_graph_text_votes, quantity, quantity));
            textFirstPlace.setText(item.getName());

            // Second place
            quantity = (new ArrayList<>(votesMap.values())).get(1);

            if (quantity == 0) {
                PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) cardFirstPlace.getLayoutParams();
                PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
                info.heightPercent = 1.0f;
                cardFirstPlace.requestLayout();

                cardSecondPlace.setVisibility(View.GONE);
                cardThirdPlace.setVisibility(View.GONE);
            } else {
                percent = (int) ((quantity / (float) totalVotes) * 100);
                item = (new ArrayList<>(votesMap.keySet())).get(1);

                graphSecondPlace.setProgressValue(percent);
                graphSecondPlace.setCenterTitle(getString(R.string.secret_voting_results_graph_text_percent, percent));
                graphSecondPlace.setBottomTitle(resources.getQuantityString(R.plurals.secret_voting_results_graph_text_votes, quantity, quantity));
                textSecondPlace.setText(item.getName());

                // Third place
                quantity = (new ArrayList<>(votesMap.values())).get(2);

                if (quantity == 0) {
                    PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) cardSecondPlace.getLayoutParams();
                    PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
                    info.widthPercent = 1.0f;
                    cardSecondPlace.requestLayout();

                    cardThirdPlace.setVisibility(View.GONE);
                } else {
                    percent = (int) ((quantity / (float) totalVotes) * 100);
                    item = (new ArrayList<>(votesMap.keySet())).get(2);

                    graphThirdPlace.setProgressValue(percent);
                    graphThirdPlace.setCenterTitle(getString(R.string.secret_voting_results_graph_text_percent, percent));
                    graphThirdPlace.setBottomTitle(resources.getQuantityString(R.plurals.secret_voting_results_graph_text_votes, quantity, quantity));
                    textThirdPlace.setText(item.getName());
                }
            }
        }
    }

    private <K, V extends Comparable<? super V>> LinkedHashMap<K, V> sortByValue(Map<K, V> map, boolean reverse) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        if (reverse) Collections.reverse(list);

        LinkedHashMap<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /***
     * This code is not used anymore but it is useful, so it will be kept here.
     *
     * @param startColor
     * @param endColor
     * @param steps
     * @return
     */
    private List<Integer> calculateColorGradient(int startColor, int endColor, int steps) {
        List<Integer> colors = new ArrayList<>();

        int r1 = Color.red(startColor);
        int g1 = Color.green(startColor);
        int b1 = Color.blue(startColor);

        int r2 = Color.red(endColor);
        int g2 = Color.green(endColor);
        int b2 = Color.blue(endColor);

        int redStep = (r2 - r1) / steps;
        int greenStep = (g2 - g1) / steps;
        int blueStep = (b2 - b1) / steps;

        for (int i = 0; i <= steps; i++)
            colors.add(Color.rgb(r1 + redStep * i, g1 + greenStep * i, b1 + blueStep * i));

        return colors;
    }
}

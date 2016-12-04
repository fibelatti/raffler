package com.fibelatti.raffler.views.extensions;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fibelatti.raffler.R;

/***
 * This code is based on the amazing tutorial provided by Ali Muzaffar at
 * https://medium.com/@ali.muzaffar/building-a-pinentryedittext-in-android-5f2eddcae5d3#.52anajwgm
 */
public class PinEntryEditText
        extends EditText {
    private static final String XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";

    private float space = 24; //24 dp by default
    private float lineSpacing = 8; //8dp by default
    private float charSize = 0;
    private float numChars = 4;
    private int maxLength = 4;

    private float lineStroke = 1; //1dp by default
    private Paint linesPaint;
    int[][] states = new int[][]{
            new int[]{android.R.attr.state_selected}, // selected
            new int[]{android.R.attr.state_focused}, // focused
            new int[]{-android.R.attr.state_focused}, // unfocused
    };
    int[] colors = new int[]{
            R.color.colorAccent,
            R.color.colorGrayDark,
            R.color.colorGray
    };

    ColorStateList mColorStates = new ColorStateList(states, colors);

    private OnClickListener clickListener;

    public PinEntryEditText(Context context) {
        super(context);
    }

    public PinEntryEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PinEntryEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PinEntryEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setBackgroundResource(0);

        float multi = context.getResources().getDisplayMetrics().density;
        space = multi * space; //convert to pixels for our density
        lineSpacing = multi * lineSpacing; //convert to pixels
        maxLength = attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", maxLength);
        numChars = maxLength;

        lineStroke = multi * lineStroke;
        linesPaint = new Paint(getPaint());
        linesPaint.setStrokeWidth(lineStroke);

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorControlActivated, outValue, true);
        final int colorActivated = outValue.data;
        colors[0] = colorActivated;

        context.getTheme().resolveAttribute(R.attr.colorPrimary, outValue, true);
        final int colorPrimary = outValue.data;
        colors[1] = colorPrimary;

        context.getTheme().resolveAttribute(R.attr.colorControlHighlight, outValue, true);
        final int colorHighlight = outValue.data;
        colors[2] = colorHighlight;

        //Disable copy paste
        super.setCustomSelectionActionModeCallback(
                new ActionMode.Callback() {
                    public boolean onPrepareActionMode(ActionMode mode,
                                                       Menu menu) {
                        return false;
                    }

                    public void onDestroyActionMode(ActionMode mode) {
                    }

                    public boolean onCreateActionMode(ActionMode mode,
                                                      Menu menu) {
                        return false;
                    }

                    public boolean onActionItemClicked(ActionMode mode,
                                                       MenuItem item) {
                        return false;
                    }
                });

        //When tapped, move cursor to end of the text
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(getText().length());
                if (clickListener != null) clickListener.onClick(v);
            }
        });
    }

    private int getColorForState(int... states) {
        return mColorStates.getColorForState(states, R.color.colorGray);
    }

    /* next = is the current char the next character to be input? */
    private void updateColorForLines(boolean next) {
        if (isFocused()) {
            linesPaint.setColor(getColorForState(android.R.attr.state_focused));

            if (next) linesPaint.setColor(getColorForState(android.R.attr.state_selected));
        } else {
            linesPaint.setColor(getColorForState(-android.R.attr.state_focused));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        CharSequence text = getText();
        int textLength = text.length();
        float[] textWidths = new float[textLength];
        int availableWidth = getWidth() - getPaddingRight() - getPaddingLeft();
        getPaint().getTextWidths(getText(), 0, textLength, textWidths);

        if (space < 0) {
            charSize = (availableWidth / (numChars * 2 - 1));
        } else {
            charSize = (availableWidth - (space * (numChars - 1))) / numChars;
        }

        int startX = getPaddingLeft();
        int bottom = getHeight() - getPaddingBottom();

        for (int i = 0; i < numChars; i++) {
            updateColorForLines(i == textLength);
            canvas.drawLine(startX, bottom, startX + charSize, bottom, linesPaint);

            if (getText().length() > i) {
                float middle = startX + charSize / 2;
                canvas.drawText(text,
                        i,
                        i + 1,
                        middle - textWidths[0] / 2,
                        bottom - lineSpacing,
                        getPaint());
            }

            if (space < 0) {
                startX += charSize * 2;
            } else {
                startX += charSize + space;
            }
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        clickListener = l;
    }

    @Override
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        throw new RuntimeException("setCustomSelectionActionModeCallback() not supported.");
    }
}
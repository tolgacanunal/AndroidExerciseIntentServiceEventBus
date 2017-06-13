package hipo.com.customviewexercise.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hipo.com.customviewexercise.R;

/**
 * Created by Tolga Can "tesleax" Ünal on 12/06/17
 */
public class AnswerView extends LinearLayout {
    @BindView(R.id.tv_selection_letter) TextView tvSelectionLetter;
    @BindView(R.id.tv_selection_description) TextView tvSelectionDescription;


    public AnswerView(Context context) {
        super(context);
        init(context,null, -1);
    }

    public AnswerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AnswerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, -1);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        View root = LayoutInflater.from(context).inflate(R.layout.answer_layout, this, true);
        ButterKnife.bind(this, root);
        setOrientation(HORIZONTAL);
        tvSelectionDescription.setClickable(true);
        tvSelectionDescription.setFocusable(true);
        tvSelectionLetter.setClickable(true);
        tvSelectionLetter.setFocusable(true);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AnswerView, 0 , 0);
        try {
            int selection_letter = a.getInt(R.styleable.AnswerView_selection, 0);
            String letter = "";
            switch (selection_letter) {
                case 0:
                    letter = "A";
                    break;
                case 1:
                    letter = "B";
                    break;
                case 2:
                    letter = "C";
                    break;
                case 3:
                    letter = "D";
                    break;
            }
            String description = "Description of the selection " + letter;
            tvSelectionDescription.setText(description);
            letter += " )";
            tvSelectionLetter.setText(letter);
        } finally {
            a.recycle();
        }
    }
}


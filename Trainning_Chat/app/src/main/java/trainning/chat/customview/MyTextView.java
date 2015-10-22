package trainning.chat.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by NGUYEN on 9/24/2015.
 */
public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int with = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (with > height) {
            with = height;
        } else {
            height = with;
        }
        setMeasuredDimension(with, height);


    }
}

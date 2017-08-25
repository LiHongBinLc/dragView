package drag.com.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017/8/24.
 */

public class DragView extends Button {
    private Boolean falg = false;
    private int mScreenWidth;
    private int mScreenHeight;
    private Context context;
    private final int radius1 = 100;
    private List<View> mData = new ArrayList<>();
    private int left;
    private int top;
    private int right;
    private int bottom;
    private ShowPopup showPopup;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //获取屏幕宽高，用于控制控件在屏幕内移动
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels - 100;//这里减去的100是下边的back键和menu键那一栏的高度，看情况而定
        showPopup = new ShowPopup(context);
    }

    int startDownX = 0;
    int startDownY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int lastMoveX = 0;
        int lastMoveY = 0;
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                falg = true;
                startDownX = (int) event.getRawX();
                startDownY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (showPopup.isShowing()) {
                    showPopup.dissPopup();
                }
                lastMoveX = (int) event.getRawX();
                lastMoveY = (int) event.getRawY();

                int dx = lastMoveX - startDownX;
                int dy = lastMoveY - startDownY;
                if (Math.abs(dx) > 2 || Math.abs(dy) > 2) {
                    falg = false;
                }

                left = getLeft() + dx;
                top = getTop() + dy;
                right = getRight() + dx;
                bottom = getBottom() + dy;
                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }
                if (right > mScreenWidth) {
                    right = mScreenWidth;
                    left = right - getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }
                if (bottom > mScreenHeight) {
                    bottom = mScreenHeight;
                    top = bottom - getHeight();
                }
                layout(left, top, right, bottom);
                Log.i("____________________", left + "___" + top + "___" + right + "___" + bottom);
                invalidate();
                startDownX = (int) event.getRawX();
                startDownY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                int lastMoveDx = Math.abs((int) event.getRawX() - startDownX);
                int lastMoveDy = Math.abs((int) event.getRawY() - startDownY);
                if (falg) {
                    ToastUtils.showToast(context, "我点击了");
                    showContent(360, true);
                }
                falg = false;
                break;
        }
        return true;
    }

    private void showContent(int angles, boolean isShow) {
        if (showPopup.isShowing()) {
            showPopup.dissPopup();
        } else {
            int excursionX = -(getWidth() / 2) - 5;
            int excursionY = 0;
            if (left == 0 && right == 0 && top == 0 && bottom == 0) {
                excursionX = -(getWidth() / 2)-5;
                excursionY = 0;
            } else if (left < 50 && top < 50) {
                excursionX = getWidth();
                excursionY = 0;
            } else if (mScreenWidth - right < 50 && top < 50) {
                excursionX = -getWidth() * 2 - 20;
                excursionY = 0;
            } else if (mScreenWidth - right < 50 && mScreenHeight - bottom < 50) {
                excursionX = -getWidth() * 2 - 20;
                excursionY = -getHeight() * 3 - 20;
            } else if (mScreenHeight - bottom < 50 && left < 50) {
                excursionX = getWidth();
                excursionY = -getHeight() * 3 - 20;
            } else if (left < 50) {
                excursionX = getWidth();
                excursionY = -getHeight() - getHeight() / 2;
            } else if (top < 50) {
                excursionX = -(getWidth() / 2)-10;
                excursionY = -10;
            } else if (mScreenWidth - right < 50) {
                excursionX = -getWidth() * 2 - 10;
                excursionY = -getHeight() - getHeight() / 2 - 10;
            } else if (mScreenHeight - bottom < 180) {
                excursionX = -getWidth() / 2 -10;
                excursionY = -getHeight() * 3 - 10;
            } else {

            }

            showPopup.showPopup(this, excursionX, excursionY);
        }
    }


    public void setData(List<View> mData) {
        this.mData = mData;
    }
}

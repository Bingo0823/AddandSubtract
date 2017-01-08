package bingo.warescountview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import static android.content.ContentValues.TAG;

/**
 * 类似商城购买商品的小控件，可以加减商品个数或手动设置数量
 * 对外暴露接口包括：
 * 监听
 * 设置默认值，否则默认值是 0
 * Created by Bingo on 2017/1/3.
 */

public class WaresCountView extends LinearLayout
        implements View.OnClickListener, TextWatcher {

    private int mCount = 0; //默认当前值
    private int mMaxCount = 9999; //默认最大值
    private int mMinCount = 0;//默认最小值

    private OnCountChangeListener mListener;

    private EditText mEtCount;

    public WaresCountView(Context context) {
        this(context, null);
    }

    public WaresCountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaresCountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intiView(context, attrs);
    }

    private void intiView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_wares_count, this);
        mEtCount = (EditText) findViewById(R.id.et_count);
        Button mBtnSubtract = (Button) findViewById(R.id.bt_subtract);
        Button mBtnAdd = (Button) findViewById(R.id.bt_add);
        mBtnSubtract.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
        mEtCount.addTextChangedListener(this);

        TypedArray obtainStyledAttributes = getContext()
                .obtainStyledAttributes(attrs, R.styleable.WaresCountView);
        int btnWidth = obtainStyledAttributes
                .getDimensionPixelSize(R.styleable.WaresCountView_WaresCountView_btnWidth
                        , LayoutParams.WRAP_CONTENT);
        int tvWidth = obtainStyledAttributes
                .getDimensionPixelSize(R.styleable.WaresCountView_WaresCountView_tvWidth, 80);
        int tvTextSize = obtainStyledAttributes
                .getDimensionPixelSize(R.styleable.WaresCountView_WaresCountView_tvTextSize, 0);
        int btnTextSize = obtainStyledAttributes
                .getDimensionPixelSize(R.styleable.WaresCountView_WaresCountView_btnTextSize, 0);
        obtainStyledAttributes.recycle();

        LayoutParams btnParams = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
        mBtnSubtract.setLayoutParams(btnParams);
        mBtnAdd.setLayoutParams(btnParams);
        if (btnTextSize != 0) {
            mBtnSubtract.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
            mBtnAdd.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        }

        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        mEtCount.setLayoutParams(textParams);
        if (tvTextSize != 0) {
            mEtCount.setTextSize(tvTextSize);
        }
    }

    /**
     * 设置控件上默认显示的数量
     * @param num int
     */
    public WaresCountView setDefaultCount(int num){
        mCount = num;
        mEtCount.setText(mCount + "");
        return this;
    }

    /**
     * 设置监听对象
     * @param OnCountChangeListener 监听
     */
    public void setOnCountChangeListener(OnCountChangeListener OnCountChangeListener) {
        this.mListener = OnCountChangeListener;
    }

    /**
     * 设置最大值
     * @param maxCount int
     */
    public WaresCountView setMaxCount(int maxCount) {
        mMaxCount = maxCount;
        return this;
    }

    /**
     * 设置最小值
     * @param minCount int
     */
    public WaresCountView setMinCount(int minCount){
        mMinCount = minCount;
        return this;
    }

    /**
     * 获取当前控件上显示的数值
     * @return 当前数值
     */
    public int getCount(){return mCount;}
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_subtract) {
            if (mCount > mMinCount) {
                mCount--;
                mEtCount.setText(mCount + "");

            }else {
                //减到最小值时回调

                if (mListener != null){
                    Log.e(TAG, "onClick: to min");
                    mListener.onToMinCount(this , mMinCount);
                }
            }

        } else if (i == R.id.bt_add) {
            if (mCount < mMaxCount) {
                mCount++;
                mEtCount.setText(mCount + "");
            }else {
                //加到最大值时回调
                if (mListener != null){
                    Log.e(TAG, "onClick: to max");
                    mListener.onToMaxCount(this , mMaxCount);
                }
            }
        }
        //点击按钮时移除焦点
        mEtCount.clearFocus();

        if (mListener != null) {
            mListener.onWaresChange(this, mCount);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty())
            return;

        mCount = Integer.valueOf(s.toString());
        if (mCount > mMaxCount) {
            mEtCount.setText(mMaxCount + "");
            return;
        }

        if (mListener != null) {
            mListener.onWaresChange(this, mCount);
        }
    }

    /**
     * 监听
     */
    public interface OnCountChangeListener {
        void onWaresChange(View view, int count);
        void onToMinCount(View view, int count);
        void onToMaxCount(View view, int count);
    }
}

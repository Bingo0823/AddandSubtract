package bingo.warescountview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private WaresCountView mWaresCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv);
        mWaresCount = (WaresCountView) findViewById(R.id.wcv);

        mWaresCount.setMinCount(0)
                .setMaxCount(10)
                .setDefaultCount(1)
                .setOnCountChangeListener(new WaresCountView.OnCountChangeListener() {
            @Override
            public void onWaresChange(View view, int count) {
                mTextView.setText(count + "");
            }

            @Override
            public void onToMinCount(View view, int count) {
                Toast.makeText(MainActivity.this, "不能更少了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onToMaxCount(View view, int count) {
                Toast.makeText(MainActivity.this, "不能更多了", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

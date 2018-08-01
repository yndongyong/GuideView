package com.yndongyong.guideview.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.yndongyong.guideview.style.BottomCenterItemDecoration;
import com.yndongyong.guideview.GuideView;
import com.yndongyong.guideview.GuideViewHelper;
import com.yndongyong.guideview.HighLightShape;
import com.yndongyong.guideview.style.LeftCenterItemDecoration;
import com.yndongyong.guideview.style.RightCenterItemDecoration;
import com.yndongyong.guideview.style.TopCenterItemDecoration;

public class MainActivity extends AppCompatActivity {

    GuideViewHelper guideViewHelper;
    GuideViewHelper.Builder guideBuilder;
    private TextView tv_text_1;
    private LinearLayout ll_image_1;
    private Switch id_switch_1;
    private RadioGroup rg_group;

    private boolean isShowAll = true;
    private int direction = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_text_1 = findViewById(R.id.tv_text_1);
        ll_image_1 = findViewById(R.id.ll_image_1);
        id_switch_1 = findViewById(R.id.id_switch_1);
        rg_group = findViewById(R.id.rg_group);
        id_switch_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isShowAll = b;
            }
        });

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_left:
                        direction = 0;
                        break;
                    case R.id.rb_top:
                        direction = 1;
                        break;
                    case R.id.rb_right:
                        direction = 2;
                        break;
                    case R.id.rb_bottom:
                        direction = 3;
                        break;

                }
            }
        });

        guideBuilder = new GuideViewHelper.Builder();
        guideBuilder.addView(id_switch_1, new BottomCenterItemDecoration(R.layout.item_decoration_0, 0, 20));
        guideBuilder.padding(5);
        guideBuilder.setHighLightShape(HighLightShape.TYPE_CIRCULAR);
        guideBuilder.showAll(isShowAll);
        guideViewHelper = guideBuilder.build();
        guideViewHelper.show(MainActivity.this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        guideBuilder = new GuideViewHelper.Builder();
        if (direction == 0) {//left
            guideBuilder.addView(tv_text_1, new LeftCenterItemDecoration(R.layout.item_decoration_1, 20, 0));
            guideBuilder.addView(ll_image_1, new LeftCenterItemDecoration(R.layout.item_decoration_2, 20, 0));
        } else if (direction == 1) { //top
            guideBuilder.addView(tv_text_1, new TopCenterItemDecoration(R.layout.item_decoration_1, 0, 20));
            guideBuilder.addView(ll_image_1, new TopCenterItemDecoration(R.layout.item_decoration_2, 0, 20));
        } else if (direction == 2) {//right
            guideBuilder.addView(tv_text_1, new RightCenterItemDecoration(R.layout.item_decoration_1, 20, 0));
            guideBuilder.addView(ll_image_1, new RightCenterItemDecoration(R.layout.item_decoration_2, 20, 0));
        } else {//bottom
            guideBuilder.addView(tv_text_1, new BottomCenterItemDecoration(R.layout.item_decoration_1, 0, 20));
            guideBuilder.addView(ll_image_1, new BottomCenterItemDecoration(R.layout.item_decoration_2, 0, 20));
        }
        guideBuilder.padding(30);
        guideBuilder.setBlurRadius(10);
        guideBuilder.showAll(isShowAll);
        switch (item.getItemId()) {
            case R.id.id_menu_show_rectangle:
                guideBuilder.setHighLightShape(HighLightShape.TYPE_RECTANGLE);
                guideViewHelper = guideBuilder.build();
                guideViewHelper.show(MainActivity.this, new GuideView.OnGuideViewDismissListener() {
                    @Override
                    public void onDisMiss() {
                        Toast.makeText(MainActivity.this, "guide view dismiss", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.id_menu_show_roundrect:
                guideBuilder.setHighLightShape(HighLightShape.TYPE_ROUNDRECT);
                guideViewHelper = guideBuilder.build();
                guideViewHelper.show(MainActivity.this);
                break;
            case R.id.id_menu_show_circle:
                guideBuilder.setHighLightShape(HighLightShape.TYPE_CIRCULAR);
                guideViewHelper = guideBuilder.build();
                guideViewHelper.show(MainActivity.this);
                break;
            case R.id.id_menu_show_oval:
                guideBuilder.setHighLightShape(HighLightShape.TYPE_OVAL);
                guideViewHelper = guideBuilder.build();
                guideViewHelper.show(MainActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

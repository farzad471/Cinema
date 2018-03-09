package com.mkhaleghy.cinema.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.mkhaleghy.cinema.R;
import com.mkhaleghy.cinema.detail.models.Detail;
import com.mkhaleghy.cinema.tools.DraggableConstraintLayout;
import com.mkhaleghy.cinema.tools.RampImageView;

import org.angmarch.views.NiceSpinner;

public class DetailActivity extends AppCompatActivity {
    public static final String TAG = "DetailActivity";
    private static String PAR_DETAIL = "d";
    private int MAX_DRAG;

    public static void start(Activity context, Detail detail, View... sharedViews) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra(PAR_DETAIL, detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String>[] sharedElements = new Pair[sharedViews.length];
            for (int i = 0; i < sharedViews.length; i++) {
                sharedElements[i] = new Pair<>(sharedViews[i], sharedViews[i].getTransitionName());
            }
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(context, sharedElements);
            context.startActivity(starter, options.toBundle());
        } else {
            context.startActivity(starter);
        }

    }

    DraggableConstraintLayout mainLay;
    LinearLayout ll_infoLay;
    ViewPager pager;
    NavigationTabStrip tabStrip;

    TextView tv_title;
    TextView tv_subTitle;
    NiceSpinner spinner;
    AppCompatImageView iv_play;
    RoundedImageView iv_icon;
    RoundedImageView iv_icon_cover;
    RampImageView iv_cover;
    AppCompatRatingBar rb_rate;
    TextView tv_rate;
    CardView cv;
    Guideline gl_top;

    Detail detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        MAX_DRAG = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        detail = getIntent().getParcelableExtra(PAR_DETAIL);
        initViews();

        pager.setAdapter(new InfoPageAdapter(getSupportFragmentManager(), detail.detailInfoPages()));
        tabStrip.setViewPager(pager);

        mainLay.addDragView(iv_icon, iv_icon_cover);

        mainLay.setMaxDrag(MAX_DRAG);
        mainLay.setDragController(new DraggableConstraintLayout.DragController() {
            @Override
            public void onDragDrop(View view, boolean captured) {

            }

            @Override
            public void onDrag(int dy) {
                Log.d(TAG, "onDrag() called with: dy = [" + dy + "]");
                if (dy >= 0 && dy <= MAX_DRAG) {
                    iv_cover.setRampDy(dy);
                    cv.setTranslationY(dy);
                    spinner.setTranslationY(dy);
                    tv_title.setTranslationY(dy);
                    tv_subTitle.setTranslationY(dy);
                    rb_rate.setTranslationY(dy);
                    tv_rate.setTranslationY(dy);
                    ll_infoLay.setTranslationY(dy);
                    iv_play.setTranslationY(dy * .4f);
                }
            }

            @Override
            public void finish() {
                supportFinishAfterTransition();
            }
        });
        bindViews();


    }

    private void bindViews() {
        tv_title.setText(detail.title());
        tv_subTitle.setText(detail.subTitle());
        rb_rate.setRating(detail.rate());
        tv_rate.setText(detail.rateText());

        String[] infoTitles = new String[detail.detailInfoPagesTitles().size()];
        infoTitles = detail.detailInfoPagesTitles().toArray(infoTitles);
        tabStrip.setTitles(infoTitles);

        spinner.attachDataSource(detail.bookTimes());


//        GlideApp.with(this).load(detail.icon()).into(iv_icon);
//        GlideApp.with(this).load(detail.cover()).into(iv_cover);
    }


    private void initViews() {
        mainLay = findViewById(R.id.mainLay);
        ll_infoLay = findViewById(R.id.ll_infoLay);
        pager = findViewById(R.id.pager);
        tabStrip = findViewById(R.id.nts_center);
        tv_title = findViewById(R.id.tv_title);
        tv_subTitle = findViewById(R.id.tv_subtitle);
        spinner = findViewById(R.id.timeSpinner);
        iv_icon = findViewById(R.id.iv_icon);
        iv_play = findViewById(R.id.iv_play);
        iv_icon_cover = findViewById(R.id.iv_icon_cover);
        iv_cover = findViewById(R.id.iv_cover);
        rb_rate = findViewById(R.id.rb_rate);
        tv_rate = findViewById(R.id.tv_rate);
        cv = findViewById(R.id.card);
        gl_top = findViewById(R.id.topGuideline);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}

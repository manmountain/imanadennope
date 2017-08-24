package mannberg.imanadennope;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
//import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

public class MainActivity extends AppCompatActivity {

    private ActionBar actionbar;
    private EditText edittext;
    private RecyclerView recyclerview;
    private ImageView icon;
    private SwipyRefreshLayout swiperefresh;
    private FloatingActionButton scrollToStart;
    private FloatingActionButton scrollToEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        icon = (ImageView) findViewById(R.id.writebackicon);
        edittext = (EditText)findViewById(R.id.writeback);
        edittext.getBackground().mutate().setColorFilter(getResources().getColor(R.color.amber600), PorterDuff.Mode.SRC_ATOP);
        edittext.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    icon.setImageResource(R.drawable.send);
                } else {
                    hideKeyboard(edittext);
                    if (edittext.getText().toString().trim().isEmpty()) {
                        icon.setImageResource(R.drawable.camera);
                    }
                }
            }
        });

        actionbar = getSupportActionBar();
        actionbar.setTitle("");
        actionbar.setHomeAsUpIndicator(R.drawable.arrow_left_thick);
        actionbar.setDefaultDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutmanager);
        final ItemAdapter adapter = new ItemAdapter();
        recyclerview.setAdapter(adapter);

        swiperefresh = (com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout) findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    scrollToStart.hide();
                }
                if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    scrollToEnd.hide();
                }
                //scrollToStart.hide();
                //scrollToEnd.hide();
                for(int i = 0; i < 10; i++) {
                    adapter.addItem();
                }
                adapter.notifyDataSetChanged();
                //swiperefresh.setRefreshing(false);
                swiperefresh.postDelayed(new Runnable() { public void run() { swiperefresh.setRefreshing(false); } }, 1500);
            }
        });
        swiperefresh.setColorSchemeResources(R.color.amber600);

        scrollToStart = (FloatingActionButton) findViewById(R.id.scrolltotop);
        scrollToEnd   = (FloatingActionButton) findViewById(R.id.scrolltobottom);

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (swiperefresh.isRefreshing()) {
                    // dont show scroll button if view is refreshing
                    return;
                }

                if(newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    scrollToStart.show();
                    scrollToEnd.show();
                }

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    scrollToStart.postDelayed(new Runnable() { public void run() { scrollToStart.hide(); } }, 2000);
                    scrollToEnd.postDelayed(new Runnable() { public void run() { scrollToEnd.hide(); } }, 2000);
                }
            }
        });

        scrollToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerview.smoothScrollToPosition(0);
            }
        });

        scrollToEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerview.smoothScrollToPosition(adapter.getItemCount()-1);
            }
        });

        recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(edittext);
                edittext.clearFocus();
                return false;
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_option_menu, menu);
        return true;
    }

}

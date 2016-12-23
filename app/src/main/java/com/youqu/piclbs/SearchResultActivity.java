package com.youqu.piclbs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.youqu.piclbs.util.KeyBoardUtil;
import com.youqu.piclbs.util.MyListView;
import com.youqu.piclbs.util.PullToRefreshLayout;
import com.youqu.piclbs.util.RecordSQLiteOpenHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sum on 1/12/16.
 */
public class SearchResultActivity extends AppCompatActivity
        implements PullToRefreshLayout.OnPullUpLoadingListener {

    private static final String ARGS_TAG = "args_tag";

    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.iv_search_clear)
    ImageView ivSearchClear;
    @BindView(R.id.articleRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.articlePtrLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @BindView(R.id.id_search_ll)
    LinearLayout idSearchLl;
    @BindView(R.id.id_search_tv)
    TextView idSearchTv;
    @BindView(R.id.listView)
    MyListView listView;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    @BindView(R.id.id_search_sv)
    ScrollView idSearchSv;
    @BindView(R.id.id_search_ll2)
    LinearLayout ll;

    private String searchTag;

    private SQLiteDatabase db;
    private BaseAdapter adapter2;
    private RecordSQLiteOpenHelper helper;

    public static void launch(Activity activity, @Nullable String tag) {
        Intent intent = new Intent(activity, SearchResultActivity.class);
        intent.putExtra(ARGS_TAG, tag);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        getWindow().setBackgroundDrawable(null);
        ButterKnife.bind(this);

        recyclerView.setVisibility(View.VISIBLE);
        // 清空搜索历史
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SearchResultActivity.this, R.style.AppAlertDialogStyle)
                        .setTitle("是否删除你的历史记录")
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteData();
                                //queryData("");
                                idSearchSv.setVisibility(View.GONE);
                                Toast.makeText(SearchResultActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                            }
                        }).create().show();
            }
        });

        initEditView();
        initPtrLayout();
        initRecyclerView();

        Intent intent = getIntent();
        searchTag = intent.getStringExtra(ARGS_TAG);
        if (TextUtils.isEmpty(searchTag)) {
            editSearch.requestFocus();
        } else {
            idSearchSv.setVisibility(View.GONE);
            editSearch.setText(searchTag);
            deletOneData(searchTag);
            insertData(searchTag);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String newSearchTag = intent.getStringExtra(ARGS_TAG);
        if (!TextUtils.isEmpty(newSearchTag) && !newSearchTag.equals(searchTag)) {
                searchTag = newSearchTag;
                editSearch.setText(searchTag);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initPtrLayout() {
        pullToRefreshLayout.setOnPullUpLoadingListener(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }




    private void initEditView() {
        helper = new RecordSQLiteOpenHelper(this);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    ivSearchClear.setVisibility(View.VISIBLE);
                    idSearchTv.setText("搜索历史");
                } else {
                    idSearchTv.setText("搜索结果");
                    recyclerView.setVisibility(View.GONE);
                    queryData("");
                }
                String tempName = editSearch.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName);
            }
        });
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE) {
                    //处理事件
                    handleSearchClick();
                    recyclerView.setVisibility(View.VISIBLE);
                    idSearchSv.setVisibility(View.GONE);
                    boolean hasData = hasData(editSearch.getText().toString().trim());
                    if (!hasData) {
                        String data = editSearch.getText().toString().trim();
                        if (data.length() > 0) {
                            insertData(data);
                        }
                        queryData("");
                    } else {
                        String data = editSearch.getText().toString().trim();
                        if (data.length() > 0) {
                            deletOneData(data);
                            insertData(data);
                        }
                        queryData("");
                    }
                    KeyBoardUtil.closeKeybord(editSearch, SearchResultActivity.this);
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recyclerView.setVisibility(View.VISIBLE);
                TextView textView = (TextView) view.findViewById(R.id.id_record_tv);
                String tag = textView.getText().toString().trim();


                    idSearchSv.setVisibility(View.GONE);
                    // isCrrHadRequest = true;

                    searchTag = tag;


                    editSearch.setText(searchTag);


                String data = editSearch.getText().toString().trim();
                if (data.length() > 0) {
                    deletOneData(data);
                    insertData(data);
                }
                queryData("");
            }
        });
        // 第一次进入查询所有的历史记录
        queryData("");
    }

    private void handleSearchClick() {
        if (TextUtils.isEmpty(editSearch.getText())
                || TextUtils.isEmpty(editSearch.getText().toString().trim())) {
            queryData("");
            if (listView.getCount() == 0) {
                idSearchSv.setVisibility(View.GONE);
            } else {
                idSearchSv.setVisibility(View.VISIBLE);
            }
        } else {
            String tag = editSearch.getText().toString().trim();
            if (!tag.equals(searchTag)) {//短时间内不重复

                }
            }
            //保存历史记录
            idSearchSv.setVisibility(View.GONE);
            boolean hasData = hasData(editSearch.getText().toString().trim());
            if (!hasData) {
                String data = editSearch.getText().toString().trim();
                if (data.length() > 0) {
                    insertData(data);
                }
                queryData("");
            } else {
                String data = editSearch.getText().toString().trim();
                if (data.length() > 0) {
                    insertData(data);
                }
                queryData("");
            }
        }

    @Override
    public void onPullUpLoading() {
        if (!TextUtils.isEmpty(searchTag)) {

        }
    }

    @OnClick({R.id.iv_back,
            R.id.iv_search_clear,
            R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.iv_search_clear: {
                editSearch.clearFocus();
                ll.setFocusable(true);
                ll.setFocusableInTouchMode(true);
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

                editSearch.setText("");
                if (listView.getCount() == 0) {
                    idSearchSv.setVisibility(View.GONE);
                } else {
                    idSearchSv.setVisibility(View.VISIBLE);
                }
                queryData("");
                ivSearchClear.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                break;
            }
            case R.id.iv_search: {
                handleSearchClick();
                recyclerView.setVisibility(View.VISIBLE);
                idSearchSv.setVisibility(View.GONE);
                boolean hasData = hasData(editSearch.getText().toString().trim());
                if (!hasData) {
                    String data = editSearch.getText().toString().trim();
                    if (data.length() > 0) {
                        insertData(data);
                    }
                    queryData("");
                } else {
                    String data = editSearch.getText().toString().trim();
                    if (data.length() > 0) {
                        deletOneData(data);
                        insertData(data);
                    }
                    queryData("");
                }
                editSearch.clearFocus();
                ll.setFocusable(true);
                ll.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                break;
            }
        }
    }


    /**
     * 插入数据
     */
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /**
     * 模糊查询数据
     */
    private void queryData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        // 创建adapter适配器对象
        adapter2 = new SimpleCursorAdapter(this, R.layout.item_search_records, cursor, new String[]{"name"},
                new int[]{R.id.id_record_tv}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        listView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }

    private void deletOneData(String tempName) {
        db = helper.getWritableDatabase();
        String whereClause = "name=?";
        String[] whereArgs = new String[]{tempName};
        db.delete("records", whereClause, whereArgs);
        db.close();
    }

}

package cn.edu.bistu.cs.se.mywordsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;

import cn.edu.bistu.cs.se.mywordsapp.contract.notes;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener {
    private static final String TAG = "myTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NoteDB noteDB = NoteDB.getWordsDB();
        if (noteDB != null)
            noteDB.close();

    }

    //初始化菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_search:
                //查找
                SearchDialog();
                return true;
            case R.id.action_insert:
                //新增
                InsertDialog();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * 更新列表
     */
    private void RefreshWordItemFragment() {
        ItemFragment itemFragment = (ItemFragment) getFragmentManager().findFragmentById(R.id.wordslist);
        itemFragment.refreshWordsList();
    }

    /**
     * 更新列表
     */
    private void RefreshWordItemFragment(String strWord) {
        ItemFragment itemFragment = (ItemFragment) getFragmentManager().findFragmentById(R.id.wordslist);
        itemFragment.refreshWordsList(strWord);
    }

    //新增对话框
    private void InsertDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        new AlertDialog.Builder(this)
                .setTitle("新增日记")//标题
                .setView(tableLayout)//设置视图
                        //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();

                        //既可以使用Sql语句插入，也可以使用使用insert方法插入
                        // InsertUserSql(strWord, strMeaning, strSample);
                        NoteDB noteDB = NoteDB.getWordsDB();
                        noteDB.Insert(strWord, strMeaning, strSample);

                        //单词已经插入到数据库，更新显示列表
                        RefreshWordItemFragment();


                    }
                })
                        //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框


    }


    //删除对话框
    private void DeleteDialog(final String strId) {
        new AlertDialog.Builder(this).setTitle("删除日记").setMessage("是否真的删除日记?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //既可以使用Sql语句删除，也可以使用使用delete方法删除
                NoteDB noteDB = NoteDB.getWordsDB();
                noteDB.DeleteUseSql(strId);

                //单词已经删除，更新显示列表
                RefreshWordItemFragment();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }


    //修改对话框
    private void UpdateDialog(final String strId, final String strWord, final String strMeaning, final String strSample) {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        ((EditText) tableLayout.findViewById(R.id.txtWord)).setText(strWord);
        ((EditText) tableLayout.findViewById(R.id.txtMeaning)).setText(strMeaning);
        ((EditText) tableLayout.findViewById(R.id.txtSample)).setText(strSample);
        new AlertDialog.Builder(this)
                .setTitle("修改日记")//标题
                .setView(tableLayout)//设置视图
                        //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strNewWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strNewMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strNewSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();

                        //既可以使用Sql语句更新，也可以使用使用update方法更新
                        NoteDB noteDB = NoteDB.getWordsDB();
                        noteDB.UpdateUseSql(strId, strWord, strNewMeaning, strNewSample);

                        //单词已经更新，更新显示列表
                        RefreshWordItemFragment();
                    }
                })
                        //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框


    }


    //查找对话框
    private void SearchDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.searchterm, null);
        new AlertDialog.Builder(this)
                .setTitle("查找日记")//标题
                .setView(tableLayout)//设置视图
                        //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtSearchWord = ((EditText) tableLayout.findViewById(R.id.txtSearchWord)).getText().toString();

                        //单词已经插入到数据库，更新显示列表
                        RefreshWordItemFragment(txtSearchWord);
                    }
                })
                        //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框

    }

    /**
     * 当用户在单词详细Fragment中单击时回调此函数
     */
    @Override
    public void onWordDetailClick(Uri uri) {

    }

    /**
     * 当用户在单词列表Fragment中单击某个单词时回调此函数
     * 判断如果横屏的话，则需要在右侧单词详细Fragment中显示
     */
    @Override
    public void onWordItemClick(String id) {

        if(isLand()) {//横屏的话则在右侧的WordDetailFragment中显示单词详细信息
            ChangeWordDetailFragment(id);
        }else{
            //启动WordDetailActivity
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailFragment.ARG_ID, id);
            startActivity(intent);
        }

    }

    //是否是横屏
    private boolean isLand(){

        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
            return true;
        return false;
    }

    private void ChangeWordDetailFragment(String id){
        Bundle arguments = new Bundle();
        arguments.putString(DetailFragment.ARG_ID, id);
        Log.v(TAG, id);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction().replace(R.id.worddetail, fragment).commit();
    }

    @Override
    public void onDeleteDialog(String strId) {
        DeleteDialog(strId);
    }

    @Override
    public void onUpdateDialog(String strId) {
        NoteDB noteDB = NoteDB.getWordsDB();
        if (noteDB != null && strId != null) {


            notes.WordDescription item = noteDB.getSingleWord(strId);
            if (item != null) {
                UpdateDialog(strId, item.title, item.author, item.content);
            }

        }

    }
}

package com.linlif.pulltorefresh;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlif.pulltorefresh.ptr.ILoadingLayout;
import com.linlif.pulltorefresh.ptr.PullToRefreshBase;
import com.linlif.pulltorefresh.ptr.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GirdViewAvtivity extends AppCompatActivity {

    private PullToRefreshGridView girdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        initView();
    }


    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:

                    girdView.onRefreshComplete();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void initView() {
        String []data = new String[] {"android","ios","wp","java","c++","c#"};

        List<String> string = new ArrayList<>();
        string.addAll(Arrays.asList(data));

        girdView = (PullToRefreshGridView) findViewById(R.id.pGridViewRights);
        girdView.getRefreshableView().setNumColumns(2);//girdView横向数量
        girdView.getRefreshableView().setVerticalSpacing(10);
        girdView.getRefreshableView().setHorizontalSpacing(10);
        girdView.setMode(PullToRefreshBase.Mode.BOTH);
        girdView.setOnRefreshListener(refreshListener);

        ILoadingLayout startLabels = girdView.getLoadingLayoutProxy();
        startLabels.setPullLabel("使劲拉吧");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("你敢放，我就敢刷新...");// 下来达到一定距离时，显示的提示
        //mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, string);

       // girdView.setAdapter(mAdapter);
        girdView.setAdapter( new myAdapter(this ,string));

        girdView.setOnRefreshListener(refreshListener);
    }


    PullToRefreshBase.OnRefreshListener2<GridView> refreshListener = new PullToRefreshBase.OnRefreshListener2<GridView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
            Toast.makeText(getApplicationContext(), "下拉刷新", Toast.LENGTH_SHORT).show();

            Thread thread =  new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message=new Message();
                    message.what=1;
                    mHandler.sendMessage(message);

                }
            });
            thread.start();


        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
            Toast.makeText(getApplicationContext(), "上拉加载", Toast.LENGTH_SHORT).show();


            Thread thread =  new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message=new Message();
                    message.what=1;
                    mHandler.sendMessage(message);

                }
            });
            thread.start();


        }
    };
    class myAdapter extends BaseAdapter{

        Context context;
        List<String> mDatas;
        private  LayoutInflater mLayoutInflater;

        public myAdapter(Context context , List<String> data){
            this.context = context;
            this.mDatas = data;
            this.mLayoutInflater =   LayoutInflater.from(this.context);
        }
        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = this.mLayoutInflater.inflate(R.layout.item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(this.mDatas.get(position));

            return convertView;
        }

        class ViewHolder{
            private  TextView text;
            public ViewHolder(View view) {
                text =   (TextView) view.findViewById(R.id.test);
            }
        }
    }
}

package com.renyu.downloadprogressdialog;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.renyu.downloadprogressdialog.myview.MyProgressDialog;


public class MyActivity extends ActionBarActivity {

    TextView showDialog=null;
    MyProgressDialog pd=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        //ע��progressDialog����ʽ
        pd=new MyProgressDialog(MyActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        pd.setTitle("��ʾ");
        pd.setMessage("���ڼ�����");
        //һ��ʼҪ���óɹ������ؿ���ʽ
        pd.setIndeterminate(true);
        pd.setMax(100);

        showDialog=(TextView) findViewById(R.id.showDialog);
        showDialog.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyDownload().execute("");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MyDownload extends AsyncTask<String, Integer, Void> {
       
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //�����Ҫ�ı���ȣ�����뽫�������ؿ�����
            pd.setIndeterminate(false); 
            pd.setProgress(values[0]);
            pd.showProgress(View.VISIBLE);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

		@Override
		protected Void doInBackground(String... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //ģ������
            int count=4;
            int currentCount=0;
            while(currentCount<count) {
                publishProgress(currentCount*100/4);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentCount++;
            }
            return null;
        }
    }
}

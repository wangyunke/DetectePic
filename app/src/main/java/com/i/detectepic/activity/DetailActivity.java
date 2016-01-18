package com.i.detectepic.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i.detectepic.R;
import com.i.detectepic.constant.Constant;
import com.i.detectepic.frame.ReqServer;
import com.i.detectepic.frame.net.HttpHandler;
import com.i.detectepic.utils.FileUtils;
import com.i.detectepic.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class DetailActivity extends BaseActivity {
    private final int TYPE_TAKE_PIC=1;
    private final int TYPE_PICK_PIC=2;
    private String path_cache__pic;
    private String capturePath = null;
    private String cardType = null;
    private  TextView content;
    private  LinearLayout ll;
    private  ImageView iv;
    private  LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
    }

    private void initView() {
        params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin=20;
        ll=(LinearLayout) findViewById(R.id.ll);
        iv=(ImageView) findViewById(R.id.iv);

        path_cache__pic=this.getExternalCacheDir().getPath();
        cardType=getIntent().getStringExtra("Type");
        String value=getIntent().getStringExtra("Value");

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle((value==null || "".equals(value))?"图片信息":value);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_take:
                        getImageFromCamera();
                        break;
                    case R.id.action_pick:
                        getImageFromAlbum();
                        break;
                }
                return true;
            }
        });
    }
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        ll.removeAllViews();
        String path=null;
        switch (arg0) {
            case TYPE_PICK_PIC:
                if (arg2 != null) {
                    path = getUriPath(arg2.getData());
                    if (null == path || "".equals(path)) {
                        Bundle bundle = arg2.getExtras();
                        if (bundle != null) {
                            Bitmap photo = (Bitmap) bundle.get("data");
                            path = FileUtils.saveImage(photo, path_cache__pic);
                        }
                    }
                }
                break;
            case TYPE_TAKE_PIC:
                if (null != capturePath) {
                    path = capturePath;
                }
        }

        if(null!=path && !"".equals(path)){
            Bitmap bmp=FileUtils.zoomImage(path);
            if(null!=bmp){
                path= FileUtils.saveImage(bmp, path_cache__pic);
                uploadPic(path);
            }
        }
    }

    private void uploadPic(String path) {
        try {
            ReqServer.reqPicUpload(Constant.URL_UPLOAD, cardType, path, new HttpHandler(this, true, "正在上传") {
                @Override
                public void onSuccess(String resp) {
                    super.onSuccess(resp);
                    try {
                        JSONObject result = new JSONObject(resp);
                        int result_code = result.optInt("error_code");
                        if (0 == result_code) {
                            JSONObject obj = result.optJSONObject("result");
                            if(null!=obj) {
                                Iterator<String> iterators= obj.keys();
                                while (iterators.hasNext()){
                                    String key=iterators.next();
                                    String value= (String) obj.get(key);
                                    if("status".equals(key) || "value".equals(key) || "type".equals(key)){
                                         continue;
                                    }else if("头像".equals(key)){
                                        if(!"".equals(value)) {
                                            byte[] bitmapArray=Base64.decode(value, Base64.DEFAULT);
                                            Bitmap bp = BitmapFactory.decodeByteArray(bitmapArray, 0,bitmapArray.length);
                                            iv.setImageBitmap(bp);
                                        }
                                    }else{
                                        TextView tv=new TextView(DetailActivity.this);
                                        tv.setLayoutParams(params);
                                        tv.setTextIsSelectable(true);
                                        tv.setTextSize(20);
                                        tv.setText(key + "：" + value);
                                        ll.addView(tv);
                                    }
                                }
                            }
                        }else{
                            ViewUtils.showToast(DetailActivity.this, result.optString("reason"));
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                    ViewUtils.showToast(DetailActivity.this, "请求失败");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getImageFromAlbum() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");// 相片类型
        // 根据版本号不同使用不同的Action
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        startActivityForResult(intent,TYPE_PICK_PIC );
    }
    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(path_cache__pic);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            capturePath = path_cache__pic + System.currentTimeMillis() + ".jpg";

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(intent,TYPE_TAKE_PIC);
        }else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }
    private String getUriPath(Uri photoUri) {
        if(photoUri == null ) {
            ViewUtils.showToast(this, "选择图片文件出错");
            return null;
        }
        String picPath= FileUtils.getPath(this,photoUri);
        if(picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG")
                ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  )) {
            return picPath;
        }else{
            ViewUtils.showToast(this, "选择图片文件不正确");
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

}

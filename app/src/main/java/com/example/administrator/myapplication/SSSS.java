package com.example.administrator.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SSSS extends AppCompatActivity implements View.OnClickListener {

    private ImageView img;
    private Button btn1;
    private Button btn2;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssss);
        filePath = Environment.getExternalStorageDirectory().
                getAbsolutePath()+"/a.png";
        //读取保存的位图（图片）
        Bitmap bitmap1= readImg();
        initView();
        if(bitmap1!=null){
            img.setImageBitmap(bitmap1);
        }else{
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x1) {
            if (data != null) {
                Uri uri = data.getData();
                getImg(uri);
            } else {
                return;
            }
        }
        if (requestCode == 0x2) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                //得到图片
                Bitmap bitmap = bundle.getParcelable("data");
                //保存到图片到本地
                saveImg(bitmap);
                //设置图片
                img.setImageBitmap(bitmap);
            } else {
                return;
            }
        }
        if (requestCode == 0x3) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                img.setImageBitmap(bitmap);
            } else {
                return;
            }
        }
    }

    //读取位图（图片）
    private Bitmap readImg() {
        File mfile = new File(filePath);
        Bitmap bm = null;
        if (mfile.exists()) {        //若该文件存在
            bm = BitmapFactory.decodeFile(filePath);
        }
        return bm;
    }

    //保存图片到本地，下次直接读取
    private void saveImg(Bitmap mBitmap) {
        File f = new File(filePath);
        try {
            //如果文件不存在，则创建文件
            if (!f.exists()) {
                f.createNewFile();
            }
            //输出流
            FileOutputStream out = new FileOutputStream(f);
            /** mBitmap.compress 压缩图片
             *
             *  Bitmap.CompressFormat.PNG   图片的格式
             *   100  图片的质量（0-100）
             *   out  文件输出流
             */
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(this, f.getAbsolutePath().toString(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getImg(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            //从输入流中解码位图
            // Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            //保存位图
            // img.setImageBitmap(bitmap);
            cutImg(uri);
            //关闭流
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //裁剪图片
    private void cutImg(Uri uri) {
        if (uri != null) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            //true:出现裁剪的框
            intent.putExtra("crop", "true");
            //裁剪宽高时的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //裁剪后的图片的大小
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);  // 返回数据
            intent.putExtra("output", uri);
            intent.putExtra("scale", true);
            uri = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, 0x2);
        } else {
            return;
        }
    }

    private void initView() {
        img = (ImageView) findViewById(R.id.img);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn1:
                //打开手机的图库;
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 0x1);
                Toast.makeText(this, "222222", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn2:
                intent = new Intent();
                //MediaStore.ACTION_IMAGE_CAPTURE  调用系统的照相机
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0x3);
                break;
        }
    }
}

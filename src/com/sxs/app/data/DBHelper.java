package com.sxs.app.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.sxs.app.everydaypoem.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DBHelper{

	 private Context context; 
     private String rootDirectory = "/data/data/com.sxs.app.everydaypoem/data/";
     private final String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
     + "/everydaypoem";
     private final String DATABASE_FILENAME = "everydaypoem.db";
     
     public DBHelper(Context context)
     {
    	 this.context = context;
     }
    //复制小于1M的数据库程序        
    public SQLiteDatabase openDatabase()   
    {   
        try  
        {   
            // 获得dictionary.db文件的绝对路径   
            String databaseFilename = DATABASE_PATH+ "/" + DATABASE_FILENAME;   
            File dir = new File(DATABASE_PATH);   
            // 如果/sdcard/dictionary目录不中存在，创建这个目录   
            if (!dir.exists())   
                dir.mkdir();   
            // 如果在/sdcard/dictionary目录中不存在   
            // dictionary.db文件，则从res\raw目录中复制这个文件到   
            // SD卡的目录（/sdcard/dictionary）   
            if (!(new File(databaseFilename)).exists()) 
//            if ((new File(databaseFilename)).exists()) 
            {   
                // 获得封装dictionary.db文件的InputStream对象   
                InputStream is = context.getResources().openRawResource(R.raw.everydaypoem);   
                FileOutputStream fos = new FileOutputStream(databaseFilename);   
                byte[] buffer = new byte[7168];   
                int count = 0;   
                // 开始复制dictionary.db文件   
                while ((count = is.read(buffer)) > 0)   
                {   
                    fos.write(buffer, 0, count);   
                }   
                fos.close();   
                is.close();   
            }   
            // 打开/sdcard/dictionary目录中的dictionary.db文件   
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);   
            return database;   
        }   
        catch (Exception e)   
        {   
        }  
        return null;
    }  
}

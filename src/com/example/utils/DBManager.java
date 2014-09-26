package com.example.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;

import com.littlebyte.weather.R;

public class DBManager {
	private final int BUFFER_SIZE = 400000;
	public static final String PACKAGE_NAME = "com.littlebyte.weather";
	public static final String DB_NAME = "myapp.db";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME + "/databases";

	private Context context;

	public DBManager(Context context) {
		this.context = context;
	}

	/**
	 * copy the database under raw
	 * 
	 * @throws IOException
	 */
	public void copyDatabase() throws IOException {

		File file = new File(DB_PATH);
		if (!file.exists()) {
			System.out.print("文件目录不存在！");
		}
		file.mkdir();

		String dbfile = DB_PATH + "/" + DB_NAME;
		if (!(new File(dbfile).exists())) {
			// 获得封装 文件的InputStream对象
			InputStream is = context.getResources().openRawResource(
					R.raw.citychina);
			FileOutputStream fos = new FileOutputStream(dbfile);
			byte[] buffer = new byte[BUFFER_SIZE];
			int count = 0;
			// 开始复制db文件
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
		}

	}
}

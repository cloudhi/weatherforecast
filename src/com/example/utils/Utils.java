package com.example.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.littlebyte.weather.R;

public class Utils {
	public static String getDate(int day) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		Date dd = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dd);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		String date = sdf.format(calendar.getTime());
		return date;
	}

	public static void setImage(ImageView iv, int i) {
		switch (i) {
		case 0:
			iv.setImageResource(R.drawable.img0);
			break;
		case 1:
			iv.setImageResource(R.drawable.img1);
			break;
		case 2:
			iv.setImageResource(R.drawable.img2);
			break;
		case 3:
			iv.setImageResource(R.drawable.img3);
			break;
		case 4:
			iv.setImageResource(R.drawable.img4);
			break;
		case 7:
			iv.setImageResource(R.drawable.img7);
			break;
		case 8:
			iv.setImageResource(R.drawable.img8);
			break;
		case 9:
			iv.setImageResource(R.drawable.img9);
			break;
		default:
			iv.setVisibility(View.INVISIBLE);
			break;

		}
	}

	public static String getCityCode(String cityName, Context context) {
		DBHelper helper = new DBHelper(context);
		DBManager manager = new DBManager(context);
		try {
			manager.copyDatabase();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String cityCode = null;
		String sql = "select * from city_table where CITY =" + "'" + cityName
				+ "'" + ";";
		Cursor cursor = helper.getReadableDatabase().rawQuery(sql, null);
		if (cursor != null ) {
			cursor.moveToFirst();
			cityCode = cursor.getString(cursor.getColumnIndex("WEATHER_ID"));

		}
		cursor.close();
		helper.close();
		if (cityCode != null)
			return cityCode;
		else
			return "获取城市代码失败！";
	}

	public static <T> T getWeather(String jsonString, Class<T> cls) {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	
}

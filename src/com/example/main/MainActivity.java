package com.example.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ParseException;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.domain.City;
import com.example.domain.WeatherInfo;
import com.example.utils.Utils;
import com.littlebyte.weather.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MainActivity extends Activity implements OnClickListener {

	private String httpUrl = null;
	private TextView city = null;
	private TextView date = null;
	private TextView week = null;
	private TextView calendar = null;
	private TextView today_temp = null;
	private TextView weather_desc = null;
	private TextView dress_suggest = null;
	private TextView tv_refresh = null;
	private TextView tv_refreshTime = null;
	private ImageView iv_weather = null;

	private TextView day2_date = null;
	private TextView day3_date = null;
	private TextView day4_date = null;
	private TextView day2_desc = null;
	private TextView day3_desc = null;
	private TextView day4_desc = null;
	private ImageView day2_v1 = null;
	private ImageView day2_v2 = null;
	private ImageView day3_v1 = null;
	private ImageView day3_v2 = null;
	private ImageView day4_v1 = null;
	private ImageView day4_v2 = null;

	private Button locate;
	private Button refresh;

	// 定位相关
	LocationClient mLocClient;
	MyLocationListenner myListener = new MyLocationListenner();
	String locationCity = "";

	private WeatherInfo weather;
	private ProgressDialog m_pDialog = null;
	private String sp_locationCity;
	private View dialogView;
	String cityName = null;
	String code = null;
	private City mCity;
	private String resultcity;
	long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		setLocationOption();
		initview();

		mLocClient.start();
		mLocClient.requestLocation();

		try {
			initdata();
		} catch (ParseException | java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		mLocClient.setLocOption(option);
	}

	public void initview() {
		city = (TextView) findViewById(R.id.city);
		date = (TextView) findViewById(R.id.date);
		week = (TextView) findViewById(R.id.week);
		calendar = (TextView) findViewById(R.id.calendar);
		today_temp = (TextView) findViewById(R.id.today_temp);
		weather_desc = (TextView) findViewById(R.id.weather_desc);
		dress_suggest = (TextView) findViewById(R.id.dress_suggest);
		tv_refresh = (TextView) findViewById(R.id.tv_refresh);
		refresh = (Button) findViewById(R.id.bt_refresh);
		locate = (Button) findViewById(R.id.locate);
		tv_refreshTime = (TextView) findViewById(R.id.time);
		iv_weather = (ImageView) findViewById(R.id.iv_weather);
		day2_date = (TextView) findViewById(R.id.day2_date);
		day3_date = (TextView) findViewById(R.id.day3_date);
		day4_date = (TextView) findViewById(R.id.day4_date);
		day2_desc = (TextView) findViewById(R.id.day2_desc);
		day3_desc = (TextView) findViewById(R.id.day3_desc);
		day4_desc = (TextView) findViewById(R.id.day4_desc);
		day2_v1 = (ImageView) findViewById(R.id.day2_iv);
		day2_v2 = (ImageView) findViewById(R.id.day2_iv2);
		day3_v1 = (ImageView) findViewById(R.id.day3_iv);
		day3_v2 = (ImageView) findViewById(R.id.day3_iv2);
		day4_v1 = (ImageView) findViewById(R.id.day4_iv);
		day4_v2 = (ImageView) findViewById(R.id.day4_iv2);

		tv_refresh.setOnClickListener(this);
		refresh.setOnClickListener(this);
		locate.setOnClickListener(this);

	}

	public void initdata() throws ParseException, java.text.ParseException {

		SharedPreferences sp = getSharedPreferences("weatherInfo", 0);
		sp_locationCity = sp.getString("locationCity", "北京");
		String sp_city = sp.getString("cityName", "北京");
		String sp_date = sp.getString("date", "2014年1月1日");
		String sp_week = sp.getString("week", "星期一");
		String sp_calendar = sp.getString("calendar", "一月初一");

		String sp_dress_suggest = sp.getString("dress_suggest",
				"天气较热，建议着短裙、短裤、短套装、T恤等夏季服装。年老体弱者宜着长袖衬衫和单裤。");
		String sp_today_temp = sp.getString("today_temp", "29℃~23℃");
		String sp_weather_desc = sp.getString("weather_desc", "晴");
		String sp_tv_refreshTime = sp.getString("tv_refreshTime", "8点");

		String sp_iv_weather = sp.getString("iv_weather", "0");
		String sp_day2_date = sp.getString("day2_date", "1月2日");
		String sp_day3_date = sp.getString("day3_date", "1月3日");
		String sp_day4_date = sp.getString("day4_date", "1月4日");
		String sp_day2_desc = sp.getString("day2_desc", "晴");
		String sp_day3_desc = sp.getString("day3_desc", "晴");
		String sp_day4_desc = sp.getString("day4_desc", "晴");
		String sp_day2_v1 = sp.getString("day2_v1", "0");
		String sp_day3_v1 = sp.getString("day3_v1", "0");
		String sp_day4_v1 = sp.getString("day4_v1", "0");
		String sp_day2_v2 = sp.getString("day2_v2", "0");
		String sp_day3_v2 = sp.getString("day3_v2", "0");
		String sp_day4_v2 = sp.getString("day4_v2", "0");
		boolean isFirst = sp.getBoolean("isfirst", true);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String s = sdf.format(sdf.parse(sp_date));
		Time t = new Time();
		t.setToNow(); // 取得系统时间。
		int hour = t.hour; // 0-23
		int old = Integer.parseInt(sp_tv_refreshTime.substring(0,
				sp_tv_refreshTime.length() - 1));

		System.out
				.println("sdf.format(new Date())___" + sdf.format(new Date()));
		System.out.println("sdf.format(sdf.parse(sp_date)___" + s);
		System.out.println("hour--->" + hour);
		System.out.println("old--->" + old);

		if (sdf.format(new Date()).equals(s) && (hour - old) < 6) {
			System.out.println("sp数据！！！！！！！！");
			city.setText(sp_city);
			date.setText(sp_date);
			week.setText(sp_week);
			calendar.setText(sp_calendar);
			today_temp.setText(sp_today_temp);
			weather_desc.setText(sp_weather_desc);
			tv_refreshTime.setText(sp_tv_refreshTime);
			dress_suggest.setText(sp_dress_suggest);
			Utils.setImage(iv_weather, Integer.parseInt(sp_iv_weather));
			day2_date.setText(sp_day2_date);
			day3_date.setText(sp_day3_date);
			day4_date.setText(sp_day4_date);
			day2_desc.setText(sp_day2_desc);
			day3_desc.setText(sp_day3_desc);
			day4_desc.setText(sp_day4_desc);
			Utils.setImage(day2_v1, Integer.parseInt(sp_day2_v1));
			Utils.setImage(day2_v2, Integer.parseInt(sp_day2_v2));
			Utils.setImage(day3_v1, Integer.parseInt(sp_day3_v1));
			Utils.setImage(day3_v2, Integer.parseInt(sp_day3_v2));
			Utils.setImage(day4_v1, Integer.parseInt(sp_day4_v1));
			Utils.setImage(day4_v2, Integer.parseInt(sp_day4_v2));
		} else {
			System.out.println("网络数据！！！！！！！！");
			showProgressDialog();
			getWeatherInfo(getUrl(Utils.getCityCode(sp_locationCity,
					getApplicationContext())));
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理

		mLocClient.stop();

	}

	@Override
	protected void onStop() {
		super.onStop();

		SharedPreferences sp = getSharedPreferences("weatherInfo", 0);
		Editor editor = sp.edit();
		if (locationCity != null && locationCity.length() > 0
				&& !locationCity.equals("")) {
			editor.putString("locationCity",
					locationCity.substring(0, locationCity.length() - 1));
		}
		editor.putString("cityName", city.getText().toString());
		editor.putString("date", date.getText().toString());
		editor.putString("week", week.getText().toString());
		editor.putString("calendar", calendar.getText().toString());

		editor.putString("today_temp", today_temp.getText().toString());
		editor.putString("weather_desc", weather_desc.getText().toString());

		editor.putString("dress_suggest", dress_suggest.getText().toString());
		editor.putString("tv_refreshTime", tv_refreshTime.getText().toString());
		editor.putString("day2_date", day2_date.getText().toString());
		editor.putString("day3_date", day3_date.getText().toString());
		editor.putString("day4_date", day4_date.getText().toString());
		editor.putString("day2_desc", day2_desc.getText().toString());
		editor.putString("day3_desc", day3_desc.getText().toString());
		editor.putString("day4_desc", day4_desc.getText().toString());
		editor.putBoolean("isfirst", false);
		if (weather != null) {
			editor.putString("iv_weather", weather.getImg1());
			editor.putString("day2_v1", weather.getImg3());
			editor.putString("day3_v1", weather.getImg5());
			editor.putString("day4_v1", weather.getImg7());
			editor.putString("day2_v2", weather.getImg4());
			editor.putString("day3_v2", weather.getImg6());
			editor.putString("day4_v2", weather.getImg8());
		}

		editor.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_refresh:
		case R.id.bt_refresh:

			getWeatherInfo(getUrl(code));

			// 防止重复点击一直出现Toast
			if ((System.currentTimeMillis() - exitTime) > 3000) {
				Toast.makeText(this, "更新成功！", 0).show();
				exitTime = System.currentTimeMillis();
			} else {
				return;
			}

			break;
		case R.id.locate:
			showSelectDialog();
			break;
		}

	}

	private void showSelectDialog() {
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(this, R.style.dialog);
		dialogView = getLayoutInflater().inflate(R.layout.customview, null);
		dialog.setContentView(dialogView);
		// 点击屏幕外侧，dialog不消失
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		// 监听dialog里的button
		/*
		 * 监听自动选择
		 */
		Button ortherbtnemil = (Button) dialogView
				.findViewById(R.id.autoselect);
		ortherbtnemil.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				if (code != null) {
					httpUrl = getUrl(code);
					getWeatherInfo(httpUrl);
					Toast.makeText(MainActivity.this, "定位成功！", 0).show();
				} else {
					Toast.makeText(MainActivity.this,
							"网络状况不佳，定位失败！请打开WIFI进行定位！", 0).show();
					return;
				}

			}
		});
		/*
		 * 监听手动选择
		 */
		Button ortherbtnweb = (Button) dialogView.findViewById(R.id.select);
		ortherbtnweb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				Intent in = new Intent(MainActivity.this,
						CitySelect1Activity.class);
				in.putExtra("city", mCity);
				startActivityForResult(in, 1);
			}
		});
		/*
		 * 监听imgbtn关闭dialog
		 */
		ImageButton customviewtvimgCancel = (ImageButton) dialogView
				.findViewById(R.id.ivCancel);
		customviewtvimgCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 0) {
			if (requestCode == 1 && data != null) {
				mCity = data.getParcelableExtra("city");
				resultcity = mCity.getCity().substring(0,
						mCity.getCity().length() - 1);
				city.setText(resultcity);
				locationCity = resultcity;
			} else {
				return;
			}

		}
	}

	public String getUrl(String code) {
		httpUrl = "http://weather.51wnl.com/weatherinfo/GetMoreWeather?cityCode="
				+ code + "&weatherType=0";
		return httpUrl;
	}

	public void getWeatherInfo(final String url) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(5000);
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, arg1, arg2);
				String result = new String(arg2);
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONObject weatherObj = jsonObject
							.getJSONObject("weatherinfo");
					weather = Utils.getWeather(weatherObj.toString(),
							WeatherInfo.class);

					System.out.println("weatherInfo--->" + weather);

					refreshData();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
				m_pDialog.dismiss();
				Toast.makeText(MainActivity.this, "请求失败  ", 0).show();
			}

		});
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location == null)
				return;
			if (city.getText().toString().length() == 0) {
				locationCity = location.getCity();
				city.setText(location.getCity());

				if (m_pDialog != null)
					m_pDialog.dismiss();
				System.out.println("locationCity-->" + locationCity);
				if (locationCity != null) {
					code = Utils
							.getCityCode(
									locationCity.substring(0,
											locationCity.length() - 1),
									getApplicationContext());
				}
			}

		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

	}

	public void showProgressDialog() {
		m_pDialog = new ProgressDialog(this);
		// 设置进度条风格，风格为圆形，旋转的
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		m_pDialog.setMessage("正在获取数据...");
		// 让ProgressDialog显示
		m_pDialog.show();

	}

	public void refreshData() {
		date.setText(weather.getDate_y());
		week.setText(weather.getWeek());
		calendar.setText(weather.getDate());
		today_temp.setText(weather.getTemp1());
		weather_desc.setText(weather.getWeather1());
		dress_suggest.setText(weather.getIndex_d());
		iv_weather.setImageResource(R.drawable.img0);
		tv_refreshTime.setText(weather.getFchh() + "点");
		Utils.setImage(iv_weather, Integer.parseInt(weather.getImg1()));

		day2_date.setText(Utils.getDate(1));
		day3_date.setText(Utils.getDate(2));
		day4_date.setText(Utils.getDate(3));
		day2_desc.setText(weather.getWeather2());
		day3_desc.setText(weather.getWeather3());
		day4_desc.setText(weather.getWeather4());
		Utils.setImage(day2_v1, Integer.parseInt(weather.getImg3()));
		Utils.setImage(day2_v2, Integer.parseInt(weather.getImg4()));
		Utils.setImage(day3_v1, Integer.parseInt(weather.getImg5()));
		Utils.setImage(day3_v2, Integer.parseInt(weather.getImg6()));
		Utils.setImage(day4_v1, Integer.parseInt(weather.getImg7()));
		Utils.setImage(day4_v2, Integer.parseInt(weather.getImg8()));

	}

}

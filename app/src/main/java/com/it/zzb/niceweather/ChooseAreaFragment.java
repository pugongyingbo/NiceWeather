package com.it.zzb.niceweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.zzb.niceweather.db.City;
import com.it.zzb.niceweather.db.County;
import com.it.zzb.niceweather.db.DbManager;
import com.it.zzb.niceweather.db.Province;

import com.it.zzb.niceweather.util.HttpUtil;
import com.it.zzb.niceweather.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ChooseAreaFragment extends Fragment {

    private  final int LEVEL_PROVINCE = 0;
    private  final int LEVEL_CITY = 1;
    private  final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;
    private DbManager dbManager;

    //private OnFragmentInteractionListener mListener;

    public ChooseAreaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_area, container, false);
        backButton = (Button) view.findViewById(R.id.back_button);
        titleText = (TextView) view.findViewById(R.id.title_text);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if(currentLevel == LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity){
                        Intent intent = new Intent(getActivity(),WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);

                    }

                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);

        //使用GreenDao
       // ProvinceDao provinceDao = GreenDaoManager.getInstance().getSession().getProvinceDao();
       // List<Province> provinceList = provinceDao.loadAll();

        //使用LitePal
        //provinceList = DataSupport.findAll(Province.class);

         dbManager = new DbManager(Myapplication.getContext());
         provinceList = dbManager.selectProvince();

        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;

        }else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }


    //获取
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);

        //使用GreenDao
        //CityDao cityDao = GreenDaoManager.getInstance().getSession().getCityDao();
        //QueryBuilder qb = cityDao.queryBuilder();
       // List<City> cityList = qb.where(CityDao.Properties.ProvinceId.eq(selectedProvince.getId())).list();

        //使用LitePal
        //cityList = DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        dbManager = new DbManager(Myapplication.getContext());
        cityList = dbManager.selectCity(selectedProvince.getId());

        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;

        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+ provinceCode;
            queryFromServer(address,"city");
        }
    }
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);

        //使用GreenDao
       // CountyDao countyDao = GreenDaoManager.getInstance().getSession().getCountyDao();
        //QueryBuilder qb = countyDao.queryBuilder();
        //List<County> countyList = qb.where(CountyDao.Properties.CityId.eq(selectedCity.getId())).list();

        //使用LitePal
        //countyList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);

        dbManager = new DbManager(Myapplication.getContext());
        countyList = dbManager.selectCounty(selectedCity.getId());

        if (countyList.size() > 0) {
            dataList.clear();
            for (County county: countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;

        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" +provinceCode +"/" +cityCode;
            queryFromServer(address,"county");
        }

    }

    private void queryFromServer(String address, final String type) {

        showProgressDialog();

        HttpUtil.sendOkHttpRequest(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDilog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           closeProgressDilog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });

    }

    //显示进度对话框
    private void showProgressDialog() {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭进度对话框
    private void closeProgressDilog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }


}

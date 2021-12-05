package com.jnu.booklist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    public static final int WHAT_DATA_OK = 1000;
    private MapView mapView;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = rootView.findViewById(R.id.baidu_map_view);

        LatLng centerPoint = new LatLng(22.254895,113.539458);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(18.0f).target(centerPoint);
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.jinan);

        Handler handler=new Handler(Looper.getMainLooper())
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==WHAT_DATA_OK)
                {
                    String content= msg.getData().getString("data");
                    if(null!=content) {
                            try {
                                JSONObject jsonObject = new JSONObject(content);
                                JSONArray books = jsonObject.getJSONArray("books");
                                for (int index = 0; index < books.length(); index++) {
                                    JSONObject book = books.getJSONObject(index);

                                    LatLng centerPoint = new LatLng(book.getDouble("latitude"), book.getDouble("longitude"));
                                    MarkerOptions markerOption = new MarkerOptions().icon(bitmap).position(centerPoint);
                                    Marker marker = (Marker) mapView.getMap().addOverlay(markerOption);
                                    OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(50)
                                            .fontColor(0xFFFF00FF).text(book.getString("name")).rotate(0).position(centerPoint);
                                    mapView.getMap().addOverlay(textOption);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }
        };

        Runnable runnable=new Runnable() {

            @Override
            public void run() {
                try {
                    URL url=new URL("http://file.nidama.net/class/mobile_develop/data/bookstore.json");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.connect();
                    if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK)
                    {
                        InputStream inputStream= httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

                        String line="";
                        StringBuffer stringBuffer=new StringBuffer();
                        while( null!=(line=bufferedReader.readLine())){
                            stringBuffer.append(line);
                        }
                        Message message=new Message();
                        message.what= WHAT_DATA_OK;
                        Bundle bundle=new Bundle();
                        bundle.putString("data",stringBuffer.toString());
                        message.setData(bundle);

                        handler.sendMessage(message);
                        Log.i("test", "onCreateView: "+stringBuffer.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();

        mapView.getMap().setOnMarkerClickListener(maker -> {
            Toast.makeText(getContext(), "Marker被点击了！", Toast.LENGTH_SHORT).show();
            return false;
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


}
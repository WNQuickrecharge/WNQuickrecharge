package com.optimumnano.quickcharge.manager;

import com.baidu.mapapi.model.LatLng;
import com.optimumnano.quickcharge.bean.StationBean;

/**
 * Created by mfwn on 2017/4/8.
 */

public class EventManager {
    public static class onInPutWrongOldPayPassword{

    }
    public static class onRetryInputOldPayPassword{

    }
    public static class onInputNewPayPassword{
        public int inputStatus;
        public onInputNewPayPassword(int inputStatus){
            this.inputStatus=inputStatus;
        }
    }

    public static class onBalanceChangeEvent{
        public String balance;
        public onBalanceChangeEvent(String finalBalance){
            this.balance=finalBalance;
        }
    }

    public static class onUserInfoChangeEvent{
    }

    public static class addCollectStation{
        public int station_id;
        public addCollectStation(int station_id){this.station_id=station_id;}
    }
    public static class startGPS{
        public LatLng latLng;
        public startGPS(LatLng latLng){
            this.latLng=latLng;
        }
    }
    public static class openStationDetail{
        public int id;
        public openStationDetail(int id){
            this.id=id;
        }

    }
    public static class cookieTimeOut{

    }

    public static class openStationActivity{
        public StationBean bean;

        public openStationActivity(StationBean bean) {
            this.bean = bean;
        }
    }
    public static class mainActivitySelectOrderTag{
//        public StationBean bean;
//
//        public mainActivitySelectOrderTag(StationBean bean) {
//            this.bean = bean;
//        }
    }

    public static class changeCity {
        public String cityname;
        public changeCity(String cityname) {
            this.cityname=cityname;
        }
    }

    public static class onFilterParamsChange {
        public onFilterParamsChange() { }
    }
}

package com.optimumnano.quickcharge.manager;

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
}

package com.app.parker;

import com.app.parker.module.Data;
import org.springframework.boot.autoconfigure.gson.GsonProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserData {

    private static HashMap<String , String> usersInfo = new HashMap<>();
    private static HashMap<String , ArrayList<Data>> userBookedPlace = new HashMap<>();


    public boolean isUserExist(String name, String password){
        return usersInfo.containsKey(name) && usersInfo.get(name).equals(password);
    }

    public HashMap<String , Integer> getAlreadyBookedData(String time,String place, String userName  ){
        Long currentTime = Long.parseLong(time);
        HashMap<String , Integer> map = new HashMap<>();
        boolean res1 = bookNewPlace(place , time , "A0", userName , false );
        boolean res2 = bookNewPlace(place , time , "A1", userName , false);
//        System.err.println("res1 = " + res1);
//        System.err.println("res2 = " + res2);
        if (!res1)
            map.put("v0" , 1);
        if (!res2)
            map.put("v1" , 1);
        return map;
    }

    public boolean createNewUser(String name , String password){
        try {
            usersInfo.put(name, password);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean bookNewPlace(String place , String time , String type, String userName , boolean isApi){
        try{
            Long currentTime = Long.parseLong(time);
            if (!usersInfo.containsKey(userName)){
                usersInfo.put(userName , "123");
            }
            for (String user : userBookedPlace.keySet() ){
                List<Data> dates = userBookedPlace.get(user);
                for (Data data : dates){
                    long twelveHoursLater = data.getTime() + (11 * 60 * 60 * 1000); // 12 hours in milliseconds
                    if (twelveHoursLater >= currentTime && type.equals(data.getType())) {
//                        System.err.println("twelveHoursLater = "+twelveHoursLater);
//                        System.err.println("currentTime = "+currentTime);
                        return false;
                    }
                }
            }
            if (!userBookedPlace.containsKey(userName)){
                userBookedPlace.put(userName, new ArrayList<>());
            }
            if(isApi){
                ArrayList<Data> arrayList = userBookedPlace.get(userName);
                arrayList.add(new Data(currentTime , place , type));
                userBookedPlace.replace(userName , arrayList);
            }
            return true;
        } catch (Exception e) {
//            System.err.println("Execption");
            return  false;
        }
    }

    public boolean userCanEnter(String userName , String place){
        try {
            ArrayList<Data> datas = userBookedPlace.get(userName);
            long currentTime = System.currentTimeMillis();
            int index = -1;

            for (int i=0;i<datas.size() ; i++){
                if (datas.get(i).getPlace().equals(place))
                    index = i;
            }
            if (index == -1) {
//                System.err.println("userCanEnter"+"userName "+userName +"  place "+place );
                return false;
            }
            Data data = datas.get(index);
//            long bookedTime = data.getTime() - (10 * 60 * 1000);;
//            if ( bookedTime <= currentTime ) return true;
        } catch (Exception exception){
//            System.err.println("userCanEnter"+"userName "+userName +"  place "+place );
            return false;
        }
//        System.err.println("userCanEnter"+"userName "+userName +"  place "+place );
        return true;
    }

    public String userTime(String userName , String place){
        ArrayList<Data> datas = userBookedPlace.get(userName);
        long currentTime = System.currentTimeMillis();
        int index = -1;
        for (int i=0;i<datas.size() ; i++){
            if (datas.get(i).getPlace().equals(place))
                index = i;
        }
        if (index == -1) return "1";
        Data data = datas.get(index);
        long bookedTime = data.getTime();
        Long timeDifferenceMillis = Math.abs(data.getTime() - currentTime);

        return timeDifferenceMillis.toString();
    }

    public String userCanExit(String userName , String place){
        try {
            ArrayList<Data> datas = userBookedPlace.get(userName);
            long currentTime = System.currentTimeMillis();
            int index = -1;
            for (int i=0;i<datas.size() ; i++){
                if (datas.get(i).getPlace().equals(place))
                    index = i;
            }
            if (index == -1) return "1";
            Data data = datas.get(index);
            long bookedTime = data.getTime();
            long timeDifferenceMillis = Math.abs(data.getTime() - currentTime);

            long timeDifferenceHours = timeDifferenceMillis / (60 * 60 * 1000);

            timeDifferenceHours = (long) (timeDifferenceHours + 0.5);
            Long payDifference = timeDifferenceHours * 50;
            String pay = payDifference.toString();
            if ( bookedTime <= currentTime ) return pay;
            datas.remove(index);
            userBookedPlace.replace(userName , datas);
        } catch (Exception exception){
            return "1";
        }
//        System.err.println("userCanExit " +"  userName = " +userName +" place " +place);
        return "1";
    }

    public String getAllPlaceData(String place,String v0 , String v1){
//        System.err.println("getAllPlaceData"+"  place = " + place + "   response v0 ="+v0+ "   response v1 ="+v1);
        return "{\"v1\":\"" + v0 + "\", \"v2\":\"" + v1 + "\"}";
    }

}




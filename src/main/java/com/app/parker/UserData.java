package com.app.parker;

import com.app.parker.module.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserData {

    private static HashMap<String , String> usersInfo = new HashMap<>();
    private static HashMap<String , ArrayList<Data>> userBookedPlace = new HashMap<>();


    public boolean isUserExist(String name, String password){
        return usersInfo.containsKey(name) && usersInfo.get(name).equals(password);
    }

    public boolean createNewUser(String name , String password){
        try {
            usersInfo.put(name, password);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean bookNewPlace(String place , String time , String type, String userName){
        try{
            Long currentTime = Long.parseLong(time);

            for (String user : userBookedPlace.keySet() ){
                List<Data> dates = userBookedPlace.get(user);
                for (Data data : dates){
                    long twelveHoursLater = data.getTime() + (12 * 60 * 60 * 1000); // 12 hours in milliseconds
                    if ( data.getPlace().equals(place) && twelveHoursLater >= currentTime && type.equals(data.getType()))
                        return  false;
                }
            }
            if (!userBookedPlace.containsKey(userName)){
                userBookedPlace.put(userName, new ArrayList<>());
            }
            ArrayList<Data> arrayList = userBookedPlace.get(userName);
            arrayList.add(new Data(currentTime , place , type));
            userBookedPlace.replace(userName , arrayList);
            return true;
        } catch (Exception e) {
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
            if (index == -1) return false;
            Data data = datas.get(index);
//            long bookedTime = data.getTime() - (10 * 60 * 1000);;
//            if ( bookedTime <= currentTime ) return true;
        } catch (Exception exception){
            return false;
        }
        return true;
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
        return "1";
    }

    public String getAllPlaceData(String place, String response){
        String v1 = "";
        String v2 = "";

        return "{\"v1\":\"" + v1 + "\", \"v2\":\"" + v2 + "\"}";
    }

}




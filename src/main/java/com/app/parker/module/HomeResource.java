//main file with all rest endpoints
package com.app.parker.module;

import com.app.parker.UserData;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Objects;

@RestController
public class HomeResource {

    private final RestTemplate restTemplate;

    private final UserData userData;

    public HomeResource(RestTemplate restTemplate, UserData userData) {
        this.restTemplate = restTemplate;
        this.userData = userData;
    }

    @RequestMapping("getparkingareajson")
    public String getParkingAreaJson(@RequestParam String place,@RequestParam String name,@RequestParam String time){

        MDC.put("username:", name);
        String apiUrl = "https://blynk.cloud/external/api/getAll?token=hKRzpe5iAnLwQMQuELweDQTZmGglW0Km";
        String response = restTemplate.getForObject(apiUrl, String.class);
        HashMap<String , Integer> map = userData.getAlreadyBookedData(time , place , name);
        char[] arr = Objects.requireNonNull(response).toCharArray();
        if (map.containsKey("A0"))
            arr[6] = '1';
        if (map.containsKey("A1"))
            arr[13] = '1';
        response = "";
        for (char c : arr) response += c;
        LoggerUtil.info("API Response" +response);
        MDC.clear();
        return response;
    }

    @RequestMapping("signup")
    public Boolean createNewUser(@RequestParam String name, @RequestParam String password){
        MDC.put("username:", name);
        boolean result = userData.createNewUser(name, password);
        LoggerUtil.info("User creates a new account"+result);
        MDC.clear();
        return result;
    }

    @RequestMapping("signin")
    public Boolean getUserExist(@RequestParam String name, @RequestParam String password){
        MDC.put("username:", name);
        boolean result = userData.isUserExist(name, password);
        LoggerUtil.info("User is login in and verify authenticity = "+result);
        MDC.clear();
        return result;
    }

    @RequestMapping("test")
    public String getUserExist(){
        return "result";
    }

    @RequestMapping("booknewslot")
    public Boolean bookNewSlot(
            @RequestParam String place, @RequestParam String time, @RequestParam String type, @RequestParam String userName
    ){
        MDC.put("username:", time);
        boolean result = userData.bookNewPlace(place , time , type , userName , true);
        LoggerUtil.info("new Slot Booked Success fully for user"+result);
        LoggerUtil.info("Parking place occupied?"+result);
        MDC.clear();
        return result;
    }

    @RequestMapping("usercanenter")
    public Boolean userCanEnter(
            @RequestParam String place, @RequestParam String userName , @RequestParam String type
    ) throws InterruptedException {
        boolean result = userData.userCanEnter(userName , place);
        LoggerUtil.info("Access for entry granted for user "+result);
        if (result) {
            String servoMotorPin = "V2" ;
            String apiUrl = "https://blynk.cloud/external/api/update?token=hKRzpe5iAnLwQMQuELweDQTZmGglW0Km&" + servoMotorPin +"="+1;
            restTemplate.getForObject(apiUrl, String.class);
            Thread.sleep(5000);
            apiUrl = "https://blynk.cloud/external/api/update?token=hKRzpe5iAnLwQMQuELweDQTZmGglW0Km&" + servoMotorPin +"="+0;
            restTemplate.getForObject(apiUrl, String.class);
        }
        return result;
    }

    @RequestMapping("usercanexit")
    public String userCanExit(
            @RequestParam String place, @RequestParam String userName
    ){
        MDC.put("username:", place);
        String result = userData.userCanExit(userName , place);
        LoggerUtil.info("Exit allowed for user "+result);
        return result;
    }

    @RequestMapping("usertime")
    public String userTime(
            @RequestParam String time, @RequestParam String userName
    ){
        MDC.put("username:", time);
        String result = userData.userTime(userName , time);
        LoggerUtil.info("user can Exit =  "+result);
        MDC.clear();
        return result;
    }

    @RequestMapping(value = "/updateslot", method = RequestMethod.POST)
    public boolean updateSlot(@RequestParam String servoMotorGateSignal, @RequestParam String servoMotorPin){
        LoggerUtil.info("Before Call");
        String apiUrl = "https://blynk.cloud/external/api/update?token=hKRzpe5iAnLwQMQuELweDQTZmGglW0Km&" + servoMotorPin +"="+servoMotorGateSignal;
        String response = restTemplate.getForObject(apiUrl, String.class);
        LoggerUtil.info("After Call");
        return true;
    }

}

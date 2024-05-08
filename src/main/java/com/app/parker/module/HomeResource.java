package com.app.parker.module;



import com.app.parker.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@RestController
public class HomeResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserData userData;

    @RequestMapping("getparkingareajson")
    public String getParkingAreaJson(@RequestParam String place,@RequestParam String name,@RequestParam String time){

        String apiUrl = "https://blynk.cloud/external/api/getAll?token=hKRzpe5iAnLwQMQuELweDQTZmGglW0Km";
        String response = restTemplate.getForObject(apiUrl, String.class);
        HashMap<String , Integer> map = userData.getAlreadyBookedData(time , place , name);
        char[] arr = response.toCharArray();
        if (map.containsKey("v0"))
            arr[6] = '1';
        if (map.containsKey("v1"))
            arr[13] = '1';
        response = "";
        for (char c : arr) response += c;
        System.err.println(response);
        return response;
    }

    @RequestMapping("signup")
    public Boolean createNewUser(@RequestParam String name, @RequestParam String password){
        boolean result = userData.createNewUser(name, password);
        if (result){
            System.err.println("New User Created ");
        }else  {
            System.err.println("User is't created");
        }

        return result;
    }

    @RequestMapping("signin")
    public Boolean getUserExist(@RequestParam String name, @RequestParam String password){
        boolean result = userData.isUserExist(name, password);
        if (result){
            System.err.println("is valid user");
        }else {
            System.err.println("Invalid User");
        }
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
        boolean result = userData.bookNewPlace(place , time , type , userName , true);
        if (result){
            System.err.println("New Slot is Successfully booked");
        }else {
            System.err.println("Failed to book new slot because slot is already booked");
        }
        return result;
    }

    @RequestMapping("usercanenter")
    public Boolean userCanEnter(
            @RequestParam String place, @RequestParam String userName , @RequestParam String type
    ) throws InterruptedException {
        boolean result = userData.userCanEnter(userName , place);
        System.err.println("user can Enter =  "+result);
        if (result) {
            String servoMotorPin = "V2" ;
            String apiUrl = "https://blynk.cloud/external/api/update?token=hKRzpe5iAnLwQMQuELweDQTZmGglW0Km&" + servoMotorPin +"="+1;
            restTemplate.getForObject(apiUrl, String.class);
            System.err.println("Gate is opened User can enter");
            Thread.sleep(5000);
            System.err.println("Gate is closed !! if not entered need to scann the qr again");
            apiUrl = "https://blynk.cloud/external/api/update?token=hKRzpe5iAnLwQMQuELweDQTZmGglW0Km&" + servoMotorPin +"="+0;
            restTemplate.getForObject(apiUrl, String.class);
        } else {
            System.err.println("Failed to Enter in to the gate");
        }
        return result;
    }

    @RequestMapping("usercanexit")
    public String userCanExit(
            @RequestParam String place, @RequestParam String userName
    ){
        String result = userData.userCanExit(userName , place);
        if (result != null){
            System.err.println("User can exit the parking after paying the bill");
        }else {
            System.err.println("need to scann again for exit");
        }
        return result;
    }

    @RequestMapping("exit")
    public void userCanExit() throws InterruptedException {
        String servoMotorPin = "V3" ;
        String apiUrl = "https://blynk.cloud/external/api/update?token=hKRzpe5iAnLwQMQuELweDQTZmGglW0Km&" + servoMotorPin +"="+1;
        restTemplate.getForObject(apiUrl, String.class);
        System.err.println("Gate is opened User can exit");
        Thread.sleep(5000);
        System.err.println("Gate is closed !! if not exit need to scann the qr again");
        apiUrl = "https://blynk.cloud/external/api/update?token=hKRzpe5iAnLwQMQuELweDQTZmGglW0Km&" + servoMotorPin +"="+0;
        restTemplate.getForObject(apiUrl, String.class);
    }



    @RequestMapping("usertime")
    public String userTime(
            @RequestParam String place, @RequestParam String userName
    ){
        String result = userData.userTime(userName , place);
        System.err.println("need to pay this INR to exit = "+result);
        return result;
    }

    @RequestMapping(value = "/updateslot", method = RequestMethod.POST)
    public boolean updateSlot(@RequestParam String servoMotorGateSignal, @RequestParam String servoMotorPin){
        System.err.println("Before Call");
        String apiUrl = "https://blynk.cloud/external/api/update?token=hKRzpe5iAnLwQMQuELweDQTZmGglW0Km&" + servoMotorPin +"="+servoMotorGateSignal;
        String response = restTemplate.getForObject(apiUrl, String.class);
        System.err.println("After Call");
        return true;
    }

}

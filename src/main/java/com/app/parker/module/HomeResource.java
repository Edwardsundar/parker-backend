package com.app.parker.module;



import com.app.parker.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class HomeResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserData userData;

    @RequestMapping("getparkingareajson")
    public String getParkingAreaJson(@RequestParam String place){

        String apiUrl = "https://blynk.cloud/external/api/getAll?token=hKRzpe5iAnLwQMQuELweDQTZmGglW0Km";
        String response = restTemplate.getForObject(apiUrl, String.class);
        System.err.println(response);
        userData.getAllPlaceData(place , response);
        return response;
    }

    @RequestMapping("signup")
    public Boolean createNewUser(@RequestParam String name, @RequestParam String password){
        boolean result = userData.createNewUser(name, password);
        System.err.println("Create New User = "+result);
        return result;
    }

    @RequestMapping("signin")
    public Boolean getUserExist(@RequestParam String name, @RequestParam String password){
        boolean result = userData.isUserExist(name, password);
        System.err.println("User is Exist in the table = "+result);
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
        boolean result = userData.bookNewPlace(place , time , type , userName);
        System.err.println("new Slot Booked Success fully = "+result);
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
        String result = userData.userCanExit(userName , place);
        System.err.println("user can Exit =  "+result);
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

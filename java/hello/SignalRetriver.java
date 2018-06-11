package hello;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class SignalRetriver{

    private String[] signalArray;
    private Signal s;
    private StepCounter stepCounter = new StepCounter();
   // private int stepCount = 0;

    public void init() {
        FileInputStream serviceAccount = null;
        FirebaseOptions options = null;
        try {
            serviceAccount = new FileInputStream("src/main/java/hello/serviceAccount.json");

            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://kiirendusandurisync-a8a7c.firebaseio.com/")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseApp.initializeApp(options);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference();



        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                signalArray = snapshot.getValue().toString().split(";");
                s = new Signal(Double.parseDouble(signalArray[0]), Double.parseDouble(signalArray[1]),
                        Double.parseDouble(signalArray[2]), Double.parseDouble(signalArray[3]));
                stepCounter.addToCountingList(s);
                //System.out.println("OnChildAdded: " + s.getxAxis() + " + " + s.getyAxis() + " + " + s.getzAxis()
                      //  + " -> " + s.getTime());
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) { }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) { }

            @Override
            public void onCancelled(DatabaseError error) { }
        });
    }

    @GetMapping(value = "/steps")
    public StepsResponse sendSteps() {
        StepsResponse response = new StepsResponse("Done", stepCounter.getCurrentStepCount());
        //HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        //System.out.println("Url: " + request.getRequestURL().toString());
        return response;
    }

    @PostMapping(value = "/nullSteps")
    public NullStepsResponse postSignal(@RequestBody NullSteps data) {
        NullStepsResponse response = new NullStepsResponse("Done", data);
        stepCounter.setCurrentStepCountToZero();
        System.out.println("NULLLLLLLL STEPSSSS");
        return response;
    }

}

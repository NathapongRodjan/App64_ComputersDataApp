package com.example.nathapong.app64_computersdataapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nathapong.app64_computersdataapp.Model.Computer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    EditText edtComputerName;
    EditText edtComputerPower;
    EditText edtComputerSpeed;
    EditText edtComputerRam;
    EditText edtComputerScreen;
    EditText edtComputerKeyboard;
    EditText edtComputerCpu;

    TextView txtComputerData;

    Button btnSendOrUpdateData;
    Button btnGetDataFromServer;

    FirebaseDatabase firebaseDatabaseInstance;
    DatabaseReference databaseReference;

    String uniqueComputerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtComputerName = (EditText)findViewById(R.id.edtComputerName);
        edtComputerPower = (EditText)findViewById(R.id.edtComputerPower);
        edtComputerSpeed = (EditText)findViewById(R.id.edtComputerSpeed);
        edtComputerRam = (EditText)findViewById(R.id.edtComputerRam);
        edtComputerScreen = (EditText)findViewById(R.id.edtComputerScreen);
        edtComputerKeyboard = (EditText)findViewById(R.id.edtComputerKeyboard);
        edtComputerCpu = (EditText)findViewById(R.id.edtComputerCpu);

        txtComputerData = (TextView)findViewById(R.id.txtComputerData);

        btnSendOrUpdateData = (Button)findViewById(R.id.btnSendOrUpdateData);
        btnGetDataFromServer = (Button)findViewById(R.id.btnGetDataFromServer);



        changeTheTextOfSendOrUpdateButtonAccordingToTheSituation();



        firebaseDatabaseInstance = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabaseInstance.getReference("Computers");

        firebaseDatabaseInstance.getReference("APPLICATION_NAME").setValue("Firebase Database");

        firebaseDatabaseInstance.getReference("APPLICATION_NAME")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);

                getSupportActionBar().setTitle(value);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnSendOrUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String computerName = edtComputerName.getText().toString();
                String computerPower = edtComputerPower.getText().toString();
                String computerSpeed = edtComputerSpeed.getText().toString();
                String computerRam = edtComputerRam.getText().toString();
                String computerScreen = edtComputerScreen.getText().toString();
                String computerKeyboard = edtComputerKeyboard.getText().toString();
                String computerCPU = edtComputerCpu.getText().toString();

                int computerPowerIntegerValue = 0;
                int computerSpeedIntegerValue = 0;
                int computerRamIntegerValue = 0;

                try {
                    computerPowerIntegerValue = Integer.parseInt(computerPower);
                }catch (Exception e){
                    Log.i("TAG", e.getMessage());
                }

                try {
                    computerSpeedIntegerValue = Integer.parseInt(computerSpeed);
                }catch (Exception e){
                    Log.i("TAG", e.getMessage());
                }

                try {
                    computerRamIntegerValue = Integer.parseInt(computerRam);
                }catch (Exception e){
                    Log.i("TAG", e.getMessage());
                }

                if (TextUtils.isEmpty(uniqueComputerID)){

                    produceANewComputerAndPutItInTheDatabase(computerName, computerPowerIntegerValue,
                            computerSpeedIntegerValue, computerRamIntegerValue, computerScreen,
                            computerKeyboard, computerCPU);

                    edtComputerName.setText("");
                    edtComputerPower.setText("");
                    edtComputerSpeed.setText("");
                    edtComputerRam.setText("");
                    edtComputerScreen.setText("");
                    edtComputerKeyboard.setText("");
                    edtComputerCpu.setText("");
                }

                else {
                    modifyTheProduceComputer(computerName, computerPower, computerSpeed,
                                            computerRam, computerScreen, computerKeyboard,
                                            computerCPU);
                }
            }
        });



        btnGetDataFromServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uniqueComputerID == null){

                    return;
                }

                computerDataChangerListener();
            }
        });

    }


    public void produceANewComputerAndPutItInTheDatabase(String computerName, int computerPower,
                                                         int computerSpeed, int computerRam,
                                                         String computerScreen, String computerKeyboard,
                                                         String computerCPU){

        if (TextUtils.isEmpty(uniqueComputerID)){

            uniqueComputerID = databaseReference.push().getKey();
        }
        Computer computer = new Computer(computerName, computerPower, computerSpeed, computerRam,
                                        computerScreen, computerKeyboard, computerCPU);

        databaseReference.child(uniqueComputerID).setValue(computer);

        changeTheTextOfSendOrUpdateButtonAccordingToTheSituation();

    }

    private void changeTheTextOfSendOrUpdateButtonAccordingToTheSituation(){

        if (TextUtils.isEmpty(uniqueComputerID)){
            btnSendOrUpdateData.setText("Create a New Computer Data");
        }
        else {
            btnSendOrUpdateData.setText("Update The Computer Data");
        }

    }

    private void modifyTheProduceComputer(String computerName, String computerPower,
                                          String computerSpeed, String computerRam,
                                          String computerScreen, String computerKeyboard,
                                          String computerCPU){

        if (!TextUtils.isEmpty(computerName)){
            databaseReference.child(uniqueComputerID).child("computerName").setValue(computerName);
        }

        if (!TextUtils.isEmpty(computerPower)){

            try {
                int computerPowerIntValue = Integer.parseInt(computerPower);
                databaseReference.child(uniqueComputerID).child("computerPower")
                        .setValue(computerPowerIntValue);

            }catch (Exception e){
                Log.i("TAG", e.getMessage());
            }
        }

        if (!TextUtils.isEmpty(computerSpeed)){

            try {
                int computerSpeedIntValue = Integer.parseInt(computerSpeed);
                databaseReference.child(uniqueComputerID).child("computerSpeed")
                        .setValue(computerSpeedIntValue);

            }catch (Exception e){
                Log.i("TAG", e.getMessage());
            }
        }

        if (!TextUtils.isEmpty(computerRam)){

            try {
                int computerRamIntValue = Integer.parseInt(computerRam);
                databaseReference.child(uniqueComputerID).child("computerRam")
                        .setValue(computerRamIntValue);

            }catch (Exception e){
                Log.i("TAG", e.getMessage());
            }
        }

        if (!TextUtils.isEmpty(computerScreen)){
            databaseReference.child(uniqueComputerID).child("computerScreen").setValue(computerScreen);
        }

        if (!TextUtils.isEmpty(computerKeyboard)){
            databaseReference.child(uniqueComputerID).child("computerKeyboard").setValue(computerKeyboard);
        }

        if (!TextUtils.isEmpty(computerCPU)){
            databaseReference.child(uniqueComputerID).child("computerCPU").setValue(computerCPU);
        }
    }


    public void computerDataChangerListener(){

        databaseReference.child(uniqueComputerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Computer computer = dataSnapshot.getValue(Computer.class);

                if (computer == null){
                    return;
                }

                txtComputerData.setText("Computer Name : " + computer.getComputerName() + " / " +
                                        "Computer Power : " + computer.getComputerPower() + " / " +
                                        "Computer Speed : " + computer.getComputerSpeed() + " / " +
                                        "Computer Ram : " + computer.getComputerRam() + " / " +
                                        "Computer Screen : " + computer.getComputerScreen() + " / " +
                                        "Computer Keyboard : " + computer.getComputerKeyboard() + " / " +
                                        "Computer CPU : " + computer.getComputerCPU());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

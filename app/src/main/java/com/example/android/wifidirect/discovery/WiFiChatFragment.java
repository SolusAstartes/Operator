
package com.example.android.wifidirect.discovery;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment handles chat related UI which includes a list view for messages
 * and a message entry field with send button.
 */
public class WiFiChatFragment extends Fragment {

    private final static String BATTERY_STATUS = "ŞARJ";
    private final static String ALERT_THRESHOLD = "ALARM EŞİĞİ";
    private final static String COORDINATES = "KOORDİNATLAR";
    private final static String UBIDOTS_CONNECTION_STATUS = "UBIDOTS BAĞLANTI DURUMU";
    private final static String ALERT_THRESHOLD_ACK = "EŞİK DEĞERİ ACK";
    private final static String ACCELERATION_ABOVE_THRESHOLD = "EŞİK ÜSTÜ İVME";
    private final static String TRACK_BUILDING = "BİNAYI TAKİP ET";
    private MediaPlayer mediaPlayer;
    private boolean IsTracking = false;
    private View view;
    private ChatManager chatManager;
    private TextView chatLine;
    private TextView tBatteryStatus;
    private TextView tAlertThreshold;
    private TextView tLocation;
    private TextView tUbidotsConnectionStatus;
    private TextView tAccelerationAboveThreshold;

    private Button ButtonStartTrack;

    public WiFiChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatLine = view.findViewById(R.id.txtChatLine);
        tBatteryStatus = view.findViewById(R.id.battery_status);
        tAlertThreshold = view.findViewById(R.id.alert_threshold);
        tLocation = view.findViewById(R.id.location);
        tUbidotsConnectionStatus = view.findViewById(R.id.ubidots_connection_status);
        tAccelerationAboveThreshold = view.findViewById(R.id.acceleration_above_threshold);
        ButtonStartTrack = view.findViewById(R.id.StartTrack);
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.uyari);

        //      adapter = new ChatMessageAdapter(getActivity(), android.R.id.text1,
        //         items);
        view.findViewById(R.id.button1).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (chatManager != null&&!chatLine.getText().toString().isEmpty()) {
                            //- koymamızın sebebi key-value tarzında işleniyor
                            chatManager.write(ALERT_THRESHOLD + "-" + chatLine.getText().toString());
                            chatLine.setText("");
                            chatLine.clearFocus();
                        }
                    }
                });
        view.findViewById(R.id.StartTrack).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (chatManager != null && !IsTracking) {
                            //- koymamızın sebebi key-value tarzında işleniyor
                            chatManager.write(TRACK_BUILDING + "-" + "BAŞLA");


                        } else if (chatManager != null && IsTracking) {
                            //- koymamızın sebebi key-value tarzında işleniyor
                            chatManager.write(TRACK_BUILDING + "-" + "BITIR");


                        }

                    }
                });
        return view;
    }

    public interface MessageTarget {
        Handler getHandler();
    }

    public void setChatManager(ChatManager obj) {
        chatManager = obj;
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.release();
        mediaPlayer = null;
        // Unregister all sensor listeners in this callback so they don't
        // continue to use resources when the app is stopped.

    }


    public void pushMessage(String readMessage) {
        if (!readMessage.isEmpty()) {
            String[] parts = readMessage.split("-");
            String key = parts[0];
            String value = parts[1];


            if (key.equals(BATTERY_STATUS)) {
                tBatteryStatus.setText((getResources().getString(R.string.placeholder_Battery) + value));


            } else if (key.equals(ALERT_THRESHOLD_ACK)) {
                tAlertThreshold.setText((getResources().getString(R.string.placeholder_AlertThreshold) + value));

            } else if (key.equals(COORDINATES)) {
                tLocation.setText((getResources().getString(R.string.placeholder_location) + value));
                IsTracking = true;
                ButtonStartTrack.setText("Bina Takibini Durdur");


            } else if (key.equals(ACCELERATION_ABOVE_THRESHOLD)) {

                tAccelerationAboveThreshold.setText(getResources().getString(R.string.placeholder_AccelerationAboveThreshold) + value);
                mediaPlayer.start();
                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(600);

            } else if (key.equals(UBIDOTS_CONNECTION_STATUS)) {
                tUbidotsConnectionStatus.setText((getResources().getString(R.string.placeholder_UbidotsConnection) + value));
            }
        }
    }


}

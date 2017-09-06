package co.getchannel.channel.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.utils.ChatBot;
import com.github.bassaer.chatmessageview.views.ChatView;
//import com.mindorks.paracamera.Camera;
import com.tylerjroach.eventsource.EventSource;
import com.tylerjroach.eventsource.EventSourceHandler;
import com.tylerjroach.eventsource.MessageEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import co.getchannel.channel.CHConfiguration;
import co.getchannel.channel.callback.SendMessageComplete;
import co.getchannel.channel.callback.ThreadFetchComplete;
import co.getchannel.channel.R;
import co.getchannel.channel.callback.UploadMessageImageComplete;
import co.getchannel.channel.helpers.CHConstants;
import co.getchannel.channel.models.CHClient;
import co.getchannel.channel.responses.CHMessageImageResponse;
import co.getchannel.channel.responses.CHMessageResponse;
import co.getchannel.channel.responses.CHThreadResponse;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class ChatActivity extends AppCompatActivity implements ThreadFetchComplete,SendMessageComplete,UploadMessageImageComplete {
    private RecyclerView recyclerView;
    private ChatView mChatView;
    private SSEHandler sseHandler = new SSEHandler();
    private ChatActivity activity;
    // Create global camera reference in an activity or fragment
//    Camera camera;

    private EventSource eventSource;
    private void startEventSource() {
        EventSource eventSource;
        Map<String,String> extraHeaderParameters = new HashMap<String,String>() ;
        String clientID = CHClient.currentClient().getClientID()==null?"":CHClient.currentClient().getClientID();
        String appID =  CHConfiguration.getApplicationId();
        extraHeaderParameters.put("X-Channel-Client-ID",clientID);
        extraHeaderParameters.put("X-Channel-Application-Key",appID);
        eventSource = new EventSource.Builder(CHConstants.BASE_URL + "subscribe")
                .eventHandler(sseHandler)
               .headers(extraHeaderParameters)
                .build();
        eventSource.connect();
    }

    private void stopEventSource() {
        if (eventSource != null)
            eventSource.close();
        sseHandler = null;
    }
    private class SSEHandler implements EventSourceHandler {

        public SSEHandler() {
        }

        @Override
        public void onConnect() {
            Log.v("SSE Connected", "True");
        }

        @Override
        public void onMessage(String event, MessageEvent message) {
//            Log.v("SSE Message", event);
//            Log.v("SSE Message: ", message.lastEventId);
//            Log.v("SSE Message: ", message.data);

            try{
                showMessage(new JSONObject(message.data));
            }
            catch (Exception e){

            }
        }

        @Override
        public void onComment(String comment) {
            //comments only received if exposeComments turned on
            Log.v("SSE Comment", comment);
        }

        @Override
        public void onError(Throwable t) {
            Log.v("SSE Error", "");
            //ignore ssl NPE on eventSource.close()
        }

        @Override
        public void onClosed(boolean willReconnect) {
            Log.v("SSE Closed", "reconnect? " + willReconnect);
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
//            src = src.replace("localhost", "10.0.2.2");
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void complete(CHMessageImageResponse data){
        Log.d(CHConstants.kChannel_tag,data.getResult().getData().getUrl());
        co.getchannel.channel.models.internal.Message message = new co.getchannel.channel.models.internal.Message();
        message.setImageData(data.getResult().getData().getUrl());
        CHClient.currentClient().sendImage(activity,message);
    }
    public void complete(CHMessageResponse data){

    }

    public void complete(CHThreadResponse data){
//        recyclerView.setAdapter(new ChatsAdapter(data.getResul   t().getData().getMessages(), R.layout.list_item_movie, getApplicationContext()));
        try{
            for (CHMessageResponse msg : data.getResult().getData().getMessages()) {
                showMessage(msg);
            }
        }catch (Exception e){

        }

    }


    public void showMessage(CHMessageResponse mainObject){
        try  {


            final String text = mainObject.getData().getText();


            String type ="";
            if(mainObject.getData().getCard() != null){
                 type = mainObject.getData().getCard().getType();

            }

            final String name = mainObject.getSender().getName();
            final String imageUrl = mainObject.getSender().getProfilePictureURL();

            final Boolean isFromBusiness = mainObject.getFromBusiness();

            final Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                cal.setTime(sdf.parse(mainObject.getCreatedAt()));// all done
            }catch (Exception e){

            }

            if (type.equals("image")){
                if(mainObject.getData().getCard().getPayload() != null){
                    final String url = mainObject.getData().getCard().getPayload().getUrl();
                    Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                Bitmap profilePic  = getBitmapFromURL(imageUrl);
                                Message receivedMessage = new Message.Builder()
                                        .setUser(new User(0, name, profilePic))
                                        .setRightMessage(!isFromBusiness)
                                        .setPicture(getBitmapFromURL(url))
                                        .setType(Message.Type.PICTURE)
                                        .setCreatedAt(cal)
                                        .hideIcon(!isFromBusiness)
                                        .build();
                                mChatView.receive(receivedMessage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();
                }
            }else{

                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            Bitmap profilePic  = getBitmapFromURL(imageUrl);
                            Message receivedMessage = new Message.Builder()
                                    .setUser(new User(0, name, profilePic))
                                    .setRightMessage(!isFromBusiness)
                                    .setMessageText(text)
                                    .setCreatedAt(cal)
                                    .hideIcon(!isFromBusiness)
                                    .build();
                            mChatView.receive(receivedMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

            }

        }catch (Exception e){

        }
    }
    public void showMessage(JSONObject mainObject){
        try  {

            JSONObject data = mainObject.getJSONObject("data");
            final String text = data.getString("text");

            String type = "";
            try{
                JSONObject messageData = data.getJSONObject("data");
                if (messageData != null){
                    type = messageData.getString("type");
                }
            }catch (Exception e){

            }


            JSONObject sender = mainObject.getJSONObject("sender");
            final String name = sender.getString("name");
            final String imageUrl = sender.getString("profilePictureURL");
            final Boolean isFromBusiness = mainObject.getBoolean("isFromBusiness");

            final Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                cal.setTime(sdf.parse(data.getString("createdAt")));// all done
            }catch (Exception e){

            }

            if (type.equals( "image")){
                JSONObject payload = data.getJSONObject("payload");
                final String url = payload.getString("url");



                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            Message receivedMessage = new Message.Builder()
                                    .setUser(new User(0, name, getBitmapFromURL(imageUrl)))
                                    .setRightMessage(!isFromBusiness)
                                    .setType(Message.Type.PICTURE)
                                    .setPicture(getBitmapFromURL(url))
                                    .setCreatedAt(cal)
                                    .hideIcon(!isFromBusiness)
                                    .build();
                            mChatView.receive(receivedMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }else{

                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            Message receivedMessage = new Message.Builder()
                                    .setUser(new User(0, name, getBitmapFromURL(imageUrl)))
                                    .setRightMessage(!isFromBusiness)
                                    .setMessageText(text)
                                    .setCreatedAt(cal)
                                    .hideIcon(!isFromBusiness)
                                    .build();
                            mChatView.receive(receivedMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

            }

        }catch (Exception e){

        }
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Get the bitmap and image path onActivityResult of an activity or fragment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), options);
                if(bitmap != null) {
//                picFrame.setImageBitmap(bitmap);
//                co.getchannel.channel.models.internal.Message m = new co.getchannel.channel.models.internal.Message();
//                m.setText(mChatView.getInputText());
//                CHClient.currentClient().sendMessage(activity,m);
                Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
                final User me = new User(0, "Tui", myIcon);
                //new message
                Message message = new Message.Builder()
                        .setUser(me)
                        .setRightMessage(true)
                        .setPicture(bitmap)
                        .hideIcon(true)
                        .setType(Message.Type.PICTURE)
                        .build();
                //Set to chat view
                mChatView.send(message);
                //Reset edit text
                mChatView.setInputText("");
                    CHClient.currentClient().uploadMessageImage(activity,toBase64(bitmap));

            }

            }



        });
//        if(requestCode == Camera.REQUEST_TAKE_PHOTO){
//            Bitmap bitmap = camera.getCameraBitmap();
//            if(bitmap != null) {
////                picFrame.setImageBitmap(bitmap);
////                co.getchannel.channel.models.internal.Message m = new co.getchannel.channel.models.internal.Message();
////                m.setText(mChatView.getInputText());
////                CHClient.currentClient().sendMessage(activity,m);
//                Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
//                final User me = new User(0, "Tui", myIcon);
//                //new message
//                Message message = new Message.Builder()
//                        .setUser(me)
//                        .setRightMessage(true)
//                        .setPicture(bitmap)
//                        .hideIcon(true)
//                        .setType(Message.Type.PICTURE)
//                        .build();
//                //Set to chat view
//                mChatView.send(message);
//                //Reset edit text
//                mChatView.setInputText("");
//
//            }else{
////                Toast.makeText(this.getApplicationContext(),"Picture not taken!",Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity = this;

// Build the camera

//        camera = new Camera.Builder()
//                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
//                .setTakePhotoRequestCode(1)
//                .setDirectory("pics")
//                .setName("channel_" + System.currentTimeMillis())
//                .setImageFormat(Camera.IMAGE_JPEG)
//                .setCompression(75)
//                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
//                .build(this);


        Intent intent = getIntent();
        HashMap<String, String> userData = (HashMap<String, String>)intent.getSerializableExtra("userData");
        String userID = (String)intent.getSerializableExtra("userID");
        CHClient.updateClientData(userID,userData);
        CHClient.activeThread(this);

        //User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        String myName = "Michael";

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Emily";

        final User me = new User(myId, myName, myIcon);
        final User you = new User(yourId, yourName, yourIcon);

        mChatView = (ChatView)findViewById(R.id.chat_view);

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.teal100));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan900));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);
        mChatView.setAutoScroll(true);

        mChatView.setOnClickOptionButtonListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 try {
                                                     mChatView.hideKeyboard();
//                                                     EasyImage.openCamera(activity,1);
                                                     EasyImage.openChooserWithGallery(activity,"",1);

                                                 }catch (Exception e){
                                                     e.printStackTrace();
                                                 }
                                             }
                                         });
        //Click Send Button
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                co.getchannel.channel.models.internal.Message m = new co.getchannel.channel.models.internal.Message();
                m.setText(mChatView.getInputText());
                CHClient.currentClient().sendMessage(activity,m);

                //new message
                Message message = new Message.Builder()
                        .setUser(me)
                        .setRightMessage(true)
                        .setMessageText(mChatView.getInputText())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                mChatView.send(message);
                //Reset edit text
                mChatView.setInputText("");
//
                //Receive message
//                final Message receivedMessage = new Message.Builder()
//                        .setUser(you)
//                        .setRightMessage(false)
//                        .setMessageText(ChatBot.talk(me.getName(), "xxx"))
//                        .build();
//
//                // This is a demo bot
//                // Return within 3 seconds
//                int sendDelay = (new Random().nextInt(4) + 1) * 1000;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mChatView.receive(receivedMessage);
//                    }
//                }, sendDelay);
            }

        });


        this.startEventSource();
    }
}

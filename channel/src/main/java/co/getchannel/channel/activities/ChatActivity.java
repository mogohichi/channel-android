package co.getchannel.channel.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import co.getchannel.channel.CHConfiguration;
import co.getchannel.channel.callback.SendMessageComplete;
import co.getchannel.channel.callback.ThreadFetchComplete;
import co.getchannel.channel.R;
import co.getchannel.channel.common.data.MessagesFixtures;
import co.getchannel.channel.helpers.CHConstants;
import co.getchannel.channel.models.CHClient;
import co.getchannel.channel.models.ui.Message;
import co.getchannel.channel.models.ui.User;
import co.getchannel.channel.responses.CHMessageResponse;
import co.getchannel.channel.responses.CHThreadResponse;
import co.getchannel.channel.callback.UploadMessageImageComplete;
import co.getchannel.channel.helpers.CHConstants;
import co.getchannel.channel.models.CHClient;
import co.getchannel.channel.responses.CHMessageImageResponse;
import co.getchannel.channel.responses.CHMessageResponse;
import co.getchannel.channel.responses.CHThreadResponse;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class ChatActivity extends AppCompatActivity implements ThreadFetchComplete,SendMessageComplete,UploadMessageImageComplete,com.stfalcon.chatkit.messages.MessagesListAdapter.SelectionListener,
        com.stfalcon.chatkit.messages.MessagesListAdapter.OnLoadMoreListener,com.stfalcon.chatkit.messages.MessageInput.InputListener,
        com.stfalcon.chatkit.messages.MessageInput.AttachmentsListener  {

    private RecyclerView recyclerView;


    private SSEHandler sseHandler = new SSEHandler();
    private ChatActivity activity;
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private MessagesList messagesList;


    private Menu menu;
    private int selectionCount;
    private Date lastLoadedDate;


    private EventSource eventSource;

    @Override
    public void onSelectionChanged(int count) {

    }
    @Override
    public void onLoadMore(int page, int totalItemsCount) {

    }

    @Override
    public void onAddAttachments() {
        messagesAdapter.addToStart(
                MessagesFixtures.getImageMessage(), true);
    }

    @Override
    public boolean onSubmit(CharSequence input) {

        User u = new User("www","tui","https://cdn.getchannel.co/img/characters/2.png",false);
        Message m = new Message("0",u,input.toString());
      //messagesAdapter.addToStart(MessagesFixtures.getTextMessage(input.toString()), true);
      messagesAdapter.addToStart(m, true);
        return true;
    }


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


    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
        return new MessagesListAdapter.Formatter<Message>() {
            @Override
            public String format(Message message) {
                String createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        .format(message.getCreatedAt());

                String text = message.getText();
                if (text == null) text = "[attachment]";

                return String.format(Locale.getDefault(), "%s: %s (%s)",
                        message.getUser().getName(), text, createdAt);
            }
        };
    }

    public void complete(CHThreadResponse data){
        final ArrayList<Message> messages = new ArrayList<Message>();
        for (CHMessageResponse msg : data.getResult().getData().getMessages()) {
            User u = new User(msg.getSender().getClientID(),msg.getSender().getName(),msg.getSender().getProfilePictureURL(),false);
            Message m = new Message(msg.getData().getText(),u,msg.getCreatedAt());
            messages.add(m);
        }

        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {
                messagesAdapter.addToEnd(messages, false);
            }
        }, 1000);
//        recyclerView.setAdapter(new ChatsAdapter(data.getResul   t().getData().getMessages(), R.layout.list_item_movie, getApplicationContext()));

//        for (CHThreadResponse.CHThreadResult.CHThreadData.CHThreadMessage msg : data.getResult().getData().getMessages()) {
//            //User id
//            int myId = 0;
//            //User icon
//            Bitmap myIcon  = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
//
////            try {
////                String img = msg.getSender().getProfilePictureURL();
////                URL url = new URL(msg.getSender().getProfilePictureURL());
////                myIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
////            } catch(Exception e) {
////
////            }
//            //User name
//            String myName = msg.getSender().getName();
//            //new message
//            final User me = new User(myId, myName, myIcon);
//
//            Calendar cal = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//            try {
//                cal.setTime(sdf.parse(msg.getCreatedAt()));// all done
//            }catch (Exception e){
//
//            }
//
//
//            if (msg.getFromBusiness()){
//                Message message = new Message.Builder()
//                        .setUser(me)
//                        .setRightMessage(false)
//                        .setMessageText(msg.getData().getText())
//                        .setCreatedAt(cal)
//                        .hideIcon(false)
//                        .build();
//
//                //Set to chat view
//                mChatView.receive(message);
//            }else{
//                Message message = new Message.Builder()
//                        .setUser(me)
//                        .setRightMessage(true)
//                        .setMessageText(msg.getData().getText())
//                        .setCreatedAt(cal)
//                        .hideIcon(true)
//                        .build();
//                //Set to chat view
//                mChatView.send(message);
//            }
//
//        }
    }


    private void initAdapter() {
        messagesAdapter = new MessagesListAdapter<>(CHClient.currentClient().getClientID(), imageLoader);
        messagesAdapter.enableSelectionMode(this);
        messagesAdapter.setLoadMoreListener(this);
        messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
                new MessagesListAdapter.OnMessageViewClickListener<Message>() {
                    @Override
                    public void onMessageViewClick(View view, Message message) {
//                        AppUtils.showToast(DefaultMessagesActivity.this,
//                                message.getUser().getName() + " avatar click",
//                                false);
                    }
                });
        this.messagesList.setAdapter(messagesAdapter);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity = this;
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(activity).load(url).into(imageView);
            }
        };

        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        initAdapter();


        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
//        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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


//        //User id
//        int myId = 0;
//        //User icon
//        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
//        //User name
//        String myName = "Michael";
//
//        int yourId = 1;
//        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
//        String yourName = "Emily";
//
//        final User me = new User(myId, myName, myIcon);
//        final User you = new User(yourId, yourName, yourIcon);
//
//        mChatView = (ChatView)findViewById(R.id.chat_view);
//
//        //Set UI parameters if you need
//        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
//        mChatView.setLeftBubbleColor(Color.WHITE);
//        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.teal100));
//        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan900));
//        mChatView.setSendIcon(R.drawable.ic_action_send);
//        mChatView.setRightMessageTextColor(Color.WHITE);
//        mChatView.setLeftMessageTextColor(Color.BLACK);
//        mChatView.setUsernameTextColor(Color.WHITE);
//        mChatView.setSendTimeTextColor(Color.WHITE);
//        mChatView.setDateSeparatorColor(Color.WHITE);
//        mChatView.setInputTextHint("new message...");
//        mChatView.setMessageMarginTop(5);
//        mChatView.setMessageMarginBottom(5);
//
//        //Click Send Button
//        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                co.getchannel.channel.models.internal.Message m = new co.getchannel.channel.models.internal.Message();
//                m.setText(mChatView.getInputText());
//                CHClient.currentClient().sendMessage(activity,m);
//
//                //new message
//                Message message = new Message.Builder()
//                        .setUser(me)
//                        .setRightMessage(true)
//                        .setMessageText(mChatView.getInputText())
//                        .hideIcon(true)

//
                //Receive message
//                final Message receivedMessage = new Message.Builder()
//                        .setUser(you)
//                        .setRightMessage(false)
//                        .setMessageText(ChatBot.talk(me.getName(), "xxx"))
//                        .build();
//                //Set to chat view
//                mChatView.send(message);
//                //Reset edit text
//                mChatView.setInputText("");
////
////                //Receive message
////                final Message receivedMessage = new Message.Builder()
////                        .setUser(you)
////                        .setRightMessage(false)
////                        .setMessageText(ChatBot.talk(me.getName(), message.getMessageText()))
////                        .build();
////
////                // This is a demo bot
////                // Return within 3 seconds
////                int sendDelay = (new Random().nextInt(4) + 1) * 1000;
////                new Handler().postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        mChatView.receive(receivedMessage);
////                    }
////                }, sendDelay);
//            }
//
//        });


        this.startEventSource();
    }
}

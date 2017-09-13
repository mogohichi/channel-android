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
        EasyImage.openChooserWithGallery(activity,"",1);
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        co.getchannel.channel.models.internal.Message msg = new co.getchannel.channel.models.internal.Message();
        msg.setText(input.toString());
        CHClient.currentClient().sendMessage(activity,msg);

        User u = new User("","channel","imageProfile",false);
        Message m = new Message("messageID",u,input.toString());
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
            try{
                JSONObject  mainObject = new JSONObject(message.data);
                JSONObject data = mainObject.getJSONObject("data");

                String type = "";
                try{
                    JSONObject messageData = data.getJSONObject("card");
                    if (messageData != null){
                        type = messageData.getString("type");
                    }
                }catch (Exception e){

                }
                JSONObject sender = mainObject.getJSONObject("sender");
                User u = new User(sender.getString("clientID"),sender.getString("name"),sender.getString("profilePictureURL"),false);
                final Message m = new Message(mainObject.getString("id"),u,null);
                if(type.equals("image")){
                    String url = "http://www.cinematografo.it/wp-content/uploads/2015/07/minions1.jpg"; //data.getJSONObject("card").getJSONObject("payload").getString("url");
                    m.setText("sent an attachment");
                    m.setImage(new Message.Image(url));
                }else{
                    m.setText(data.getString("text"));
                }

                // Get a handler that can be used to post to the main thread
                Handler mainHandler = new Handler(Looper.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {messagesAdapter.addToStart(m,true);} // This is your code
                };
                mainHandler.post(myRunnable);


            }catch (Exception e){

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

    //upload complete
    public void complete(CHMessageImageResponse data){
        Log.d(CHConstants.kChannel_tag,data.getResult().getData().getUrl());
        co.getchannel.channel.models.internal.Message message = new co.getchannel.channel.models.internal.Message();
        message.setImageData(data.getResult().getData().getUrl());
        CHClient.currentClient().sendImage(activity,message);
        User u = new User("","channel","imageProfile",false);
        Message m = new Message("",u,"sent and attachment");
        m.setImage(new Message.Image(data.
                getResult().getData().getUrl()));
        //messagesAdapter.addToStart(MessagesFixtures.getTextMessage(input.toString()), true);
        messagesAdapter.addToStart(m, true);

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
            Message m = new Message(msg.getID(),u,null);

            if (msg.getData().getCard() != null){
                //
                String url = "http://www.cinematografo.it/wp-content/uploads/2015/07/minions1.jpg"; //msg.getData().getCard().getPayload().getUrl();
                m.setImage(new Message.Image(url));
                m.setText("sent an attachment");
            }else{
                m.setText(msg.getData().getText());
            }
            messages.add(m);
        }

        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {
                messagesAdapter.addToEnd(messages, true);
            }
        }, 1000);
    }


    private void initAdapter() {
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(activity).load(url).into(imageView);
            }
        };

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
                    CHClient.currentClient().uploadMessageImage(activity,toBase64(bitmap));
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity = this;

        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        initAdapter();


        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
        input.setAttachmentsListener(this);



        Intent intent = getIntent();
        HashMap<String, String> userData = (HashMap<String, String>)intent.getSerializableExtra("userData");
        String userID = (String)intent.getSerializableExtra("userID");
        CHClient.updateClientData(userID,userData);
        CHClient.activeThread(this);

        this.startEventSource();
    }
}

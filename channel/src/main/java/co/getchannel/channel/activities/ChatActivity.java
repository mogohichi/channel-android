package co.getchannel.channel.activities;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.launchdarkly.eventsource.EventHandler;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import co.getchannel.channel.CHConfiguration;
import co.getchannel.channel.callback.NotificationFetchComplete;
import co.getchannel.channel.callback.SendMessageComplete;
import co.getchannel.channel.callback.ThreadFetchComplete;
import co.getchannel.channel.R;
import co.getchannel.channel.helpers.CHConstants;
import co.getchannel.channel.models.CHClient;
import co.getchannel.channel.callback.ChannelProcessComplete;
import co.getchannel.channel.models.ui.Message;
import co.getchannel.channel.models.ui.User;
import co.getchannel.channel.responses.CHMessageResponse;
import co.getchannel.channel.responses.CHNotificationResponse;
import co.getchannel.channel.responses.CHThreadResponse;
import co.getchannel.channel.callback.UploadMessageImageComplete;
import co.getchannel.channel.responses.CHMessageImageResponse;

import okhttp3.Headers;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class ChatActivity extends AppCompatActivity implements ThreadFetchComplete,SendMessageComplete,UploadMessageImageComplete,NotificationFetchComplete,com.stfalcon.chatkit.messages.MessagesListAdapter.SelectionListener,
        com.stfalcon.chatkit.messages.MessagesListAdapter.OnLoadMoreListener,com.stfalcon.chatkit.messages.MessageInput.InputListener,
        com.stfalcon.chatkit.messages.MessageInput.AttachmentsListener  {

    private ChatActivity activity;
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private MessagesList messagesList;
    private MessageInput messageInput;



    private com.launchdarkly.eventsource.EventSource eventSource;
    private EventHandler sseHandler = new EventHandler() {
        @Override
        public void onOpen() throws Exception {
            Log.d("sse","onOpen");
        }

        @Override
        public void onClosed() throws Exception {
            Log.d("sse","onClosed");
        }

        @Override
        public void onMessage(String event, com.launchdarkly.eventsource.MessageEvent messageEvent) throws Exception {
            Log.d("sse","onMessage");
            try{
                List<CHMessageResponse> messages = new ArrayList<CHMessageResponse>();
                CHMessageResponse msg = new Gson().fromJson(messageEvent.getData(), CHMessageResponse.class);
                messages.add(msg);
                showMessages(messages);

            }catch (Exception e){

            }
        }

        @Override
        public void onComment(String comment) throws Exception {
            Log.d("sse","onComment");
        }

        @Override
        public void onError(Throwable t) {
            Log.d("sse","onError");
        }
    };

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
        final String inputMessage = input.toString();
        co.getchannel.channel.models.internal.Message msg = new co.getchannel.channel.models.internal.Message();
        msg.setText(inputMessage);

        CHClient.currentClient().sendMessage(activity, msg, new ChannelProcessComplete() {
            @Override
            public void onSuccess() {
                User u = new User(CHClient.currentClient().getClientID(),"me","imageProfile",false);
                final Message m = new Message("messageID",u,inputMessage);

                Handler mainHandler = new Handler(Looper.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        messagesAdapter.addToStart(m, true);
                    } // This is your code
                };
                mainHandler.post(myRunnable);

            }

            @Override
            public void onFail(String message) {

            }
        });
        return true;
    }



    private void stopEventSource() {
        if (eventSource != null){
            try{
                eventSource.close();
            }catch (Exception e){

            }
        }

        eventSource = null;
    }

    private void startSubscribe() {
        try{
            //this subscribe find active thread id on server and subscibe it
            String clientID = CHClient.currentClient().getClientID()==null?"":CHClient.currentClient().getClientID();
            String appID =  CHConfiguration.getApplicationId();

            String[] nameAndValues = {"X-Channel-Client-ID",clientID,"X-Channel-Application-Key",appID};

            com.launchdarkly.eventsource.EventSource.Builder b
                    = new com.launchdarkly.eventsource.EventSource.Builder(sseHandler, java.net.URI.create(CHConstants.BASE_URL + "subscribe"))
                    .headers(Headers.of(nameAndValues));

            eventSource = new com.launchdarkly.eventsource.EventSource(b);
            eventSource.start();

        }catch (Exception e){

        }

    }

    private void showMessages(List<CHMessageResponse> reponseMessages){
        final ArrayList<Message> messages = new ArrayList<Message>();
        CHMessageResponse latestMessage = null;
        for (CHMessageResponse msg : reponseMessages) {

            String pic = msg.getSender().getProfilePictureURL();
            User u = new User(msg.getSender().getClientID(),msg.getSender().getName(),pic,false);
            Message m = new Message(msg.getID(),u,null);


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date date = format.parse(msg.getCreatedAt());
                m.setCreatedAt(date);
            } catch (Exception e) {

            }

            if (msg.getData().getCard() != null){
                //
                String url = msg.getData().getCard().getPayload().getUrl();
                m.setImage(new Message.Image(url));
                m.setText("sent an attachment");
            }else{
                m.setText(msg.getData().getText());
            }

            latestMessage = msg;
            messages.add(m);
        }

        if (latestMessage != null) {
            if (latestMessage.getData().getButtons() != null) {
                showQuickReply(latestMessage.getData().getButtons());
            }
        }

        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
               if( messages.size() == 1){
                   messagesAdapter.addToStart(messages.get(0),true);
               }else{
                   messagesAdapter.addToEnd(messages,true );
               }

            } // This is your code
        };
        mainHandler.post(myRunnable);
    }



    public void showQuickReply(final List<CHMessageResponse.CHMessageData.CHButton> buttons){

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                messageInput.getButton().setVisibility(View.GONE);
                messageInput.getAttachmentButton().setVisibility(View.GONE);
                messageInput.getInputEditText().setVisibility(View.GONE);

                HorizontalScrollView sv= new HorizontalScrollView(activity);
                sv.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT,ScrollView.LayoutParams.WRAP_CONTENT));
                messageInput.addView(sv,0);

                LinearLayout layout = new LinearLayout(activity);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
                sv.addView(layout);

                for (final CHMessageResponse.CHMessageData.CHButton button : buttons) {
                    final Button btnTag = new Button(activity);
                    int[][] states = new int[][] {
                            new int[] { android.R.attr.state_enabled}, // enabled
                    };

                    int[] colors = new int[] {
                            button.getBackgroundColor() == null ? Color.parseColor("#0080FF") :
                                    Color.parseColor(button.getBackgroundColor()),
                    };

                    ColorStateList myList = new ColorStateList(states, colors);
                    btnTag.setTransformationMethod(null);
                    btnTag.setBackgroundTintList(myList);
                    btnTag.setTextColor(button.getTextColor() == null ? Color.parseColor("#FFFFFF") :
                            Color.parseColor(button.getTextColor()));
                    btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    btnTag.setText(button.getTitle());
                    //   btnTag.setId(i);
                    btnTag.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Perform action on click
                            messageInput.getButton().setVisibility(View.VISIBLE);
                            messageInput.getAttachmentButton().setVisibility(View.VISIBLE);
                            messageInput.getInputEditText().setVisibility(View.VISIBLE);
                            messageInput.removeViewAt(0);

                            co.getchannel.channel.models.internal.Message msg = new co.getchannel.channel.models.internal.Message();
                            msg.setText(button.getTitle());
                            msg.setPostback(button.getPayload() == null ? "" :button.getPayload());
                            CHClient.currentClient().sendMessage(activity, msg, new ChannelProcessComplete() {
                                @Override
                                public void onSuccess() {
                                    User u = new User(CHClient.currentClient().getClientID(),"me","imageProfile",false);
                                    Message m = new Message("messageID",u,button.getTitle());
                                    messagesAdapter.addToStart(m, true);

                                    if (button.getType().equals("web_url")){
                                        String url = button.getUrl();
                                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                            url = "http://" + url;
                                        }
                                        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(browserIntent);
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onFail(String message) {

                                }
                            });

                        }
                    });
                    layout.addView(btnTag, new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                }

            }
        });
    }

    //upload complete
    public void complete(CHMessageImageResponse data){
        final String imageData = data.getResult().getData().getUrl();
        co.getchannel.channel.models.internal.Message message = new co.getchannel.channel.models.internal.Message();
        message.setImageData(imageData);
        CHClient.currentClient().sendImage(activity, message, new ChannelProcessComplete() {
            @Override
            public void onSuccess() {
                User u = new User(CHClient.currentClient().getClientID(),"me","imageProfile",false);
                final Message m = new Message("",u,"sent an attachment");
                m.setImage(new Message.Image(imageData));
                //messagesAdapter.addToStart(MessagesFixtures.getTextMessage(input.toString()), true);


                Handler mainHandler = new Handler(Looper.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        messagesAdapter.addToStart(m, true);
                    } // This is your code
                };
                mainHandler.post(myRunnable);
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    //sent message complete
    public void complete(CHMessageResponse data){

    }

    //fetch notification complete
    public void complete(CHNotificationResponse data){

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


    //fetch complete
    public void complete(CHThreadResponse data){
        showMessages(data.getResult().getData().getMessages());
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
                final Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), options);
                if(bitmap != null) {
                    CHClient.currentClient().uploadMessageImage(activity, toBase64(bitmap), new ChannelProcessComplete() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail(String message) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroy () {
        //do your stuff here
        this.stopEventSource();
        super.onDestroy();

    }

    @Override
    public void onStop () {
        //do your stuff here
        super.onStop();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity = this;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        initAdapter();

        messageInput = (MessageInput) findViewById(R.id.input);
        messageInput.setInputListener(this);
        messageInput.setAttachmentsListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            HashMap<String, String> userData = (HashMap<String, String>)intent.getSerializableExtra("userData");
            String userID = (String)intent.getSerializableExtra("userID");
            CHClient.updateClientData(userID, userData, new ChannelProcessComplete() {
                @Override
                public void onSuccess() {
                    CHClient.activeThread(activity, new ChannelProcessComplete() {
                        @Override
                        public void onSuccess() {
                            initAdapter();
                        }

                        @Override
                        public void onFail(String message) {

                        }
                    });
                }
                @Override
                public void onFail(String message) {

                }
            });
        }

        this.startSubscribe();
    }
}

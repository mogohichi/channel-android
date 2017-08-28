package co.getchannel.channel.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.utils.ChatBot;
import com.github.bassaer.chatmessageview.views.ChatView;
import com.tylerjroach.eventsource.EventSource;
import com.tylerjroach.eventsource.EventSourceHandler;
import com.tylerjroach.eventsource.MessageEvent;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import co.getchannel.channel.CHConfiguration;
import co.getchannel.channel.callback.SendMessageComplete;
import co.getchannel.channel.callback.ThreadFetchComplete;
import co.getchannel.channel.R;
import co.getchannel.channel.helpers.CHConstants;
import co.getchannel.channel.models.CHClient;
import co.getchannel.channel.responses.CHMessageResponse;
import co.getchannel.channel.responses.CHThreadResponse;

public class ChatActivity extends AppCompatActivity implements ThreadFetchComplete,SendMessageComplete {
    private RecyclerView recyclerView;
    private ChatView mChatView;
    private SSEHandler sseHandler = new SSEHandler();
    private ChatActivity activity;

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


            try  {
                JSONObject mainObject = new JSONObject(message.data);
                JSONObject data = mainObject.getJSONObject("data");
                String text = data.getString("text");

                JSONObject sender = mainObject.getJSONObject("sender");
                String name = sender.getString("name");

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    cal.setTime(sdf.parse(data.getString("createdAt")));// all done
                }catch (Exception e){

                }
                Bitmap myIcon  = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
                final User me = new User(0, name, myIcon);
                final Message receivedMessage = new Message.Builder()
                        .setUser(me)
                        .setRightMessage(false)
                        .setMessageText(text)
                        .setCreatedAt(cal)
                        .hideIcon(false)
                        .build();
                mChatView.receive(receivedMessage);

//                // Return within 3 seconds
//                int sendDelay = (new Random().nextInt(4) + 1) * 1000;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mChatView.receive(receivedMessage);
//                    }
//                }, sendDelay);


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

    public void complete(CHMessageResponse data){

    }

    public void complete(CHThreadResponse data){
//        recyclerView.setAdapter(new ChatsAdapter(data.getResul   t().getData().getMessages(), R.layout.list_item_movie, getApplicationContext()));

        for (CHThreadResponse.CHThreadResult.CHThreadData.CHThreadMessage msg : data.getResult().getData().getMessages()) {
            //User id
            int myId = 0;
            //User icon
            Bitmap myIcon  = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);

//            try {
//                String img = msg.getSender().getProfilePictureURL();
//                URL url = new URL(msg.getSender().getProfilePictureURL());
//                myIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            } catch(Exception e) {
//
//            }
            //User name
            String myName = msg.getSender().getName();
            //new message
            final User me = new User(myId, myName, myIcon);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                cal.setTime(sdf.parse(msg.getCreatedAt()));// all done
            }catch (Exception e){

            }


            if (msg.getFromBusiness()){
                Message message = new Message.Builder()
                        .setUser(me)
                        .setRightMessage(false)
                        .setMessageText(msg.getData().getText())
                        .setCreatedAt(cal)
                        .hideIcon(false)
                        .build();

                //Set to chat view
                mChatView.receive(message);
            }else{
                Message message = new Message.Builder()
                        .setUser(me)
                        .setRightMessage(true)
                        .setMessageText(msg.getData().getText())
                        .setCreatedAt(cal)
                        .hideIcon(true)
                        .build();
                //Set to chat view
                mChatView.send(message);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity = this;

//        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        Intent intent = getIntent();
//        HashMap<String, String> userData = (HashMap<String, String>)intent.getSerializableExtra("userData");
//        String userID = (String)intent.getSerializableExtra("userID");
//        CHClient.updateClientData(userID,userData);
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
//                //Receive message
//                final Message receivedMessage = new Message.Builder()
//                        .setUser(you)
//                        .setRightMessage(false)
//                        .setMessageText(ChatBot.talk(me.getName(), message.getMessageText()))
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

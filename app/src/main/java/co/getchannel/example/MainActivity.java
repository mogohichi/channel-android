package co.getchannel.example;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import co.getchannel.channel.Channel;
import co.getchannel.channel.api.CHAPI;
import co.getchannel.channel.api.CHAPIInterface;
import co.getchannel.channel.helpers.CHConstants;
import co.getchannel.channel.models.Agent;
import co.getchannel.channel.responses.CHApplicationInfoResponse;
import co.getchannel.channel.responses.CHClientResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WeakReference<Activity> weakReference = new WeakReference<Activity>((Activity)this);
        Channel.setupActivityWithApplicationId(weakReference,"app_dK5cqQHzHiSYE6DfW7SfCUDemfj444LmEmow0OkpyUw");
        Channel.applicationInfo();
    }
}

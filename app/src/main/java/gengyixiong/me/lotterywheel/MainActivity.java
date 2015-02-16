package gengyixiong.me.lotterywheel;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    private static final long ONE_WHEEL_TIME = 500; //point one round time && light show interval
    private boolean lightsOn = true; //if the lights are on?
    private int startDegree = 0;     //initial degree

    private ImageView lightImageView;
    private ImageView pointImageView;

    private int[] laps = {15, 17, 20, 25};
    private int[] angles = {0, 60, 120, 180, 240, 300}; //six options, 60 degree
    private String[] lotterStr = {"Sony PSP", "10 Lucky Money", "Thanks", "DNF Wallet", "OPPO MP3", "5 Lucky Money"};

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if(lightsOn){
                        lightImageView.setVisibility(View.INVISIBLE);
                        lightsOn = false;
                    }else{
                        lightImageView.setVisibility(View.VISIBLE);
                        lightsOn = true;
                    }
                break;

                default:
                break;
            }
        }
    };

    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            String name = lotterStr[startDegree % 360 / 60];
            Toast.makeText(MainActivity.this, name, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightImageView = (ImageView)findViewById(R.id.light);
        pointImageView = (ImageView)findViewById(R.id.point);

        flashLights();
        pointImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lap = laps[(int)(Math.random()*4)];
                int angle = angles[(int)(Math.random()*6)];
                int increasDegree = lap * 360 + angle;
                RotateAnimation rotateAnimation = new RotateAnimation(
                        startDegree, startDegree + increasDegree,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f
                );
                startDegree += increasDegree;   //assign last time end degree to new start degree
                long time = (lap + angle / 360) * ONE_WHEEL_TIME;
                rotateAnimation.setDuration(time);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setAnimationListener(animationListener);
                pointImageView.setAnimation(rotateAnimation);
            }
        });
    }


    private void flashLights(){
        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(tt, 0, ONE_WHEEL_TIME);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.myAndroid.helloworld.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.myAndroid.helloworld.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressLint({"NewApi", "InflateParams"})
public class MainActivity extends Activity {
    private final static int NOTIFICATION_ID = 1;
    @Bind(R.id.link_url)
    TextView linkUrl;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.button4)
    Button button4;

    private Notification notification;
    private NotificationManager notificationManager;

    private Menu barMenu;

    public void IntentManager(View view) {
        try {
            Intent i = new Intent(this, Class.forName(view.getTag().toString()));
            startActivity(i);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void notification(View view) {
        if (notification == null) {
            Intent intent = new Intent(this, ThirdActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            // 注意这里Builder的用法,设定了很多Notification的属性,而其中setContent(RemoteView)可以使你用一个自定义的布局来填充Notification(此方法并未例句出来,因为会覆盖这之前所有的显示设置)
            RemoteViews remoteViews = new RemoteViews(MainActivity.this.getPackageName(), R.layout.mynotification);

            notification = new Notification.Builder(this).setContentText("Hello World!").setContentTitle("Hello").setSmallIcon(R.drawable.ic_notification).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.image_view2)).setContentIntent(pendingIntent).setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS).setTicker("Come on!Baby!").build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
        } else {

        }
    }

    @Override
    // Action Bar
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        if (null == barMenu) {
            barMenu = menu;// 第一次加载时,为barMenu赋值,保存此menu的引用
        }

        barMenu.clear();

        /**
         * mi1使用编码方式填充其布局显示
         */
        // begin
        MenuItem mi1 = barMenu.add(0, 0, 0, "Item1");// 第三个int参数用来指定按钮的排列顺序,具体可以看其源码的参数命名
        mi1.setIcon(R.drawable.ic_001);
        mi1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        // LayoutInflater layoutInflater = getLayoutInflater();这种方式也可以
        View view = layoutInflater.inflate(R.layout.actionbar_item_view, null);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.actionBarItemLayout);

        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        final TextView newTextView = new TextView(this);
        ColorStateList colorStateList = getResources().getColorStateList(R.color.white);
        newTextView.setText("M1-ActionView");
        newTextView.setTextColor(colorStateList);
        newTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "ActionBar item", Toast.LENGTH_SHORT).show();
            }
        });

        RelativeLayout.LayoutParams rLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // rLayoutParams.addRule(RelativeLayout.RIGHT_OF,textView.getId());// 设置rgithOf属性,其他的可举一反三
        relativeLayout.addView(newTextView, rLayoutParams);

        mi1.setActionView(relativeLayout);
        // end

        MenuItem mi2 = barMenu.add(0, 1, 1, "Item2");
        mi2.setIcon(R.drawable.ic_launcher);
        mi2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mi2.setTitleCondensed("Short Title");// ????
        mi2.setShortcut('0', 'b');// 快捷键

        MenuItem mi3 = barMenu.add(0, 2, 2, "Item3");
        mi3.setIcon(R.drawable.ic_launcher);
        mi3.setChecked(true);

        MenuItem mi4 = barMenu.add(0, 3, 3, "Item4");
        mi4.setIcon(R.drawable.ic_launcher);

        // 设置单选框模式
        // 第一个参数指定要被设为单选框的menuItem们的groupId
        // 第二个参数必须为true
        // 第三个参数true->单选框;false->复选框
        menu.setGroupCheckable(0, true, true);

        MenuItem mi5 = barMenu.add(1, 4, 4, "Item5");
        mi5.setCheckable(true);// 将menuItem设置成复选框
        mi5.setIcon(R.drawable.image_view2);

        // 添加子菜单
        SubMenu subMenu = barMenu.addSubMenu(1, 5, 5, "SubMenu-1");
        subMenu.setIcon(R.drawable.ic_launcher);
        subMenu.add(0, 66, 0, "SubMenu_Item-1");// 子菜单里每个MenuItem的itemId在onOptionsItemSelected()方法中依然适用
        subMenu.add(0, 88, 1, "SubMenu_Item-2");

        return true;
    }

    @Override
    // 点击bar中的选项时触发的事件
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                /**
                 * 为程序图片添加返回主界面的功能; FLAG_ACTIVITY_CLEAR_TOP是为了清除back
                 * stack中的其他一系列活动,如此一来如果用户单击Back键,应用程序的其他活动将不再显示
                 */
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;
            case 1:
                ActionBar bar = getActionBar();
                bar.setDisplayHomeAsUpEnabled(false);

                break;
            default:
                Toast.makeText(this, "" + item.getItemId(), Toast.LENGTH_SHORT).show();

                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            Toast.makeText(this, data.getStringExtra("back"), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 此选项使得ActionBar悬浮于Activity之上,致使其可能遮盖Activity部分内容
        // getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toast.makeText(MainActivity.this, getResources().getString(R.string.testFormat, "aaa", "bbb"), Toast.LENGTH_SHORT).show();

        linkUrl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Linkify.addLinks((TextView) findViewById(R.id.link_url), Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);

                return false;
            }
        });

        final TextView longContentTextView = (TextView) findViewById(R.id.long_content_textView);
        final TextView moreContentTextView = (TextView) findViewById(R.id.more_content_textView);

        StringBuffer longContent = new StringBuffer();
        for (int i = 0; i < 40; i++) {
            longContent.append("好多内容");
        }
        longContentTextView.setText(longContent.toString());
        moreContentTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "当前行数：" + longContentTextView.getLineCount(), Toast.LENGTH_SHORT).show();
                longContentTextView.setMaxLines(50);
                moreContentTextView.setVisibility(View.GONE);
            }
        });

        // 时间显示
        final TextView timeTextView = (TextView) findViewById(R.id.timeTextView);
        final SimpleDateFormat sdp = new SimpleDateFormat("HH:mm:ss");
        timeTextView.setText("时间: " + sdp.format(new Date()));
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                timeTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        timeTextView.setText("时间: " + sdp.format(new Date()));
                    }
                });
            }
        }, 0, 1, TimeUnit.SECONDS);

        ActionBar bar = getActionBar();

        // bar.setDisplayUseLogoEnabled(false);???
        bar.setDisplayHomeAsUpEnabled(true);// 使action bar可以被点击

        // 硬编码绘制ActionBar的内容-begin
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.actionbar_view, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.actionBarLayout);

        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        final TextView newTextView1 = new TextView(this);
        //        newTextView1.setId(1993);
        ColorStateList colorStateList = getResources().getColorStateList(R.color.white);
        newTextView1.setText("/ActionBar1");
        newTextView1.setTextColor(colorStateList);
        newTextView1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newTextView1.setBackgroundColor(0x286BB9);
            }
        });

        LinearLayout.LayoutParams rLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.addView(newTextView1, rLayoutParams);

        final TextView newTextView = new TextView(this);
        newTextView.setText("/ActionBar2");
        newTextView.setTextColor(colorStateList);
        newTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newTextView1.setBackgroundColor(0x286BB9);
            }
        });

        linearLayout.addView(newTextView, rLayoutParams);

        bar.setDisplayShowCustomEnabled(true);// 使ActionBar可以显示自定义的界面
        bar.setCustomView(view);
        // 硬编码绘制ActionBar的内容-end

        // bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST); // 下拉框模式ActionBar

        // 显示意图,并等待回值
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("success", "The Intent has been success!");
                startActivityForResult(intent, 100);
            }
        });

        // 下拉框
        String[] names = {"abc", "efg", "hig"};
        spinner.setAdapter(new ArrayAdapter<Object>(this, android.R.layout.simple_selectable_list_item, names));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                Toast.makeText(MainActivity.this, spinner.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //

            }

        });

        final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.togglebutton);
        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (true == isChecked) {
                    Toast.makeText(MainActivity.this, "Checked!-下载", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "UnChecked!-删除", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 一个有意思的动画,偷懒专用 ^_______________^
        Switch switchButton = (Switch) findViewById(R.id.switchButton);
        switchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Animation fadein = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
                fadein.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toggleButton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                });

                Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out);
                fadeOut.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toggleButton.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                });

                if (isChecked) {
                    toggleButton.startAnimation(fadeOut);
                } else {
                    toggleButton.startAnimation(fadein);
                }
            }
        });

        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        findViewById(R.id.progress_bar).startAnimation(rotateAnimation);

        View changeBar = findViewById(R.id.changeBar);
        changeBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateOptionsMenu(barMenu);
            }
        });
        changeBar.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                menu.setQwertyMode(true);
                menu.add(0, 1, 2, "111");
                menu.add(0, 2, 0, "222");
                menu.add(0, 3, 1, "333");
            }
        });
    }

    /**
     * 显示带有图片的Toast
     *
     * @param view
     */
    public void showToastWithImage(View view) {
        View toastView = getLayoutInflater().inflate(R.layout.view_toast_show_image, null);
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastView);
        toast.show();
    }
}

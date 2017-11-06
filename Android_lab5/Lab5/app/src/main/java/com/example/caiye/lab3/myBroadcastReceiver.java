package com.example.caiye.lab3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * Created by caiye on 2017/11/1.
 */

public class myBroadcastReceiver extends BroadcastReceiver {
    int cnt = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        //静态广播，应用启动时
        if(intent.getAction().equals("com.example.caiye.lab3.start")){
            Bundle bundle = intent.getExtras();
            String showName = bundle.getString("name");
            String showPrice = bundle.getString("price");
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),bundle.getInt("pic"));

            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("新商品热卖")
                    .setContentText(showName+"仅售"+showPrice+"!")
                    .setLargeIcon(bm)
                    .setSmallIcon(bundle.getInt("pic"))
                    .setAutoCancel(true);
            //获取状态通知栏管理
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            //点击notification，跳转到商品详情
            Intent intent1 = new Intent(context,DetailsActivity.class);
            intent1.putExtras(bundle);
        //    intent1.putExtra("name",showName);
        //    intent1.putExtra("price",showPrice);
        //    intent1.putExtra("pic",bundle.getInt("pic"));

            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            //绑定notification，发生通知请求
            Notification notify = builder.build();
            notificationManager.notify(0,notify);
        }
        //动态广播，加入购物车时
        else if(intent.getAction().equals("com.example.caiye.lab3.dynamic")){
            Bundle bundle = intent.getExtras();
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),bundle.getInt("pic"));

            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("马上下单")
                    .setContentText(bundle.getString("name")+"已添加到购物车")
                    .setLargeIcon(bm)
                    .setSmallIcon(bundle.getInt("pic"))
                    .setAutoCancel(true);
            //获取状态通知栏管理
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            //点击notification，跳转到购物车列表
            Intent intent1 = new Intent(context,MainActivity.class);
            intent1.putExtra("jump",true);

            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            //绑定notification，发生通知请求
            Notification notify = builder.build();
            notificationManager.notify(cnt++,notify);
        }
    }

}

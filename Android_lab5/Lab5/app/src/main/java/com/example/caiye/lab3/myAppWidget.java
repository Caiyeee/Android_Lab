package com.example.caiye.lab3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class myAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews updateView = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
        updateView.setTextViewText(R.id.appwidget_text, widgetText);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("jump",false);//跳转到商品列表而不是购物车列表
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        updateView.setOnClickPendingIntent(R.id.widget,pendingIntent);//设置点击事件
        ComponentName componentName = new ComponentName(context,myAppWidget.class);
        appWidgetManager.updateAppWidget(componentName,updateView);

        // Instruct the widget manager to update the widget
    //    appWidgetManager.updateAppWidget(appWidgetId, updateView);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context,Intent intent){
        super.onReceive(context,intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Bundle bundle = intent.getExtras();
        if(intent.getAction().equals("com.example.lab3.MyStaticFlites")){
            //设置widget的商品推荐
            RemoteViews updateViews = new RemoteViews(context.getPackageName(),R.layout.my_app_widget);
            updateViews.setTextViewText(R.id.appwidget_text,bundle.getString("name")+"仅售"+bundle.getString("price")+"!");
            updateViews.setImageViewResource(R.id.appwidget_pic,bundle.getInt("pic"));
            //点击widget跳转到商品详情
            Intent intent1 = new Intent(context,DetailsActivity.class);
            intent1.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.widget,pendingIntent);
            ComponentName componentName = new ComponentName(context,myAppWidget.class);
            appWidgetManager.updateAppWidget(componentName,updateViews);
        } else if(intent.getAction().equals("com.example.caiye.lab3.dynamic_widget")){
            //加入购物车widget提示
            RemoteViews updateViews = new RemoteViews(context.getPackageName(),R.layout.my_app_widget);
            updateViews.setTextViewText(R.id.appwidget_text,bundle.getString("name")+"已添加到购物车");
            updateViews.setImageViewResource(R.id.appwidget_pic,bundle.getInt("pic"));
            //点击widget跳转到购物车列表
            Intent intent1 = new Intent(context,MainActivity.class);
            intent1.putExtras(bundle);
            intent1.putExtra("jump",true);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.widget,pendingIntent);
            ComponentName componentName = new ComponentName(context,myAppWidget.class);
            appWidgetManager.updateAppWidget(componentName,updateViews);
        }
    }
}


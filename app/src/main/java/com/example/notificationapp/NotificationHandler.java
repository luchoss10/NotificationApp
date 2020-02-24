package com.example.notificationapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;

public class NotificationHandler extends ContextWrapper{

    //Notification Mannager
    private NotificationManager manager;

    //IDS y Nombres de los canales de notificacion
    public static  final String CHANNEL_HIGH_ID = "1";
    private final String CHANNEL_HIGH_NAME = "HIGH CHANNEL";

    public static  final String CHANNEL_LOW_ID = "2";
    private final String CHANNEL_LOW_NAME = "LOW CHANNEL";

    private final int SUMMARY_GROUP_ID =  120;
    private final String SUUMMARY_GRUOP_NAME =  "GROUPING_NOTIFICATION";

    //Metodo Constructor Que crea los canales de notificacion por defecto
    public NotificationHandler(Context context) {
        super(context);
        createChannels();
    }

    //Metodo para obtener el Notification Manager
    public NotificationManager getManager(){
        if (manager== null){
            manager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return  manager;
    }

    //Metodo para la creaccion de los canales dependiendo de la version de Android >= API 26 (Oreo)
    private void createChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Creating High Channel
            NotificationChannel highChannel = new NotificationChannel(
                    CHANNEL_HIGH_ID, CHANNEL_HIGH_NAME, NotificationManager.IMPORTANCE_HIGH);

            // ...Extra Config...
            //Led de notificacion
            highChannel.enableLights(true);
            //Color del led de notificacion
            highChannel.setLightColor(Color.BLUE);
            //Badge punto de notificacion
            highChannel.setShowBadge(true);
            //Vibracion de notificacion
            highChannel.enableVibration(true);
            //Patron de vibracion
            //highChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,200,400});
            //visualizacion de notificacion en el la lockScreen
            highChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);


            //Creating Low Channel
            NotificationChannel lowChannel = new NotificationChannel(
                    CHANNEL_LOW_ID, CHANNEL_LOW_NAME, NotificationManager.IMPORTANCE_LOW);
            lowChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);


            getManager().createNotificationChannel(highChannel);
            getManager().createNotificationChannel(lowChannel);
        }
    }

    //Metodo para la creacion de la notifiacion deseada pasando el titulo el mensaje y la importancia
    public Notification.Builder createNotification(String title, String message, Boolean isHighImportance){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(isHighImportance) {
                return this.createNotificationWithChannel(title, message, CHANNEL_HIGH_ID);
            }
            return this.createNotificationWithChannel(title, message, CHANNEL_LOW_ID);
        }
        return this.createNotificationWithoutChannel(title,message);
    }

    //Metodo que crea la notificación con el canal para versiones superiores a la API 26 (Oreo)
    private Notification.Builder createNotificationWithChannel(String title, String message, String channelId){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Creacion de intent para cuando se pulse la notifcacion
            Intent intent =  new Intent(this, DetailsActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("message", message);
            //Creacion de lka bandera para que una vez seleccuonada la app no se devuelva al activity abierto
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //Creaccion del pedingIntent que espera para ser llamado si se pulsa la notificacion
            //Recibe como árametro el context, variableRequest Code, el intent diseñado anteroirmente, y se cierra una vez lanzado,
            PendingIntent pIntent =  PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

            Notification.Action action =
                    new Notification.Action.Builder(Icon.createWithResource
                            (this, android.R.drawable.ic_menu_send),"See Details", pIntent).build();

            //dentro de la creacion de la creacion del notification builder, .setContentIntent(pIntent) para lanzar el PendingIntent al pulsar la notificacion
            return new Notification.Builder(getApplicationContext(),channelId)
                    .setContentTitle(title)
                    .setContentText(message)
                    //.addAction(action)
                    .setColor(getColor(R.color.colorPrimary))
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setGroup(SUUMMARY_GRUOP_NAME)
                    .setAutoCancel(true);
        }
        return null;
    }

    //Metodo que crea la notificación con el canal para versiones inferiores a la API 26 (Oreo)
    private Notification.Builder createNotificationWithoutChannel(String title, String message){
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setAutoCancel(true);
    }

    public void publishNotificationSummaryGruop(boolean isHighImportance){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId =  (isHighImportance)? CHANNEL_HIGH_ID : CHANNEL_LOW_ID;
            Notification summaryNotification =  new Notification.Builder(getApplicationContext(), channelId)
                    .setSmallIcon(android.R.drawable.stat_notify_call_mute)
                    .setGroup(SUUMMARY_GRUOP_NAME)
                    .setGroupSummary(true)
                    .build();
            getManager().notify(SUMMARY_GROUP_ID, summaryNotification);
        }
    }
}

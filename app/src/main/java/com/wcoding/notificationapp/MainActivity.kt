package com.wcoding.notificationapp

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    //UI Views
    private lateinit var showNotificationBtn: Button


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Inicia a UI View
        showNotificationBtn = findViewById(R.id.showNotificationBtn)


        showNotificationBtn.setOnClickListener{
            //Verifica se a permissão para notificação foi concedida, se sim exibe a notificação, senão é pedido a permissão
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                requestNotificationPermission()
            }else {
                showNotification()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    //Função para requisitar a permissão para notificação
    private fun requestNotificationPermission(){

        //Verifica-se se a notificação já foi pedida, se já foi e não foi cedida aparece uma explicação para o porque cedê-la
        if(shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.apply {

                //Título do Alert
                setTitle("Permissão necessária")

                //Mensagem do Alert
                setMessage("Para que o aplicativo funcione corretamente, é necessário autorizar o acesso à notificação.")

                //Ação ao clicar no OK do box
                setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    // Solicita a permissão da notificação novamente
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        100
                    )
                })

                //Ação ao clicar no Cancelar do box
                setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                setCancelable(false)
            }
            //Para mostrar o Alert
            alertDialog.show()
        //Se não tiver sido negado a permissão, ela é pedida
        }else{
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(){
        createNotificationChannel()


        val date = Date()
        val notificationId = SimpleDateFormat("ddHHmmss", Locale.US).format(date).toInt()

        //Builder da notificação
        val notificationBuilder = NotificationCompat.Builder(this,"channel01")

        //Ícone da notificação
        notificationBuilder.setSmallIcon(R.drawable.ic_notification)

        //Título da notificação
        notificationBuilder.setContentTitle("Você clicou no botão")

        //Descrição da notificação
        notificationBuilder.setContentText("Após ter clicado no botão, essa notificação aparece")

        //Prioridade da notificação
        notificationBuilder.priority = NotificationCompat.PRIORITY_DEFAULT

        //Manager da notificação
        val notificationManagerCompat = NotificationManagerCompat.from(this)

        //Executa a notificação
        notificationManagerCompat.notify(notificationId, notificationBuilder.build())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        //Para mostrar notificação a partir do Android Oreo, é necessário criar um canal de comunicação
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "MyNotification"

            val description = "Notificação ao clicar o botão"

            //Definir a importância da notificação
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            //Configurar o canal de notificação
            val notificationChannel = NotificationChannel("channel01", name, importance)
            notificationChannel.description = description

            //Manager para criar o canal de notificação
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
}


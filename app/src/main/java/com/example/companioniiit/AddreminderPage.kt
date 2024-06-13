package com.example.companioniiit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.companioniiit.databinding.ActivityAddreminderPageBinding
import com.example.companioniiit.databinding.ReminderDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.time.Year
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class AddreminderPage : AppCompatActivity() {

    private  lateinit var binding: ActivityAddreminderPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityAddreminderPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Addreminder.setOnClickListener {
            addReminder()
        }

        }

    private fun addReminder() {
        if(Build.VERSION.SDK_INT >= 33 && !NotificationManagerCompat.from(this).areNotificationsEnabled()){
            showNotificationPermissionDialog()
        } else{
            addReminderDialog()
        }

    }

    private fun addReminderDialog() {
        val dialogBinding= ReminderDialogBinding.bind(layoutInflater.inflate(R.layout.reminder_dialog,null))
        val dialog=AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .show()

        dialogBinding.ReminderType.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,resources.getStringArray(R.array.reminderTypes))
        dialogBinding.cancelbtn.setOnClickListener {
            dialog.dismiss()
        }


        val pickedDate= Calendar.getInstance()
        dialogBinding.selectbtn.setOnClickListener {

            val year=pickedDate.get(Calendar.YEAR)
            val month=pickedDate.get(Calendar.MONTH)
            val day=pickedDate.get(Calendar.DATE)
            val hour=pickedDate.get(Calendar.HOUR_OF_DAY)
            val minute=pickedDate.get(Calendar.MINUTE)

            DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
                        // show timepicker
                        TimePickerDialog(this,
                            TimePickerDialog.OnTimeSetListener{view, hourOfDay, minute ->
                                pickedDate.set(year,month,dayOfMonth,hourOfDay, minute)
                                Log.d("DateAndTime","Picked Date and time $pickedDate")
                                dialogBinding.pickedDateAndTime.text=getCurrentDateAndTime(pickedDate.timeInMillis)
                            },
                            hour,
                            minute,
                            false
                        ).show()


                }
                ,year,month,day)
                .show()


        }

        dialogBinding.submitbtn.setOnClickListener{
            if (dialogBinding.edittitle.text.isNullOrEmpty()){
                dialogBinding.edittitle.requestFocus()
                dialogBinding.edittitle.setError("Please provide Title or description")

            }else if (dialogBinding.pickedDateAndTime.text== resources.getString(R.string.date_and_time)){
                dialogBinding.pickedDateAndTime.setError("please select date and time!!!")


            }
            else{
                // adding reminder
                val timeDelayInSeconds = (pickedDate.timeInMillis / 1000L) - (Calendar.getInstance().timeInMillis / 1000L)
                if(timeDelayInSeconds<0){
                    Toast.makeText(this,"Can not set reminders for past dates",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                // work manager to set reminder
                createWorkRequest(dialogBinding.edittitle.text.toString(),resources.getStringArray(R.array.reminderTypes)[dialogBinding.ReminderType.selectedItemPosition],
                    timeDelayInSeconds)
                Toast.makeText(this,"Reminder Added",Toast.LENGTH_LONG).show()
                dialog.dismiss()

            }
        }

    }

    private fun createWorkRequest(title: String, reminderType: String,     delay: Long) {
        val reminderWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "Title" to "Todo: $reminderType",
                    "Message" to title
                )
            )
            .build()
        WorkManager.getInstance(this).enqueue(reminderWorkRequest)
    }

    private fun getCurrentDateAndTime(millis:Long):String{
        return SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault()).format(Date(millis))




    }


    private fun showNotificationPermissionDialog() {
       MaterialAlertDialogBuilder(this,
       com.google.android.material.R.style.MaterialAlertDialog_Material3
       )
           .setTitle("Notification Permission")
           .setMessage("We need permission to send notifications")
           .setPositiveButton("Ok"){ dialog, which ->
               // notification launcher
               if(Build.VERSION.SDK_INT >= 33){
                   notificationLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
               }
           }
           .setNegativeButton("Cancel",null)
           .show()
    }

    val notificationLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        isGranted ->
        if(isGranted){
            if(Build.VERSION.SDK_INT >= 33){
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)){
                    showNotificationPermissionDialog()
                }else{
                    showSettingDialog()
                }



            }

        }else{
            Toast.makeText(this,"Notification permission granted", Toast.LENGTH_SHORT).show()
        }


    }

    private fun showSettingDialog() {
       MaterialAlertDialogBuilder(this,

           com.google.android.material.R.style.MaterialAlertDialog_Material3)
           .setTitle("Notification Permission")
           .setMessage("We need permission to send notifications")
           .setPositiveButton("Ok"){ _ , _ ->
               val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
               intent.data = Uri.parse("package:${this.applicationContext.packageName}")
               startActivity(intent)
           }
           .setNegativeButton("Cancel",null)
           .show()

           }

    }




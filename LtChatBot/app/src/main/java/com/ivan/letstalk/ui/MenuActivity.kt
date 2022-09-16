package com.ivan.letstalk.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ivan.letstalk.R
import com.ivan.letstalk.helper.SessionManager

class MenuActivity : AppCompatActivity() {
    private lateinit var  bottomNavHome : LinearLayoutCompat
    private lateinit var  bottomNavChat : LinearLayoutCompat
    private lateinit var  bottomNavPerson : LinearLayoutCompat
    private lateinit var  bottomNavHamburger : LinearLayoutCompat

    private lateinit var  bottomIvHome : AppCompatImageView
    private lateinit var  bottomIvChat : AppCompatImageView
    private lateinit var  bottomIvPerson : AppCompatImageView
    private lateinit var  bottomIvHamburger : AppCompatImageView

    private lateinit var ivMenu: AppCompatImageView
    private lateinit var rrProfile : RelativeLayout
    private lateinit var rrHealthVitals : RelativeLayout
    private lateinit var rrSideEffects : RelativeLayout
    private lateinit var rrDocuments : RelativeLayout
    private lateinit var rrMedicineReminder : RelativeLayout
    private lateinit var rrKnowSideEffects : RelativeLayout
    private lateinit var rrGlossary : RelativeLayout
    private lateinit var rrFaq : RelativeLayout
    private lateinit var  btnStartChat : MaterialButton
    private lateinit var  btnLogout : RelativeLayout
    private lateinit var sessionManager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        sessionManager = SessionManager(this)
        btnStartChat = findViewById(R.id.btn_start_chat)
        rrProfile = findViewById(R.id.rr_profile)
        rrHealthVitals = findViewById(R.id.rr_health_vitals)
        rrSideEffects = findViewById(R.id.rr_side_effects)
        rrDocuments = findViewById(R.id.rr_documents)
        rrMedicineReminder = findViewById(R.id.rr_medicine_reminder)
        rrKnowSideEffects = findViewById(R.id.rr_know_side_effects)
        rrGlossary = findViewById(R.id.rr_glossary)
        rrFaq = findViewById(R.id.rr_faq)
        btnLogout = findViewById(R.id.btn_logout)

        bottomNavHome =  findViewById(R.id.bottom_nav_home)
        bottomNavChat =  findViewById(R.id.bottom_nav_chat)
        bottomNavPerson =  findViewById(R.id.bottom_nav_person)
        bottomNavHamburger =  findViewById(R.id.bottom_nav_hamburger)

        bottomIvHome = findViewById(R.id.bottom_dot_home)
        bottomIvChat = findViewById(R.id.bottom_dot_chat)
        bottomIvPerson = findViewById(R.id.bottom_dot_person)
        bottomIvHamburger = findViewById(R.id.bottom_dot_hamburger)

        ivMenu = findViewById(R.id.iv_menu)
        ivMenu.setBackgroundResource(R.drawable.ic_menu_fill)

        bottomIvHamburger.visibility = View.VISIBLE

        bottomNavHome.setOnClickListener{
            /*bottomIvPerson.visibility =  View.INVISIBLE
            bottomIvHamburger.visibility =  View.INVISIBLE
            bottomIvChat.visibility =  View.INVISIBLE
            bottomIvHome.visibility =  View.VISIBLE*/
            navigateToDashboard()
        }

        bottomNavChat.setOnClickListener{
            bottomIvHome.visibility =  View.INVISIBLE
            bottomIvPerson.visibility =  View.INVISIBLE
            bottomIvHamburger.visibility =  View.INVISIBLE
            // bottomIvChat.visibility =  View.VISIBLE
            navigateToChat()
        }

        bottomNavPerson.setOnClickListener{
            /*bottomIvHome.visibility =  View.INVISIBLE
            bottomIvHamburger.visibility =  View.INVISIBLE
            bottomIvChat.visibility =  View.INVISIBLE
            bottomIvPerson.visibility =  View.VISIBLE*/
            navigateToProfile()
        }

        rrProfile.setOnClickListener{
            navigateToProfile()
        }

        rrHealthVitals.setOnClickListener{
            navigateToMyHealthVitals()
        }

        rrSideEffects.setOnClickListener{
            navigateToMySideEffectHistory()
        }

        rrDocuments.setOnClickListener{
            navigateToMyDocuments()
        }

        rrMedicineReminder.setOnClickListener{
            navigateToMedicineReminder()
        }

        rrKnowSideEffects.setOnClickListener{
            navigateToKnowYourSideEffects()
        }

        rrGlossary.setOnClickListener{
            navigateToGlossary()
        }

        rrFaq.setOnClickListener{
            navigateToFaq()
        }

        btnStartChat.setOnClickListener {
            navigateToChat()
        }

        btnLogout.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(this,R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog)
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to Logout?")
            // val customFont = Typeface.createFromAsset(assets, "font/gilroy_medium.ttf")
            builder.setPositiveButton("Yes"){ dialog,which->
                sessionManager.logoutUser()
            }
            builder.setNegativeButton("No"){dialog,which->
                dialog.dismiss()
            }
            /*builder.setNeutralButton("Cancel"){dialog,which->

            }*/
            builder.setOnCancelListener {

            }
            builder.setOnDismissListener {

            }
            builder.setCancelable(false)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.alert_dialog_back)
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.blue))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.blue))
        }
    }

    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToChat() {
        val intent = Intent(this, ALKChatActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToMyHealthVitals() {
        val intent = Intent(this, MyHealthVitals::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToMySideEffectHistory() {
        val intent = Intent(this, MySideEffectHistory::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToMyDocuments() {
        val intent = Intent(this, MyDocumentsActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToMedicineReminder() {
        val intent = Intent(this, MedicineReminderActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToKnowYourSideEffects() {
        val intent = Intent(this, KnowYourSideEffectsActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToGlossary() {
        val intent = Intent(this, GlossaryActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToFaq() {
        val intent = Intent(this, FaqActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
}
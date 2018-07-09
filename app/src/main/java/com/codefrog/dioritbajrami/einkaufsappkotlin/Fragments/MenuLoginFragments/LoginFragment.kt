package com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MenuLoginFragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.BuildConfig
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.MainActivity

import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import android.app.Dialog
import android.app.ProgressDialog
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.custom_alert.*
import android.support.v7.app.AppCompatActivity
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.ViewPagerAdapter
import com.rd.PageIndicatorView




class LoginFragment : Fragment() {

    private val RC_SIGN_IN = 123

    var googlSignInButton: SignInButton?=null
    var adminSignInButton: Button?=null

    var mAuth: FirebaseAuth?=null

    var mProgress: ProgressDialog?=null

    var pagerAdapter: ViewPagerAdapter?=null
    var viewPager: ViewPager?=null

    var title = arrayOf("Race your Friend", "Find new Roads", "Try new Things")
    var titleText = arrayOf("Try to get to the end before your friend, the winner gets it all.",
            "Chose anypoint in the Map and get there within the time.",
            "Try the latest App which allows you to go through woods, roads until you reach the destionation.")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        activity!!.title = "Anmelden"
        (activity as MainActivity).supportActionBar!!.hide()

        pagerAdapter = ViewPagerAdapter((activity as MainActivity).applicationContext,title,titleText)
        viewPager = view.findViewById(R.id.pager)
        viewPager!!.adapter = pagerAdapter


        val pageIndicatorView = view.findViewById(R.id.pageIndicatorView) as PageIndicatorView
        pageIndicatorView.setViewPager(viewPager)


        mAuth = FirebaseAuth.getInstance()


        googlSignInButton = view.findViewById(R.id.SignInButtonID)
        googlSignInButton!!.setOnClickListener{
            signIn()
        }

        adminSignInButton = view.findViewById(R.id.administratorLoginID)

        adminSignInButton!!.setOnClickListener{
            alertDialog()
        }


        mProgress = ProgressDialog(context);
        mProgress!!.setTitle("Anmeldung...");
        mProgress!!.setMessage("Bitte warten")
        mProgress!!.setCancelable(false);
        mProgress!!.setIndeterminate(true);

        return view
    }

    fun signIn(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                        .setAvailableProviders(
                                Arrays.asList(AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .setTosUrl("link to app terms and service")
                        .setPrivacyPolicyUrl("link to app privacy policy")
                        .build(),
                RC_SIGN_IN)
        mProgress!!.show()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            /*
                this checks if the activity result we are getting is for the sign in
                as we can have more than activity to be started in our Activity.
             */
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                /*
                    Checks if the User sign in was successful
                 */
                replaceFragment()
                mProgress!!.dismiss()
                Toast.makeText(context!!, "Willkommen " + mAuth!!.currentUser!!.displayName, Toast.LENGTH_LONG).show()

                return
            }
            else {
                mProgress!!.dismiss()
                Toast.makeText(context!!, "Anmeldung Fehlgeschlagen", Toast.LENGTH_LONG).show()

                return
            }
        }
        Toast.makeText(context!!, "Anmeldeantwort unbekannt", Toast.LENGTH_SHORT).show()
    }

    fun loginAdministrator(email:String, password:String){
        mAuth!!.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(activity!!){ task ->
                    if(task.isSuccessful){
                        //saveImage()
                        replaceFragment()
                        mProgress!!.dismiss()
                    } else {
                        Toast.makeText(activity, "Passwort oder Email falsch", Toast.LENGTH_SHORT).show()
                        mProgress!!.dismiss()
                    }
                }
    }

    fun alertDialog(){
        val d = Dialog(activity)
        d.setTitle("Einloggen")
        d.setContentView(R.layout.custom_alert)

        d.loginButton.setOnClickListener{
            if(d.usernameID.text.toString().equals("")){
                Toast.makeText(activity, "Email eingeben!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if(d.passwortID.text.toString().equals("")){
                Toast.makeText(activity, "Passwort eingeben!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else {
                mProgress!!.show()
                loginAdministrator(d.usernameID.text.toString(), d.passwortID.text.toString())
                d.dismiss()
            }

        }

        d.show()
    }


    fun replaceFragment(){
        val activity = activity as MainActivity?

        getActivity()!!.getSupportFragmentManager().beginTransaction().remove(this).commit()

        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.content_frame, activity!!.menuFragment).commit()

    }
}

package com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments

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
import kotlinx.android.synthetic.main.custom_alert.*


class LoginFragment : Fragment() {

    private val RC_SIGN_IN = 123

    var googlSignInButton: SignInButton?=null
    var adminSignInButton: Button?=null

    var mAuth: FirebaseAuth?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        mAuth = FirebaseAuth.getInstance()


        googlSignInButton = view.findViewById(R.id.SignInButtonID)
        googlSignInButton!!.setOnClickListener{
            signIn()
        }

        adminSignInButton = view.findViewById(R.id.administratorLoginID)

        adminSignInButton!!.setOnClickListener{
            alertDialog()
        }


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

                return
            }
            else {
                if(response == null){
                    //If no response from the Server
                    Toast.makeText(context!!, "No Response from server", Toast.LENGTH_SHORT).show()
                    return
                }
                if(response.errorCode == ErrorCodes.NO_NETWORK){
                    //If there was a network problem the user's phone
                    Toast.makeText(context!!, "Network Problem", Toast.LENGTH_SHORT).show()
                    return
                }
                if(response.errorCode == ErrorCodes.UNKNOWN_ERROR){
                    //If the error cause was unknown
                    Toast.makeText(context!!, "Unknown Error", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
        Toast.makeText(context!!, "Sign In Response Unknown", Toast.LENGTH_SHORT).show()
    }

    fun loginAdministrator(email:String, password:String){
        mAuth!!.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(activity!!){ task ->
                    if(task.isSuccessful){
                        //saveImage()
                        replaceFragment()
                    } else {
                        Toast.makeText(activity, "Passwort oder Email falsch", Toast.LENGTH_SHORT).show()
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

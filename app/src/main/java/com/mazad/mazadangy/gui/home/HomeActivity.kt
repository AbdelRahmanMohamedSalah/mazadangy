package com.mazad.mazadangy.gui.home

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mazad.mazadangy.R
import com.mazad.mazadangy.adapter.AdsAdapter
import com.mazad.mazadangy.gui.AddAds.AddAdsActivity
import com.mazad.mazadangy.gui.AddAds.AddPostActivity
import com.mazad.mazadangy.gui.UserDetails.UserDetailsActivity
import com.mazad.mazadangy.gui.favorite.FavoritePostActivity
import com.mazad.mazadangy.gui.login.LoginActivity
import com.mazad.mazadangy.model.AdsModel
import com.mazad.mazadangy.model.UserModel
import com.mazad.mazadangy.utels.StaticMethod
import com.mazad.mazadangy.utels.ToastUtel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header.view.*


class HomeActivity : AppCompatActivity(), HomeInterface,
    NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var homePresenter: HomePresenter
    lateinit var adsAdapter: AdsAdapter
    lateinit var intent_obj: Intent
    lateinit var catCheck: String
    lateinit var datarefrence: DatabaseReference
    lateinit var drawerLayout: DrawerLayout
    lateinit var currentFirebaseUser: FirebaseAuth
    lateinit var navView: NavigationView
    private var progressDialog: ProgressDialog? = null
    lateinit var userModel: UserModel
    var navDrawer: NavigationView? = null
    var listAds: ArrayList<AdsModel>? = null
    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navDrawer?.setNavigationItemSelectedListener(this)

        setupNavButton()
        nav_icon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                drawer_layout.openDrawer(GravityCompat.END)
            }
        })
        PostsRecycler.layoutManager = LinearLayoutManager(this)
        homePresenter = HomePresenter(this)
        checkCat()
        refresh()
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_drawer)
        val user = FirebaseAuth.getInstance().currentUser


        navView.setNavigationItemSelectedListener(this)

        datarefrence =
            FirebaseDatabase.getInstance().getReference("users").child(user?.uid.toString() + "")

        val header = nav_drawer.getHeaderView(0) as LinearLayout


        val menuListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }


            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var firstname = dataSnapshot.child("firstName").getValue(String::class.java)
                var lastname = dataSnapshot.child("lastName").getValue(String::class.java)
                var email = dataSnapshot.child("email").getValue(String::class.java)
                var phonenumber = dataSnapshot.child("phoneNumber").getValue(String::class.java)
                var interest = dataSnapshot.child("interest").getValue(String::class.java)
                var nickname = dataSnapshot.child("nickname").getValue(String::class.java)
                var uid = dataSnapshot.child("uId").getValue(String::class.java)
                var image_profile = dataSnapshot.child("image_profile").getValue(String::class.java)

                userModel = UserModel(
                    uid!!,
                    phonenumber!!,
                    nickname!!,
                    lastname!!,
                    interest!!,
                    firstname!!,
                    email!!,
                    image_profile!!
                )

                Picasso.get().load(dataSnapshot.child("image_profile").getValue(String::class.java))
                Picasso.get().load(userModel.image_profile.toString())
                    .into(header.imgeProfileCvNavHeader)

                header.nameUserTvNavHeader.setText(userModel.firstName.toString())

            }
        }
        datarefrence.addListenerForSingleValueEvent(menuListener)



        //
    }


    private fun refresh() {
        itemsswipetorefresh.setOnRefreshListener {
            checkCat()
        }
    }


    private fun setupNavButton() {
        val bottomNavigationView =
            findViewById(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.getItemId()) {
                    R.id.upload -> {

                        if (catCheck.equals("posts")) {
                            intent_obj = Intent(this@HomeActivity, AddPostActivity::class.java)
                            startActivity(intent_obj)

                        } else {
                            intent_obj = Intent(this@HomeActivity, AddAdsActivity::class.java)
                            startActivity(intent_obj)

                        }
                    }
                    R.id.home -> {
                        finish()
                    }
                    R.id.favorit -> {

                        if (catCheck.equals("posts")) {
                            intent_obj = Intent(this@HomeActivity, FavoritePostActivity::class.java)
                            intent_obj.putExtra("fromActivity", "posts")
                            startActivity(intent_obj)


                        } else {
                            intent_obj = Intent(this@HomeActivity, FavoritePostActivity::class.java)
                            intent_obj.putExtra("fromActivity", "money_post")
                            startActivity(intent_obj)


                        }


                    }
                }
                return true
            }

        })
    }


    private fun checkCat() {
        progressDialog = StaticMethod.createProgressDialog(this)
        progressDialog?.show()
        listAds?.clear()
        var checkIntent: Intent = getIntent()
        catCheck = checkIntent.getStringExtra("category")
        homePresenter.getDataPosts(catCheck, this)

    }

    override fun noConnection() {
        progressDialog?.cancel()
        ToastUtel.errorToast(this, "تحقق من وجود الانترنت")
        itemsswipetorefresh.isRefreshing = false

    }


    override fun sucuss(adsList: ArrayList<AdsModel>) {
        listAds = adsList
        adsAdapter = AdsAdapter(this, listAds!!, catCheck)
        PostsRecycler.adapter = adsAdapter
        progressDialog?.cancel()
        itemsswipetorefresh.isRefreshing = false
        adsAdapter.notifyDataSetChanged()
        //  ToastUtel.errorToast(this,"data is done")


    }

    override fun onCancelled() {
        progressDialog?.cancel()
        itemsswipetorefresh.isRefreshing = false
        ToastUtel.errorToast(this, "cancelled")

    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.getItemId()) {
            R.id.pesonProfile -> {
                intent_obj = Intent(this@HomeActivity, UserDetailsActivity::class.java)
                intent_obj.putExtra("UserModelPostDetailsActivity", userModel.uId + "")
                intent_obj.putExtra("fromActivity", "HomeActivity")
                startActivity(intent_obj)

            }
            R.id.app_info -> {
                // ToastUtel.errorToast(this, "cancelled")
            }
            R.id.nav_AboutUs -> {

            }
            R.id.contact_menu -> {

            }
            R.id.logout -> {


                val builder =
                    AlertDialog.Builder(this@HomeActivity)
                builder.setMessage("تاكيد تسجيل الخروج")
                builder.setPositiveButton(
                    "تاكيد"
                ) { dialog, id ->
                    dialog.dismiss()

                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    finish()
                    startActivity(intent)


                }
                builder.setNegativeButton(
                    "الغاء"
                ) { dialog, id ->
                    dialog.dismiss()


                }
                val alert = builder.create()
                alert.show()




            }
        }


        return true

    }


}

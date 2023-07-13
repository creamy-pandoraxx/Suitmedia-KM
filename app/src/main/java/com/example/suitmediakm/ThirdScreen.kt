package com.example.suitmediakm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.*
import okio.IOException

class ThirdScreen : AppCompatActivity() {
    private lateinit var userList: ListView
    private lateinit var emptyStateText: TextView

    private val client = OkHttpClient()
    private var currentPage: Int = 1
    private val perPage: Int = 10
    private lateinit var userAdapter: UserAdapter
    private val userListUrl = "https://reqres.in/api/users?page=$currentPage&per_page=$perPage"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_screen)

        userList = findViewById(R.id.userList)
        emptyStateText = findViewById(R.id.emptyStateText)

        userAdapter = UserAdapter(ArrayList())
        userList.adapter = userAdapter
        val backButton: ImageButton = findViewById(R.id.btn_back)

        fetchUserList()

        userList.setOnItemClickListener { parent, view, position, id ->
            val selectedUser = userAdapter.getItem(position) as User
            val intent = Intent()
            intent.putExtra("selectedUserName", selectedUser.fullName)
            setResult(RESULT_OK, intent)
            finish()
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun fetchUserList() {
        val request = Request.Builder()
            .url(userListUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    showErrorMessage()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                response.close()

                if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                    val userListResponse = Gson().fromJson(responseBody, UserListResponse::class.java)
                    val users = userListResponse.data
                    runOnUiThread {
                        userAdapter.addAll(users)
                        userAdapter.notifyDataSetChanged()
                        updateEmptyState()
                    }
                } else {
                    runOnUiThread {
                        showErrorMessage()
                    }
                }
            }
        })
    }

    private fun updateEmptyState() {
        if (userAdapter.isEmpty) {
            emptyStateText.visibility = View.VISIBLE
        } else {
            emptyStateText.visibility = View.GONE
        }
    }

    private fun showErrorMessage() {
        Toast.makeText(this, "Failed to fetch user list", Toast.LENGTH_SHORT).show()
    }

    private inner class UserAdapter(users: List<User>) : ArrayAdapter<User>(this, R.layout.list_item_user, users) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView
            val holder: ViewHolder

            if (view == null) {
                view = layoutInflater.inflate(R.layout.list_item_user, parent, false)
                holder = ViewHolder()
                holder.fullNameText = view.findViewById(R.id.fullNameText)
                holder.emailText = view.findViewById(R.id.emailText)
                holder.avatarImage = view.findViewById(R.id.avatarImage)
                view.tag = holder
            } else {
                holder = view.tag as ViewHolder
            }

            val user = getItem(position)
            holder.fullNameText.text = user?.fullName
            holder.emailText.text = user?.email
            Glide.with(this@ThirdScreen)
                .load(user?.avatar)
                .into(holder.avatarImage)

            return view!!
        }
    }

    private class ViewHolder {
        lateinit var fullNameText: TextView
        lateinit var emailText: TextView
        lateinit var avatarImage: ImageView
    }
}
data class UserListResponse(val data: List<User>)
data class User(val id: Int, val email: String, val first_name: String, val last_name: String, val avatar: String){
    val fullName: String
        get() = "$first_name $last_name"
}
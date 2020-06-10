package com.xkf.ppjoke.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tencent.connect.UserInfo
import com.tencent.connect.auth.QQToken
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.xkf.libnetwork.common.ApiResponse
import com.xkf.libnetwork.common.JsonCallback
import com.xkf.libnetwork.request.ApiService
import com.xkf.ppjoke.R
import com.xkf.ppjoke.databinding.ActivityLoginBinding
import com.xkf.ppjoke.model.User
import org.json.JSONObject


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var viewBinding: ActivityLoginBinding
    private val tencent: Tencent by lazy {
        Tencent.createInstance("101794421", applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.action_close -> {
                finish()
            }
            R.id.action_login -> {
                login()
            }
        }
    }

    private fun login() {
        tencent.login(this, "all", object : IUiListener {
            override fun onComplete(json: Any) {
                val response = json as JSONObject
                val openId = response.getString("openid")
                val accessToken = response.getString("access_token")
                val expiresIn = response.getString("expires_in")
                val expiresTime = response.getLong("expires_time")
                tencent.openId = openId
                tencent.setAccessToken(accessToken, expiresIn)
                val qqToken = tencent.qqToken
                getUserInfo(qqToken, expiresTime, openId)
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "登录取消", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: UiError) {
                Toast.makeText(
                    this@LoginActivity,
                    "登录失败 : ${error.errorMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getUserInfo(qqToken: QQToken, expiresTime: Long, openId: String) {
        val userInfo = UserInfo(applicationContext, qqToken)
        userInfo.getUserInfo(object : IUiListener {
            override fun onComplete(obj: Any) {
                val response = obj as JSONObject
                val nickName = response.getString("nickname")
                val figureUrl = response.getString("figureurl_2")
                save(nickName, figureUrl, openId, expiresTime)
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "登录取消", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: UiError) {
                Toast.makeText(
                    this@LoginActivity,
                    "登录失败 : ${error.errorMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun save(nickName: String, avatar: String, openId: String, expiresTime: Long) {
        ApiService.get<User>("user/insert")
            .addParam("name", nickName)
            .addParam("avatar", avatar)
            .addParam("qqOpenId", openId)
            .addParam("expires_time", expiresTime)
            .execute(object : JsonCallback<User>() {
                override fun onSuccess(response: ApiResponse<User>) {
                    if (response.body != null) {
                        UserManager.save(response.body!!)
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                "登录失败 : ${response.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onError(response: ApiResponse<User>) {
                    Toast.makeText(
                        this@LoginActivity,
                        "登录失败 : ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
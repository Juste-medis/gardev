package com.medis.garage.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.medis.garage.R
import com.medis.garage.adapters.ExpenseListAdapter
import com.medis.garage.common.CommonConstantAd
import com.medis.garage.common.CommonConstants
import com.medis.garage.common.CommonUtilities
import com.medis.garage.interfaces.AdapterItemCallback
import com.medis.garage.interfaces.ExpenseListDownloadCallback
import com.medis.garage.pojo.ExpenseClass
import com.medis.garage.pojo.VehicleClass
import com.medis.garage.services.GetExpenseList
import kotlinx.android.synthetic.main.activity_expense_list.*
import kotlinx.android.synthetic.main.activity_expense_list.imgAdd
import kotlinx.android.synthetic.main.activity_expense_list.imgBack
import kotlinx.android.synthetic.main.activity_expense_list.llAdView
import kotlinx.android.synthetic.main.activity_expense_list.llAdViewFacebook
import kotlinx.android.synthetic.main.activity_expense_list.llNoData

class ExpenseListActivity : AppCompatActivity(), View.OnClickListener, ExpenseListDownloadCallback,
    AdapterItemCallback {

    private var expenseClassArrayList = ArrayList<ExpenseClass>()
    private var vehicleId = ""
    private var vehicleMinDate = ""
    private var isDataUpdated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)
        initViews()
        loadDataFromIntent()
//        CommonUtilities.loadBannerAd(findViewById(R.id.adViewBottom))
        if (CommonUtilities.getPref(this,CommonConstants.AD_TYPE_FB_GOOGLE,"") == CommonConstants.AD_GOOGLE &&
            CommonUtilities.getPref(this,CommonConstants.STATUS_ENABLE_DISABLE,"") == CommonConstants.ENABLE) {
            CommonConstantAd.loadBannerGoogleAd(this,llAdView)
            llAdViewFacebook.visibility= View.GONE
            llAdView.visibility = View.VISIBLE
        } else if (CommonUtilities.getPref(this,CommonConstants.AD_TYPE_FB_GOOGLE,"") == CommonConstants.AD_FACEBOOK
            && CommonUtilities.getPref(this,CommonConstants.STATUS_ENABLE_DISABLE,"") == CommonConstants.ENABLE) {
            CommonConstantAd.loadBannerFacebookAd(this,llAdViewFacebook)
            llAdViewFacebook.visibility= View.VISIBLE
            llAdView.visibility = View.GONE
        }else{
            llAdView.visibility = View.GONE
            llAdViewFacebook.visibility= View.GONE
        }
        CommonUtilities.addKeyboardDetectListener(this,llAdView,llAdViewFacebook)
    }

    private fun initViews() {
        rvExpenseList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )

        imgBack.setOnClickListener(this)
        imgAdd.setOnClickListener(this)
    }

    private fun loadDataFromIntent() {
        val intent = intent
        if (intent!!.hasExtra(CommonConstants.KeyVehicleDetail)) {
            try {
                val aClass = intent.getSerializableExtra(CommonConstants.KeyVehicleDetail) as VehicleClass
                vehicleId = aClass.vehicleId
                vehicleMinDate = aClass.vehiclePurchaseDate
                GetExpenseList(this, vehicleId, this@ExpenseListActivity).callGetExpenseListService()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun setExpenseDetailDownloadCallback(expenseClassArrayList: ArrayList<ExpenseClass>) {
        this.expenseClassArrayList = expenseClassArrayList
        if (expenseClassArrayList.size > 0) {
            rvExpenseList.visibility = View.VISIBLE
            llNoData.visibility = View.GONE
            val mLayoutManager = LinearLayoutManager(this)
            mLayoutManager.reverseLayout = true
            mLayoutManager.stackFromEnd = true
            rvExpenseList.layoutManager = mLayoutManager
            rvExpenseList.adapter = ExpenseListAdapter(this, expenseClassArrayList, this@ExpenseListActivity)
        } else {
            rvExpenseList.visibility = View.GONE
            llNoData.visibility = View.VISIBLE
        }
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        if (id == R.id.imgBack) {
            onBackPressed()
        } else if (id == R.id.imgAdd) {
//            CommonUtilities.onClickAddVehicleDetail(this, this@ExpenseListActivity)
            openExpenseDetailActivity(null)
        }
    }

    override fun onItemTypeClickCallback(mPos: Int) {
        if (CommonUtilities.isOnline(this)) {
            try {
                val aClass = expenseClassArrayList[mPos]
                openExpenseDetailActivity(aClass)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            startActivity(Intent(this, NoInternetActivity::class.java))
        }
    }

   /* override fun adLoadingFailed() {
        openExpenseDetailActivity(null)
    }

    override fun adClose() {
        openExpenseDetailActivity(null)
    }

    override fun startNextScreen() {
        openExpenseDetailActivity(null)
    }*/

    private fun openExpenseDetailActivity(aClass: ExpenseClass?) {
        val intent = Intent(this, ExpenseDetailActivity::class.java)
        intent.putExtra(CommonConstants.KeyVehicleId, vehicleId)
        intent.putExtra(CommonConstants.KeyVehicleMinDate, vehicleMinDate)
        if (aClass != null) {
            intent.putExtra(CommonConstants.KeyExpenseDetail, aClass)
        }
        startActivityForResult(intent, CommonConstants.RequestDataUpdated)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val key = CommonConstants.KeyIsDataUpdated
        if (resultCode == Activity.RESULT_OK) {
            if (data!!.hasExtra(key)) {
                if (data.getBooleanExtra(key, false)) {
                    isDataUpdated = true
                    GetExpenseList(this, vehicleId, this@ExpenseListActivity).callGetExpenseListService()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (isDataUpdated) {
            val intent = Intent()
            intent.putExtra(CommonConstants.KeyIsDataUpdated, true)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }
}
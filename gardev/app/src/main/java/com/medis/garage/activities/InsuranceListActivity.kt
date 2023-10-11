package com.medis.garage.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.medis.garage.R
import com.medis.garage.adapters.InsuranceListAdapter
import com.medis.garage.common.CommonConstantAd
import com.medis.garage.common.CommonConstants
import com.medis.garage.common.CommonUtilities
import com.medis.garage.interfaces.AdapterItemCallback
import com.medis.garage.interfaces.InsuranceListDownloadCallback
import com.medis.garage.pojo.InsuranceClass
import com.medis.garage.pojo.VehicleClass
import com.medis.garage.services.GetInsuranceList
import kotlinx.android.synthetic.main.activity_insurance_list.*
import kotlinx.android.synthetic.main.activity_insurance_list.imgAdd
import kotlinx.android.synthetic.main.activity_insurance_list.imgBack
import kotlinx.android.synthetic.main.activity_insurance_list.llAdView
import kotlinx.android.synthetic.main.activity_insurance_list.llAdViewFacebook
import kotlinx.android.synthetic.main.activity_insurance_list.llNoData

class InsuranceListActivity : AppCompatActivity(), View.OnClickListener, InsuranceListDownloadCallback,
    AdapterItemCallback {

    private var insuranceClassArrayList = ArrayList<InsuranceClass>()
    private var vehicleId = ""
    private var vehicleMinDate = ""
    private var isDataUpdated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_list)
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
        rvInsuranceList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
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
                GetInsuranceList(this, vehicleId, this@InsuranceListActivity).callGetInsuranceListService()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun setInsuranceDetailDownloadCallback(insuranceClassArrayList: ArrayList<InsuranceClass>) {
        this.insuranceClassArrayList = insuranceClassArrayList
        if (insuranceClassArrayList.size > 0) {
            rvInsuranceList.visibility = View.VISIBLE
            llNoData.visibility = View.GONE
            val mLayoutManager = LinearLayoutManager(this)
            mLayoutManager.reverseLayout = true
            mLayoutManager.stackFromEnd = true
            rvInsuranceList.layoutManager = mLayoutManager
            rvInsuranceList.adapter = InsuranceListAdapter(this, insuranceClassArrayList, this@InsuranceListActivity)
        } else {
            rvInsuranceList.visibility = View.GONE
            llNoData.visibility = View.VISIBLE
        }
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        if (id == R.id.imgBack) {
            onBackPressed()
        } else if (id == R.id.imgAdd) {
//            CommonUtilities.onClickAddVehicleDetail(this, this@InsuranceListActivity)
            openInsuranceDetailActivity(null)
        }
    }

    override fun onItemTypeClickCallback(mPos: Int) {
        if (CommonUtilities.isOnline(this)) {
            try {
                val aClass = insuranceClassArrayList[mPos]
                openInsuranceDetailActivity(aClass)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            startActivity(Intent(this, NoInternetActivity::class.java))
        }
    }

   /* override fun adLoadingFailed() {
        openInsuranceDetailActivity(null)
    }

    override fun adClose() {
        openInsuranceDetailActivity(null)
    }

    override fun startNextScreen() {
        openInsuranceDetailActivity(null)
    }
*/
    private fun openInsuranceDetailActivity(aClass: InsuranceClass?) {
        val intent = Intent(this, InsuranceDetailActivity::class.java)
        intent.putExtra(CommonConstants.KeyVehicleId, vehicleId)
        intent.putExtra(CommonConstants.KeyVehicleMinDate, vehicleMinDate)
        if (aClass != null) {
            intent.putExtra(CommonConstants.KeyInsuranceDetail, aClass)
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
                    GetInsuranceList(this, vehicleId, this@InsuranceListActivity).callGetInsuranceListService()
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
/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the _status of the most recent request
    private val _status = MutableLiveData<String>()

    // The external immutable LiveData for the request _status String
    val status: LiveData<String>
        get() = _status

    private val _propertyList = MutableLiveData<List<MarsProperty>>()
    val property: LiveData<List<MarsProperty>>
        get() = _propertyList

    /**
     * Call getMarsRealEstateProperties() on init so we can display _status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the _status LiveData to the Mars API _status.
     */
    private fun getMarsRealEstateProperties()
    {
        viewModelScope.launch {
            try {
                var listResult = MarsApi.retrofitServices.getProperties()
                if(listResult.size > 0)
                {
                    _propertyList.value = listResult
                }
                //_status.value = "Success: ${listResult.size} Mars Property"
            }catch(e: java.lang.Exception)
            {
                _status.value = "Fail: ${e.message}"
            }
        }

        // this code is deprecated because we will use coroutines with retrofit
//        MarsApi.retrofitServices.getProperties().enqueue(object: Callback<List<MarsProperty>> {
//            override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>)
//            {
//                _status.value = "Success: ${response.body()?.size.toString()} Mars Property"
//
//            }
//
//            override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable)
//            {
//                _status.value = "Fail: ${t.message}"
//            }
//        })

    }
}

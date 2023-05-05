package de.codeelements.utilities.contentprovider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InMemoryDbProviderViewModel : ViewModel() {

    private val _providerValid = MutableLiveData<Boolean>().apply {
        value = true
    }

    val providerValid : LiveData<Boolean> = _providerValid


    fun setProviderValid(valid:Boolean) = _providerValid.postValue(valid)

 }
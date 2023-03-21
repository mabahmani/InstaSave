package com.mabahmani.instasave.ui.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabahmani.instasave.domain.interactor.SearchTagUseCase
import com.mabahmani.instasave.domain.model.SearchTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchTagUseCase: SearchTagUseCase
) : ViewModel() {

    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchUiState: StateFlow<SearchUiState> = _searchUiState

    fun searchTag(
        tag: String,
        maxId: String = ""
    ){
        viewModelScope.launch {

            _searchUiState.value = SearchUiState.Loading

            val result = searchTagUseCase.invoke(tag = tag, maxId = maxId)
            if (result.isSuccess){
                val list = mutableListOf<SearchTag>()
                val sections = result.getOrNull()?.data?.recent?.sections
                sections?.forEach {
                    val medias = it?.layoutContent?.medias
                    medias?.forEach {
                        if (it?.item?.productType.toString() == "igtv"){
                            list.add(
                                SearchTag(
                                    it?.item?.user?.username.toString(),
                                    it?.item?.user?.fullName.toString(),
                                    it?.item?.user?.profilePicUrl.toString(),
                                    it?.item?.productType.toString(),
                                )
                            )
                        }
                    }
                }
                if (list.isEmpty()){
                    _searchUiState.value = SearchUiState.EmptyList(result.getOrNull()?.data?.recent?.nextMaxId?:"")
                }
                else{
                    _searchUiState.value = SearchUiState.ShowTagsMedia(
                        Pair(list, result.getOrNull()?.data?.recent?.nextMaxId?:"")
                    )
                }
            }

            else{
                _searchUiState.value = SearchUiState.Error(
                    result.exceptionOrNull()?.message.toString()
                )
            }
        }
    }
}
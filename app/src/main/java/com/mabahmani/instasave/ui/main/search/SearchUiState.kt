package com.mabahmani.instasave.ui.main.search

import com.mabahmani.instasave.domain.model.SearchTag

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    class EmptyList(val nextMaxId: String) : SearchUiState()
    class Error(val message: String) : SearchUiState()
    class ShowTagsMedia(val data: Pair<List<SearchTag>, String>) : SearchUiState()
}

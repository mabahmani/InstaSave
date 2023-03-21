package com.mabahmani.instasave.ui.main.search

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.mabahmani.instasave.R
import com.mabahmani.instasave.domain.model.SearchTag
import com.mabahmani.instasave.ui.common.EmptyView
import com.mabahmani.instasave.ui.theme.Ubuntu

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
){
    val inputValue = remember { mutableStateOf("") }
    val nextMaxId = remember { mutableStateOf("") }
    val buttonText = remember { mutableStateOf("Search") }
    val list = remember { mutableStateOf<MutableList<SearchTag>>(mutableListOf()) }

    val state = viewModel.searchUiState.collectAsState()

    when (state.value){
        is SearchUiState.ShowTagsMedia ->{
            list.value.addAll(0,(state.value as SearchUiState.ShowTagsMedia).data.first)
            nextMaxId.value = (state.value as SearchUiState.ShowTagsMedia).data.second
            buttonText.value = "Load More"
        }
        is SearchUiState.Error ->{
            Toast.makeText(LocalContext.current, "Error: %s".format( (state.value as SearchUiState.Error).message), Toast.LENGTH_SHORT).show()
        }
        is SearchUiState.EmptyList ->{
            Toast.makeText(LocalContext.current, "EmptyList", Toast.LENGTH_SHORT).show()
            nextMaxId.value = (state.value as SearchUiState.EmptyList).nextMaxId
            buttonText.value = "Load More"
        }
        else -> {}
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.search_tag),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(16.dp)
            )
        }

        Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.padding(horizontal = 16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colors.primaryVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_hashtag), contentDescription = "",
                    Modifier
                        .width(16.dp)
                        .height(16.dp),
                    tint = MaterialTheme.colors.secondary
                )

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = inputValue.value,
                    onValueChange = {
                        nextMaxId.value = ""
                        list.value = mutableListOf()
                        inputValue.value = it
                        buttonText.value = "Search"
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.secondary,
                        fontSize = 12.sp,
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Light,
                    ),
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.primaryVariant)
                        .fillMaxWidth(),
                    decorationBox = {
                        if (inputValue.value.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.search_tag_input_hint),
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.secondary,
                            )
                        }

                        it()
                    },
                    cursorBrush = SolidColor(MaterialTheme.colors.secondary),
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        )
        {
            AnimatedVisibility(visible = state.value is SearchUiState.Loading) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colors.primary,
                        strokeWidth = 2.dp
                    )

                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),

                visible = state.value is SearchUiState.ShowTagsMedia || state.value is SearchUiState.EmptyList) {
                LazyColumn(contentPadding = PaddingValues(top = 4.dp)) {
                    items(
                        list.value
                    ) {
                        Column {

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = it.avatarUrl),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .width(32.dp)
                                            .height(32.dp)
                                            .clip(CircleShape)
                                            .border(
                                                1.dp,
                                                MaterialTheme.colors.primaryVariant,
                                                CircleShape
                                            ),
                                        contentScale = ContentScale.FillBounds
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Row {

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Column {
                                            Text(
                                                text = it.username,
                                                style = MaterialTheme.typography.h6,
                                                color = MaterialTheme.colors.primary
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(
                                                text = it.fullName,
                                                style = MaterialTheme.typography.caption,
                                                color = MaterialTheme.colors.primary
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(
                                                text = it.mediaType,
                                                style = MaterialTheme.typography.caption,
                                                color = MaterialTheme.colors.primary
                                            )

                                        }
                                    }
                                }
                            }

                            Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)
                        }
                    }
                }
            }

            TextButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                onClick = {
                          viewModel.searchTag(inputValue.value, nextMaxId.value)
                },
            ) {
                Text(
                    text = buttonText.value,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.primary
                )
            }
        }

    }
}
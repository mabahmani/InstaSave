package com.mabahmani.instasave.ui.main.livestream

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.mabahmani.instasave.R
import com.mabahmani.instasave.ui.common.EmptyView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LiveStreamScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {

        Text(
            text = stringResource(id = R.string.live_streams),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(16.dp)
        )
        Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)

//        EmptyView(title = stringResource(id = R.string.no_live_streams))

        LazyVerticalGrid(
            cells = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(
                listOf(
                    "https://dkstatics-public.digikala.com/digikala-adservice-banners/6d15c8d1293c05b6265cbde82287f178d78cd993_1632670051.jpg?x-oss-process=image/quality,q_95",
                    "https://dkstatics-public.digikala.com/digikala-adservice-banners/46bf29fa89cf200cefce08b024a6f44069438fe9_1632669967.jpg?x-oss-process=image/quality,q_95",
                    "https://dkstatics-public.digikala.com/digikala-adservice-banners/6d15c8d1293c05b6265cbde82287f178d78cd993_1632670051.jpg?x-oss-process=image/quality,q_95",
                    "https://dkstatics-public.digikala.com/digikala-adservice-banners/46bf29fa89cf200cefce08b024a6f44069438fe9_1632669967.jpg?x-oss-process=image/quality,q_95",
                    "https://dkstatics-public.digikala.com/digikala-adservice-banners/6d15c8d1293c05b6265cbde82287f178d78cd993_1632670051.jpg?x-oss-process=image/quality,q_95",
                    "https://dkstatics-public.digikala.com/digikala-adservice-banners/46bf29fa89cf200cefce08b024a6f44069438fe9_1632669967.jpg?x-oss-process=image/quality,q_95",
                    "https://dkstatics-public.digikala.com/digikala-adservice-banners/6d15c8d1293c05b6265cbde82287f178d78cd993_1632670051.jpg?x-oss-process=image/quality,q_95",
                    "https://dkstatics-public.digikala.com/digikala-adservice-banners/46bf29fa89cf200cefce08b024a6f44069438fe9_1632669967.jpg?x-oss-process=image/quality,q_95",
                )
            ) {
                Card(modifier = Modifier.padding(8.dp)) {
                    Box {
                        Image(
                            painter = rememberAsyncImagePainter(model = it),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.6f),
                            contentScale = ContentScale.FillBounds
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(8.dp , 6.dp)
                                .background(
                                    MaterialTheme.colors.primary,
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "adasd",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primaryVariant,
                                modifier = Modifier.padding(6.dp, 6.dp),
                                style = MaterialTheme.typography.caption,
                                fontSize = 10.sp
                            )
                        }

                    }

                }
            }
        }
    }
}
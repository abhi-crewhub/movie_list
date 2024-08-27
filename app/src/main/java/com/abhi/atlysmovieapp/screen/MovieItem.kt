package com.abhi.atlysmovieapp.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.abhi.atlysmovieapp.model.Movie

@Composable
fun MovieItem(movie: Movie, navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clickable {
                navController.navigate("movie_detail/${movie.imdbID}")
            }
    ) {
        AsyncImage(
            model = movie.Poster,
            contentDescription = movie.Title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = movie.Title,
            fontSize = 18.sp, // Increase font size
            fontWeight = FontWeight.Bold, // Make text bold
            maxLines = 2, // Allow up to 2 lines
            overflow = TextOverflow.Ellipsis, // Show ellipsis if text overflows
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
                .padding(top = 4.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

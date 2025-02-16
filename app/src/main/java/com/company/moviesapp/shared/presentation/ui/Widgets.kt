package com.company.moviesapp.shared.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.company.moviesapp.listscreen.presentation.model.MovieDisplayModel

@Composable
fun MovieItem(
    movie: MovieDisplayModel,
    onClick: (String) -> Unit,
    onToggleWatchLater: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onClick(movie.id)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row {
            ImageWithPlaceholder(
                imageUrl = movie.image,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = movie.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    WatchLaterIcon(
                        isAddedToWatchLater = movie.addToWatch,
                        onToggleWatchLater = { onToggleWatchLater(!movie.addToWatch) })
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.overview,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ImageWithPlaceholder(
    imageUrl: String, modifier: Modifier = Modifier
) {
    val posterAspectRatio = 2f / 3f
    SubcomposeAsyncImage(
        model = imageUrl, contentDescription = null, modifier = modifier
    ) {
        val state = painter.state
        if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
            // Show a grey placeholder while loading or if there's an error
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.35f) // Take 50% of the screen width
                    .aspectRatio(posterAspectRatio) // Maintain 2:3 aspect ratio
                    .background(Color.LightGray)
            )
        } else {
            // Display the image once it's loaded
            SubcomposeAsyncImageContent()
        }
    }
}

@Composable
fun WatchLaterIcon(
    isAddedToWatchLater: Boolean,
    onToggleWatchLater: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = if (isAddedToWatchLater) {
            Icons.Filled.Favorite // Filled icon when added to watch later
        } else {
            Icons.Filled.FavoriteBorder // Outlined icon when not added
        },
        contentDescription = if (isAddedToWatchLater) {
            "Remove from Watch Later"
        } else {
            "Add to Watch Later"
        },
        tint = if (isAddedToWatchLater) Color.Blue else Color.Gray, // Customize colors
        modifier = modifier
            .size(24.dp) // Set icon size
            .clickable { onToggleWatchLater() } // Handle click
    )
}
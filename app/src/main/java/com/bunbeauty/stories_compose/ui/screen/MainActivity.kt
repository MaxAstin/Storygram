package com.bunbeauty.stories_compose.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bunbeauty.stories_compose.navigation.Arguments.GROUP_ID
import com.bunbeauty.stories_compose.navigation.Destinations.STORIES_ROTE
import com.bunbeauty.stories_compose.navigation.Destinations.STORY_DETAILS_ROTE
import com.bunbeauty.stories_compose.ui.screen.stories.StoriesScreen
import com.bunbeauty.stories_compose.ui.screen.story_details.StoryDetailsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = STORIES_ROTE
            ) {
                composable(STORIES_ROTE) { StoriesScreen(navController) }
                composable(
                    route = "$STORY_DETAILS_ROTE/{$GROUP_ID}",
                    arguments = listOf(navArgument(GROUP_ID) {
                        type = NavType.IntType
                        defaultValue = 0
                    })
                ) {
                    StoryDetailsScreen()
                }
            }
        }
    }
}
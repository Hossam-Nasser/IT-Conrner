package io.itcorner.moodle.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.itcorner.moodle.feature.coursedetail.CourseDetailScreen
import io.itcorner.moodle.feature.courses.CoursesScreen
import io.itcorner.moodle.feature.grades.GradesScreen
import io.itcorner.moodle.feature.login.LoginScreen

@Composable
fun MoodleAppRoot(gate: StartGateViewModel = hiltViewModel()) {
    val rootNav = rememberNavController()
    val start = if (gate.initiallyLoggedIn) Routes.COURSES else Routes.LOGIN
    NavHost(navController = rootNav, startDestination = start) {
        composable(Routes.LOGIN) {
            LoginScreen(onSuccess = {
                rootNav.navigate(Routes.COURSES) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            })
        }
        composable(Routes.COURSES) { MainShell(rootNav::navigate) }
        composable(
            route = Routes.COURSE_DETAIL,
            arguments = listOf(
                navArgument(NavArgs.COURSE_ID) { type = NavType.LongType },
                navArgument(NavArgs.COURSE_TITLE) { type = NavType.StringType; defaultValue = "Course" },
            ),
        ) {
            CourseDetailScreen(onBack = { rootNav.popBackStack() })
        }
    }
}

@Composable
private fun MainShell(navigateRoot: (String) -> Unit) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = current == Routes.COURSES,
                    onClick = { if (current != Routes.COURSES) nav.navigate(Routes.COURSES) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.School, contentDescription = null) },
                    label = { Text("Courses") },
                )
                NavigationBarItem(
                    selected = current == Routes.GRADES,
                    onClick = { if (current != Routes.GRADES) nav.navigate(Routes.GRADES) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text("Grades") },
                )
            }
        },
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Routes.COURSES,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.COURSES) {
                CoursesScreen(onCourseClick = { course ->
                    navigateRoot(Routes.courseDetail(course.id, course.name))
                })
            }
            composable(Routes.GRADES) { GradesScreen() }
        }
    }
}

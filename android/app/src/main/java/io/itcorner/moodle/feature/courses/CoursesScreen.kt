package io.itcorner.moodle.feature.courses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.itcorner.moodle.core.ui.UiState
import io.itcorner.moodle.core.ui.components.CourseImage
import io.itcorner.moodle.core.ui.components.EmptyView
import io.itcorner.moodle.core.ui.components.ErrorView
import io.itcorner.moodle.core.ui.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(
    onCourseClick: (Course) -> Unit,
    viewModel: CoursesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("My Courses", fontWeight = FontWeight.SemiBold)
            })
        },
    ) { padding ->
        when (val s = state) {
            UiState.Idle, UiState.Loading -> LoadingView(Modifier.padding(padding))
            is UiState.Error -> ErrorView(s.message, viewModel::load, Modifier.padding(padding))
            is UiState.Success -> if (s.data.isEmpty()) {
                EmptyView("You're not enrolled in any courses yet.", Modifier.padding(padding))
            } else {
                CoursesList(s.data, padding, onCourseClick)
            }
        }
    }
}

@Composable
private fun CoursesList(
    courses: List<Course>,
    padding: PaddingValues,
    onClick: (Course) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(courses, query) {
        if (query.isBlank()) courses
        else courses.filter { it.name.contains(query, ignoreCase = true) || it.shortName.orEmpty().contains(query, ignoreCase = true) }
    }
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Filter my courses") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        items(filtered, key = { it.id }) { course ->
            CourseCard(course, onClick)
        }
        if (filtered.isEmpty()) {
            item { EmptyView("No courses match \"$query\".") }
        }
    }
}

@Composable
private fun CourseCard(course: Course, onClick: (Course) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick(course) },
    ) {
        Column {
            CourseImage(
                url = course.imageUrl,
                fallbackText = course.shortName ?: course.name,
                seed = course.id,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            )
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(course.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                course.shortName?.let { CategoryChip(it) }
                val pct = course.progressPercent
                if (pct != null) {
                    Box(Modifier.fillMaxWidth()) {
                        LinearProgressIndicator(
                            progress = { pct.coerceIn(0, 100) / 100f },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    Text("$pct% complete", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

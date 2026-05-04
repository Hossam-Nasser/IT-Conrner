package io.itcorner.moodle.feature.grades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.itcorner.moodle.core.ui.UiState
import io.itcorner.moodle.core.ui.components.EmptyView
import io.itcorner.moodle.core.ui.components.ErrorView
import io.itcorner.moodle.core.ui.components.LoadingView
import io.itcorner.moodle.feature.courses.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesScreen(viewModel: GradesViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        TopAppBar(title = { Text("Grades", fontWeight = FontWeight.SemiBold) })
    }) { padding ->
        when (val courses = state.courses) {
            UiState.Idle, UiState.Loading -> LoadingView(Modifier.padding(padding))
            is UiState.Error -> ErrorView(courses.message, viewModel::loadCourses, Modifier.padding(padding))
            is UiState.Success -> if (courses.data.isEmpty()) {
                EmptyView("No courses to show grades for.", Modifier.padding(padding))
            } else {
                GradesContent(
                    padding = padding,
                    courses = courses.data,
                    selectedCourseId = state.selectedCourseId,
                    items = state.items,
                    onSelect = viewModel::selectCourse,
                    onRetry = { state.selectedCourseId?.let(viewModel::selectCourse) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GradesContent(
    padding: PaddingValues,
    courses: List<Course>,
    selectedCourseId: Long?,
    items: UiState<List<GradeItem>>,
    onSelect: (Long) -> Unit,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        var expanded by remember { mutableStateOf(false) }
        val selectedName = courses.firstOrNull { it.id == selectedCourseId }?.name ?: "Select course"
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            OutlinedTextField(
                value = selectedName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Course") },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                courses.forEach { course ->
                    DropdownMenuItem(text = { Text(course.name) }, onClick = {
                        expanded = false
                        onSelect(course.id)
                    })
                }
            }
        }

        when (val s = items) {
            UiState.Idle, UiState.Loading -> LoadingView()
            is UiState.Error -> ErrorView(s.message, onRetry)
            is UiState.Success -> if (s.data.isEmpty()) {
                EmptyView("No grades have been recorded for this course.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(s.data, key = { it.id }) { item ->
                        GradeRow(item)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun GradeRow(item: GradeItem) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
        )
        val display = item.percentage ?: item.grade
        if (display != null) {
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    text = display,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                )
            }
        } else {
            Text(
                "Not graded yet",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

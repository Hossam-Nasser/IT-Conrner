package io.itcorner.moodle.feature.coursedetail

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.itcorner.moodle.core.ui.UiState
import io.itcorner.moodle.core.ui.components.EmptyView
import io.itcorner.moodle.core.ui.components.ErrorView
import io.itcorner.moodle.core.ui.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    onBack: () -> Unit,
    viewModel: CourseDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewModel.courseTitle, maxLines = 1, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        when (val s = state) {
            UiState.Idle, UiState.Loading -> LoadingView(Modifier.padding(padding))
            is UiState.Error -> ErrorView(s.message, viewModel::load, Modifier.padding(padding))
            is UiState.Success -> if (s.data.isEmpty()) {
                EmptyView("This course has no sections yet.", Modifier.padding(padding))
            } else SectionsList(s.data, padding)
        }
    }
}

@Composable
private fun SectionsList(sections: List<Section>, padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        sections.forEachIndexed { index, section ->
            item(key = "h-${section.id}") { SectionHeader(section) }
            if (section.modules.isEmpty()) {
                item(key = "e-${section.id}") {
                    Text(
                        "No items in this section",
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            } else {
                items(items = section.modules, key = { "m-${it.id}" }) { module ->
                    ModuleRow(module)
                }
            }
            if (index < sections.lastIndex) {
                item(key = "d-${section.id}") {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(section: Section) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = section.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f),
        )
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            Text(
                text = section.modules.size.toString() + if (section.modules.size == 1) " item" else " items",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Composable
private fun ModuleRow(module: Module) {
    val context = LocalContext.current
    val clickable = !module.url.isNullOrBlank()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = clickable) {
                module.url?.let {
                    context.startActivity(Intent(Intent.ACTION_VIEW, it.toUri()))
                }
            }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (!module.iconUrl.isNullOrBlank()) {
            AsyncImage(
                model = module.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = module.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
        )
        module.modName?.let {
            Text(
                text = it.replaceFirstChar { c -> c.uppercase() },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

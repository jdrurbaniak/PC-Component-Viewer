package com.example.pccomponentviewer

import android.graphics.fonts.FontStyle
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pccomponentviewer.data.ComponentType
import com.example.pccomponentviewer.data.GraphicsCard
import com.example.pccomponentviewer.ui.theme.PCComponentViewerTheme
import com.example.pccomponentviewer.data.PCComponent
import com.example.pccomponentviewer.data.PCComponents
import com.example.pccomponentviewer.data.RAM
import com.example.pccomponentviewer.data.Storage
import kotlin.reflect.KClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PCComponentViewerApp()
        }
    }
}

@Composable
fun PCComponentViewerApp(modifier: Modifier = Modifier) {
    PCComponentViewerTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                PCComponentList(
                    onItemClick = { component ->
                        navController.navigate("detail/${component.id}")
                    }
                )
            }
            composable(
                route = "detail/{componentId}",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(350)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(350)
                    )
                },
                arguments = listOf(navArgument("componentId") { type = NavType.IntType })
            ) { backStackEntry ->
                val componentId = backStackEntry.arguments?.getInt("componentId") ?: return@composable
                val component = PCComponents.getComponentById(componentId)
                DetailScreen(
                    pcComponent = component,
                    onNavigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PCComponentList(
    onItemClick: (PCComponent) -> Unit
) {
    var selectedType by remember { mutableStateOf(ComponentType.ALL) }

    Scaffold(
        topBar = { PCComponentViewerTopAppBar() }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            FilterChips(
                modifier = Modifier.padding(horizontal = 8.dp),
                selectedType = selectedType,
                onTypeSelected = { selectedType = it }
            )

            LazyColumn {
                items(PCComponents.components.filter {
                    when (selectedType) {
                        ComponentType.ALL -> true
                        ComponentType.RAM -> it is RAM
                        ComponentType.STORAGE -> it is Storage
                        ComponentType.GRAPHICS_CARD -> it is GraphicsCard
                    }
                }) { component ->
                    Card(
                        onClick = { onItemClick(component) },
                        modifier = Modifier
                            .padding(8.dp)
                            .animateItem()
                    ) {
                        PCComponentItem(pcComponent = component)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChips(
    modifier: Modifier = Modifier,
    selectedType: ComponentType,
    onTypeSelected: (ComponentType) -> Unit
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedType == ComponentType.ALL,
            onClick = { onTypeSelected(ComponentType.ALL) },
            label = { Text("Wszystkie") }
        )
        FilterChip(
            selected = selectedType == ComponentType.RAM,
            onClick = { onTypeSelected(ComponentType.RAM) },
            label = { Text("RAM") }
        )
        FilterChip(
            selected = selectedType == ComponentType.STORAGE,
            onClick = { onTypeSelected(ComponentType.STORAGE) },
            label = { Text("Dyski") }
        )
        FilterChip(
            selected = selectedType == ComponentType.GRAPHICS_CARD,
            onClick = { onTypeSelected(ComponentType.GRAPHICS_CARD) },
            label = { Text("Karty graficzne") }
        )
    }
}



@Composable
fun PCComponentItem(
    pcComponent: PCComponent,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            PCComponentIcon(pcComponent.imageResourceId)
            PCComponentInformation(pcComponent.name)
        }
    }
}

@Composable
fun PCComponentInformation(
    pcComponentName: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = pcComponentName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Test",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun PCComponentIcon(
    @DrawableRes pcComponentIcon: Int,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(64.dp)
            .padding(8.dp)
            .clip(MaterialTheme.shapes.small),
        painter = painterResource(pcComponentIcon),
        contentDescription = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PCComponentViewerTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 8.dp, bottom = 8.dp, top = 8.dp),
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.None
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun PCComponentAppPreview() {
    PCComponentViewerApp()
}

package com.reccode.filegrouper.ui.screen

import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reccode.filegrouper.util.PreferencesUtil
import com.reccode.filegrouper.util.loadMdFiles
import com.reccode.filegrouper.viewmodel.AppViewModel


@Composable
fun HomeScreen(navController: NavController, viewModel: AppViewModel) {
    val context = LocalContext.current
    var folderUri by remember { mutableStateOf<Uri?>(null) }
    var mdFiles by remember { mutableStateOf<List<Triple<String, Uri, String>>>(emptyList()) }

    var showOpenFileButton by remember { mutableStateOf(true) }

    val folderPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri ->
            uri?.let {
                PreferencesUtil.saveFolderUri(context, it)
                folderUri = it
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                loadMdFiles(context, it) { files ->
                    mdFiles = files
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        val savedUri = PreferencesUtil.getSavedFolderUri(context)
        savedUri?.let {
            folderUri = it
            loadMdFiles(context, it) { files ->
                mdFiles = files
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {

            if (folderUri == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SolicitarAcessoCard {
                        folderPicker.launch(null)
                        showOpenFileButton = true
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                    .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 84.dp) // 52dp button + 32dp do padding
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clickable { navController.popBackStack() }
                                    .padding(vertical = 16.dp)
                            ) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Voltar",
                                    modifier = Modifier
                                )
                            }

                            Text(
                                text = "Listas",
                                style = MaterialTheme.typography.titleLarge,
                            )

                        }

                        FileGridView(
                            mdFiles = mdFiles,
                            navController = navController,
                            viewModel = viewModel
                        )

                    }

                    Box(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .align(Alignment.BottomCenter)
                    ) {
                        CreateListButton { folderPicker.launch(null) }
                    }
                }
            }
        }
    }
}

@Composable
fun SolicitarAcessoCard(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceDim
        ),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Solicitação de Acesso",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary

            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Para iniciar, por favor escolha uma pasta e permita o acesso a ela.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onContinue) {
                    Text(
                        text = "Continuar",
                    )
                }
            }
        }
    }
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileGridView(
    mdFiles: List<Triple<String, Uri, String>>,
    navController: NavController,
    viewModel: AppViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(mdFiles) { (name, uri, mimeType) ->
            val isDirectory = mimeType == DocumentsContract.Document.MIME_TYPE_DIR
            val icon = when {
                isDirectory -> Icons.Default.AddCircle
                else -> Icons.Default.Create
            }
            val iconColor = when {
                isDirectory -> MaterialTheme.colorScheme.tertiary
                else -> MaterialTheme.colorScheme.secondary
            }

            Column(
                modifier = Modifier
                    .width(72.dp)
                    .clickable {
                        viewModel.selectFile(uri)
                        // Aqui você pode decidir o que fazer ao clicar na pasta
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = iconColor,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(50.dp)
                )
            }
        }
    }
}

@Composable
fun CreateListButton( onClick: () -> Unit ) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = "Criar uma lista",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

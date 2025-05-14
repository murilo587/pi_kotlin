package com.example.tagarela.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.tagarela.R
import com.example.tagarela.data.models.NewCard
import com.example.tagarela.data.repository.CardRepository
import com.example.tagarela.ui.components.Head
import com.example.tagarela.ui.components.Menu
import com.example.tagarela.ui.viewmodel.CardViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tagarela.data.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun RegisterCardScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: CardViewModel = viewModel(factory = CardViewModel.Factory(CardRepository(context)))
    var name by remember { mutableStateOf("") }
    var syllables by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var audioUri by remember { mutableStateOf<Uri?>(null) }
    val userPreferences = UserPreferences(context)
    val userIdFlow = userPreferences.userId.collectAsState(initial = null)
    val userId = userIdFlow.value ?: ""

    var videoSelected by remember { mutableStateOf(false) }
    var audioSelected by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("meus cartoes") }
    val isLoading by viewModel.loading.collectAsState(initial = false)

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "Card cadastrado com sucesso!",
                    duration = SnackbarDuration.Short
                )
                delay(1000)
                showSuccessMessage = false
                navController.navigate("home")
            }
        }
    }

    fun copyFileToAppDirectory(context: Context, uri: Uri, prefix: String, extension: String): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "$prefix-${System.currentTimeMillis()}.$extension")
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            imageUri = uri
            if (uri != null) {
                val imageFile = copyFileToAppDirectory(context, uri, "image", "jpg")
                if (imageFile != null) {
                    viewModel.uploadImageAndGetCategory(imageFile)
                }
            }
        }
    )

    val pickVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            videoUri = uri
            videoSelected = true
        }
    )

    val pickAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            audioUri = uri
            audioSelected = true
        }
    )


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { Head() },
        bottomBar = { Menu(navController) },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                SectionTitle("ADICIONAR CARTÃO")
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 12.dp),
                    thickness = 2.dp,
                    color = Color.LightGray
                )
            }

            item {
                SectionTitle("CARREGAR IMAGEM")
                ImageUploadBox(imageUri?.toString()) {
                    pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }

            item {
                SectionTitle("CARREGAR VÍDEO")
                UploadButton(
                    text = if (videoSelected) "Vídeo Selecionado" else "⬇ Selecionar Vídeo"
                ) {
                    pickVideoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
                }
            }

            item {
                SectionTitle("CARREGAR ÁUDIO")
                UploadButton(
                    text = if (audioSelected) "Áudio Selecionado" else "⬇ Selecionar Áudio"
                ) {
                    pickAudioLauncher.launch("audio/*")
                }
            }

            item {
                SectionTitle("INFORMAÇÕES")
                InputField("NOME", name) { name = it }
                InputField("SÍLABAS (SEPARADAS POR - )", syllables) { syllables = it }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
                Button(
                    onClick = {
                        if (!isLoading && name.isNotBlank() && syllables.isNotBlank() && imageUri != null && videoUri != null && audioUri != null) {
                            val imageFile = copyFileToAppDirectory(context, imageUri!!, "image", "jpg")
                            val videoFile = copyFileToAppDirectory(context, videoUri!!, "video", "mp4")
                            val audioFile = copyFileToAppDirectory(context, audioUri!!, "audio", "mp3")

                            if (imageFile != null && videoFile != null && audioFile != null) {
                                val newCard = NewCard(
                                    name = name,
                                    syllables = syllables,
                                    image = imageFile,
                                    video = videoFile,
                                    audio = audioFile,
                                    category = category,
                                    subcategory = "default"
                                )

                                viewModel.addNewCard(newCard, userId)

                                coroutineScope.launch {
                                    showSuccessMessage = true
                                }

                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Erro ao salvar arquivos!")
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Por favor, preencha todos os campos!")
                            }
                        }
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "SALVAR CARTÃO",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 8.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun ImageUploadBox(imageUri: String?, onImageSelected: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(350.dp)
                .height(190.dp)
                .drawBehind {
                    val pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(160f, 40f),
                        phase = 0f
                    )
                    drawRect(
                        color = Color(0xFF7E57C2),
                        size = size,
                        style = Stroke(width = 7f, pathEffect = pathEffect),
                    )
                }
                .clickable {
                    onImageSelected()
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_up),
                    contentDescription = "Ícone de upload",
                    modifier = Modifier.size(40.dp)
                )
            } else {
                Image(
                    painter = rememberImagePainter(imageUri),
                    contentDescription = "Imagem carregada",
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    }
}

@Composable
fun UploadButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InputField(placeholder: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        singleLine = true
    )
}

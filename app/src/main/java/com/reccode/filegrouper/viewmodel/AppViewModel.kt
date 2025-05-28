package com.reccode.filegrouper.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    // URI do arquivo selecionado pelo usuário
    private val _selectedFileUri = MutableStateFlow<Uri?>(null)
    val selectedFileUri: StateFlow<Uri?> = _selectedFileUri.asStateFlow()

    // Mensagens para a UI (erros, status, etc)
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    // Estado de carregamento para mostrar progress indicators
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Seleciona arquivo (URI)
    fun selectFile(uri: Uri) {
        _selectedFileUri.value = uri
    }

    /**
     * Função auxiliar para extrair a URI da pasta pai (tree Uri) a partir de um DocumentUri.
     * Pode ser necessária para manipular arquivos com Storage Access Framework.
     */
    private fun getTreeUriFromDocumentUri(documentUri: Uri): Uri {
        val uriString = documentUri.toString()
        return if (uriString.contains("/tree/")) {
            Uri.parse(uriString.substringBefore("/document/"))
        } else {
            documentUri
        }
    }

    // Limpa as mensagens da UI
    fun clearMessage() {
        _message.value = null
    }
}

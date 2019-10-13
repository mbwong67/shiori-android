package net.bouzuya.sample5

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

class HomeViewModel(private val _bookmarkRepository: BookmarkRepository) : ViewModel() {

    private val _editBookmarkEvent = MutableLiveData<Bookmark>()
    val editBookmarkEvent: LiveData<Bookmark> = _editBookmarkEvent

    private val _goToTagEvent = MutableLiveData<Event<Unit>>()
    val goToTagEvent: LiveData<Event<Unit>> = _goToTagEvent

    private val _bookmarkCount = MutableLiveData<Int>()
    val bookmarkCount: LiveData<String> = Transformations.map(_bookmarkCount) { it.toString() }

    private val _nameText = MutableLiveData<String>()
    val nameText: LiveData<String> = _nameText

    private val _urlText = MutableLiveData<String>()
    val urlText: LiveData<String> = _urlText

    private val _bookmarkList = MutableLiveData<List<Bookmark>>()
    val bookmarkList: LiveData<List<Bookmark>> = _bookmarkList

    init {
        viewModelScope.launch {
            refreshList()
        }
    }

    fun deleteAll() = viewModelScope.launch {
        _bookmarkRepository.deleteAll()

        refreshList()
    }

    fun edit(bookmark: Bookmark) {
        _editBookmarkEvent.value = bookmark
    }

    fun goToTag() {
        _goToTagEvent.value = Event(Unit)
    }

    fun insert() = viewModelScope.launch {
        val name = _nameText.value ?: return@launch
        val url = _urlText.value ?: return@launch
        val createdAt = Instant.now().atZone(ZoneOffset.UTC).toOffsetDateTime()
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val bookmark = Bookmark(0, name, url, createdAt)
        _bookmarkRepository.insert(bookmark)

        _nameText.value = ""
        _urlText.value = ""

        refreshList()
    }

    fun refresh() = viewModelScope.launch {
        refreshList()
    }

    fun updateNameText(s: String) {
        _nameText.value = s
    }

    fun updateUrlText(s: String) {
        _urlText.value = s
    }

    private suspend fun refreshList() {
        _bookmarkList.value = _bookmarkRepository.findAll()
        _bookmarkCount.value = _bookmarkList.value?.size ?: 0
    }
}

package com.playgroundagc.songtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playgroundagc.songtracker.data.SongDatabase
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.repository.SongRepository
import com.playgroundagc.songtracker.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Created by Amadou on 07/06/2021, 17:44
 *
 * Song ViewModel File
 *
 */

class SongViewModel(application: Application): AndroidViewModel(application) {

    val readAllDataASC: Flow<List<Song>>
    val readAllDataByNameASC: Flow<List<Song>>
    val readAllDataByStatusASC: Flow<List<Song>>
    val readAllDataDESC: Flow<List<Song>>
    val readAllDataByNameDESC: Flow<List<Song>>
    val readAllDataByStatusDESC: Flow<List<Song>>
    private val repository: SongRepository

    private val _currentSong = SingleLiveEvent<Song>()
    val currentSong : LiveData<Song>
        get() = _currentSong

    val sortSelect = MutableLiveData(0)
    val alphaSelect = MutableLiveData(true)

    init {
        val songDao = SongDatabase.getDatabase(application).songDao()
        repository = SongRepository(songDao)
        readAllDataASC = repository.readAllDataASC
        readAllDataByNameASC = repository.readAllDataByNameASC
        readAllDataByStatusASC = repository.readAllDataByStatusASC
        readAllDataDESC = repository.readAllDataDESC
        readAllDataByNameDESC = repository.readAllDataByNameDESC
        readAllDataByStatusDESC = repository.readAllDataByStatusDESC
    }

    fun assignSong(currentSong: Song) {
        _currentSong.value = currentSong
    }

    fun assignSelection(int: Int) {
        sortSelect.value = int
    }

    fun assignAlphaSelect(value: Boolean) {
        alphaSelect.value = value
    }
    
    fun addSong(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSong(song)
        }
    }

    fun updateSong() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSong(currentSong.value!!)
        }
    }

    fun deleteSong() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSong(currentSong.value!!)
        }
    }

    fun deleteAllSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllSongs()
        }
    }
}
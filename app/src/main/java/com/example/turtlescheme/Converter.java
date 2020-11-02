package com.example.turtlescheme;

import android.util.ArraySet;

import com.example.turtlescheme.Multimedia.Book;
import com.example.turtlescheme.Multimedia.BooksGA.BooksGA;
import com.example.turtlescheme.Multimedia.BooksGA.Item;
import com.example.turtlescheme.Multimedia.Multimedia;
import com.example.turtlescheme.Multimedia.Music;
import com.example.turtlescheme.Multimedia.MusicGA.Datum;
import com.example.turtlescheme.Multimedia.MusicGA.MusicGA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Converter
{
    public static List<Book> convertToBookList(BooksGA books)
    {
        List<Book> bookList = new ArrayList<>();
        for(Item item : books.getItems())
        {
            Book newBook = new Book();
            newBook.setId(item.getId());
            newBook.setTitle(item.getVolumeInfo().getTitle());
            newBook.setActors_authors(item.getVolumeInfo().getAuthors());
            newBook.setPublishDate(item.getVolumeInfo().getPublishedDate());
            newBook.setGender(item.getVolumeInfo().getCategories());
            newBook.setLanguage(item.getVolumeInfo().getLanguage());
            newBook.setCover(item.getVolumeInfo().getImageLinks().getThumbnail());
            newBook.setPlot(item.getVolumeInfo().getDescription());
            newBook.setPublisher(item.getVolumeInfo().getPublisher());
            newBook.setType(Multimedia.BOOK);
            bookList.add(newBook);
        }
        return bookList;
    }

    public static List<Music> convertToMusicList(MusicGA music)
    {
        Set<Music> musicList = new ArraySet<>();
        for(Datum data : music.getData()) //NO gender, no publisher
        {
            Music newMusic = new Music();
            newMusic.setId(data.getAlbum().getId().toString());
            newMusic.setTitle(data.getAlbum().getTitle());
            //Integer duration = (data.getAlbum().getTracklist().length()*data.getDuration());
            newMusic.setDuration(Integer.valueOf((data.getDuration()/60)).toString()); //Temporary while we don't access the tracklist to SUM all track's duration.
            newMusic.setActors_authors(new ArrayList<>(Collections.singleton(data.getArtist().getName())));
            newMusic.setCover(data.getAlbum().getCoverXl());
            newMusic.setType(Multimedia.MUSIC);
            newMusic.setUrl(data.getAlbum().getTracklist());
            musicList.add(newMusic);
        }
        return new ArrayList<>(musicList);
    }
}

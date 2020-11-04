package com.example.turtlescheme.Multimedia.MusicGA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/*
public class Album
{
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("cover")
        @Expose
        private String cover;
        @SerializedName("cover_small")
        @Expose
        private String coverSmall;
        @SerializedName("cover_medium")
        @Expose
        private String coverMedium;
        @SerializedName("cover_big")
        @Expose
        private String coverBig;
        @SerializedName("cover_xl")
        @Expose
        private String coverXl;
        @SerializedName("md5_image")
        @Expose
        private String md5Image;
        @SerializedName("tracklist")
        @Expose
        private String tracklist;
        @SerializedName("type")
        @Expose
        private String type;

        public Integer getId()
        {
            return id;
        }

        public void setId(Integer id)
        {
            this.id = id;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getCover()
        {
            return cover;
        }

        public void setCover(String cover)
        {
            this.cover = cover;
        }

        public String getCoverSmall()
        {
            return coverSmall;
        }

        public void setCoverSmall(String coverSmall)
        {
            this.coverSmall = coverSmall;
        }

        public String getCoverMedium()
        {
            return coverMedium;
        }

        public void setCoverMedium(String coverMedium)
        {
            this.coverMedium = coverMedium;
        }

        public String getCoverBig()
        {
            return coverBig;
        }

        public void setCoverBig(String coverBig)
        {
            this.coverBig = coverBig;
        }

        public String getCoverXl()
        {
            return coverXl;
        }

        public void setCoverXl(String coverXl)
        {
            this.coverXl = coverXl;
        }

        public String getMd5Image()
        {
            return md5Image;
        }

        public void setMd5Image(String md5Image)
        {
            this.md5Image = md5Image;
        }

        public String getTracklist()
        {
            return tracklist;
        }

        public void setTracklist(String tracklist)
        {
            this.tracklist = tracklist;
        }

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }
}*/

public class Album
{
        @SerializedName("idAlbum")
        @Expose
        private String idAlbum;
        @SerializedName("idArtist")
        @Expose
        private String idArtist;
        @SerializedName("idLabel")
        @Expose
        private String idLabel;
        @SerializedName("strAlbum")
        @Expose
        private String strAlbum;
        @SerializedName("strAlbumStripped")
        @Expose
        private String strAlbumStripped;
        @SerializedName("strArtist")
        @Expose
        private String strArtist;
        @SerializedName("strArtistStripped")
        @Expose
        private String strArtistStripped;
        @SerializedName("intYearReleased")
        @Expose
        private String intYearReleased;
        @SerializedName("strStyle")
        @Expose
        private String strStyle;
        @SerializedName("strGenre")
        @Expose
        private String strGenre;
        @SerializedName("strLabel")
        @Expose
        private String strLabel;
        @SerializedName("strReleaseFormat")
        @Expose
        private String strReleaseFormat;
        @SerializedName("intSales")
        @Expose
        private String intSales;
        @SerializedName("strAlbumThumb")
        @Expose
        private String strAlbumThumb;
        @SerializedName("strAlbumThumbHQ")
        @Expose
        private Object strAlbumThumbHQ;
        @SerializedName("strAlbumThumbBack")
        @Expose
        private String strAlbumThumbBack;
        @SerializedName("strAlbumCDart")
        @Expose
        private String strAlbumCDart;
        @SerializedName("strAlbumSpine")
        @Expose
        private String strAlbumSpine;
        @SerializedName("strAlbum3DCase")
        @Expose
        private Object strAlbum3DCase;
        @SerializedName("strAlbum3DFlat")
        @Expose
        private Object strAlbum3DFlat;
        @SerializedName("strAlbum3DFace")
        @Expose
        private Object strAlbum3DFace;
        @SerializedName("strAlbum3DThumb")
        @Expose
        private Object strAlbum3DThumb;
        @SerializedName("strDescriptionEN")
        @Expose
        private String strDescriptionEN;
        @SerializedName("strDescriptionDE")
        @Expose
        private String strDescriptionDE;
        @SerializedName("strDescriptionFR")
        @Expose
        private String strDescriptionFR;
        @SerializedName("strDescriptionCN")
        @Expose
        private String strDescriptionCN;
        @SerializedName("strDescriptionIT")
        @Expose
        private String strDescriptionIT;
        @SerializedName("strDescriptionJP")
        @Expose
        private String strDescriptionJP;
        @SerializedName("strDescriptionRU")
        @Expose
        private String strDescriptionRU;
        @SerializedName("strDescriptionES")
        @Expose
        private String strDescriptionES;
        @SerializedName("strDescriptionPT")
        @Expose
        private String strDescriptionPT;
        @SerializedName("strDescriptionSE")
        @Expose
        private String strDescriptionSE;
        @SerializedName("strDescriptionNL")
        @Expose
        private String strDescriptionNL;
        @SerializedName("strDescriptionHU")
        @Expose
        private String strDescriptionHU;
        @SerializedName("strDescriptionNO")
        @Expose
        private String strDescriptionNO;
        @SerializedName("strDescriptionIL")
        @Expose
        private String strDescriptionIL;
        @SerializedName("strDescriptionPL")
        @Expose
        private String strDescriptionPL;
        @SerializedName("intLoved")
        @Expose
        private String intLoved;
        @SerializedName("intScore")
        @Expose
        private String intScore;
        @SerializedName("intScoreVotes")
        @Expose
        private String intScoreVotes;
        @SerializedName("strReview")
        @Expose
        private String strReview;
        @SerializedName("strMood")
        @Expose
        private String strMood;
        @SerializedName("strTheme")
        @Expose
        private Object strTheme;
        @SerializedName("strSpeed")
        @Expose
        private String strSpeed;
        @SerializedName("strLocation")
        @Expose
        private Object strLocation;
        @SerializedName("strMusicBrainzID")
        @Expose
        private String strMusicBrainzID;
        @SerializedName("strMusicBrainzArtistID")
        @Expose
        private String strMusicBrainzArtistID;
        @SerializedName("strAllMusicID")
        @Expose
        private String strAllMusicID;
        @SerializedName("strBBCReviewID")
        @Expose
        private String strBBCReviewID;
        @SerializedName("strRateYourMusicID")
        @Expose
        private String strRateYourMusicID;
        @SerializedName("strDiscogsID")
        @Expose
        private String strDiscogsID;
        @SerializedName("strWikidataID")
        @Expose
        private String strWikidataID;
        @SerializedName("strWikipediaID")
        @Expose
        private Object strWikipediaID;
        @SerializedName("strGeniusID")
        @Expose
        private String strGeniusID;
        @SerializedName("strLyricWikiID")
        @Expose
        private Object strLyricWikiID;
        @SerializedName("strMusicMozID")
        @Expose
        private String strMusicMozID;
        @SerializedName("strItunesID")
        @Expose
        private Object strItunesID;
        @SerializedName("strAmazonID")
        @Expose
        private Object strAmazonID;
        @SerializedName("strLocked")
        @Expose
        private String strLocked;

        public String getIdAlbum() {
                return idAlbum;
        }

        public void setIdAlbum(String idAlbum) {
                this.idAlbum = idAlbum;
        }

        public String getIdArtist() {
                return idArtist;
        }

        public void setIdArtist(String idArtist) {
                this.idArtist = idArtist;
        }

        public String getIdLabel() {
                return idLabel;
        }

        public void setIdLabel(String idLabel) {
                this.idLabel = idLabel;
        }

        public String getStrAlbum() {
                return strAlbum;
        }

        public void setStrAlbum(String strAlbum) {
                this.strAlbum = strAlbum;
        }

        public String getStrAlbumStripped() {
                return strAlbumStripped;
        }

        public void setStrAlbumStripped(String strAlbumStripped) {
                this.strAlbumStripped = strAlbumStripped;
        }

        public String getStrArtist() {
                return strArtist;
        }

        public void setStrArtist(String strArtist) {
                this.strArtist = strArtist;
        }

        public String getStrArtistStripped() {
                return strArtistStripped;
        }

        public void setStrArtistStripped(String strArtistStripped) {
                this.strArtistStripped = strArtistStripped;
        }

        public String getIntYearReleased() {
                return intYearReleased;
        }

        public void setIntYearReleased(String intYearReleased) {
                this.intYearReleased = intYearReleased;
        }

        public String getStrStyle() {
                return strStyle;
        }

        public void setStrStyle(String strStyle) {
                this.strStyle = strStyle;
        }

        public String getStrGenre() {
                return strGenre;
        }

        public void setStrGenre(String strGenre) {
                this.strGenre = strGenre;
        }

        public String getStrLabel() {
                return strLabel;
        }

        public void setStrLabel(String strLabel) {
                this.strLabel = strLabel;
        }

        public String getStrReleaseFormat() {
                return strReleaseFormat;
        }

        public void setStrReleaseFormat(String strReleaseFormat) {
                this.strReleaseFormat = strReleaseFormat;
        }

        public String getIntSales() {
                return intSales;
        }

        public void setIntSales(String intSales) {
                this.intSales = intSales;
        }

        public String getStrAlbumThumb() {
                return strAlbumThumb;
        }

        public void setStrAlbumThumb(String strAlbumThumb) {
                this.strAlbumThumb = strAlbumThumb;
        }

        public Object getStrAlbumThumbHQ() {
                return strAlbumThumbHQ;
        }

        public void setStrAlbumThumbHQ(Object strAlbumThumbHQ) {
                this.strAlbumThumbHQ = strAlbumThumbHQ;
        }

        public String getStrAlbumThumbBack() {
                return strAlbumThumbBack;
        }

        public void setStrAlbumThumbBack(String strAlbumThumbBack) {
                this.strAlbumThumbBack = strAlbumThumbBack;
        }

        public String getStrAlbumCDart() {
                return strAlbumCDart;
        }

        public void setStrAlbumCDart(String strAlbumCDart) {
                this.strAlbumCDart = strAlbumCDart;
        }

        public String getStrAlbumSpine() {
                return strAlbumSpine;
        }

        public void setStrAlbumSpine(String strAlbumSpine) {
                this.strAlbumSpine = strAlbumSpine;
        }

        public Object getStrAlbum3DCase() {
                return strAlbum3DCase;
        }

        public void setStrAlbum3DCase(Object strAlbum3DCase) {
                this.strAlbum3DCase = strAlbum3DCase;
        }

        public Object getStrAlbum3DFlat() {
                return strAlbum3DFlat;
        }

        public void setStrAlbum3DFlat(Object strAlbum3DFlat) {
                this.strAlbum3DFlat = strAlbum3DFlat;
        }

        public Object getStrAlbum3DFace() {
                return strAlbum3DFace;
        }

        public void setStrAlbum3DFace(Object strAlbum3DFace) {
                this.strAlbum3DFace = strAlbum3DFace;
        }

        public Object getStrAlbum3DThumb() {
                return strAlbum3DThumb;
        }

        public void setStrAlbum3DThumb(Object strAlbum3DThumb) {
                this.strAlbum3DThumb = strAlbum3DThumb;
        }

        public String getStrDescriptionEN() {
                return strDescriptionEN;
        }

        public void setStrDescriptionEN(String strDescriptionEN) {
                this.strDescriptionEN = strDescriptionEN;
        }

        public String getStrDescriptionDE() {
                return strDescriptionDE;
        }

        public void setStrDescriptionDE(String strDescriptionDE) {
                this.strDescriptionDE = strDescriptionDE;
        }

        public String getStrDescriptionFR() {
                return strDescriptionFR;
        }

        public void setStrDescriptionFR(String strDescriptionFR) {
                this.strDescriptionFR = strDescriptionFR;
        }

        public String getStrDescriptionCN() {
                return strDescriptionCN;
        }

        public void setStrDescriptionCN(String strDescriptionCN) {
                this.strDescriptionCN = strDescriptionCN;
        }

        public String getStrDescriptionIT() {
                return strDescriptionIT;
        }

        public void setStrDescriptionIT(String strDescriptionIT) {
                this.strDescriptionIT = strDescriptionIT;
        }

        public String getStrDescriptionJP() {
                return strDescriptionJP;
        }

        public void setStrDescriptionJP(String strDescriptionJP) {
                this.strDescriptionJP = strDescriptionJP;
        }

        public String getStrDescriptionRU() {
                return strDescriptionRU;
        }

        public void setStrDescriptionRU(String strDescriptionRU) {
                this.strDescriptionRU = strDescriptionRU;
        }

        public String getStrDescriptionES() {
                return strDescriptionES;
        }

        public void setStrDescriptionES(String strDescriptionES) {
                this.strDescriptionES = strDescriptionES;
        }

        public String getStrDescriptionPT() {
                return strDescriptionPT;
        }

        public void setStrDescriptionPT(String strDescriptionPT) {
                this.strDescriptionPT = strDescriptionPT;
        }

        public String getStrDescriptionSE() {
                return strDescriptionSE;
        }

        public void setStrDescriptionSE(String strDescriptionSE) {
                this.strDescriptionSE = strDescriptionSE;
        }

        public String getStrDescriptionNL() {
                return strDescriptionNL;
        }

        public void setStrDescriptionNL(String strDescriptionNL) {
                this.strDescriptionNL = strDescriptionNL;
        }

        public String getStrDescriptionHU() {
                return strDescriptionHU;
        }

        public void setStrDescriptionHU(String strDescriptionHU) {
                this.strDescriptionHU = strDescriptionHU;
        }

        public String getStrDescriptionNO() {
                return strDescriptionNO;
        }

        public void setStrDescriptionNO(String strDescriptionNO) {
                this.strDescriptionNO = strDescriptionNO;
        }

        public String getStrDescriptionIL() {
                return strDescriptionIL;
        }

        public void setStrDescriptionIL(String strDescriptionIL) {
                this.strDescriptionIL = strDescriptionIL;
        }

        public String getStrDescriptionPL() {
                return strDescriptionPL;
        }

        public void setStrDescriptionPL(String strDescriptionPL) {
                this.strDescriptionPL = strDescriptionPL;
        }

        public String getIntLoved() {
                return intLoved;
        }

        public void setIntLoved(String intLoved) {
                this.intLoved = intLoved;
        }

        public String getIntScore() {
                return intScore;
        }

        public void setIntScore(String intScore) {
                this.intScore = intScore;
        }

        public String getIntScoreVotes() {
                return intScoreVotes;
        }

        public void setIntScoreVotes(String intScoreVotes) {
                this.intScoreVotes = intScoreVotes;
        }

        public String getStrReview() {
                return strReview;
        }

        public void setStrReview(String strReview) {
                this.strReview = strReview;
        }

        public String getStrMood() {
                return strMood;
        }

        public void setStrMood(String strMood) {
                this.strMood = strMood;
        }

        public Object getStrTheme() {
                return strTheme;
        }

        public void setStrTheme(Object strTheme) {
                this.strTheme = strTheme;
        }

        public String getStrSpeed() {
                return strSpeed;
        }

        public void setStrSpeed(String strSpeed) {
                this.strSpeed = strSpeed;
        }

        public Object getStrLocation() {
                return strLocation;
        }

        public void setStrLocation(Object strLocation) {
                this.strLocation = strLocation;
        }

        public String getStrMusicBrainzID() {
                return strMusicBrainzID;
        }

        public void setStrMusicBrainzID(String strMusicBrainzID) {
                this.strMusicBrainzID = strMusicBrainzID;
        }

        public String getStrMusicBrainzArtistID() {
                return strMusicBrainzArtistID;
        }

        public void setStrMusicBrainzArtistID(String strMusicBrainzArtistID) {
                this.strMusicBrainzArtistID = strMusicBrainzArtistID;
        }

        public String getStrAllMusicID() {
                return strAllMusicID;
        }

        public void setStrAllMusicID(String strAllMusicID) {
                this.strAllMusicID = strAllMusicID;
        }

        public String getStrBBCReviewID() {
                return strBBCReviewID;
        }

        public void setStrBBCReviewID(String strBBCReviewID) {
                this.strBBCReviewID = strBBCReviewID;
        }

        public String getStrRateYourMusicID() {
                return strRateYourMusicID;
        }

        public void setStrRateYourMusicID(String strRateYourMusicID) {
                this.strRateYourMusicID = strRateYourMusicID;
        }

        public String getStrDiscogsID() {
                return strDiscogsID;
        }

        public void setStrDiscogsID(String strDiscogsID) {
                this.strDiscogsID = strDiscogsID;
        }

        public String getStrWikidataID() {
                return strWikidataID;
        }

        public void setStrWikidataID(String strWikidataID) {
                this.strWikidataID = strWikidataID;
        }

        public Object getStrWikipediaID() {
                return strWikipediaID;
        }

        public void setStrWikipediaID(Object strWikipediaID) {
                this.strWikipediaID = strWikipediaID;
        }

        public String getStrGeniusID() {
                return strGeniusID;
        }

        public void setStrGeniusID(String strGeniusID) {
                this.strGeniusID = strGeniusID;
        }

        public Object getStrLyricWikiID() {
                return strLyricWikiID;
        }

        public void setStrLyricWikiID(Object strLyricWikiID) {
                this.strLyricWikiID = strLyricWikiID;
        }

        public String getStrMusicMozID() {
                return strMusicMozID;
        }

        public void setStrMusicMozID(String strMusicMozID) {
                this.strMusicMozID = strMusicMozID;
        }

        public Object getStrItunesID() {
                return strItunesID;
        }

        public void setStrItunesID(Object strItunesID) {
                this.strItunesID = strItunesID;
        }

        public Object getStrAmazonID() {
                return strAmazonID;
        }

        public void setStrAmazonID(Object strAmazonID) {
                this.strAmazonID = strAmazonID;
        }

        public String getStrLocked() {
                return strLocked;
        }

        public void setStrLocked(String strLocked) {
                this.strLocked = strLocked;
        }
}

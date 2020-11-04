package com.example.turtlescheme.Multimedia.MusicGA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/*
public class Artist
{
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("picture")
        @Expose
        private String picture;
        @SerializedName("picture_small")
        @Expose
        private String pictureSmall;
        @SerializedName("picture_medium")
        @Expose
        private String pictureMedium;
        @SerializedName("picture_big")
        @Expose
        private String pictureBig;
        @SerializedName("picture_xl")
        @Expose
        private String pictureXl;
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

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getLink()
        {
            return link;
        }

        public void setLink(String link)
        {
            this.link = link;
        }

        public String getPicture()
        {
            return picture;
        }

        public void setPicture(String picture)
        {
            this.picture = picture;
        }

        public String getPictureSmall()
        {
            return pictureSmall;
        }

        public void setPictureSmall(String pictureSmall)
        {
            this.pictureSmall = pictureSmall;
        }

        public String getPictureMedium()
        {
            return pictureMedium;
        }

        public void setPictureMedium(String pictureMedium)
        {
            this.pictureMedium = pictureMedium;
        }

        public String getPictureBig()
        {
            return pictureBig;
        }

        public void setPictureBig(String pictureBig)
        {
            this.pictureBig = pictureBig;
        }

        public String getPictureXl()
        {
            return pictureXl;
        }

        public void setPictureXl(String pictureXl)
        {
            this.pictureXl = pictureXl;
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
public class Artist
{
        @SerializedName("idArtist")
        @Expose
        private String idArtist;
        @SerializedName("strArtist")
        @Expose
        private String strArtist;
        @SerializedName("strArtistStripped")
        @Expose
        private Object strArtistStripped;
        @SerializedName("strArtistAlternate")
        @Expose
        private String strArtistAlternate;
        @SerializedName("strLabel")
        @Expose
        private String strLabel;
        @SerializedName("idLabel")
        @Expose
        private String idLabel;
        @SerializedName("intFormedYear")
        @Expose
        private String intFormedYear;
        @SerializedName("intBornYear")
        @Expose
        private String intBornYear;
        @SerializedName("intDiedYear")
        @Expose
        private Object intDiedYear;
        @SerializedName("strDisbanded")
        @Expose
        private Object strDisbanded;
        @SerializedName("strStyle")
        @Expose
        private String strStyle;
        @SerializedName("strGenre")
        @Expose
        private String strGenre;
        @SerializedName("strMood")
        @Expose
        private String strMood;
        @SerializedName("strWebsite")
        @Expose
        private String strWebsite;
        @SerializedName("strFacebook")
        @Expose
        private String strFacebook;
        @SerializedName("strTwitter")
        @Expose
        private String strTwitter;
        @SerializedName("strBiographyEN")
        @Expose
        private String strBiographyEN;
        @SerializedName("strBiographyDE")
        @Expose
        private String strBiographyDE;
        @SerializedName("strBiographyFR")
        @Expose
        private String strBiographyFR;
        @SerializedName("strBiographyCN")
        @Expose
        private String strBiographyCN;
        @SerializedName("strBiographyIT")
        @Expose
        private String strBiographyIT;
        @SerializedName("strBiographyJP")
        @Expose
        private String strBiographyJP;
        @SerializedName("strBiographyRU")
        @Expose
        private String strBiographyRU;
        @SerializedName("strBiographyES")
        @Expose
        private String strBiographyES;
        @SerializedName("strBiographyPT")
        @Expose
        private String strBiographyPT;
        @SerializedName("strBiographySE")
        @Expose
        private String strBiographySE;
        @SerializedName("strBiographyNL")
        @Expose
        private String strBiographyNL;
        @SerializedName("strBiographyHU")
        @Expose
        private String strBiographyHU;
        @SerializedName("strBiographyNO")
        @Expose
        private String strBiographyNO;
        @SerializedName("strBiographyIL")
        @Expose
        private String strBiographyIL;
        @SerializedName("strBiographyPL")
        @Expose
        private String strBiographyPL;
        @SerializedName("strGender")
        @Expose
        private String strGender;
        @SerializedName("intMembers")
        @Expose
        private String intMembers;
        @SerializedName("strCountry")
        @Expose
        private String strCountry;
        @SerializedName("strCountryCode")
        @Expose
        private String strCountryCode;
        @SerializedName("strArtistThumb")
        @Expose
        private String strArtistThumb;
        @SerializedName("strArtistLogo")
        @Expose
        private String strArtistLogo;
        @SerializedName("strArtistClearart")
        @Expose
        private String strArtistClearart;
        @SerializedName("strArtistWideThumb")
        @Expose
        private String strArtistWideThumb;
        @SerializedName("strArtistFanart")
        @Expose
        private String strArtistFanart;
        @SerializedName("strArtistFanart2")
        @Expose
        private String strArtistFanart2;
        @SerializedName("strArtistFanart3")
        @Expose
        private String strArtistFanart3;
        @SerializedName("strArtistBanner")
        @Expose
        private String strArtistBanner;
        @SerializedName("strMusicBrainzID")
        @Expose
        private String strMusicBrainzID;
        @SerializedName("strLastFMChart")
        @Expose
        private String strLastFMChart;
        @SerializedName("intCharted")
        @Expose
        private String intCharted;
        @SerializedName("strLocked")
        @Expose
        private String strLocked;

        public String getIdArtist()
        {
                return idArtist;
        }

        public void setIdArtist(String idArtist)
        {
                this.idArtist = idArtist;
        }

        public String getStrArtist()
        {
                return strArtist;
        }

        public void setStrArtist(String strArtist)
        {
                this.strArtist = strArtist;
        }

        public Object getStrArtistStripped()
        {
                return strArtistStripped;
        }

        public void setStrArtistStripped(Object strArtistStripped)
        {
                this.strArtistStripped = strArtistStripped;
        }

        public String getStrArtistAlternate()
        {
                return strArtistAlternate;
        }

        public void setStrArtistAlternate(String strArtistAlternate)
        {
                this.strArtistAlternate = strArtistAlternate;
        }

        public String getStrLabel()
        {
                return strLabel;
        }

        public void setStrLabel(String strLabel)
        {
                this.strLabel = strLabel;
        }

        public String getIdLabel()
        {
                return idLabel;
        }

        public void setIdLabel(String idLabel)
        {
                this.idLabel = idLabel;
        }

        public String getIntFormedYear()
        {
                return intFormedYear;
        }

        public void setIntFormedYear(String intFormedYear)
        {
                this.intFormedYear = intFormedYear;
        }

        public String getIntBornYear()
        {
                return intBornYear;
        }

        public void setIntBornYear(String intBornYear)
        {
                this.intBornYear = intBornYear;
        }

        public Object getIntDiedYear()
        {
                return intDiedYear;
        }

        public void setIntDiedYear(Object intDiedYear)
        {
                this.intDiedYear = intDiedYear;
        }

        public Object getStrDisbanded()
        {
                return strDisbanded;
        }

        public void setStrDisbanded(Object strDisbanded)
        {
                this.strDisbanded = strDisbanded;
        }

        public String getStrStyle()
        {
                return strStyle;
        }

        public void setStrStyle(String strStyle)
        {
                this.strStyle = strStyle;
        }

        public String getStrGenre()
        {
                return strGenre;
        }

        public void setStrGenre(String strGenre)
        {
                this.strGenre = strGenre;
        }

        public String getStrMood()
        {
                return strMood;
        }

        public void setStrMood(String strMood)
        {
                this.strMood = strMood;
        }

        public String getStrWebsite()
        {
                return strWebsite;
        }

        public void setStrWebsite(String strWebsite)
        {
                this.strWebsite = strWebsite;
        }

        public String getStrFacebook()
        {
                return strFacebook;
        }

        public void setStrFacebook(String strFacebook)
        {
                this.strFacebook = strFacebook;
        }

        public String getStrTwitter()
        {
                return strTwitter;
        }

        public void setStrTwitter(String strTwitter)
        {
                this.strTwitter = strTwitter;
        }

        public String getStrBiographyEN()
        {
                return strBiographyEN;
        }

        public void setStrBiographyEN(String strBiographyEN)
        {
                this.strBiographyEN = strBiographyEN;
        }

        public String getStrBiographyDE()
        {
                return strBiographyDE;
        }

        public void setStrBiographyDE(String strBiographyDE)
        {
                this.strBiographyDE = strBiographyDE;
        }

        public String getStrBiographyFR()
        {
                return strBiographyFR;
        }

        public void setStrBiographyFR(String strBiographyFR)
        {
                this.strBiographyFR = strBiographyFR;
        }

        public String getStrBiographyCN() {
                return strBiographyCN;
        }

        public void setStrBiographyCN(String strBiographyCN)
        {
                this.strBiographyCN = strBiographyCN;
        }

        public String getStrBiographyIT() {
                return strBiographyIT;
        }

        public void setStrBiographyIT(String strBiographyIT)
        {
                this.strBiographyIT = strBiographyIT;
        }

        public String getStrBiographyJP() {
                return strBiographyJP;
        }

        public void setStrBiographyJP(String strBiographyJP)
        {
                this.strBiographyJP = strBiographyJP;
        }

        public String getStrBiographyRU()
        {
                return strBiographyRU;
        }

        public void setStrBiographyRU(String strBiographyRU)
        {
                this.strBiographyRU = strBiographyRU;
        }

        public String getStrBiographyES()
        {
                return strBiographyES;
        }

        public void setStrBiographyES(String strBiographyES)
        {
                this.strBiographyES = strBiographyES;
        }

        public String getStrBiographyPT()
        {
                return strBiographyPT;
        }

        public void setStrBiographyPT(String strBiographyPT)
        {
                this.strBiographyPT = strBiographyPT;
        }

        public String getStrBiographySE()
        {
                return strBiographySE;
        }

        public void setStrBiographySE(String strBiographySE)
        {
                this.strBiographySE = strBiographySE;
        }

        public String getStrBiographyNL()
        {
                return strBiographyNL;
        }

        public void setStrBiographyNL(String strBiographyNL)
        {
                this.strBiographyNL = strBiographyNL;
        }

        public String getStrBiographyHU()
        {
                return strBiographyHU;
        }

        public void setStrBiographyHU(String strBiographyHU)
        {
                this.strBiographyHU = strBiographyHU;
        }

        public String getStrBiographyNO()
        {
                return strBiographyNO;
        }

        public void setStrBiographyNO(String strBiographyNO)
        {
                this.strBiographyNO = strBiographyNO;
        }

        public String getStrBiographyIL()
        {
                return strBiographyIL;
        }

        public void setStrBiographyIL(String strBiographyIL)
        {
                this.strBiographyIL = strBiographyIL;
        }

        public String getStrBiographyPL()
        {
                return strBiographyPL;
        }

        public void setStrBiographyPL(String strBiographyPL)
        {
                this.strBiographyPL = strBiographyPL;
        }

        public String getStrGender()
        {
                return strGender;
        }

        public void setStrGender(String strGender)
        {
                this.strGender = strGender;
        }

        public String getIntMembers()
        {
                return intMembers;
        }

        public void setIntMembers(String intMembers)
        {
                this.intMembers = intMembers;
        }

        public String getStrCountry()
        {
                return strCountry;
        }

        public void setStrCountry(String strCountry)
        {
                this.strCountry = strCountry;
        }

        public String getStrCountryCode()
        {
                return strCountryCode;
        }

        public void setStrCountryCode(String strCountryCode)
        {
                this.strCountryCode = strCountryCode;
        }

        public String getStrArtistThumb()
        {
                return strArtistThumb;
        }

        public void setStrArtistThumb(String strArtistThumb)
        {
                this.strArtistThumb = strArtistThumb;
        }

        public String getStrArtistLogo()
        {
                return strArtistLogo;
        }

        public void setStrArtistLogo(String strArtistLogo)
        {
                this.strArtistLogo = strArtistLogo;
        }

        public String getStrArtistClearart()
        {
                return strArtistClearart;
        }

        public void setStrArtistClearart(String strArtistClearart)
        {
                this.strArtistClearart = strArtistClearart;
        }

        public String getStrArtistWideThumb()
        {
                return strArtistWideThumb;
        }

        public void setStrArtistWideThumb(String strArtistWideThumb)
        {
                this.strArtistWideThumb = strArtistWideThumb;
        }

        public String getStrArtistFanart()
        {
                return strArtistFanart;
        }

        public void setStrArtistFanart(String strArtistFanart)
        {
                this.strArtistFanart = strArtistFanart;
        }

        public String getStrArtistFanart2()
        {
                return strArtistFanart2;
        }

        public void setStrArtistFanart2(String strArtistFanart2)
        {
                this.strArtistFanart2 = strArtistFanart2;
        }

        public String getStrArtistFanart3()
        {
                return strArtistFanart3;
        }

        public void setStrArtistFanart3(String strArtistFanart3)
        {
                this.strArtistFanart3 = strArtistFanart3;
        }

        public String getStrArtistBanner()
        {
                return strArtistBanner;
        }

        public void setStrArtistBanner(String strArtistBanner)
        {
                this.strArtistBanner = strArtistBanner;
        }

        public String getStrMusicBrainzID()
        {
                return strMusicBrainzID;
        }

        public void setStrMusicBrainzID(String strMusicBrainzID)
        {
                this.strMusicBrainzID = strMusicBrainzID;
        }

        public String getStrLastFMChart()
        {
                return strLastFMChart;
        }

        public void setStrLastFMChart(String strLastFMChart)
        {
                this.strLastFMChart = strLastFMChart;
        }

        public String getIntCharted()
        {
                return intCharted;
        }

        public void setIntCharted(String intCharted)
        {
                this.intCharted = intCharted;
        }

        public String getStrLocked()
        {
                return strLocked;
        }

        public void setStrLocked(String strLocked)
        {
                this.strLocked = strLocked;
        }

}
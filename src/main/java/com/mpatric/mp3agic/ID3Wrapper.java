package com.mpatric.mp3agic;

public class ID3Wrapper {

	private final ID3v1 id3v1Tag;
	private final ID3v2 id3v2Tag;

	public ID3Wrapper(ID3v1 id3v1Tag, ID3v2 id3v2Tag) {
		this.id3v1Tag = id3v1Tag;
		this.id3v2Tag = id3v2Tag;
	}

	public ID3v1 getId3v1Tag() {
		return id3v1Tag;
	}

	public ID3v2 getId3v2Tag() {
		return id3v2Tag;
	}

	public String getTrack() {
		if (id3v2Tag != null && id3v2Tag.getTrack() != null && id3v2Tag.getTrack().length() > 0) {
			return id3v2Tag.getTrack();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getTrack();
		} else {
			return null;
		}
	}

	public void setTrack(String track) {
		if (id3v2Tag != null) {
			id3v2Tag.setTrack(track);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setTrack(track);
		}
	}

	public String getArtist() {
		if (id3v2Tag != null && id3v2Tag.getArtist() != null && !id3v2Tag.getArtist().isEmpty()) {
			return id3v2Tag.getArtist();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getArtist();
		} else {
			return null;
		}
	}

	public void setArtist(String artist) {
		if (id3v2Tag != null) {
			id3v2Tag.setArtist(artist);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setArtist(artist);
		}
	}

	public String getTitle() {
		if (id3v2Tag != null && id3v2Tag.getTitle() != null && !id3v2Tag.getTitle().isEmpty()) {
			return id3v2Tag.getTitle();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getTitle();
		} else {
			return null;
		}
	}

	public void setTitle(String title) {
		if (id3v2Tag != null) {
			id3v2Tag.setTitle(title);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setTitle(title);
		}
	}

	public String getAlbum() {
		if (id3v2Tag != null && id3v2Tag.getAlbum() != null && !id3v2Tag.getAlbum().isEmpty()) {
			return id3v2Tag.getAlbum();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getAlbum();
		} else {
			return null;
		}
	}

	public void setAlbum(String album) {
		if (id3v2Tag != null) {
			id3v2Tag.setAlbum(album);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setAlbum(album);
		}
	}

	public void setGenreDescription(String genreDescription) {
		if (id3v2Tag != null) {
			id3v2Tag.setGenreDescription(genreDescription);
		}
	}

	public String getYear() {
		if (id3v2Tag != null && id3v2Tag.getYear() != null && !id3v2Tag.getYear().isEmpty()) {
			return id3v2Tag.getYear();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getYear();
		} else {
			return null;
		}
	}

	public void setYear(String year) {
		if (id3v2Tag != null) {
			id3v2Tag.setYear(year);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setYear(year);
		}
	}

	public int getGenre() {
		if (id3v2Tag != null && id3v2Tag.getGenre() != -1) {
			return id3v2Tag.getGenre();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getGenre();
		} else {
			return -1;
		}
	}

	public void setGenre(int genre) {
		if (id3v2Tag != null) {
			id3v2Tag.setGenre(genre);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setGenre(genre);
		}
	}

	public String getGenreDescription() {
		if (id3v2Tag != null) {
			return id3v2Tag.getGenreDescription();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getGenreDescription();
		} else {
			return null;
		}
	}

	public String getComment() {
		if (id3v2Tag != null && id3v2Tag.getComment() != null && !id3v2Tag.getComment().isEmpty()) {
			return id3v2Tag.getComment();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getComment();
		} else {
			return null;
		}
	}

	public void setComment(String comment) {
		if (id3v2Tag != null) {
			id3v2Tag.setComment(comment);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setComment(comment);
		}
	}

	public String getComposer() {
		if (id3v2Tag != null) {
			return id3v2Tag.getComposer();
		} else {
			return null;
		}
	}

	public void setComposer(String composer) {
		if (id3v2Tag != null) {
			id3v2Tag.setComposer(composer);
		}
	}

	public String getOriginalArtist() {
		if (id3v2Tag != null) {
			return id3v2Tag.getOriginalArtist();
		} else {
			return null;
		}
	}

	public void setOriginalArtist(String originalArtist) {
		if (id3v2Tag != null) {
			id3v2Tag.setOriginalArtist(originalArtist);
		}
	}

	public void setAlbumArtist(String albumArtist) {
		if (id3v2Tag != null) {
			id3v2Tag.setAlbumArtist(albumArtist);
		}
	}

	public String getAlbumArtist() {
		if (id3v2Tag != null) {
			return id3v2Tag.getAlbumArtist();
		} else {
			return null;
		}
	}

	public String getCopyright() {
		if (id3v2Tag != null) {
			return id3v2Tag.getCopyright();
		} else {
			return null;
		}
	}

	public void setCopyright(String copyright) {
		if (id3v2Tag != null) {
			id3v2Tag.setCopyright(copyright);
		}
	}

	public String getUrl() {
		if (id3v2Tag != null) {
			return id3v2Tag.getUrl();
		} else {
			return null;
		}
	}

	public void setUrl(String url) {
		if (id3v2Tag != null) {
			id3v2Tag.setUrl(url);
		}
	}

	public String getEncoder() {
		if (id3v2Tag != null) {
			return id3v2Tag.getEncoder();
		} else {
			return null;
		}
	}

	public void setEncoder(String encoder) {
		if (id3v2Tag != null) {
			id3v2Tag.setEncoder(encoder);
		}
	}

	public byte[] getAlbumImage() {
		if (id3v2Tag != null) {
			return id3v2Tag.getAlbumImage();
		} else {
			return null;
		}
	}

	public void setAlbumImage(byte[] albumImage, String mimeType) {
		if (id3v2Tag != null) {
			id3v2Tag.setAlbumImage(albumImage, mimeType);
		}
	}

	public String getAlbumImageMimeType() {
		if (id3v2Tag != null) {
			return id3v2Tag.getAlbumImageMimeType();
		} else {
			return null;
		}
	}

	public void setLyrics(String lyrics) {
		if (id3v2Tag != null) {
			id3v2Tag.setLyrics(lyrics);
		}
	}

	public String getLyrics() {
		if (id3v2Tag != null) {
			return id3v2Tag.getLyrics();
		} else {
			return null;
		}
	}

	public void clearComment() {
		if (id3v2Tag != null) {
			id3v2Tag.clearFrameSet(AbstractID3v2Tag.ID_COMMENT);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setComment(null);
		}
	}

	public void clearCopyright() {
		if (id3v2Tag != null) {
			id3v2Tag.clearFrameSet(AbstractID3v2Tag.ID_COPYRIGHT);
		}
	}

	public void clearEncoder() {
		if (id3v2Tag != null) {
			id3v2Tag.clearFrameSet(AbstractID3v2Tag.ID_ENCODER);
		}
	}
	
	public void setLyricist(String lyricist) {
		if (id3v2Tag != null) {
			id3v2Tag.setLyricist(lyricist);
		}
	}

	public String getLyricist() {
		if (id3v2Tag != null) {
			return id3v2Tag.getLyricist();
		} else {
			return null;
		}
	}
	
	public void setConductor(String conductor) {
		if (id3v2Tag != null) {
			id3v2Tag.setConductor(conductor);
		}
	}

	public String getConductor() {
		if (id3v2Tag != null) {
			return id3v2Tag.getConductor();
		} else {
			return null;
		}
	}
	
	public void setMixArtist(String mixArtist) {
		if (id3v2Tag != null) {
			id3v2Tag.setMixArtist(mixArtist);
		}
	}

	public String getMixArtist() {
		if (id3v2Tag != null) {
			return id3v2Tag.getMixArtist();
		} else {
			return null;
		}
	}

	public void setMediaType(String mediaType) {
		if (id3v2Tag != null) {
			id3v2Tag.setMediaType(mediaType);
		}
	}

	public String getMediaType() {
		if (id3v2Tag != null) {
			return id3v2Tag.getMediaType();
		} else {
			return null;
		}
	}

	public void setPublisher(String publisher) {
		if (id3v2Tag != null) {
			id3v2Tag.setPublisher(publisher);
		}
	}

	public String getPublisher() {
		if (id3v2Tag != null) {
			return id3v2Tag.getPublisher();
		} else {
			return null;
		}
	}

	public void setInvolved(String involved) {
		if (id3v2Tag != null) {
			id3v2Tag.setInvolved(involved);
		}
	}

	public String getInvolved() {
		if (id3v2Tag != null) {
			return id3v2Tag.getInvolved();
		} else {
			return null;
		}
	}

	public void setPartOfSet(String partOfSet) {
		if (id3v2Tag != null) {
			id3v2Tag.setPartOfSet(partOfSet);
		}
	}

	public String getPartOfSet() {
		if (id3v2Tag != null) {
			return id3v2Tag.getPartOfSet();
		} else {
			return null;
		}
	}

	public void setTrackLength(String trackLength) {
		if (id3v2Tag != null) {
			id3v2Tag.setTrackLength(trackLength);
		}
	}

	public String getTrackLength() {
		if (id3v2Tag != null) {
			return id3v2Tag.getTrackLength();
		} else {
			return null;
		}
	}

	public void setEncoderSettings(String encoderSettings) {
		if (id3v2Tag != null) {
			id3v2Tag.setEncoderSettings(encoderSettings);
		}
	}

	public String getEncoderSettings() {
		if (id3v2Tag != null) {
			return id3v2Tag.getEncoderSettings();
		} else {
			return null;
		}
	}

	public void setCompilation(boolean compilation) {
		if (id3v2Tag != null) {
			id3v2Tag.setCompilation(compilation);
		}
	}

	public boolean isCompilation() {
		if (id3v2Tag != null) {
			return id3v2Tag.isCompilation();
		} else {
			return false;
		}
	}
}

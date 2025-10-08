package com.mpatric.mp3agic;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

public class Mp3agicFileTest {

	private static final String FS = File.separator;
	private static final String MP3_WITH_NO_TAGS = "src" + FS + "test" + FS + "resources" + FS + "notags.mp3";
	private static final String MP3_WITH_ID3V1_AND_ID3V23_TAGS = "src" + FS + "test" + FS + "resources" + FS + "v1andv23tags.mp3";
	private static final String MP3_WITH_DUMMY_START_AND_END_FRAMES = "src" + FS + "test" + FS + "resources" + FS + "dummyframes.mp3";
	private static final String MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS = "src" + FS + "test" + FS + "resources" + FS + "v1andv23andcustomtags.mp3";
	private static final String MP3_WITH_ID3V23_UNICODE_TAGS = "src" + FS + "test" + FS + "resources" + FS + "v23unicodetags.mp3";
	private static final String NOT_AN_MP3 = "src" + FS + "test" + FS + "resources" + FS + "notanmp3.mp3";
	private static final String MP3_WITH_INCOMPLETE_MPEG_FRAME = "src" + FS + "test" + FS + "resources" + FS + "incompletempegframe.mp3";

	@Test
	public void shouldLoadMp3WithNoTags() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 41);
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 256);
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 1024);
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 5000);
		loadAndCheckTestMp3WithNoTags(new File(MP3_WITH_NO_TAGS), 41);
		loadAndCheckTestMp3WithNoTags(new File(MP3_WITH_NO_TAGS), 256);
		loadAndCheckTestMp3WithNoTags(new File(MP3_WITH_NO_TAGS), 1024);
		loadAndCheckTestMp3WithNoTags(new File(MP3_WITH_NO_TAGS), 5000);
	}

	@Test
	public void shouldLoadMp3WithId3Tags() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 41);
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 256);
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 1024);
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 5000);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_ID3V1_AND_ID3V23_TAGS), 41);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_ID3V1_AND_ID3V23_TAGS), 256);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_ID3V1_AND_ID3V23_TAGS), 1024);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_ID3V1_AND_ID3V23_TAGS), 5000);
	}

	@Test
	public void shouldLoadMp3WithFakeStartAndEndFrames() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 41);
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 256);
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 1024);
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 5000);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_DUMMY_START_AND_END_FRAMES), 41);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_DUMMY_START_AND_END_FRAMES), 256);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_DUMMY_START_AND_END_FRAMES), 1024);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_DUMMY_START_AND_END_FRAMES), 5000);
	}

	@Test
	public void shouldLoadMp3WithCustomTag() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 41);
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 256);
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 1024);
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 5000);
		loadAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 41);
		loadAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 256);
		loadAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 1024);
		loadAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 5000);
	}

	@Test
	public void shouldThrowExceptionForFileThatIsNotAnMp3() throws Exception {
		try {
			new Mp3agicFile(NOT_AN_MP3);
			fail("InvalidDataException expected but not thrown");
		} catch (InvalidDataException e) {
			assertEquals("No mpegs frames found", e.getMessage());
		}
	}

	@Test
	public void shouldThrowExceptionForFileThatIsNotAnMp3ForFileConstructor() throws Exception {
		try {
			new Mp3agicFile(new File(NOT_AN_MP3));
			fail("InvalidDataException expected but not thrown");
		} catch (InvalidDataException e) {
			assertEquals("No mpegs frames found", e.getMessage());
		}
	}

	@Test
	public void shouldFindProbableStartOfMpegFramesWithPrescan() throws IOException {
		Mp3FileForTesting mp3File = new Mp3FileForTesting(MP3_WITH_ID3V1_AND_ID3V23_TAGS);
		testShouldFindProbableStartOfMpegFramesWithPrescan(mp3File);
	}

	@Test
	public void shouldFindProbableStartOfMpegFramesWithPrescanForFileConstructor() throws IOException {
		Mp3FileForTesting mp3File = new Mp3FileForTesting(new File(MP3_WITH_ID3V1_AND_ID3V23_TAGS));
		testShouldFindProbableStartOfMpegFramesWithPrescan(mp3File);
	}

	private void testShouldFindProbableStartOfMpegFramesWithPrescan(Mp3FileForTesting mp3File) {
		assertEquals(0x44B, mp3File.preScanResult);
	}

	@Test
	public void shouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile() throws Exception {
		Mp3agicFile mp3agicFile = new Mp3agicFile(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
		testShouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile(mp3agicFile);
	}

	@Test
	public void shouldThrowExceptionIfSavingMp3WithSameNameAsSourceFileForFileConstructor() throws Exception {
		Mp3agicFile mp3agicFile = new Mp3agicFile(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
		testShouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile(mp3agicFile);
	}

	private void testShouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile(Mp3agicFile mp3agicFile) throws NotSupportedException, IOException {
		System.out.println(mp3agicFile.getFilename());
		System.out.println(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
		try {
			mp3agicFile.save(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
			fail("IllegalArgumentException expected but not thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Save filename same as source filename", e.getMessage());
		}
	}

	@Test
	public void shouldSaveLoadedMp3WhichIsEquivalentToOriginal() throws Exception {
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 41);
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 256);
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 1024);
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 5000);
		copyAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 41);
		copyAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 256);
		copyAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 1024);
		copyAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 5000);
	}

	@Test
	public void shouldLoadAndCheckMp3ContainingUnicodeFields() throws Exception {
		loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 41);
		loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 256);
		loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 1024);
		loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 5000);
		loadAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 41);
		loadAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 256);
		loadAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 1024);
		loadAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 5000);
	}

	@Test
	public void shouldSaveLoadedMp3WithUnicodeFieldsWhichIsEquivalentToOriginal() throws Exception {
		copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 41);
		copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 256);
		copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 1024);
		copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 5000);
		copyAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 41);
		copyAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 256);
		copyAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 1024);
		copyAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 5000);
	}

	@Test
	public void shouldIgnoreIncompleteMpegFrame() throws Exception {
		Mp3agicFile mp3agicFile = new Mp3agicFile(MP3_WITH_INCOMPLETE_MPEG_FRAME, 256);
		testShouldIgnoreIncompleteMpegFrame(mp3agicFile);
	}

	@Test
	public void shouldIgnoreIncompleteMpegFrameForFileConstructor() throws Exception {
		Mp3agicFile mp3agicFile = new Mp3agicFile(new File(MP3_WITH_INCOMPLETE_MPEG_FRAME), 256);
		testShouldIgnoreIncompleteMpegFrame(mp3agicFile);
	}

	private void testShouldIgnoreIncompleteMpegFrame(Mp3agicFile mp3agicFile) {
		assertEquals(0x44B, mp3agicFile.getXingOffset());
		assertEquals(0x5EC, mp3agicFile.getStartOffset());
		assertEquals(0xF17, mp3agicFile.getEndOffset());
		assertTrue(mp3agicFile.hasId3v1Tag());
		assertTrue(mp3agicFile.hasId3v2Tag());
		assertEquals(5, mp3agicFile.getFrameCount());
	}

	@Test
	public void shouldInitialiseProperlyWhenNotScanningFile() throws Exception {
		Mp3agicFile mp3agicFile = new Mp3agicFile(MP3_WITH_INCOMPLETE_MPEG_FRAME, 256, false);
		testShouldInitialiseProperlyWhenNotScanningFile(mp3agicFile);
	}

	@Test
	public void shouldInitialiseProperlyWhenNotScanningFileForFileConstructor() throws Exception {
		Mp3agicFile mp3agicFile = new Mp3agicFile(new File(MP3_WITH_INCOMPLETE_MPEG_FRAME), 256, false);
		testShouldInitialiseProperlyWhenNotScanningFile(mp3agicFile);
	}

	private void testShouldInitialiseProperlyWhenNotScanningFile(Mp3agicFile mp3agicFile) {
		assertTrue(mp3agicFile.hasId3v1Tag());
		assertTrue(mp3agicFile.hasId3v2Tag());
	}

	@Test
	public void shouldRemoveId3v1Tag() throws Exception {
		String filename = MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS;
		testShouldRemoveId3v1Tag(new Mp3agicFile(filename));
	}

	@Test
	public void shouldRemoveId3v1TagForFileConstructor() throws Exception {
		File filename = new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
		testShouldRemoveId3v1Tag(new Mp3agicFile(filename));
	}

	private void testShouldRemoveId3v1Tag(Mp3agicFile mp3agicFile) throws Exception {
		String saveFilename = mp3agicFile.getFilename() + ".copy";
		try {
			mp3agicFile.removeId3v1Tag();
			mp3agicFile.save(saveFilename);
			Mp3agicFile newMp3File = new Mp3agicFile(saveFilename);
			assertFalse(newMp3File.hasId3v1Tag());
			assertTrue(newMp3File.hasId3v2Tag());
			assertTrue(newMp3File.hasCustomTag());
		} finally {
			TestHelper.deleteFile(saveFilename);
		}
	}

	@Test
	public void shouldRemoveId3v2Tag() throws Exception {
		String filename = MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS;
		testShouldRemoveId3v2Tag(new Mp3agicFile(filename));
	}

	@Test
	public void shouldRemoveId3v2TagForFileConstructor() throws Exception {
		File filename = new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
		testShouldRemoveId3v2Tag(new Mp3agicFile(filename));
	}

	private void testShouldRemoveId3v2Tag(Mp3agicFile mp3agicFile) throws Exception {
		String saveFilename = mp3agicFile.getFilename() + ".copy";
		try {
			mp3agicFile.removeId3v2Tag();
			mp3agicFile.save(saveFilename);
			Mp3agicFile newMp3File = new Mp3agicFile(saveFilename);
			assertTrue(newMp3File.hasId3v1Tag());
			assertFalse(newMp3File.hasId3v2Tag());
			assertTrue(newMp3File.hasCustomTag());
		} finally {
			TestHelper.deleteFile(saveFilename);
		}
	}

	@Test
	public void shouldRemoveCustomTag() throws Exception {
		String filename = MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS;
		testShouldRemoveCustomTag(new Mp3agicFile(filename));
	}

	@Test
	public void shouldRemoveCustomTagForFileConstructor() throws Exception {
		File filename = new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
		testShouldRemoveCustomTag(new Mp3agicFile(filename));
	}

	private void testShouldRemoveCustomTag(Mp3agicFile mp3agicFile) throws Exception {
		String saveFilename = mp3agicFile.getFilename() + ".copy";
		try {
			mp3agicFile.removeCustomTag();
			mp3agicFile.save(saveFilename);
			Mp3agicFile newMp3File = new Mp3agicFile(saveFilename);
			assertTrue(newMp3File.hasId3v1Tag());
			assertTrue(newMp3File.hasId3v2Tag());
			assertFalse(newMp3File.hasCustomTag());
		} finally {
			TestHelper.deleteFile(saveFilename);
		}
	}

	@Test
	public void shouldRemoveId3v1AndId3v2AndCustomTags() throws Exception {
		String filename = MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS;
		testShouldRemoveId3v1AndId3v2AndCustomTags(new Mp3agicFile(filename));
	}

	@Test
	public void shouldRemoveId3v1AndId3v2AndCustomTagsForFileConstructor() throws Exception {
		File filename = new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
		testShouldRemoveId3v1AndId3v2AndCustomTags(new Mp3agicFile(filename));
	}

	private void testShouldRemoveId3v1AndId3v2AndCustomTags(Mp3agicFile mp3agicFile) throws Exception {
		String saveFilename = mp3agicFile.getFilename() + ".copy";
		try {
			mp3agicFile.removeId3v1Tag();
			mp3agicFile.removeId3v2Tag();
			mp3agicFile.removeCustomTag();
			mp3agicFile.save(saveFilename);
			Mp3agicFile newMp3File = new Mp3agicFile(saveFilename);
			assertFalse(newMp3File.hasId3v1Tag());
			assertFalse(newMp3File.hasId3v2Tag());
			assertFalse(newMp3File.hasCustomTag());
		} finally {
			TestHelper.deleteFile(saveFilename);
		}
	}

	private Mp3agicFile copyAndCheckTestMp3WithCustomTag(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3WithCustomTag(filename, bufferLength);
		return copyAndCheckTestMp3WithCustomTag(mp3agicFile);
	}

	private Mp3agicFile copyAndCheckTestMp3WithCustomTag(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3WithCustomTag(filename, bufferLength);
		return copyAndCheckTestMp3WithCustomTag(mp3agicFile);
	}

	private Mp3agicFile copyAndCheckTestMp3WithCustomTag(Mp3agicFile mp3agicFile) throws NotSupportedException, IOException, UnsupportedTagException, InvalidDataException {
		String saveFilename = mp3agicFile.getFilename() + ".copy";
		try {
			mp3agicFile.save(saveFilename);
			Mp3agicFile copyMp3file = loadAndCheckTestMp3WithCustomTag(saveFilename, 5000);
			assertEquals(mp3agicFile.getId3v1Tag(), copyMp3file.getId3v1Tag());
			assertEquals(mp3agicFile.getId3v2Tag(), copyMp3file.getId3v2Tag());
			assertArrayEquals(mp3agicFile.getCustomTag(), copyMp3file.getCustomTag());
			return copyMp3file;
		} finally {
			TestHelper.deleteFile(saveFilename);
		}
	}

	private Mp3agicFile copyAndCheckTestMp3WithUnicodeFields(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3WithUnicodeFields(filename, bufferLength);
		return copyAndCheckTestMp3WithUnicodeFields(mp3agicFile);
	}

	private Mp3agicFile copyAndCheckTestMp3WithUnicodeFields(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3WithUnicodeFields(filename, bufferLength);
		return copyAndCheckTestMp3WithUnicodeFields(mp3agicFile);
	}

	private Mp3agicFile copyAndCheckTestMp3WithUnicodeFields(Mp3agicFile mp3agicFile) throws NotSupportedException, IOException, UnsupportedTagException, InvalidDataException {
		String saveFilename = mp3agicFile.getFilename() + ".copy";
		try {
			mp3agicFile.save(saveFilename);
			Mp3agicFile copyMp3file = loadAndCheckTestMp3WithUnicodeFields(saveFilename, 5000);
			assertEquals(mp3agicFile.getId3v2Tag(), copyMp3file.getId3v2Tag());
			return copyMp3file;
		} finally {
			TestHelper.deleteFile(saveFilename);
		}
	}

	private Mp3agicFile loadAndCheckTestMp3WithNoTags(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithNoTags(mp3agicFile);
	}

	private Mp3agicFile loadAndCheckTestMp3WithNoTags(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithNoTags(mp3agicFile);
	}

	private Mp3agicFile loadAndCheckTestMp3WithNoTags(Mp3agicFile mp3agicFile) {
		assertEquals(0x000, mp3agicFile.getXingOffset());
		assertEquals(0x1A1, mp3agicFile.getStartOffset());
		assertEquals(0xB34, mp3agicFile.getEndOffset());
		assertFalse(mp3agicFile.hasId3v1Tag());
		assertFalse(mp3agicFile.hasId3v2Tag());
		assertFalse(mp3agicFile.hasCustomTag());
		return mp3agicFile;
	}

	private Mp3agicFile loadAndCheckTestMp3WithTags(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithTags(mp3agicFile);
	}

	private Mp3agicFile loadAndCheckTestMp3WithTags(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithTags(mp3agicFile);
	}

	private Mp3agicFile loadAndCheckTestMp3WithTags(Mp3agicFile mp3agicFile) {
		assertEquals(0x44B, mp3agicFile.getXingOffset());
		assertEquals(0x5EC, mp3agicFile.getStartOffset());
		assertEquals(0xF7F, mp3agicFile.getEndOffset());
		assertTrue(mp3agicFile.hasId3v1Tag());
		assertTrue(mp3agicFile.hasId3v2Tag());
		assertFalse(mp3agicFile.hasCustomTag());
		return mp3agicFile;
	}

	private Mp3agicFile loadAndCheckTestMp3WithUnicodeFields(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithUnicodeFields(mp3agicFile);
	}

	private Mp3agicFile loadAndCheckTestMp3WithUnicodeFields(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithUnicodeFields(mp3agicFile);
	}

	private Mp3agicFile loadAndCheckTestMp3WithUnicodeFields(Mp3agicFile mp3agicFile) {
		assertEquals(0x0F0, mp3agicFile.getXingOffset());
		assertEquals(0x291, mp3agicFile.getStartOffset());
		assertEquals(0xC24, mp3agicFile.getEndOffset());
		assertTrue(mp3agicFile.hasId3v1Tag());
		assertTrue(mp3agicFile.hasId3v2Tag());
		assertTrue(mp3agicFile.hasCustomTag());
		return mp3agicFile;
	}

	private Mp3agicFile loadAndCheckTestMp3WithCustomTag(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithCustomTag(mp3agicFile);
	}

	private Mp3agicFile loadAndCheckTestMp3WithCustomTag(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3agicFile mp3agicFile = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithCustomTag(mp3agicFile);
	}

	private Mp3agicFile loadAndCheckTestMp3WithCustomTag(Mp3agicFile mp3agicFile) {
		assertEquals(0x44B, mp3agicFile.getXingOffset());
		assertEquals(0x5EC, mp3agicFile.getStartOffset());
		assertEquals(0xF7F, mp3agicFile.getEndOffset());
		assertTrue(mp3agicFile.hasId3v1Tag());
		assertTrue(mp3agicFile.hasId3v2Tag());
		assertTrue(mp3agicFile.hasCustomTag());
		return mp3agicFile;
	}

	private Mp3agicFile loadAndCheckTestMp3(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3agicFile mp3agicFile = new Mp3agicFile(filename, bufferLength);
		return loadAndCheckTestMp3(mp3agicFile);
	}

	private Mp3agicFile loadAndCheckTestMp3(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3agicFile mp3agicFile = new Mp3agicFile(filename, bufferLength);
		return loadAndCheckTestMp3(mp3agicFile);
	}

	private Mp3agicFile loadAndCheckTestMp3(Mp3agicFile mp3agicFile) {
		assertTrue(mp3agicFile.hasXingFrame());
		assertEquals(6, mp3agicFile.getFrameCount());
		assertEquals(MpegFrame.MPEG_VERSION_1_0, mp3agicFile.getVersion());
		assertEquals(MpegFrame.MPEG_LAYER_3, mp3agicFile.getLayer());
		assertEquals(44100, mp3agicFile.getSampleRate());
		assertEquals(MpegFrame.CHANNEL_MODE_JOINT_STEREO, mp3agicFile.getChannelMode());
		assertEquals(MpegFrame.EMPHASIS_NONE, mp3agicFile.getEmphasis());
		assertTrue(mp3agicFile.isOriginal());
		assertFalse(mp3agicFile.isCopyright());
		assertEquals(128, mp3agicFile.getXingBitrate());
		assertEquals(125, mp3agicFile.getBitrate());
		assertEquals(1, (mp3agicFile.getBitrates().get(224)).getValue());
		assertEquals(1, (mp3agicFile.getBitrates().get(112)).getValue());
		assertEquals(2, (mp3agicFile.getBitrates().get(96)).getValue());
		assertEquals(1, (mp3agicFile.getBitrates().get(192)).getValue());
		assertEquals(1, (mp3agicFile.getBitrates().get(32)).getValue());
		assertEquals(156, mp3agicFile.getLengthInMilliseconds());
		return mp3agicFile;
	}

	private static class Mp3FileForTesting extends Mp3agicFile {

		int preScanResult;

		public Mp3FileForTesting(String filename) throws IOException {
			SeekableByteChannel file = Files.newByteChannel(Paths.get(filename), StandardOpenOption.READ);
			preScanResult = preScanFile(file);
		}

		public Mp3FileForTesting(File filename) throws IOException {
			SeekableByteChannel file = Files.newByteChannel(filename.toPath(), StandardOpenOption.READ);
			preScanResult = preScanFile(file);
		}
	}
}

package dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import datatype.Map;
import datatype.Marker;

public class MapFileRepository {
	Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

	private static final String DATA_DIRECTORY = "./maps/"; // Adatok tárolásának könyvtára

	private void handleException(IOException e) {
		// TODO
		e.printStackTrace();
	}

	private File getMapFile(String mapId) {
		String fileName = DATA_DIRECTORY + mapId + ".json";
		File file = new File(fileName);
		return file;
	}

	public MapFileRepository() {
		File directory = new File(DATA_DIRECTORY);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	public Map save(Map map) {
		File file = getMapFile(map.getMapId());
		try (FileWriter fw = new FileWriter(file)) {
			fw.write(gson.toJson(map));
		} catch (IOException e) {
			handleException(e);
		}
		return map;
	}

	public Map findByMapId(String mapId) {
		File file = getMapFile(mapId);
		return readMapFromFile(file);
	}

	private Map readMapFromFile(File file) {
		if (file != null && file.exists() && file.isFile() && file.getName().endsWith(".json")) {
			try (FileReader fr = new FileReader(file)) {
				return gson.fromJson(fr, Map.class);
			} catch (IOException e) {
				handleException(e);
			}
		}
		return new Map();
	}

	public void delete(Map map) {
		File file = getMapFile(map.getMapId());

		if (file.exists()) {
			file.delete();
		}
	}

	public List<Map> findAll() {
		List<Map> maps = new ArrayList<>();
		File directory = new File(DATA_DIRECTORY);
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile() && file.getName().endsWith(".json")) {
					try (FileReader fr = new FileReader(file)) {
						maps.add(gson.fromJson(fr, Map.class));
					} catch (IOException e) {
						handleException(e);
					}
				}
			}
		}
		return maps;
	}

	public List<Map> findByEmailAddress(String emailAddress) {
		List<Map> maps = new ArrayList<>();
		File directory = new File(DATA_DIRECTORY);
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile() && file.getName().endsWith(".json")) {
					try (FileReader fr = new FileReader(file)) {
						Map map = gson.fromJson(fr, Map.class);
						if (map.getEmailAddress().equals(emailAddress)) {
							maps.add(map);
						}
					} catch (IOException e) {
						handleException(e);
					}
				}
			}
		}
		return maps;
	}

	public Marker save(Marker marker) {
		Map map = findByMapId(marker.getMapId());
		map.getMarkers().add(marker);
		map = save(map);
		return marker;
	}

	public void delete(Marker marker) {
		Map map = findByMapId(marker.getMapId());
		map.getMarkers().remove(marker);
		map = save(map);
	}

	public List<Marker> findMarkersByMapId(String mapId) {
		Map map = findByMapId(mapId);
		return map.getMarkers();
	}

	public Optional<Marker> findMarkerByEmailAddress(String mapId, String emailAddress) {
		Map map = findByMapId(mapId);
		for (Marker marker : map.getMarkers()) {
			if (marker.getEmailAddress().equals(emailAddress)) {
				return Optional.of(marker);
			}
		}
		return Optional.empty();
	}
}
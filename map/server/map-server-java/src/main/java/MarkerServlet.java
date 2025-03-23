import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import dao.MapFileRepository;
import datatype.Map;
import datatype.Marker;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/MarkerServlet")
public class MarkerServlet extends HttpServlet {
	private MapFileRepository mapRepository = new MapFileRepository();
	private Gson gson = new Gson();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Marker mentése
		BufferedReader reader = request.getReader();
		Marker marker;
		try {
			marker = gson.fromJson(reader, Marker.class);
		} catch (JsonSyntaxException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		Marker savedMarker = mapRepository.save(marker);
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(savedMarker));
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Marker törlése
		BufferedReader reader = request.getReader();
		Marker marker;
		try {
			marker = gson.fromJson(reader, Marker.class);
		} catch (JsonSyntaxException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		if (mapRepository.findMarkerByEmailAddress(marker.getMapId(), marker.getEmailAddress()).isPresent()) {
			mapRepository.delete(marker);
		}

		response.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Markerek lekérdezése mapId és emailAddress alapján
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			// eltávolítjuk az első '/' karaktert
			String[] pathParts = pathInfo.substring(1).split("/");
			if (pathParts.length == 2) {
				String mapId = pathParts[0];
				String emailAddress = pathParts[1];

				response.setContentType("application/json");
				Map map = mapRepository.findByMapId(mapId);

				List<Marker> markers = map.getMarkers();

				// Ha az admin kéri le a pontokat, akkor a pontokhoz tartozó emailcímek is
				// megjelennek neki.
				if (map.getEmailAddress().equals(emailAddress)) {
					markers.forEach(m -> {
						m.setContent(m.getContent() + " - " + m.getEmailAddress());
					});
				}

				// A marker létrehozójának emailcímének elrejtése, ha nem ő kéri le a térképet
				markers.forEach(m -> {
					if (!m.getEmailAddress().equals(emailAddress)) {
						m.setEmailAddress(null);
					}
				});
				response.getWriter().write(gson.toJson(markers));
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid pathInfo format");
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
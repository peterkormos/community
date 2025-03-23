import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import dao.MapFileRepository;
import datatype.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/MapManagerServlet")
public class MapManagerServlet extends HttpServlet {
	private Gson gson = new Gson();

	private MapFileRepository mapRepository = new MapFileRepository();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Térkép létrehozás

		// Olvassuk ki a paramétereket a kérésből
		BufferedReader reader = request.getReader();
		try {
			Map map = gson.fromJson(reader, Map.class);
			// Ellenőrizzük, hogy a szükséges paraméterek megvannak-e
			if (map.getEmailAddress() == null || map.getMapDescription() == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hiányzó paraméterek.");
				return;
			}

			// Hozzunk létre egy új Map objektumot
			map.setMapId(UUID.randomUUID().toString());
			map = mapRepository.save(map);

			// Generáljuk a linket
			String link = getLink(map, request);

			// Küldjük vissza a választ
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(link);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (JsonSyntaxException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Térkép törlés

		// Olvassuk ki a mapId-t az URL-ből
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			String[] pathParts = pathInfo.substring(1).split("/");
			if (pathParts.length == 2) {
				String mapId = pathParts[0];
				String emailAddress = pathParts[1];

				// Töröljük a térképet
				Map map = mapRepository.findByMapId(mapId);
				if (map != null) {
					mapRepository.delete(map);
					PrintWriter out = response.getWriter();
					out.println(getLinks(mapRepository.findByEmailAddress(emailAddress), request));

					response.setStatus(HttpServletResponse.SC_OK);
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();

		if ("/links".equals(pathInfo)) {
			getAllLinks(request, response);
		} else if ("/mylinks".equals(pathInfo)) {
			getMyLinks(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void getMyLinks(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Linkek email cím alapján
		String emailAddress = request.getParameter("emailAddress");
		if (emailAddress != null) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(getLinks(mapRepository.findByEmailAddress(emailAddress), request));
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hiányzó emailAddress paraméter.");
		}
	}

	private void getAllLinks(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Összes link
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(getLinks(mapRepository.findAll(), request));
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private StringBuilder getLinks(List<Map> maps, HttpServletRequest request) {
		StringBuilder html = new StringBuilder();
		
		html.append("<link rel='stylesheet' href='base.css' media='screen' />");

		html.append("<table style='width: 100%; border-collapse: collapse;' border='1'>");
		html.append("<tr><th>Leírás</th><th>pontok a térképen</th><th>térkép admin emailcíme</th><th>Művelet</th></tr>");

		for (Map map : maps) {
			html.append(getLink(map, request));
		}
		html.append("</table>");
		return html;
	}

	private String getLink(Map map, HttpServletRequest request) {
		return String.format(
				"<tr><td><a href='map.html?mapId=%1$s'>%2$s</a></td><td>%3$d pont</td><td>%4$s</td><td>"
						+ "<button type='submit' onclick=\"deleteMap('%1$s', '%5$s')\" >Töröl</button></td></tr>",
				map.getMapId(), map.getMapDescription(), map.getMarkers().size(), map.getEmailAddress(),
				map.getEmailAddress());
	}
}
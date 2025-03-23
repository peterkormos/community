import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.MapFileRepository;
import datatype.Map;

@Path("/maps")
@Produces(MediaType.TEXT_HTML)
@Consumes(MediaType.APPLICATION_JSON)
public class MapManagerRestService {

	private MapFileRepository mapRepository = new MapFileRepository();

	@POST
	public Response createMapLink(Map mapRequest) {
		String mapId = UUID.randomUUID().toString();
		Map map = new Map(mapRequest.getEmailAddress(), mapRequest.getMapDescription(), mapId);
		map = mapRepository.save(map);

		String link = getLink(map);

		return Response.ok(link).build();
	}

	@DELETE
	@Path("/{mapId}")
	public Response deleteMap(@PathParam("mapId") String mapId) {
		Map map = mapRepository.findByMapId(mapId);
		if (map != null) {
			mapRepository.delete(map);
		}
		return Response.ok().build();
	}

	@GET
	@Path("/links")
	public Response getAllMapLinks() {
		List<Map> maps = mapRepository.findAll();

		StringBuilder html = getLinks(maps);

		return Response.ok(html.toString()).build();
	}

	@GET
	@Path("/mylinks")
	public Response getMapLinksByEmail(@QueryParam("emailAddress") String emailAddress) {
		List<Map> maps = mapRepository.findByEmailAddress(emailAddress);

		StringBuilder html = getLinks(maps);

		return Response.ok(html.toString()).build();
	}

	private StringBuilder getLinks(List<Map> maps) {
		StringBuilder html = new StringBuilder();
		for (Map map : maps) {
			html.append(getLink(map));
		}
		return html;
	}

	private String getLink(Map map) {
		return "<a href='map.html?mapId=" + map.getMapId() + "'>" + map.getMapDescription() + " megnyitása. (Admin: "
				+ map.getEmailAddress() + ")</a><br>";
	}
}
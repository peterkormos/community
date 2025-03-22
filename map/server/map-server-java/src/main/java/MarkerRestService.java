
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.MarkerRepository;
import datatype.Marker;

@Path("/markers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerRestService {
	private MarkerRepository markerRepository = new MarkerRepository();

	@POST
	@Path("/marker")
	public Response saveMarker(Marker marker) {
		Marker savedMarker = markerRepository.save(marker);
		return Response.ok(savedMarker).build();
	}
	
	@DELETE
	@Path("/marker")
	public Response deleteMarker(Marker marker) {
		markerRepository.delete(marker);
		return Response.ok().build();
	}

	@GET
	@Path("/{mapId}")
	public Response getMarkersByMapId(@PathParam("mapId") String mapId) {
		List<Marker> markers = markerRepository.findByMapId(mapId);
		return Response.ok(markers).build();
	}
}
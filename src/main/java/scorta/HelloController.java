package scorta;

import java.util.Date;

import io.javalin.http.Context;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiResponse;

public class HelloController {

	@OpenApi(path = "/api/hello", methods = io.javalin.openapi.HttpMethod.GET, responses = {
			@OpenApiResponse(status = "200", description = "Success") })
	public static void get(Context ctx) {

		ctx.result("Hello from Java Backend! " + new Date());
	}
}
package tw.ghostIsland;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GhostIslandGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GhostIslandGatewayApplication.class, args);
    }

    @Value("${service.cargo.port}")
    private int cargoPort;

    @Value("${service.dashboard.port}")
    private int dashboardPort;

    @Value("${service.batch.port}")
    private int batchPort;

    @Value("${service.analysis.port}")
    private int analysisPort;

    @Bean
    public RouteLocator serviceRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("cargo_route", r -> r.path("/api/**")
                        .filters(f -> f.rewritePath("^/api", ""))
                        .uri("http://localhost:"+cargoPort)
                )
                .route("analysis_route", r -> r.path("/analysis/**")
                        .filters(f -> f.rewritePath("^/analysis", ""))
                        .uri("http://localhost:"+analysisPort)
                )
                .route("batch_route", r -> r.path("/jobLauncher")
                        .uri("http://localhost:"+batchPort)
                )
                .route("dashboard_route", r -> r.path("/**")
                        .uri("http://localhost:"+dashboardPort)
                )
                .build();
    }

}
